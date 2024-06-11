import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class RobotRendererTest {

    RobotRenderer robotRenderer;

    @Before
    public void setup() {
        robotRenderer = new RobotRenderer();
    }

    @Test
    public void testIsRenderDead() {
        assertEquals(true, robotRenderer.isRenderDead());
        robotRenderer.setRenderDead(false);
        assertEquals(false, robotRenderer.isRenderDead());
    }

    @Test
    public void testShowStatusBars() {
        assertEquals(true, robotRenderer.isShowStatusBars());
        robotRenderer.setShowStatusBars(false);
        assertEquals(false, robotRenderer.isShowStatusBars());
    }

    @Test
    public void testShowName() {
        assertEquals(true, robotRenderer.isShowName());
        robotRenderer.setShowName(false);
        assertEquals(false, robotRenderer.isShowName());
    }

    @Test
    public void testFillShield() {
        assertEquals(true, robotRenderer.isFillShield());
        robotRenderer.setFillShield(false);
        assertEquals(false, robotRenderer.isFillShield());
    }

    @Test
    public void testRender() {
        Graphics2D g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        Set<Integer> selectedRobotIds = new HashSet<>();

        // Test when RenderDead is true and robotSnapshot is dead
        robotRenderer.setRenderDead(true);
        robotSnapshot.setDead(true);
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        // Verify that nothing is rendered since the renderDead condition is not met and the robot is dead
        assertNull(g2d.getPaint());

        // Test when RenderDead is true and robotSnapshot is not dead
        g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        robotSnapshot.setDead(false);
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        // Verify that the different parts of the robot are rendered properly
        assertNotNull(g2d.getPaint());
        assertNotNull(g2d.draw());
        assertNotNull(g2d.fill());

        // Test when RenderDead is false and robotSnapshot is dead
        g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        robotRenderer.setRenderDead(false);
        robotSnapshot.setDead(true);
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        // Verify that the different parts of the robot are rendered properly
        assertNotNull(g2d.getPaint());
        assertNotNull(g2d.draw());
        assertNull(g2d.fill());

        // Test when RenderDead is false and robotSnapshot is not dead
        g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        robotSnapshot.setDead(false);
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        // Verify that the different parts of the robot are rendered properly
        assertNotNull(g2d.getPaint());
        assertNotNull(g2d.draw());
        assertNotNull(g2d.fill());

        // Test when selectedRobotIds contains robotSnapshot's id
        g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        selectedRobotIds.add(robotSnapshot.getId());
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        // Verify that the selection is painted
        assertNotNull(g2d.getPaint());
        assertNotNull(g2d.fill());
    }

    @Test
    public void testPaintSelection() {
        Graphics2D g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        Set<Integer> selectedRobotIds = new HashSet<>();

        // Test when robotSnapshot is not selected
        robotRenderer.paintSelection(g2d, robotSnapshot);
        // Verify that nothing is painted
        assertNull(g2d.getPaint());

        // Test when robotSnapshot is selected
        g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        selectedRobotIds.add(robotSnapshot.getId());
        robotRenderer.paintSelection(g2d, robotSnapshot);
        // Verify that the selection is painted
        assertNotNull(g2d.getPaint());
        assertNotNull(g2d.fill());
    }

    @Test
    public void testPaintShield() {
        Graphics2D g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        RobotSnapshot robotSnapshot = new RobotSnapshot();

        // Test when fillShield is true and robot has an active shield
        robotRenderer.setFillShield(true);
        robotSnapshot.setActiveShield(true);
        robotRenderer.paintShield(g2d, robotSnapshot);
        // Verify that the shield is filled
        assertNotNull(g2d.getPaint());
        assertNotNull(g2d.fill());

        // Test when fillShield is true and robot does not have an active shield
        g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        robotSnapshot.setActiveShield(false);
        robotRenderer.paintShield(g2d, robotSnapshot);
        // Verify that the shield is not filled
        assertNull(g2d.getPaint());
        assertNotNull(g2d.draw());

        // Test when fillShield is false and robot has an active shield
        g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        robotRenderer.setFillShield(false);
        robotSnapshot.setActiveShield(true);
        robotRenderer.paintShield(g2d, robotSnapshot);
        // Verify that the shield is not filled
        assertNull(g2d.getPaint());
        assertNotNull(g2d.draw());

        // Test when fillShield is false and robot does not have an active shield
        g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        robotSnapshot.setActiveShield(false);
        robotRenderer.paintShield(g2d, robotSnapshot);
        // Verify that the shield is not filled
        assertNull(g2d.getPaint());
        assertNotNull(g2d.draw());
    }

    @Test
    public void testPaintName() {
        Graphics2D g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        RobotSnapshot robotSnapshot = new RobotSnapshot();

        // Test when showName is true
        robotRenderer.setShowName(true);
        robotSnapshot.setName("Robot1");
        robotRenderer.paintName(g2d, robotSnapshot);
        // Verify that the name is painted
        assertEquals(Color.yellow, g2d.getPaint());
        assertNotNull(g2d.drawString());

        // Test when showName is false
        g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        robotRenderer.setShowName(false);
        robotSnapshot.setName("Robot1");
        robotRenderer.paintName(g2d, robotSnapshot);
        // Verify that the name is not painted
        assertNull(g2d.getPaint());
        assertNull(g2d.drawString());
    }

    @Test
    public void testPaintHeat() {
        Graphics2D g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        RobotSnapshot robotSnapshot = new RobotSnapshot();

        // Test when robot is not dead
        robotSnapshot.setDead(false);
        robotSnapshot.setTemperature(new Measure(0.5));
        robotRenderer.paintHeat(g2d, robotSnapshot);
        // Verify that the heat is painted
        assertNotNull(g2d.getPaint());
        assertNotNull(g2d.fill());

        // Test when robot is dead
        g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        robotSnapshot.setDead(true);
        robotRenderer.paintHeat(g2d, robotSnapshot);
        // Verify that the heat is not painted
        assertNull(g2d.getPaint());
        assertNotNull(g2d.draw());
    }

    @Test
    public void testPaintArmor() {
        Graphics2D g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        RobotSnapshot robotSnapshot = new RobotSnapshot();

        robotSnapshot.setArmor(50);
        robotRenderer.paintArmor(g2d, robotSnapshot);
        // Verify that the armor is painted
        assertNotNull(g2d.getPaint());
        assertNotNull(g2d.fill());
        assertNotNull(g2d.draw());
    }

    @Test
    public void testPaintTurret() {
        Graphics2D g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        RobotSnapshot robotSnapshot = new RobotSnapshot();

        robotRenderer.paintTurret(g2d, robotSnapshot);
        // Verify that the turret is painted
        assertEquals(Color.white, g2d.getPaint());
        assertNotNull(g2d.draw());
    }

    @Test
    public void testPaintBody() {
        Graphics2D g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        RobotSnapshot robotSnapshot = new RobotSnapshot();

        // Test when robot is not dead
        robotSnapshot.setDead(false);
        robotRenderer.paintBody(g2d, robotSnapshot);
        // Verify that the body is filled
        assertEquals(Color.red, g2d.getPaint());
        assertNotNull(g2d.fill());

        // Test when robot is dead
        g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        robotSnapshot.setDead(true);
        robotRenderer.paintBody(g2d, robotSnapshot);
        // Verify that the body is not filled
        assertEquals(Color.orange.darker().darker(), g2d.getPaint());
        assertNotNull(g2d.draw());
    }

}