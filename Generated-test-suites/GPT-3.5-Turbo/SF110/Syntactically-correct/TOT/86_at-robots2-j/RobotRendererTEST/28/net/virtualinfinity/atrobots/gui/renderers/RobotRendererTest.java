import org.junit.*;
import static org.junit.Assert.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.*;

public class RobotRendererTest {

    private RobotRenderer robotRenderer;
    
    @Before
    public void setUp() {
        robotRenderer = new RobotRenderer();
    }
    
    @Test
    public void testRender_WithDeadRobot_DeathRendered() {
        Graphics2D g2d = (Graphics2D) Toolkit.getDefaultToolkit().getScreenDevices()[0].getDefaultConfiguration().createCompatibleImage(1, 1).createGraphics();
        RobotSnapshot deadRobot = new RobotSnapshot(1, "DeadRobot", new Position(0.0, 0.0), new Angle(0.0), new Angle(0.0), 0.0, false, 0.0, 0.0, false);
        Set<Integer> selectedRobotIds = new HashSet<>();
        
        robotRenderer.setRenderDead(false);
        robotRenderer.render(g2d, deadRobot, selectedRobotIds);
        
        // Verify that nothing is painted when renderDead is false and the robot is dead
        assertEquals(0, g2d.getPaintedShapes().size());
    }
    
    @Test
    public void testRender_WithAliveRobot_BodyRendered() {
        Graphics2D g2d = (Graphics2D) Toolkit.getDefaultToolkit().getScreenDevices()[0].getDefaultConfiguration().createCompatibleImage(1, 1).createGraphics();
        RobotSnapshot aliveRobot = new RobotSnapshot(2, "AliveRobot", new Position(0.0, 0.0), new Angle(0.0), new Angle(0.0), 0.0, true, 0.0, 0.0, false);
        Set<Integer> selectedRobotIds = new HashSet<>();
        
        robotRenderer.render(g2d, aliveRobot, selectedRobotIds);
        
        // Verify that body is painted with correct color when the robot is alive
        assertTrue(g2d.getPaintedShapes().get(0) instanceof GeneralPath);
        assertEquals(Color.red, g2d.getPaint().getColor());
    }
    
    @Test
    public void testRender_WithActiveShield_ShieldRendered() {
        Graphics2D g2d = (Graphics2D) Toolkit.getDefaultToolkit().getScreenDevices()[0].getDefaultConfiguration().createCompatibleImage(1, 1).createGraphics();
        RobotSnapshot robotWithActiveShield = new RobotSnapshot(3, "RobotWithShield", new Position(0.0, 0.0), new Angle(0.0), new Angle(0.0), 0.0, true, 0.0, 0.0, true);
        Set<Integer> selectedRobotIds = new HashSet<>();
        
        robotRenderer.setFillShield(true);
        robotRenderer.render(g2d, robotWithActiveShield, selectedRobotIds);
        
        // Verify that shield is painted with correct color and shape when it is active and fillShield is true
        assertTrue(g2d.getPaintedShapes().get(0) instanceof Ellipse2D.Double);
        assertEquals(new Color(0.5f, 0.5f, 0.0f, 0.8f), g2d.getPaint().getColor());
    }
    
    @Test
    public void testRender_WithoutActiveShield_ShieldNotRendered() {
        Graphics2D g2d = (Graphics2D) Toolkit.getDefaultToolkit().getScreenDevices()[0].getDefaultConfiguration().createCompatibleImage(1, 1).createGraphics();
        RobotSnapshot robotWithoutActiveShield = new RobotSnapshot(4, "RobotWithoutShield", new Position(0.0, 0.0), new Angle(0.0), new Angle(0.0), 0.0, true, 0.0, 0.0, false);
        Set<Integer> selectedRobotIds = new HashSet<>();
        
        robotRenderer.setFillShield(true);
        robotRenderer.render(g2d, robotWithoutActiveShield, selectedRobotIds);
        
        // Verify that shield is not painted when it is not active
        assertEquals(0, g2d.getPaintedShapes().size());
    }
    
    @Test
    public void testRender_WithStatusBars_StatusBarsRendered() {
        Graphics2D g2d = (Graphics2D) Toolkit.getDefaultToolkit().getScreenDevices()[0].getDefaultConfiguration().createCompatibleImage(1, 1).createGraphics();
        RobotSnapshot robotWithStatusBars = new RobotSnapshot(5, "RobotWithStatusBars", new Position(0.0, 0.0), new Angle(0.0), new Angle(0.0), 0.0, true, 0.5, 0.5, true);
        Set<Integer> selectedRobotIds = new HashSet<>();
        
        robotRenderer.setShowStatusBars(true);
        robotRenderer.render(g2d, robotWithStatusBars, selectedRobotIds);
        
        // Verify that armor and heat status bars are painted when showStatusBars is true
        assertEquals(2, g2d.getPaintedShapes().size());
        assertTrue(g2d.getPaintedShapes().get(0) instanceof Rectangle2D.Double);
        assertTrue(g2d.getPaintedShapes().get(1) instanceof Rectangle2D.Double);
    }
    
    @Test
    public void testRender_WithoutStatusBars_StatusBarsNotRendered() {
        Graphics2D g2d = (Graphics2D) Toolkit.getDefaultToolkit().getScreenDevices()[0].getDefaultConfiguration().createCompatibleImage(1, 1).createGraphics();
        RobotSnapshot robotWithoutStatusBars = new RobotSnapshot(6, "RobotWithoutStatusBars", new Position(0.0, 0.0), new Angle(0.0), new Angle(0.0), 0.0, true, 0.0, 0.0, true);
        Set<Integer> selectedRobotIds = new HashSet<>();
        
        robotRenderer.setShowStatusBars(false);
        robotRenderer.render(g2d, robotWithoutStatusBars, selectedRobotIds);
        
        // Verify that status bars are not painted when showStatusBars is false
        assertEquals(0, g2d.getPaintedShapes().size());
    }
    
    @Test
    public void testRender_WithShowName_NameRendered() {
        Graphics2D g2d = (Graphics2D) Toolkit.getDefaultToolkit().getScreenDevices()[0].getDefaultConfiguration().createCompatibleImage(1, 1).createGraphics();
        RobotSnapshot robotWithShowName = new RobotSnapshot(7, "RobotWithShowName", new Position(0.0, 0.0), new Angle(0.0), new Angle(0.0), 0.0, true, 0.0, 0.0, true);
        Set<Integer> selectedRobotIds = new HashSet<>();
        
        robotRenderer.setShowName(true);
        robotRenderer.render(g2d, robotWithShowName, selectedRobotIds);
        
        // Verify that robot name is painted when showName is true
        assertTrue(g2d.getPaintedShapes().get(0) instanceof String);
        assertEquals(Color.yellow, g2d.getPaint().getColor());
    }
    
    @Test
    public void testRender_WithoutShowName_NameNotRendered() {
        Graphics2D g2d = (Graphics2D) Toolkit.getDefaultToolkit().getScreenDevices()[0].getDefaultConfiguration().createCompatibleImage(1, 1).createGraphics();
        RobotSnapshot robotWithoutShowName = new RobotSnapshot(8, "RobotWithoutShowName", new Position(0.0, 0.0), new Angle(0.0), new Angle(0.0), 0.0, true, 0.0, 0.0, true);
        Set<Integer> selectedRobotIds = new HashSet<>();
        
        robotRenderer.setShowName(false);
        robotRenderer.render(g2d, robotWithoutShowName, selectedRobotIds);
        
        // Verify that robot name is not painted when showName is false
        assertEquals(0, g2d.getPaintedShapes().size());
    }
    
    @Test
    public void testRender_WithSelectedRobot_SelectionRendered() {
        Graphics2D g2d = (Graphics2D) Toolkit.getDefaultToolkit().getScreenDevices()[0].getDefaultConfiguration().createCompatibleImage(1, 1).createGraphics();
        RobotSnapshot selectedRobot = new RobotSnapshot(9, "SelectedRobot", new Position(0.0, 0.0), new Angle(0.0), new Angle(0.0), 0.0, true, 0.0, 0.0, true);
        Set<Integer> selectedRobotIds = new HashSet<>();
        selectedRobotIds.add(selectedRobot.getId());
        
        robotRenderer.render(g2d, selectedRobot, selectedRobotIds);
        
        // Verify that selection is painted with the correct color and shape when the robot is selected
        assertTrue(g2d.getPaintedShapes().get(0) instanceof Ellipse2D.Double);
        assertEquals(new Color(1.0f, 1.0f, 0.0f, 0.25f), g2d.getPaint().getColor());
    }
    
    @Test
    public void testRender_WithoutSelectedRobot_SelectionNotRendered() {
        Graphics2D g2d = (Graphics2D) Toolkit.getDefaultToolkit().getScreenDevices()[0].getDefaultConfiguration().createCompatibleImage(1, 1).createGraphics();
        RobotSnapshot unselectedRobot = new RobotSnapshot(10, "UnselectedRobot", new Position(0.0, 0.0), new Angle(0.0), new Angle(0.0), 0.0, true, 0.0, 0.0, true);
        Set<Integer> selectedRobotIds = new HashSet<>();
        
        robotRenderer.render(g2d, unselectedRobot, selectedRobotIds);
        
        // Verify that selection is not painted when the robot is not selected
        assertEquals(0, g2d.getPaintedShapes().size());
    }
}