import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import net.virtualinfinity.atrobots.measures.AbsoluteAngle;
import net.virtualinfinity.atrobots.measures.RelativeAngle;
import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;

public class RobotRendererTest {

    private RobotRenderer robotRenderer;
    
    @Before
    public void setUp() {
        robotRenderer = new RobotRenderer();
    }
    
    @Test
    public void testRender_notRenderDeadAndRobotDead_shouldReturn() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        
        when(robotSnapshot.isDead()).thenReturn(true);
        
        robotRenderer.setRenderDead(false);
        
        robotRenderer.render(g2d, robotSnapshot, new HashSet<Integer>());
        
        verifyZeroInteractions(g2d);
    }
    
    @Test
    public void testRender_renderDeadAndRobotDead_shouldPaintBody() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        
        when(robotSnapshot.isDead()).thenReturn(true);
        
        robotRenderer.setRenderDead(true);
        
        robotRenderer.render(g2d, robotSnapshot, new HashSet<Integer>());
        
        verify(g2d).setPaint(Color.orange.darker().darker());
        verify(g2d, atLeastOnce()).draw(any(GeneralPath.class));
    }
    
    @Test
    public void testRender_notRenderDeadAndRobotAlive_shouldPaintBody() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        
        when(robotSnapshot.isDead()).thenReturn(false);
        
        robotRenderer.setRenderDead(false);
        
        robotRenderer.render(g2d, robotSnapshot, new HashSet<Integer>());
        
        verify(g2d).setPaint(Color.red);
        verify(g2d, atLeastOnce()).fill(any(GeneralPath.class));
    }
    
    @Test
    public void testRender_renderDeadAndRobotAlive_shouldPaintBody() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        
        when(robotSnapshot.isDead()).thenReturn(false);
        
        robotRenderer.setRenderDead(true);
        
        robotRenderer.render(g2d, robotSnapshot, new HashSet<Integer>());
        
        verify(g2d).setPaint(Color.red);
        verify(g2d, atLeastOnce()).fill(any(GeneralPath.class));
    }
    
    @Test
    public void testRender_showStatusBars_robotAlive_shouldPaintShield() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        
        when(robotSnapshot.isDead()).thenReturn(false);
        when(robotSnapshot.isActiveShield()).thenReturn(true);
        
        robotRenderer.setShowStatusBars(true);
        
        robotRenderer.render(g2d, robotSnapshot, new HashSet<Integer>());
        
        verify(g2d).fill(any(Ellipse2D.Double.class));
    }
    
    @Test
    public void testRender_showStatusBars_robotDead_shouldNotPaintShield() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        
        when(robotSnapshot.isDead()).thenReturn(true);
        when(robotSnapshot.isActiveShield()).thenReturn(true);
        
        robotRenderer.setShowStatusBars(true);
        
        robotRenderer.render(g2d, robotSnapshot, new HashSet<Integer>());
        
        verifyZeroInteractions(g2d);
    }
    
    @Test
    public void testRender_notShowStatusBars_robotDead_shouldNotPaintShield() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        
        when(robotSnapshot.isDead()).thenReturn(true);
        when(robotSnapshot.isActiveShield()).thenReturn(true);
        
        robotRenderer.setShowStatusBars(false);
        
        robotRenderer.render(g2d, robotSnapshot, new HashSet<Integer>());
        
        verifyZeroInteractions(g2d);
    }
    
    @Test
    public void testRender_showStatusBars_robotAlive_shouldPaintArmor() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        
        when(robotSnapshot.isDead()).thenReturn(false);
        when(robotSnapshot.getArmor()).thenReturn(10.0);
        
        robotRenderer.setShowStatusBars(true);
        
        robotRenderer.render(g2d, robotSnapshot, new HashSet<Integer>());
        
        verify(g2d).fill(any(Rectangle2D.Double.class));
        verify(g2d).draw(any(Rectangle2D.Double.class));
    }
    
    @Test
    public void testRender_showStatusBars_robotDead_shouldNotPaintArmor() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        
        when(robotSnapshot.isDead()).thenReturn(true);
        when(robotSnapshot.getArmor()).thenReturn(10.0);
        
        robotRenderer.setShowStatusBars(true);
        
        robotRenderer.render(g2d, robotSnapshot, new HashSet<Integer>());
        
        verifyZeroInteractions(g2d);
    }
    
    @Test
    public void testRender_notShowStatusBars_robotDead_shouldNotPaintArmor() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        
        when(robotSnapshot.isDead()).thenReturn(true);
        when(robotSnapshot.getArmor()).thenReturn(10.0);
        
        robotRenderer.setShowStatusBars(false);
        
        robotRenderer.render(g2d, robotSnapshot, new HashSet<Integer>());
        
        verifyZeroInteractions(g2d);
    }
    
    @Test
    public void testRender_showStatusBars_robotAlive_shouldPaintHeat() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        Temperature temperature = mock(Temperature.class);
        
        when(robotSnapshot.isDead()).thenReturn(false);
        when(robotSnapshot.getTemperature()).thenReturn(temperature);
        when(temperature.getLogScale()).thenReturn(10.0);
        
        robotRenderer.setShowStatusBars(true);
        
        robotRenderer.render(g2d, robotSnapshot, new HashSet<Integer>());
        
        verify(g2d).fill(any(Rectangle2D.Double.class));
        verify(g2d).draw(any(Rectangle2D.Double.class));
    }
    
    @Test
    public void testRender_showStatusBars_robotDead_shouldNotPaintHeat() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        Temperature temperature = mock(Temperature.class);
        
        when(robotSnapshot.isDead()).thenReturn(true);
        when(robotSnapshot.getTemperature()).thenReturn(temperature);
        when(temperature.getLogScale()).thenReturn(10.0);
        
        robotRenderer.setShowStatusBars(true);
        
        robotRenderer.render(g2d, robotSnapshot, new HashSet<Integer>());
        
        verifyZeroInteractions(g2d);
    }
    
    @Test
    public void testRender_notShowStatusBars_robotDead_shouldNotPaintHeat() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        Temperature temperature = mock(Temperature.class);
        
        when(robotSnapshot.isDead()).thenReturn(true);
        when(robotSnapshot.getTemperature()).thenReturn(temperature);
        when(temperature.getLogScale()).thenReturn(10.0);
        
        robotRenderer.setShowStatusBars(false);
        
        robotRenderer.render(g2d, robotSnapshot, new HashSet<Integer>());
        
        verifyZeroInteractions(g2d);
    }
    
    @Test
    public void testRender_showName_shouldPaintName() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        
        when(robotSnapshot.isDead()).thenReturn(false);
        when(robotSnapshot.getName()).thenReturn("TestName");
        
        robotRenderer.setShowName(true);
        
        robotRenderer.render(g2d, robotSnapshot, new HashSet<Integer>());
        
        verify(g2d).drawString("TestName", (float) (robotSnapshot.getX() - g2d.getFontMetrics().getStringBounds("TestName", g2d).getWidth() / 2.0),
                (float) (robotSnapshot.getY() - 10.0));
    }
    
    @Test
    public void testRender_notShowName_shouldNotPaintName() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        
        robotRenderer.setShowName(false);
        
        robotRenderer.render(g2d, robotSnapshot, new HashSet<Integer>());
        
        verifyZeroInteractions(g2d);
    }
    
    @Test
    public void testRender_robotInSelectedRobotIds_shouldPaintSelection() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        
        Set<Integer> selectedRobotIds = new HashSet<Integer>();
        selectedRobotIds.add(robotSnapshot.getId());
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        verify(g2d).fill(any(Ellipse2D.Double.class));
    }
    
    @Test
    public void testRender_robotNotInSelectedRobotIds_shouldNotPaintSelection() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        
        Set<Integer> selectedRobotIds = new HashSet<Integer>();
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        verifyZeroInteractions(g2d);
    }
}