import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

public class RobotRendererTest {

    @Test
    public void testRender_withDeadRobotNoRenderDead_shouldNotRenderBody() {
        RobotSnapshot robotSnapshot = Mockito.mock(RobotSnapshot.class);
        Graphics2D g2d = Mockito.mock(Graphics2D.class);
        Set<Integer> selectedRobotIds = new HashSet<>();
        selectedRobotIds.add(robotSnapshot.getId());
        
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setRenderDead(false);
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        Mockito.verify(g2d, Mockito.never()).draw(Mockito.any(GeneralPath.class));
        Mockito.verify(g2d, Mockito.never()).fill(Mockito.any(GeneralPath.class));
    }
    
    @Test
    public void testRender_withActiveShieldAndFillShield_shouldFillShield() {
        RobotSnapshot robotSnapshot = Mockito.mock(RobotSnapshot.class);
        Graphics2D g2d = Mockito.mock(Graphics2D.class);
        Set<Integer> selectedRobotIds = new HashSet<>();
        
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setFillShield(true);
        Mockito.when(robotSnapshot.isActiveShield()).thenReturn(true);
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        Mockito.verify(g2d).fill(Mockito.any(Ellipse2D.Double.class));
        Mockito.verify(g2d, Mockito.never()).draw(Mockito.any(Ellipse2D.Double.class));
    }
    
    @Test
    public void testRender_withActiveShieldAndNotFillShield_shouldDrawShield() {
        RobotSnapshot robotSnapshot = Mockito.mock(RobotSnapshot.class);
        Graphics2D g2d = Mockito.mock(Graphics2D.class);
        Set<Integer> selectedRobotIds = new HashSet<>();
        
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setFillShield(false);
        Mockito.when(robotSnapshot.isActiveShield()).thenReturn(true);
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        Mockito.verify(g2d, Mockito.never()).fill(Mockito.any(Ellipse2D.Double.class));
        Mockito.verify(g2d).draw(Mockito.any(Ellipse2D.Double.class));
    }
    
    @Test
    public void testRender_withShowStatusBars_shouldPaintArmorAndHeat() {
        RobotSnapshot robotSnapshot = Mockito.mock(RobotSnapshot.class);
        Graphics2D g2d = Mockito.mock(Graphics2D.class);
        Set<Integer> selectedRobotIds = new HashSet<>();
        
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setShowStatusBars(true);
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        Mockito.verify(g2d).fill(Mockito.any(Rectangle2D.Double.class));
        Mockito.verify(g2d).draw(Mockito.any(Rectangle2D.Double.class));
    }
    
    @Test
    public void testRender_withShowName_shouldPaintName() {
        RobotSnapshot robotSnapshot = Mockito.mock(RobotSnapshot.class);
        Graphics2D g2d = Mockito.mock(Graphics2D.class);
        Set<Integer> selectedRobotIds = new HashSet<>();
        
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setShowName(true);
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        Mockito.verify(g2d).drawString(Mockito.anyString(), Mockito.anyFloat(), Mockito.anyFloat());
    }
    
    @Test
    public void testRender_withSelectedRobot_shouldPaintSelection() {
        RobotSnapshot robotSnapshot = Mockito.mock(RobotSnapshot.class);
        Graphics2D g2d = Mockito.mock(Graphics2D.class);
        Set<Integer> selectedRobotIds = new HashSet<>();
        selectedRobotIds.add(robotSnapshot.getId());
        
        RobotRenderer robotRenderer = new RobotRenderer();
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        Mockito.verify(g2d).fill(Mockito.any(Ellipse2D.Double.class));
    }
    
    @Test
    public void testPaintSelection_shouldSetPaintToYellow() {
        RobotSnapshot robotSnapshot = Mockito.mock(RobotSnapshot.class);
        Graphics2D g2d = Mockito.mock(Graphics2D.class);
        
        RobotRenderer robotRenderer = new RobotRenderer();
        
        robotRenderer.paintSelection(g2d, robotSnapshot);
        
        Mockito.verify(g2d).setPaint(Color.yellow);
    }
    
    @Test
    public void testPaintHeat_withDeadRobot_shouldNotFillHeat() {
        RobotSnapshot robotSnapshot = Mockito.mock(RobotSnapshot.class);
        Graphics2D g2d = Mockito.mock(Graphics2D.class);
        
        RobotRenderer robotRenderer = new RobotRenderer();
        Mockito.when(robotSnapshot.isDead()).thenReturn(true);
        
        robotRenderer.paintHeat(g2d, robotSnapshot);
        
        Mockito.verify(g2d, Mockito.never()).fill(Mockito.any(Rectangle2D.Double.class));
    }
    
    @Test
    public void testPaintArmor_shouldFillArmor() {
        RobotSnapshot robotSnapshot = Mockito.mock(RobotSnapshot.class);
        Graphics2D g2d = Mockito.mock(Graphics2D.class);
        
        RobotRenderer robotRenderer = new RobotRenderer();
        
        robotRenderer.paintArmor(g2d, robotSnapshot);
        
        Mockito.verify(g2d).fill(Mockito.any(Rectangle2D.Double.class));
    }
    
    @Test
    public void testPaintTurret_shouldSetPaintToWhite() {
        RobotSnapshot robotSnapshot = Mockito.mock(RobotSnapshot.class);
        Graphics2D g2d = Mockito.mock(Graphics2D.class);
        
        RobotRenderer robotRenderer = new RobotRenderer();
        
        robotRenderer.paintTurret(g2d, robotSnapshot);
        
        Mockito.verify(g2d).setPaint(Color.white);
    }
    
    @Test
    public void testPaintBody_withDeadRobot_shouldSetPaintToOrangeDarkerDarker() {
        RobotSnapshot robotSnapshot = Mockito.mock(RobotSnapshot.class);
        Graphics2D g2d = Mockito.mock(Graphics2D.class);
        
        RobotRenderer robotRenderer = new RobotRenderer();
        Mockito.when(robotSnapshot.isDead()).thenReturn(true);
        
        robotRenderer.paintBody(g2d, robotSnapshot);
        
        Mockito.verify(g2d).setPaint(Color.orange.darker().darker());
    }

}