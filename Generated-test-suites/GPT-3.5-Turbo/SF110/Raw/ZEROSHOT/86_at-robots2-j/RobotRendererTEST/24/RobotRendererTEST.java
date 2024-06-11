import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito.*;

import java.awt.*;
import java.util.*;
import java.awt.geom.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;

public class RobotRendererTest {
    private RobotRenderer robotRenderer;
    private Graphics2D graphics2D;
    private RobotSnapshot robotSnapshot;
    private Set<Integer> selectedRobotIds;
    
    @Before
    public void setUp() {
        robotRenderer = new RobotRenderer();
        graphics2D = mock(Graphics2D.class);
        robotSnapshot = mock(RobotSnapshot.class);
        selectedRobotIds = new HashSet<Integer>();
    }
    
    @Test
    public void testRender_RenderDeadFalseRobotDead_Returns() {
        when(robotSnapshot.isDead()).thenReturn(true);
        
        robotRenderer.setRenderDead(false);
        robotRenderer.render(graphics2D, robotSnapshot, selectedRobotIds);
        
        verifyZeroInteractions(graphics2D);
    }
    
    @Test
    public void testRender_RenderDeadTrueRobotDead_RenderMethodsCalled() {
        when(robotSnapshot.isDead()).thenReturn(true);
        
        robotRenderer.setRenderDead(true);
        robotRenderer.render(graphics2D, robotSnapshot, selectedRobotIds);
        
        verify(robotRenderer, times(1)).paintBody(any(Graphics2D.class), any(RobotSnapshot.class));
    }
    
    @Test
    public void testRender_RobotNotDead_RenderMethodsCalled() {    
        robotRenderer.render(graphics2D, robotSnapshot, selectedRobotIds);
        
        verify(robotRenderer, times(1)).paintBody(any(Graphics2D.class), any(RobotSnapshot.class));
        verify(robotRenderer, times(1)).paintShield(any(Graphics2D.class), any(RobotSnapshot.class));
        verify(robotRenderer, times(1)).paintTurret(any(Graphics2D.class), any(RobotSnapshot.class));
        verify(robotRenderer, times(1)).paintArmor(any(Graphics2D.class), any(RobotSnapshot.class));
        verify(robotRenderer, times(1)).paintHeat(any(Graphics2D.class), any(RobotSnapshot.class));
        verify(robotRenderer, times(1)).paintName(any(Graphics2D.class), any(RobotSnapshot.class));
    }
    
    @Test
    public void testRender_ShowStatusBarsFalse_NoArmorOrHeatRendered() {
        robotRenderer.setShowStatusBars(false);
        
        robotRenderer.render(graphics2D, robotSnapshot, selectedRobotIds);
        
        verifyZeroInteractions(graphics2D);
    }
    
    @Test
    public void testRender_ShowStatusBarsTrue_RenderArmorAndHeatMethodsCalled() {
        robotRenderer.setShowStatusBars(true);
        
        robotRenderer.render(graphics2D, robotSnapshot, selectedRobotIds);
        
        verify(robotRenderer, times(1)).paintArmor(any(Graphics2D.class), any(RobotSnapshot.class));
        verify(robotRenderer, times(1)).paintHeat(any(Graphics2D.class), any(RobotSnapshot.class));
    }
    
    @Test
    public void testRender_ShowNameFalse_NoNameRendered() {
        robotRenderer.setShowName(false);
        
        robotRenderer.render(graphics2D, robotSnapshot, selectedRobotIds);
        
        verifyZeroInteractions(graphics2D);
    }
    
    @Test
    public void testRender_ShowNameTrue_NameRendered() {
        robotRenderer.setShowName(true);
        
        robotRenderer.render(graphics2D, robotSnapshot, selectedRobotIds);
        
        verify(robotRenderer, times(1)).paintName(any(Graphics2D.class), any(RobotSnapshot.class));
    }
    
    @Test
    public void testRender_SelectedRobotIdsContainsId_SelectionRendered() {
        selectedRobotIds.add(1);
        
        robotRenderer.render(graphics2D, robotSnapshot, selectedRobotIds);
        
        verify(robotRenderer, times(1)).paintSelection(any(Graphics2D.class), any(RobotSnapshot.class));
    }
    
    @Test
    public void testIsRenderDead_DefaultValue_ReturnsTrue() {
        assertTrue(robotRenderer.isRenderDead());
    }
    
    @Test
    public void testSetRenderDead_ValueSupplied_ValueSet() {
        robotRenderer.setRenderDead(false);
        
        assertFalse(robotRenderer.isRenderDead());
    }
    
    @Test
    public void testIsShowStatusBars_DefaultValue_ReturnsTrue() {
        assertTrue(robotRenderer.isShowStatusBars());
    }
    
    @Test
    public void testSetShowStatusBars_ValueSupplied_ValueSet() {
        robotRenderer.setShowStatusBars(false);
        
        assertFalse(robotRenderer.isShowStatusBars());
    }
    
    @Test
    public void testIsShowName_DefaultValue_ReturnsTrue() {
        assertTrue(robotRenderer.isShowName());
    }
    
    @Test
    public void testSetShowName_ValueSupplied_ValueSet() {
        robotRenderer.setShowName(false);
        
        assertFalse(robotRenderer.isShowName());
    }
    
    @Test
    public void testIsFillShield_DefaultValue_ReturnsTrue() {
        assertTrue(robotRenderer.isFillShield());
    }
    
    @Test
    public void testSetFillShield_ValueSupplied_ValueSet() {
        robotRenderer.setFillShield(false);
        
        assertFalse(robotRenderer.isFillShield());
    }
    
    @Test
    public void testPaintSelection_SelectedRobotIdSupplied_PaintCalled() {
        robotRenderer.paintSelection(graphics2D, robotSnapshot);
        
        verify(graphics2D, times(1)).setPaint(any(Color.class));
        verify(graphics2D, times(1)).fill(any(Ellipse2D.Double.class));
    }
    
    @Test
    public void testPaintShield_ActiveShieldFalse_NothingRendered() {
        when(robotSnapshot.isActiveShield()).thenReturn(false);
        
        robotRenderer.paintShield(graphics2D, robotSnapshot);
        
        verifyZeroInteractions(graphics2D);
    }
    
    @Test
    public void testPaintShield_ActiveShieldTrue_FillShieldTrue_RenderFillShield() {
        when(robotSnapshot.isActiveShield()).thenReturn(true);
        robotRenderer.setFillShield(true);
        
        robotRenderer.paintShield(graphics2D, robotSnapshot);
        
        verify(graphics2D, times(2)).fill(any(Ellipse2D.Double.class));
    }
    
    @Test
    public void testPaintShield_ActiveShieldTrue_FillShieldFalse_RenderStrokeShield() {
        when(robotSnapshot.isActiveShield()).thenReturn(true);
        robotRenderer.setFillShield(false);
        
        robotRenderer.paintShield(graphics2D, robotSnapshot);
        
        verify(graphics2D, times(1)).draw(any(Ellipse2D.Double.class));
    }
    
    @Test
    public void testPaintName_NameSupplied_MethodCalledWithCorrectArguments() {
        when(robotSnapshot.getName()).thenReturn("testName");
        
        robotRenderer.paintName(graphics2D, robotSnapshot);
        
        verify(graphics2D, times(1)).setPaint(Color.yellow);
        verify(graphics2D, times(1)).drawString("testName", 0.0f, 0.0f);
    }
    
    @Test
    public void testPaintHeat_HeatColorSupplied_MethodCalledWithCorrectArguments() {
        Color color1 = new Color(255, 0, 0, 25);
        Color color2 = new Color(255, 255, 0);
        when(robotSnapshot.getTemperature()).thenReturn(0);
        
        robotRenderer.paintHeat(graphics2D, robotSnapshot);
        
        verify(graphics2D, times(1)).setPaint(any(GradientPaint.class));
        verify(graphics2D, times(1)).fill(any(Rectangle2D.Double.class));
    }
    
    @Test
    public void testPaintArmor_ArmorSupplied_MethodCalledWithCorrectArguments() {
        when(robotSnapshot.getArmor()).thenReturn(50.0);
        
        robotRenderer.paintArmor(graphics2D, robotSnapshot);
        
        verify(graphics2D, times(1)).setPaint(any(Color.class));
        verify(graphics2D, times(1)).fill(any(Rectangle2D.Double.class));
        verify(graphics2D, times(1)).draw(any(Rectangle2D.Double.class));
    }
    
    @Test
    public void testPaintTurret_TurretHeadingSupplied_MethodCalledWithCorrectArguments() {
        when(robotSnapshot.getTurretHeading()).thenReturn(new RelativeAngle());
        
        robotRenderer.paintTurret(graphics2D, robotSnapshot);
        
        verify(graphics2D, times(1)).setPaint(Color.white);
        verify(graphics2D, times(1)).draw(any(Line2D.Double.class));
    }
    
    @Test
    public void testPaintBody_HeadingSupplied_MethodCalledWithCorrectArguments() {
        when(robotSnapshot.getHeading()).thenReturn(new RelativeAngle());
        
        robotRenderer.paintBody(graphics2D, robotSnapshot);
        
        verify(graphics2D, times(1)).setPaint(any(Color.class));
        verify(graphics2D, times(1)).draw(any(GeneralPath.class));
        verify(graphics2D, times(1)).fill(any(GeneralPath.class));
        verifyNoMoreInteractions(graphics2D);
    }
}