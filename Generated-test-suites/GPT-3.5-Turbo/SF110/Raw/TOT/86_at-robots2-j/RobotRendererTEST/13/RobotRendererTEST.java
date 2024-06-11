import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

public class RobotRendererTest {

    private RobotRenderer robotRenderer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        robotRenderer = new RobotRenderer();
    }

    @Test
    public void testRender_RenderDeadFalseAndRobotDead_ReturnsImmediately() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        when(robotSnapshot.isDead()).thenReturn(true);

        robotRenderer.setRenderDead(false);
        robotRenderer.render(g2d, robotSnapshot, new HashSet<>());

        verifyNoMoreInteractions(g2d);
    }

    @Test
    public void testRender_RenderDeadTrueAndRobotDead_RendersBody() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        when(robotSnapshot.isDead()).thenReturn(true);

        robotRenderer.setRenderDead(true);
        robotRenderer.render(g2d, robotSnapshot, new HashSet<>());

        // Verify paintBody was called
        verify(robotRenderer).paintBody(g2d, robotSnapshot);
    }

    @Test
    public void testRender_ActivateShield_RenderShieldBodyTurretAndStatusBarsIfEnabled() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        when(robotSnapshot.isActiveShield()).thenReturn(true);
        Set<Integer> selectedRobotIds = new HashSet<>();
        selectedRobotIds.add(robotSnapshot.getId());

        robotRenderer.setShowStatusBars(true);
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        // Verify paintShield, paintBody, paintTurret, paintArmor, paintHeat, paintName were called
        verify(robotRenderer).paintShield(g2d, robotSnapshot);
        verify(robotRenderer).paintBody(g2d, robotSnapshot);
        verify(robotRenderer).paintTurret(g2d, robotSnapshot);
        verify(robotRenderer).paintArmor(g2d, robotSnapshot);
        verify(robotRenderer).paintHeat(g2d, robotSnapshot);
        verify(robotRenderer).paintName(g2d, robotSnapshot);
        verify(robotRenderer).paintSelection(g2d, robotSnapshot);
    }

    @Test
    public void testRender_DeactivateShield_RendersBodyAndTurretOnly() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        when(robotSnapshot.isActiveShield()).thenReturn(false);

        robotRenderer.setFillShield(false);
        robotRenderer.render(g2d, robotSnapshot, new HashSet<>());

        // Verify paintBody and paintTurret were called
        verify(robotRenderer).paintBody(g2d, robotSnapshot);
        verify(robotRenderer).paintTurret(g2d, robotSnapshot);
    }

    @Test
    public void testSetFillShield_NoEffectAfterRendering() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        when(robotSnapshot.isActiveShield()).thenReturn(true);

        robotRenderer.setFillShield(false);
        robotRenderer.render(g2d, robotSnapshot, new HashSet<>());

        // Verify only paintBody and paintTurret were called
        verify(robotRenderer).paintBody(g2d, robotSnapshot);
        verify(robotRenderer).paintTurret(g2d, robotSnapshot);
        verifyNoMoreInteractions(g2d);
    }

    @Test
    public void testSetRenderDead_RenderDeadSetToFalse() {
        robotRenderer.setRenderDead(false);

        boolean result = robotRenderer.isRenderDead();

        assertFalse(result);
    }

    @Test
    public void testSetShowStatusBars_ShowStatusBarsSetToFalse() {
        robotRenderer.setShowStatusBars(false);

        boolean result = robotRenderer.isShowStatusBars();

        assertFalse(result);
    }

    @Test
    public void testSetShowName_ShowNameSetToFalse() {
        robotRenderer.setShowName(false);

        boolean result = robotRenderer.isShowName();

        assertFalse(result);
    }

    @Test
    public void testSetFillShield_FillShieldSetToFalse() {
        robotRenderer.setFillShield(false);

        boolean result = robotRenderer.isFillShield();

        assertFalse(result);
    }

    @Test
    public void testPaintSelection_PaintsSelectionEllipse() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);

        robotRenderer.paintSelection(g2d, robotSnapshot);

        // Verify fill method was called with an Ellipse2D.Double object
        verify(g2d).fill(any(Ellipse2D.Double.class));
    }

    @Test
    public void testPaintShield_ActiveShieldAndFillShieldTrue_PaintsFilledEllipse() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        when(robotSnapshot.isActiveShield()).thenReturn(true);

        robotRenderer.setFillShield(true);
        robotRenderer.paintShield(g2d, robotSnapshot);

        // Verify fill method was called with an Ellipse2D.Double object
        verify(g2d, times(2)).fill(any(Ellipse2D.Double.class));
    }

    @Test
    public void testPaintShield_ActiveShieldAndFillShieldFalse_PaintsUnfilledEllipse() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        when(robotSnapshot.isActiveShield()).thenReturn(true);

        robotRenderer.setFillShield(false);
        robotRenderer.paintShield(g2d, robotSnapshot);

        // Verify draw method was called with an Ellipse2D.Double object
        verify(g2d).draw(any(Ellipse2D.Double.class));
    }

    @Test
    public void testPaintName_PaintsNameAsYellowString() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);

        robotRenderer.paintName(g2d, robotSnapshot);

        // Verify setPaint and drawString methods were called with specific parameters
        verify(g2d).setPaint(Color.yellow);
        verify(g2d).drawString(eq(robotSnapshot.getName()), anyFloat(), anyFloat());
    }

    @Test
    public void testPaintHeat_NotDead_PaintsHeatGradient() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        when(robotSnapshot.isDead()).thenReturn(false);

        robotRenderer.paintHeat(g2d, robotSnapshot);

        // Verify setPaint and fill methods were called with specific parameters
        verify(g2d).setPaint(any(GradientPaint.class));
        verify(g2d).fill(any(Rectangle2D.Double.class));
        verify(g2d).draw(any(Rectangle2D.Double.class));
    }

    @Test
    public void testPaintHeat_Dead_DoesNotPaintHeatGradient() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        when(robotSnapshot.isDead()).thenReturn(true);

        robotRenderer.paintHeat(g2d, robotSnapshot);

        // Verify only draw method was called with a Rectangle2D.Double object
        verify(g2d).draw(any(Rectangle2D.Double.class));
        verifyNoMoreInteractions(g2d);
    }

    @Test
    public void testPaintArmor_PaintsArmorAndOutlineRectangles() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);

        robotRenderer.paintArmor(g2d, robotSnapshot);

        // Verify fill and draw methods were called with specific parameters
        verify(g2d).fill(any(Rectangle2D.Double.class));
        verify(g2d).draw(any(Rectangle2D.Double.class));
    }

    @Test
    public void testPaintTurret_PaintsLine() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);

        robotRenderer.paintTurret(g2d, robotSnapshot);

        // Verify draw method was called with a Line2D.Double object
        verify(g2d).draw(any(Line2D.Double.class));
    }

    @Test
    public void testPaintBody_Dead_RendersPathWithoutFilling() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        when(robotSnapshot.isDead()).thenReturn(true);

        robotRenderer.paintBody(g2d, robotSnapshot);

        // Verify draw method was called with a GeneralPath object
        verify(g2d).draw(any(GeneralPath.class));
        verifyNoMoreInteractions(g2d);
    }

    @Test
    public void testPaintBody_NotDead_RendersPathWithFilling() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        when(robotSnapshot.isDead()).thenReturn(false);

        robotRenderer.paintBody(g2d, robotSnapshot);

        // Verify fill method was called with a GeneralPath object
        verify(g2d).fill(any(GeneralPath.class));
    }
}