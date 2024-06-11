import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


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
        selectedRobotIds = Collections.singleton(1);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRender_RenderDeadIsTrueAndRobotIsDead_ShouldReturn() {
        robotRenderer.setRenderDead(true);
        when(robotSnapshot.isDead()).thenReturn(true);

        robotRenderer.render(graphics2D, robotSnapshot, selectedRobotIds);

        verifyZeroInteractions(graphics2D);
    }

    @Test
    public void testRender_RenderDeadIsTrueAndRobotIsAlive_ShouldCallAllPaintMethods() {
        robotRenderer.setRenderDead(true);
        when(robotSnapshot.isDead()).thenReturn(false);

        robotRenderer.render(graphics2D, robotSnapshot, selectedRobotIds);

        verify(robotRenderer).paintBody(any(Graphics2D.class), any(RobotSnapshot.class));
        verify(robotRenderer).paintShield(any(Graphics2D.class), any(RobotSnapshot.class));
        verify(robotRenderer).paintTurret(any(Graphics2D.class), any(RobotSnapshot.class));
        verify(robotRenderer).paintArmor(any(Graphics2D.class), any(RobotSnapshot.class));
        verify(robotRenderer).paintHeat(any(Graphics2D.class), any(RobotSnapshot.class));
        verify(robotRenderer).paintName(any(Graphics2D.class), any(RobotSnapshot.class));
    }

    @Test
    public void testRender_RenderDeadIsFalseAndRobotIsDead_ShouldCallPaintSelection() {
        robotRenderer.setRenderDead(false);
        when(robotSnapshot.isDead()).thenReturn(true);

        robotRenderer.render(graphics2D, robotSnapshot, selectedRobotIds);

        verify(robotRenderer).paintSelection(any(Graphics2D.class), any(RobotSnapshot.class));
    }

    @Test
    public void testRender_RenderDeadIsFalseAndRobotIsAlive_ShouldCallAllPaintMethods() {
        robotRenderer.setRenderDead(false);
        when(robotSnapshot.isDead()).thenReturn(false);

        robotRenderer.render(graphics2D, robotSnapshot, selectedRobotIds);

        verify(robotRenderer).paintBody(any(Graphics2D.class), any(RobotSnapshot.class));
        verify(robotRenderer).paintShield(any(Graphics2D.class), any(RobotSnapshot.class));
        verify(robotRenderer).paintTurret(any(Graphics2D.class), any(RobotSnapshot.class));
        verify(robotRenderer).paintArmor(any(Graphics2D.class), any(RobotSnapshot.class));
        verify(robotRenderer).paintHeat(any(Graphics2D.class), any(RobotSnapshot.class));
        verify(robotRenderer).paintName(any(Graphics2D.class), any(RobotSnapshot.class));
        verify(robotRenderer).paintSelection(any(Graphics2D.class), any(RobotSnapshot.class));
    }

    @Test
    public void testIsRenderDead_DefaultConstructor_ShouldReturnTrue() {
        assertTrue(robotRenderer.isRenderDead());
    }

    @Test
    public void testSetRenderDead_RenderDeadIsTrue_ShouldSetRenderDeadToTrue() {
        robotRenderer.setRenderDead(true);

        assertTrue(robotRenderer.isRenderDead());
    }

    @Test
    public void testSetRenderDead_RenderDeadIsFalse_ShouldSetRenderDeadToFalse() {
        robotRenderer.setRenderDead(false);

        assertFalse(robotRenderer.isRenderDead());
    }

    @Test
    public void testIsShowStatusBars_DefaultConstructor_ShouldReturnTrue() {
        assertTrue(robotRenderer.isShowStatusBars());
    }

    @Test
    public void testSetShowStatusBars_ShowStatusBarsIsTrue_ShouldSetShowStatusBarsToTrue() {
        robotRenderer.setShowStatusBars(true);

        assertTrue(robotRenderer.isShowStatusBars());
    }

    @Test
    public void testSetShowStatusBars_ShowStatusBarsIsFalse_ShouldSetShowStatusBarsToFalse() {
        robotRenderer.setShowStatusBars(false);

        assertFalse(robotRenderer.isShowStatusBars());
    }

    @Test
    public void testIsShowName_DefaultConstructor_ShouldReturnTrue() {
        assertTrue(robotRenderer.isShowName());
    }

    @Test
    public void testSetShowName_ShowNameIsTrue_ShouldSetShowNameToTrue() {
        robotRenderer.setShowName(true);

        assertTrue(robotRenderer.isShowName());
    }

    @Test
    public void testSetShowName_ShowNameIsFalse_ShouldSetShowNameToFalse() {
        robotRenderer.setShowName(false);

        assertFalse(robotRenderer.isShowName());
    }

    @Test
    public void testIsFillShield_DefaultConstructor_ShouldReturnTrue() {
        assertTrue(robotRenderer.isFillShield());
    }

    @Test
    public void testSetFillShield_FillShieldIsTrue_ShouldSetFillShieldToTrue() {
        robotRenderer.setFillShield(true);

        assertTrue(robotRenderer.isFillShield());
    }

    @Test
    public void testSetFillShield_FillShieldIsFalse_ShouldSetFillShieldToFalse() {
        robotRenderer.setFillShield(false);

        assertFalse(robotRenderer.isFillShield());
    }

    @Test
    public void testPaintSelection_ShouldCallGraphics2DFillMethod() {
        robotRenderer.paintSelection(graphics2D, robotSnapshot);
        
        verify(graphics2D).fill(any(Ellipse2D.Double.class));
    }

    @Test
    public void testPaintShield_WithFillShieldTrueAndActiveShieldIsTrue_ShouldCallGraphics2DFillMethod() {
        when(robotSnapshot.isActiveShield()).thenReturn(true);
        robotRenderer.setFillShield(true);

        robotRenderer.paintShield(graphics2D, robotSnapshot);

        verify(graphics2D, times(2)).fill(any(Ellipse2D.Double.class));
    }

    @Test
    public void testPaintShield_WithFillShieldTrueAndActiveShieldIsFalse_ShouldCallGraphics2DFillMethod() {
        when(robotSnapshot.isActiveShield()).thenReturn(false);
        robotRenderer.setFillShield(true);

        robotRenderer.paintShield(graphics2D, robotSnapshot);

        verify(graphics2D, times(0)).fill(any(Ellipse2D.Double.class));
    }

    @Test
    public void testPaintShield_WithFillShieldFalseAndActiveShieldIsTrue_ShouldCallGraphics2DDrawMethod() {
        when(robotSnapshot.isActiveShield()).thenReturn(true);
        robotRenderer.setFillShield(false);

        robotRenderer.paintShield(graphics2D, robotSnapshot);

        verify(graphics2D).draw(any(Ellipse2D.Double.class));
    }

    @Test
    public void testPaintShield_WithFillShieldFalseAndActiveShieldIsFalse_ShouldCallGraphics2DDrawMethod() {
        when(robotSnapshot.isActiveShield()).thenReturn(false);
        robotRenderer.setFillShield(false);

        robotRenderer.paintShield(graphics2D, robotSnapshot);

        verify(graphics2D, times(0)).draw(any(Ellipse2D.Double.class));
    }

    @Test
    public void testPaintName_ShouldCallGraphics2DDrawStringMethod() {
        robotRenderer.paintName(graphics2D, robotSnapshot);
        
        verify(graphics2D).drawString(anyString(), anyFloat(), anyFloat());
    }

    @Test
    public void testPaintHeat_WithAliveRobot_ShouldCallGraphics2DFillMethod() {
        when(robotSnapshot.isDead()).thenReturn(false);
        robotRenderer.paintHeat(graphics2D, robotSnapshot);
        
        verify(graphics2D).fill(any(Rectangle2D.Double.class));
    }

    @Test
    public void testPaintHeat_WithDeadRobot_ShouldNOTCallGraphics2DFillMethod() {
        when(robotSnapshot.isDead()).thenReturn(true);
        robotRenderer.paintHeat(graphics2D, robotSnapshot);
        
        verify(graphics2D, times(0)).fill(any(Rectangle2D.Double.class));
    }

    @Test
    public void testPaintArmor_ShouldCallGraphics2DFillAndDrawMethods() {
        robotRenderer.paintArmor(graphics2D, robotSnapshot);
        
        verify(graphics2D).fill(any(Rectangle2D.Double.class));
        verify(graphics2D).draw(any(Rectangle2D.Double.class));
    }

    @Test
    public void testPaintTurret_ShouldCallGraphics2DDrawMethod() {
        robotRenderer.paintTurret(graphics2D, robotSnapshot);
        
        verify(graphics2D).draw(any(Line2D.Double.class));
    }

    @Test
    public void testPaintBody_WithDeadRobot_ShouldCallGraphics2DDrawMethod() {
        when(robotSnapshot.isDead()).thenReturn(true);
        robotRenderer.paintBody(graphics2D, robotSnapshot);
        
        verify(graphics2D).draw(any(GeneralPath.class));
    }

    @Test
    public void testPaintBody_WithAliveRobot_ShouldCallGraphics2DFillMethod() {
        when(robotSnapshot.isDead()).thenReturn(false);
        robotRenderer.paintBody(graphics2D, robotSnapshot);
        
        verify(graphics2D).fill(any(GeneralPath.class));
    }

}