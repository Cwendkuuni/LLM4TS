import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RobotRendererTest {

    private RobotRenderer robotRenderer;

    @BeforeEach
    void setUp() {
        robotRenderer = new RobotRenderer();
    }

    @Test
    void testRender_deadRobotNotRendered() {
        Graphics2D g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        RobotSnapshot deadRobotSnapshot = new RobotSnapshot();
        deadRobotSnapshot.setDead(true);

        robotRenderer.render(g2d, deadRobotSnapshot, new HashSet<>());

        // Verify that the graphics object was not affected
        assertNotEquals(Color.orange.darker().darker(), g2d.getPaint());
        assertEquals(1.0f, g2d.getStroke().getLineWidth());
    }

    @Test
    void testRender_liveRobotRendered() {
        Graphics2D g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        RobotSnapshot liveRobotSnapshot = new RobotSnapshot();
        liveRobotSnapshot.setDead(false);

        robotRenderer.render(g2d, liveRobotSnapshot, new HashSet<>());

        // Verify that the graphics object was affected
        assertEquals(Color.red, g2d.getPaint());
        assertNull(g2d.getStroke().getDashArray());
    }

    @Test
    void testRender_selectedRobotRenderedWithSelection() {
        Graphics2D g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        RobotSnapshot selectedRobotSnapshot = new RobotSnapshot();
        selectedRobotSnapshot.setId(1);
        Set<Integer> selectedRobotIds = new HashSet<>();
        selectedRobotIds.add(1);

        robotRenderer.render(g2d, selectedRobotSnapshot, selectedRobotIds);

        // Verify that the graphics object was affected
        assertEquals(new Color(1.0f, 1.0f, 0.0f, 0.25f), g2d.getPaint());
        assertTrue(g2d.getShape() instanceof Ellipse2D.Double);
    }

    @Test
    void testIsRenderDead_defaultValue() {
        assertTrue(robotRenderer.isRenderDead());
    }

    @Test
    void testSetRenderDead() {
        robotRenderer.setRenderDead(false);

        assertFalse(robotRenderer.isRenderDead());
    }

    @Test
    void testIsShowStatusBars_defaultValue() {
        assertTrue(robotRenderer.isShowStatusBars());
    }

    @Test
    void testSetShowStatusBars() {
        robotRenderer.setShowStatusBars(false);

        assertFalse(robotRenderer.isShowStatusBars());
    }

    @Test
    void testIsShowName_defaultValue() {
        assertTrue(robotRenderer.isShowName());
    }

    @Test
    void testSetShowName() {
        robotRenderer.setShowName(false);

        assertFalse(robotRenderer.isShowName());
    }

    @Test
    void testIsFillShield_defaultValue() {
        assertTrue(robotRenderer.isFillShield());
    }

    @Test
    void testSetFillShield() {
        robotRenderer.setFillShield(false);

        assertFalse(robotRenderer.isFillShield());
    }

    @Test
    void testPaintSelection() {
        Graphics2D g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setX(10.0);
        robotSnapshot.setY(10.0);

        robotRenderer.paintSelection(g2d, robotSnapshot);

        // Verify that the graphics object was affected
        assertEquals(new Color(1.0f, 1.0f, 0.0f, 0.25f), g2d.getPaint());
        assertTrue(g2d.getShape() instanceof Ellipse2D.Double);
    }

    @Test
    void testPaintShield_activeShieldAndFillShield() {
        Graphics2D g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setActiveShield(true);
        
        robotRenderer.paintShield(g2d, robotSnapshot);

        // Verify that the graphics object was affected
        assertEquals(2, g2d.getPaint().getColorComponents(null).length);
        assertTrue(g2d.getShape() instanceof Ellipse2D.Double);
    }

    @Test
    void testPaintShield_activeShieldAndNoFillShield() {
        Graphics2D g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setActiveShield(true);
        robotRenderer.setFillShield(false);

        robotRenderer.paintShield(g2d, robotSnapshot);

        // Verify that the graphics object was affected
        assertEquals(new Color(0.8f, 0.6f, 1.0f), g2d.getPaint());
        assertTrue(g2d.getShape() instanceof Ellipse2D.Double);
    }

    @Test
    void testPaintName() {
        Graphics2D g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setName("TestRobotName");
        robotSnapshot.setX(10.0);
        robotSnapshot.setY(10.0);

        robotRenderer.paintName(g2d, robotSnapshot);

        // Verify that the graphics object was affected
        assertEquals(Color.yellow, g2d.getPaint());
        assertNotNull(g2d.getStringBounds(robotSnapshot.getName(), g2d.getFontMetrics().getFontRenderContext()));
    }

    @Test
    void testPaintHeat_liveRobotNotDead() {
        Graphics2D g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        RobotSnapshot liveRobotSnapshot = new RobotSnapshot();
        liveRobotSnapshot.setDead(false);
        liveRobotSnapshot.getTemperature().setLogScale(0.5);

        robotRenderer.paintHeat(g2d, liveRobotSnapshot);

        // Verify that the graphics object was affected
        assertEquals(new Color(1.0f, 0.0f, 0.0f, 0.1f), g2d.getPaint());
        assertTrue(g2d.getShape() instanceof Rectangle2D.Double);
    }

    @Test
    void testPaintHeat_deadRobot() {
        Graphics2D g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        RobotSnapshot deadRobotSnapshot = new RobotSnapshot();
        deadRobotSnapshot.setDead(true);

        robotRenderer.paintHeat(g2d, deadRobotSnapshot);

        // Verify that the graphics object was not affected
        assertNull(g2d.getPaint());
    }

    @Test
    void testPaintArmor() {
        Graphics2D g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setArmor(50.0);

        robotRenderer.paintArmor(g2d, robotSnapshot);

        // Verify that the graphics object was affected
        assertEquals(new Color(0.0f, 0.0f, 1.0f, 0.6f), g2d.getPaint());
        assertTrue(g2d.getShape() instanceof Rectangle2D.Double);
        assertEquals(new Color(0.0f, 1.0f, 0.0f, 0.6f), g2d.getColor());
        assertTrue(g2d.getShape() instanceof Rectangle2D.Double);
    }

    @Test
    void testPaintTurret() {
        Graphics2D g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setX(10.0);
        robotSnapshot.setY(10.0);
        robotSnapshot.getTurretHeading().setCosine(0.5);
        robotSnapshot.getTurretHeading().setSine(0.5);

        robotRenderer.paintTurret(g2d, robotSnapshot);

        // Verify that the graphics object was affected
        assertEquals(Color.white, g2d.getPaint());
        assertTrue(g2d.getShape() instanceof Line2D);
    }

    @Test
    void testPaintBody_liveRobot() {
        Graphics2D g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        RobotSnapshot liveRobotSnapshot = new RobotSnapshot();
        liveRobotSnapshot.setDead(false);
        liveRobotSnapshot.setX(10.0);
        liveRobotSnapshot.setY(10.0);
        liveRobotSnapshot.getHeading().setCosine(0.5);
        liveRobotSnapshot.getHeading().setSine(0.5);

        robotRenderer.paintBody(g2d, liveRobotSnapshot);

        // Verify that the graphics object was affected
        assertEquals(Color.red, g2d.getPaint());
        assertTrue(g2d.getShape() instanceof GeneralPath);
        assertEquals(1.0f, g2d.getStroke().getLineWidth());
    }

    @Test
    void testPaintBody_deadRobot() {
        Graphics2D g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        RobotSnapshot deadRobotSnapshot = new RobotSnapshot();
        deadRobotSnapshot.setDead(true);

        robotRenderer.paintBody(g2d, deadRobotSnapshot);

        // Verify that the graphics object was affected
        assertEquals(Color.orange.darker().darker(), g2d.getPaint());
        assertTrue(g2d.getStroke() instanceof BasicStroke);
    }

    // Add more test cases for other methods...

}