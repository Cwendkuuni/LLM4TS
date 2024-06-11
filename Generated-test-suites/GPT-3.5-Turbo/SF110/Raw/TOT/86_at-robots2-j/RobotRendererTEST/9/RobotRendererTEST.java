import org.junit.*;
import static org.junit.Assert.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class RobotRendererTest {

    @Test
    public void testRender_deadRobotNotRendered() {
        RobotRenderer robotRenderer = new RobotRenderer();
        Graphics2D g2d = new Graphics2DStub();
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setDead(true);
        Set<Integer> selectedRobotIds = new HashSet<>();
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        // Assert that the body is not painted
        assertEquals(0, g2d.getPaintedShapes().size());
    }
    
    @Test
    public void testRender_aliveRobotRendered() {
        RobotRenderer robotRenderer = new RobotRenderer();
        Graphics2D g2d = new Graphics2DStub();
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setDead(false);
        Set<Integer> selectedRobotIds = new HashSet<>();
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        // Assert that the body is painted
        assertEquals(1, g2d.getPaintedShapes().size());
        assertTrue(g2d.getPaintedShape(0) instanceof GeneralPath);
    }
    
    @Test
    public void testRender_activeShieldFilled() {
        RobotRenderer robotRenderer = new RobotRenderer();
        Graphics2D g2d = new Graphics2DStub();
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setActiveShield(true);
        Set<Integer> selectedRobotIds = new HashSet<>();
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        // Assert that the shield is filled
        assertEquals(1, g2d.getFilledShapes().size());
        assertTrue(g2d.getFilledShape(0) instanceof Ellipse2D.Double);
    }
    
    @Test
    public void testRender_inactiveShieldOutlined() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setFillShield(false); // Set fillShield to false
        Graphics2D g2d = new Graphics2DStub();
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setActiveShield(true);
        Set<Integer> selectedRobotIds = new HashSet<>();
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        // Assert that the shield is outlined
        assertEquals(1, g2d.getOutlinedShapes().size());
        assertTrue(g2d.getOutlinedShape(0) instanceof Ellipse2D.Double);
    }
    
    @Test
    public void testRender_statusBarsDisplayed() {
        RobotRenderer robotRenderer = new RobotRenderer();
        Graphics2D g2d = new Graphics2DStub();
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setDead(false);
        Set<Integer> selectedRobotIds = new HashSet<>();
        
        // Enable showStatusBars
        robotRenderer.setShowStatusBars(true);
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        // Assert that the armor and heat bars are displayed
        assertEquals(2, g2d.getFilledShapes().size());
        assertTrue(g2d.getFilledShape(0) instanceof Rectangle2D.Double);
        assertTrue(g2d.getFilledShape(1) instanceof Rectangle2D.Double);
    }
    
    @Test
    public void testRender_statusBarsNotDisplayed() {
        RobotRenderer robotRenderer = new RobotRenderer();
        Graphics2D g2d = new Graphics2DStub();
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setDead(false);
        Set<Integer> selectedRobotIds = new HashSet<>();
        
        // Disable showStatusBars
        robotRenderer.setShowStatusBars(false);
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        // Assert that the armor and heat bars are not displayed
        assertEquals(0, g2d.getFilledShapes().size());
    }
    
    @Test
    public void testRender_nameDisplayed() {
        RobotRenderer robotRenderer = new RobotRenderer();
        Graphics2D g2d = new Graphics2DStub();
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setDead(false);
        Set<Integer> selectedRobotIds = new HashSet<>();
        
        // Enable showName
        robotRenderer.setShowName(true);
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        // Assert that the name is displayed
        assertEquals(1, g2d.getStrings().size());
        assertEquals(robotSnapshot.getName(), g2d.getString(0));
    }
    
    @Test
    public void testRender_nameNotDisplayed() {
        RobotRenderer robotRenderer = new RobotRenderer();
        Graphics2D g2d = new Graphics2DStub();
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setDead(false);
        Set<Integer> selectedRobotIds = new HashSet<>();
        
        // Disable showName
        robotRenderer.setShowName(false);
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        // Assert that the name is not displayed
        assertEquals(0, g2d.getStrings().size());
    }
    
    @Test
    public void testRender_robotSelected() {
        RobotRenderer robotRenderer = new RobotRenderer();
        Graphics2D g2d = new Graphics2DStub();
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setDead(false);
        Set<Integer> selectedRobotIds = new HashSet<>();
        selectedRobotIds.add(robotSnapshot.getId());
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        // Assert that the selection is painted
        assertEquals(1, g2d.getFilledShapes().size());
        assertTrue(g2d.getFilledShape(0) instanceof Ellipse2D.Double);
    }
    
    // Test cases for individual methods omitted for brevity
    
}