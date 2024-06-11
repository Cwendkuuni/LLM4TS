import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.HashSet;
import java.awt.*;
import java.awt.geom.*;
import java.util.Set;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RobotRendererTest {

    @Mock
    private Graphics2D graphics2D;
    @Mock
    private RobotSnapshot robotSnapshot;
    @Mock
    private Set<Integer> selectedRobotIds;
    
    @InjectMocks
    private RobotRenderer robotRenderer;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testRender_WhenDeadRobotNotRendered() {
        when(robotSnapshot.isDead()).thenReturn(true);
        
        robotRenderer.render(graphics2D, robotSnapshot, selectedRobotIds);
        
        verifyZeroInteractions(graphics2D);
    }
    
    @Test
    public void testRender_WhenDeadRobotRendered() {
        when(robotSnapshot.isDead()).thenReturn(true);
        robotRenderer.setRenderDead(true);
        
        robotRenderer.render(graphics2D, robotSnapshot, selectedRobotIds);
        
        verify(graphics2D, times(2)).draw(any(Shape.class));
    }
    
    @Test
    public void testRender_WhenAliveRobotNotSelected() {
        when(robotSnapshot.isDead()).thenReturn(false);
        when(selectedRobotIds.contains(robotSnapshot.getId())).thenReturn(false);
        
        robotRenderer.render(graphics2D, robotSnapshot, selectedRobotIds);
        
        verify(graphics2D, times(6)).draw(any(Shape.class));
        verify(graphics2D, times(2)).fill(any(Shape.class));
    }
    
    @Test
    public void testRender_WhenAliveRobotSelected() {
        when(robotSnapshot.isDead()).thenReturn(false);
        when(selectedRobotIds.contains(robotSnapshot.getId())).thenReturn(true);
        
        robotRenderer.render(graphics2D, robotSnapshot, selectedRobotIds);
        
        verify(graphics2D, times(6)).draw(any(Shape.class));
        verify(graphics2D, times(3)).fill(any(Shape.class));
    }
    
    @Test
    public void testIsRenderDead() {
        robotRenderer.setRenderDead(true);
        
        boolean actual = robotRenderer.isRenderDead();
        
        assertTrue(actual);
    }
    
    @Test
    public void testSetRenderDead() {
        robotRenderer.setRenderDead(true);
        
        verify(robotSnapshot, times(1)).isDead();
    }
    
    @Test
    public void testIsShowStatusBars() {
        robotRenderer.setShowStatusBars(true);
        
        boolean actual = robotRenderer.isShowStatusBars();
        
        assertTrue(actual);
    }
    
    @Test
    public void testSetShowStatusBars() {
        robotRenderer.setShowStatusBars(true);
        
        verify(robotSnapshot, times(1)).isDead();
    }
    
    @Test
    public void testIsShowName() {
        robotRenderer.setShowName(true);
        
        boolean actual = robotRenderer.isShowName();
        
        assertTrue(actual);
    }
    
    @Test
    public void testSetShowName() {
        robotRenderer.setShowName(true);
        
        verify(robotSnapshot, times(1)).isDead();
    }
    
    @Test
    public void testIsFillShield() {
        robotRenderer.setFillShield(true);
        
        boolean actual = robotRenderer.isFillShield();
        
        assertTrue(actual);
    }
    
    @Test
    public void testSetFillShield() {
        robotRenderer.setFillShield(true);
        
        verify(robotSnapshot, times(1)).isActiveShield();
    }
    
    @Test
    public void testPaintSelection() {
        robotRenderer.paintSelection(graphics2D, robotSnapshot);
        
        verify(graphics2D, times(1)).setPaint(any(Color.class));
        verify(graphics2D, times(1)).fill(any(Shape.class));
    }
    
    @Test
    public void testPaintShield_WhenFillShieldTrue() {
        robotRenderer.setFillShield(true);
        when(robotSnapshot.isActiveShield()).thenReturn(true);
        
        robotRenderer.paintShield(graphics2D, robotSnapshot);
        
        verify(graphics2D, times(2)).fill(any(Shape.class));
    }
    
    @Test
    public void testPaintShield_WhenFillShieldFalse() {
        robotRenderer.setFillShield(false);
        when(robotSnapshot.isActiveShield()).thenReturn(true);
        
        robotRenderer.paintShield(graphics2D, robotSnapshot);
        
        verify(graphics2D, times(1)).draw(any(Shape.class));
        verify(graphics2D, never()).fill(any(Shape.class));
    }
    
    @Test
    public void testPaintShield_WhenNotActiveShield() {
        robotRenderer.setFillShield(true);
        when(robotSnapshot.isActiveShield()).thenReturn(false);
        
        robotRenderer.paintShield(graphics2D, robotSnapshot);
        
        verify(graphics2D, never()).draw(any(Shape.class));
        verify(graphics2D, never()).fill(any(Shape.class));
    }
    
    @Test
    public void testPaintName() {
        robotRenderer.paintName(graphics2D, robotSnapshot);
        
        verify(graphics2D, times(1)).setPaint(Color.yellow);
        verify(graphics2D, times(1)).drawString(anyString(), anyFloat(), anyFloat());
    }
    
    @Test
    public void testPaintHeat_WhenAliveRobot() {
        when(robotSnapshot.isDead()).thenReturn(false);
        
        robotRenderer.paintHeat(graphics2D, robotSnapshot);
        
        verify(graphics2D, times(1)).fill(any(Shape.class));
    }
    
    @Test
    public void testPaintHeat_WhenDeadRobot() {
        when(robotSnapshot.isDead()).thenReturn(true);
        
        robotRenderer.paintHeat(graphics2D, robotSnapshot);
        
        verify(graphics2D, never()).fill(any(Shape.class));
    }
    
    @Test
    public void testPaintArmor() {
        robotRenderer.paintArmor(graphics2D, robotSnapshot);
        
        verify(graphics2D, times(1)).fill(any(Shape.class));
        verify(graphics2D, times(1)).draw(any(Shape.class));
    }
    
    @Test
    public void testPaintTurret() {
        robotRenderer.paintTurret(graphics2D, robotSnapshot);
        
        verify(graphics2D, times(1)).draw(any(Shape.class));
    }
    
    @Test
    public void testPaintBody_WhenDeadRobot() {
        when(robotSnapshot.isDead()).thenReturn(true);
        
        robotRenderer.paintBody(graphics2D, robotSnapshot);
        
        verify(graphics2D, times(1)).draw(any(Shape.class));
        verify(graphics2D, never()).fill(any(Shape.class));
    }
    
    @Test
    public void testPaintBody_WhenAliveRobot() {
        when(robotSnapshot.isDead()).thenReturn(false);
        
        robotRenderer.paintBody(graphics2D, robotSnapshot);
        
        verify(graphics2D, times(1)).draw(any(Shape.class));
        verify(graphics2D, times(1)).fill(any(Shape.class));
    }
}