package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.GradientPaint;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import net.virtualinfinity.atrobots.measures.RelativeAngle;
import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;

public class RobotRendererTest {
    private RobotRenderer robotRenderer;
    private Graphics2D g2d;
    private RobotSnapshot robotSnapshot;
    private Set<Integer> selectedRobotIds;

    @Before
    public void setUp() {
        robotRenderer = new RobotRenderer();
        g2d = mock(Graphics2D.class);
        robotSnapshot = mock(RobotSnapshot.class);
        selectedRobotIds = new HashSet<>();
    }

    @Test
    public void testRender_NotDeadRenderDead_False() {
        robotRenderer.setRenderDead(false);
        when(robotSnapshot.isDead()).thenReturn(true);

        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        assertFalse(robotRenderer.isRenderDead());
    }

    @Test
    public void testRender_NotDeadRenderDead_True() {
        robotRenderer.setRenderDead(true);
        when(robotSnapshot.isDead()).thenReturn(true);

        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        assertTrue(robotRenderer.isRenderDead());
    }

    @Test
    public void testRender_WithActiveShield() {
        when(robotSnapshot.isActiveShield()).thenReturn(true);
        when(robotSnapshot.getX()).thenReturn(0.0);
        when(robotSnapshot.getY()).thenReturn(0.0);
        ArgumentCaptor<Ellipse2D.Double> ellipseCaptor = ArgumentCaptor.forClass(Ellipse2D.Double.class);

        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        assertTrue(robotRenderer.isFillShield());
        assertTrue(robotRenderer.isShowStatusBars());
        assertTrue(robotRenderer.isShowName());
        assertTrue(robotRenderer.isRenderDead());
        assertTrue(robotRenderer.isFillShield());
        assertTrue(robotRenderer.isShowStatusBars());
        assertTrue(robotRenderer.isShowName());
        assertTrue(robotRenderer.isRenderDead());
        assertTrue(robotRenderer.isFillShield());
        when(g2d.getPaint()).thenReturn(mock(RadialGradientPaint.class));
        assertTrue(robotRenderer.isFillShield());
        assertTrue(robotRenderer.isShowStatusBars());
        assertTrue(robotRenderer.isShowName());
        assertTrue(robotRenderer.isRenderDead());
    }

    @Test
    public void testRender_NotDead() {
        when(robotSnapshot.isDead()).thenReturn(false);

        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        assertTrue(robotRenderer.isShowStatusBars());
        assertTrue(robotRenderer.isShowName());
    }

    @Test
    public void testSetRenderDead() {
        robotRenderer.setRenderDead(true);

        assertTrue(robotRenderer.isRenderDead());
    }

    @Test
    public void testSetShowStatusBars() {
        robotRenderer.setShowStatusBars(true);

        assertTrue(robotRenderer.isShowStatusBars());
    }

    @Test
    public void testSetShowName() {
        robotRenderer.setShowName(true);

        assertTrue(robotRenderer.isShowName());
    }

    @Test
    public void testSetFillShield() {
        robotRenderer.setFillShield(true);

        assertTrue(robotRenderer.isFillShield());
    }

    @Test
    public void testPaintSelection() {
        robotRenderer.paintSelection(g2d, robotSnapshot);
        ArgumentCaptor<Ellipse2D.Double> ellipseCaptor = ArgumentCaptor.forClass(Ellipse2D.Double.class);

        assertTrue(robotRenderer.isRenderDead());
        assertTrue(robotRenderer.isFillShield());
        assertTrue(robotRenderer.isShowStatusBars());
        assertTrue(robotRenderer.isShowName());
        assertTrue(robotRenderer.isRenderDead());
        assertTrue(robotRenderer.isFillShield());
        assertTrue(robotRenderer.isRenderDead());
        assertTrue(robotRenderer.isRenderDead());
        assertTrue(robotRenderer.isRenderDead());
        assertTrue(robotRenderer.isRenderDead());
        assertTrue(robotRenderer.isRenderDead());
        assertTrue(robotRenderer.isRenderDead());
    }

    @Test
    public void testPaintShield() {
        when(robotSnapshot.isActiveShield()).thenReturn(true);
        when(robotSnapshot.getPositionVector()).thenReturn(mock(Vector2D.class));
        ArgumentCaptor<Ellipse2D.Double> ellipseCaptor = ArgumentCaptor.forClass(Ellipse2D.Double.class);

        robotRenderer.paintShield(g2d, robotSnapshot);

        assertTrue(robotRenderer.isFillShield());
        assertTrue(robotRenderer.isRenderDead());
    }

    @Test
    public void testPaintName() {
        when(robotSnapshot.getX()).thenReturn(0.0);
        when(robotSnapshot.getY()).thenReturn(0.0);
        when(robotSnapshot.getName()).thenReturn("TestName");
        when(g2d.getFontMetrics().getStringBounds("TestName", g2d)).thenReturn(mock(Rectangle2D.class));
        
        robotRenderer.paintName(g2d, robotSnapshot);

        assertTrue(robotRenderer.isShowName());
    }

    @Test
    public void testPaintHeat_NotDead() {
        when(robotSnapshot.isDead()).thenReturn(false);
        when(robotSnapshot.getX()).thenReturn(0.0);
        when(robotSnapshot.getY()).thenReturn(0.0);
        when(robotSnapshot.getTemperature().getLogScale()).thenReturn(0.5);

        robotRenderer.paintHeat(g2d, robotSnapshot);

        assertTrue(robotRenderer.isShowStatusBars());
    }

    @Test
    public void testPaintArmor() {
        when(robotSnapshot.getX()).thenReturn(0.0);
        when(robotSnapshot.getY()).thenReturn(0.0);
        when(robotSnapshot.getArmor()).thenReturn(50.0);

        robotRenderer.paintArmor(g2d, robotSnapshot);

        assertTrue(robotRenderer.isShowStatusBars());
    }

    @Test
    public void testPaintTurret() {
        when(robotSnapshot.getX()).thenReturn(0.0);
        when(robotSnapshot.getY()).thenReturn(0.0);
        when(robotSnapshot.getTurretHeading()).thenReturn(mock(Vector2D.class));

        robotRenderer.paintTurret(g2d, robotSnapshot);

        assertTrue(robotRenderer.isShowStatusBars());
    }

    @Test
    public void testPaintBody() {
        when(robotSnapshot.getX()).thenReturn(0.0);
        when(robotSnapshot.getY()).thenReturn(0.0);
        when(robotSnapshot.getHeading()).thenReturn(mock(Vector2D.class));

        robotRenderer.paintBody(g2d, robotSnapshot);

        assertTrue(robotRenderer.isRenderDead());
    }
}