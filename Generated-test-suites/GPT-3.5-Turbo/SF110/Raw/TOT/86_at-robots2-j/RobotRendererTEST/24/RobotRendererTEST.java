import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RobotRendererTest {

    @Test
    public void testRender_DeadRobotNotRendered() {
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        Graphics2D g2d = mock(Graphics2D.class);
        Set<Integer> selectedRobotIds = mock(Set.class);

        when(robotSnapshot.isDead()).thenReturn(true);

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        verify(g2d, never()).fill(any(Ellipse2D.class));
        verify(g2d, never()).draw(any(Ellipse2D.class));
        verify(g2d, never()).setPaint(eq(Color.orange.darker().darker()));
        verify(g2d, never()).setPaint(eq(Color.red));
    }

    @Test
    public void testRender_AliveRobot_RenderAllComponents() {
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        Graphics2D g2d = mock(Graphics2D.class);
        Set<Integer> selectedRobotIds = mock(Set.class);

        when(robotSnapshot.isDead()).thenReturn(false);
        when(robotSnapshot.isActiveShield()).thenReturn(true);

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        verify(g2d, times(2)).fill(any(Ellipse2D.class));
        verify(g2d).setPaint(any(RadialGradientPaint.class));
        verify(g2d, times(2)).draw(any(Ellipse2D.class));
        verify(g2d).setPaint(eq(Color.yellow));
        verify(g2d).setPaint(any(GradientPaint.class));
        verify(g2d).fill(any(Rectangle2D.class));
        verify(g2d).draw(any(Rectangle2D.class));
        verify(g2d).setPaint(any(Color.class));
        verify(g2d).draw(any(Line2D.class));
        verify(g2d).setPaint(eq(Color.red));
        verify(g2d).fill(any(GeneralPath.class));
    }

    @Test
    public void testIsRenderDead_DefaultValue_ReturnsTrue() {
        RobotRenderer robotRenderer = new RobotRenderer();
        assertTrue(robotRenderer.isRenderDead());
    }

    @Test
    public void testSetRenderDead_NewValue_SetCorrectly() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setRenderDead(false);
        assertFalse(robotRenderer.isRenderDead());
    }

    @Test
    public void testIsShowStatusBars_DefaultValue_ReturnsTrue() {
        RobotRenderer robotRenderer = new RobotRenderer();
        assertTrue(robotRenderer.isShowStatusBars());
    }

    @Test
    public void testSetShowStatusBars_NewValue_SetCorrectly() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setShowStatusBars(false);
        assertFalse(robotRenderer.isShowStatusBars());
    }

    @Test
    public void testIsShowName_DefaultValue_ReturnsTrue() {
        RobotRenderer robotRenderer = new RobotRenderer();
        assertTrue(robotRenderer.isShowName());
    }

    @Test
    public void testSetShowName_NewValue_SetCorrectly() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setShowName(false);
        assertFalse(robotRenderer.isShowName());
    }

    @Test
    public void testIsFillShield_DefaultValue_ReturnsTrue() {
        RobotRenderer robotRenderer = new RobotRenderer();
        assertTrue(robotRenderer.isFillShield());
    }

    @Test
    public void testSetFillShield_NewValue_SetCorrectly() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setFillShield(false);
        assertFalse(robotRenderer.isFillShield());
    }

    @Test
    public void testPaintSelection_RenderSelection() {
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        Graphics2D g2d = mock(Graphics2D.class);

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintSelection(g2d, robotSnapshot);

        verify(g2d).setPaint(Color.yellow);
        verify(g2d).fill(any(Ellipse2D.class));
    }

    @Test
    public void testPaintShield_ActiveShield_RenderFilledOrOutlinedShield() {
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        Graphics2D g2d = mock(Graphics2D.class);

        when(robotSnapshot.isActiveShield()).thenReturn(true);

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintShield(g2d, robotSnapshot);

        verify(g2d, times(2)).fill(any(Ellipse2D.class));
        verify(g2d).setPaint(eq(Color(0.5f, 0.5f, 0.0f, 0.8f)));
        verify(g2d).setPaint(any(Color.class));
        verify(g2d).draw(any(Ellipse2D.class));
    }

    @Test
    public void testPaintShield_NotActiveShield_RenderNothing() {
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        Graphics2D g2d = mock(Graphics2D.class);

        when(robotSnapshot.isActiveShield()).thenReturn(false);

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintShield(g2d, robotSnapshot);

        verify(g2d, never()).fill(any(Ellipse2D.class));
        verify(g2d, never()).draw(any(Ellipse2D.class));
        verify(g2d, never()).setPaint(eq(Color(0.8f, 0.6f, 1.0f)));
    }

    @Test
    public void testPaintName_RenderName() {
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        Graphics2D g2d = mock(Graphics2D.class);

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintName(g2d, robotSnapshot);

        verify(g2d).setPaint(eq(Color.yellow));
        verify(g2d).drawString(anyString(), anyFloat(), anyFloat());
    }

    @Test
    public void testPaintHeat_RenderHeat() {
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        Graphics2D g2d = mock(Graphics2D.class);

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintHeat(g2d, robotSnapshot);

        verify(g2d, times(2)).fill(any(Rectangle2D.class));
        verify(g2d).setPaint(any(GradientPaint.class));
        verify(g2d).draw(any(Rectangle2D.class));
    }

    @Test
    public void testPaintArmor_RenderArmor() {
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        Graphics2D g2d = mock(Graphics2D.class);

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintArmor(g2d, robotSnapshot);

        verify(g2d).fill(any(Rectangle2D.class));
        verify(g2d).draw(any(Rectangle2D.class));
    }

    @Test
    public void testPaintTurret_RenderTurret() {
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        Graphics2D g2d = mock(Graphics2D.class);

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintTurret(g2d, robotSnapshot);

        verify(g2d).draw(any(Line2D.class));
    }

    @Test
    public void testPaintBody_RenderBody() {
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        Graphics2D g2d = mock(Graphics2D.class);

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintBody(g2d, robotSnapshot);

        verify(g2d, times(2)).fill(any(GeneralPath.class));
        verify(g2d).setStroke(any(Stroke.class));
        verify(g2d).setColor(eq(Color.orange.darker().darker()));
        verify(g2d).setColor(eq(Color.red));
        verify(g2d).draw(any(GeneralPath.class));
    }

}