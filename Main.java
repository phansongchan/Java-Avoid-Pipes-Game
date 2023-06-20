/*
	Phan Song Chan;
	Last update: June 19th, 2023.
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class ControlWindow extends JPanel implements ActionListener, KeyListener {
	public static final int M_WIDTH  = 450;
	public static final int M_HEIGHT = 670;
	public static final int M_DELAY  = 1000 / 60;
	
	private static final int M_PIPE_WIDTH  = 47;
	private static final int M_PIPE_GAP	   = 165;
	private static final int M_TOTAL_PIPES = 3; 
	
	private Timer m_timer = new Timer(M_DELAY, this);
	
	private boolean[] m_hasPassedPipe = {false, false, false};
	private boolean m_gameOver = false;
	private int m_y = (M_HEIGHT - 30) / 2, m_dy = 0;
	private int m_count = 0;
	private int m_score = 0;
	private int[] m_pipeX = {200, 400, 600};
	private int m_pipeDX = 4;
	
	// CUSTOM UP PIPE VARS
	private int[] m_upPipeHeight = {
		new java.util.Random().nextInt(360 - 150) + 150,
		new java.util.Random().nextInt(360 - 150) + 150,
		new java.util.Random().nextInt(360 - 150) + 150
	};
	private int m_upPipeY = 0;

	// CUSTOM DOWN PIPE VARS
	private int[] m_downPipeHeight = {
		M_HEIGHT - m_upPipeHeight[0] - M_PIPE_GAP,
		M_HEIGHT - m_upPipeHeight[1] - M_PIPE_GAP,
		M_HEIGHT - m_upPipeHeight[2] - M_PIPE_GAP,
	};
	private int[] m_downPipeY = {
		m_upPipeY + m_upPipeHeight[0] + M_PIPE_GAP,
		m_upPipeY + m_upPipeHeight[1] + M_PIPE_GAP,
		m_upPipeY + m_upPipeHeight[2] + M_PIPE_GAP
	};
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);		// Better quality for texts and shapes
		// PLAYER
		g.setColor(Color.YELLOW);
		g.fillOval(55, m_y, 30, 30);
		// UP PIPES
		g.setColor(Color.GREEN);
		for (int i = 0; i < M_TOTAL_PIPES; i++)
			g.fillRect(m_pipeX[i], m_upPipeY, M_PIPE_WIDTH, m_upPipeHeight[i]);
		// DOWN PIPES
		g.setColor(Color.GREEN);
		for (int i = 0; i < M_TOTAL_PIPES; i++)
			g.fillRect(m_pipeX[i], m_downPipeY[i], M_PIPE_WIDTH, m_downPipeHeight[i]);
		// COUNTING TIME
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 28));
		g.drawString("TIME " + m_count, 5, 34);
		// SCORE
		g.drawString("SCORE " + m_score, 5, 63);		
		// GAME OVER
		if (m_gameOver) {
			g.setColor(Color.RED);
			g.setFont(new Font("Arial", Font.BOLD, 45));
			g.drawString("GAME OVER", M_WIDTH - 362, M_HEIGHT / 2 - 40);
		}
		g.dispose();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (!m_gameOver) {
			m_count++;
			m_dy++;
			m_y += m_dy;
			for (int i = 0; i < M_TOTAL_PIPES; i++)
				m_pipeX[i] -= m_pipeDX;		// Moving the pipes
			// CHECK IF THE PIPES HAVE OUTED THE BOUNDS
			for (int i = 0; i < M_TOTAL_PIPES; i++) {
				if (m_pipeX[i] < -M_PIPE_WIDTH) {
					m_pipeX[i] = M_WIDTH + 100;
					m_upPipeHeight[i] = new java.util.Random().nextInt(360 - 150) + 150;
					m_downPipeHeight[i] = M_HEIGHT - m_upPipeHeight[i] - M_PIPE_GAP;
					m_downPipeY[i] = m_upPipeY + m_upPipeHeight[i] + M_PIPE_GAP;
					m_hasPassedPipe[i] = false;
				}
			}
			// COLLISION
			for (int i = 0; i < M_TOTAL_PIPES; i++) {
				if (new Rectangle(55, m_y, 25, 25).intersects(
				    new Rectangle(m_pipeX[i], m_upPipeY, M_PIPE_WIDTH, m_upPipeHeight[i]))) m_gameOver = true;
				if (new Rectangle(55, m_y, 25, 25).intersects(
				    new Rectangle(m_pipeX[i], m_downPipeY[i], M_PIPE_WIDTH, m_downPipeHeight[i]))) m_gameOver = true;
			}
			if (m_y > (M_HEIGHT - 33) || m_y < 0) m_gameOver = true;		// Check if the player has outed of the bounds
			if (m_count % 200 == 0) m_pipeDX++;		// Increase the speed
			// SCORE SYSTEM
			for (int i = 0; i < M_TOTAL_PIPES; i++) {
				if (m_pipeX[i] + M_PIPE_WIDTH <= 55 && m_hasPassedPipe[i] == false) {
					m_score++;
					m_hasPassedPipe[i] = true;
				}
			}
		}
		repaint();
	}
	
	@Override 
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE && !m_gameOver) 
			m_dy = -11;
	}
	
	@Override 
	public void keyReleased(KeyEvent e) {}
	
	@Override
	public void keyTyped(KeyEvent e) {}
	
	public ControlWindow() {
		this.setPreferredSize(new Dimension(M_WIDTH, M_HEIGHT));
		this.setFocusable(true);
		this.setDoubleBuffered(true);
		this.setBackground(new Color(15, 10, 255));
		this.addKeyListener(this);
		m_timer.start();
	}
}

public class Main extends JFrame {
	public Main() {
		super("Avoid Pipes");
		this.add(new ControlWindow());
		this.pack();
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new Main();
	}
}