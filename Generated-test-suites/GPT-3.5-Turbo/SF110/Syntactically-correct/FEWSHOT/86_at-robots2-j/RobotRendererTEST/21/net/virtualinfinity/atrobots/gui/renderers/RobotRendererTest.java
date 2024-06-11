package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import net.virtualinfinity.atrobots.measures.Angle;
import net.virtualinfinity.atrobots.measures.Coordinate;
import net.virtualinfinity.atrobots.measures.RelativeAngle;
import net.virtualinfinity.atrobots.measures.RobotSnapshot;
import net.virtualinfinity.atrobots.measures.Vector;

public class RobotRendererTest {
	
	private RobotRenderer renderer;
	private Graphics2D g2d;
	private RobotSnapshot robotSnapshot;
	private Set<Integer> selectedRobotIds;
	
	@Before
	public void setUp() {
		renderer = new RobotRenderer();
		BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
		g2d = image.createGraphics();
		robotSnapshot = new RobotSnapshot(0, new Coordinate(10, 10), Angle.fromDegrees(90), Angle.fromDegrees(45), 0, false, "Robot1");
		selectedRobotIds = new HashSet<>();
	}
	
	@Test
	public void testRender() {
		renderer.render(g2d, robotSnapshot, selectedRobotIds);
	}
	
	@Test
	public void testIsRenderDead() {
		assertTrue(renderer.isRenderDead());
		renderer.setRenderDead(false);
		assertFalse(renderer.isRenderDead());
		renderer.setRenderDead(true);
		assertTrue(renderer.isRenderDead());
	}
	
	@Test
	public void testIsShowStatusBars() {
		assertTrue(renderer.isShowStatusBars());
		renderer.setShowStatusBars(false);
		assertFalse(renderer.isShowStatusBars());
		renderer.setShowStatusBars(true);
		assertTrue(renderer.isShowStatusBars());
	}
	
	@Test
	public void testIsShowName() {
		assertTrue(renderer.isShowName());
		renderer.setShowName(false);
		assertFalse(renderer.isShowName());
		renderer.setShowName(true);
		assertTrue(renderer.isShowName());
	}
	
	@Test
	public void testIsFillShield() {
		assertTrue(renderer.isFillShield());
		renderer.setFillShield(false);
		assertFalse(renderer.isFillShield());
		renderer.setFillShield(true);
		assertTrue(renderer.isFillShield());
	}
	
	@Test
	public void testPaintSelection() {
		renderer.paintSelection(g2d, robotSnapshot);
	}
	
	@Test
	public void testPaintShield() {
		renderer.paintShield(g2d, robotSnapshot);
	}
	
	@Test
	public void testPaintName() {
		renderer.paintName(g2d, robotSnapshot);
	}
	
	@Test
	public void testPaintHeat() {
		renderer.paintHeat(g2d, robotSnapshot);
	}
	
	@Test
	public void testPaintArmor() {
		renderer.paintArmor(g2d, robotSnapshot);
	}
	
	@Test
	public void testPaintTurret() {
		renderer.paintTurret(g2d, robotSnapshot);
	}
	
	@Test
	public void testPaintBody() {
		renderer.paintBody(g2d, robotSnapshot);
	}
}