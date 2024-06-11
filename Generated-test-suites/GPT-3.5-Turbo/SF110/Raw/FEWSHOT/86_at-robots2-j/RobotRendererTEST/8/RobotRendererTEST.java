
package net.virtualinfinity.atrobots.gui.renderers;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Set;

import static org.mockito.Mockito.mock;

public class RobotRendererTest {

    @Test
    public void testRender() {
        Graphics2D g2d = mock(Graphics2D.class);
        Set<Integer> selectedRobotIds = mock(Set.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        Mockito.verify(g2d, Mockito.times(1)).setPaint(Mockito.any(Color.class));
        Mockito.verify(g2d, Mockito.times(1)).fill(Mockito.any(Ellipse2D.Double.class));
        Mockito.verify(g2d, Mockito.times(2)).draw(Mockito.any(Ellipse2D.Double.class));
        Mockito.verify(g2d, Mockito.times(1)).draw(Mockito.any(Line2D.Double.class));
        Mockito.verify(g2d, Mockito.times(2)).fill(Mockito.any(Rectangle2D.Double.class));
        Mockito.verify(g2d, Mockito.times(1)).setStroke(Mockito.any(BasicStroke.class));
        Mockito.verify(g2d, Mockito.times(1)).draw(Mockito.any(GeneralPath.class));
        Mockito.verify(g2d, Mockito.times(1)).fill(Mockito.any(GeneralPath.class));
    }

    @Test
    public void testIsRenderDead() {
        RobotRenderer robotRenderer = new RobotRenderer();
        Assert.assertTrue(robotRenderer.isRenderDead());
    }

    @Test
    public void testSetRenderDead() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setRenderDead(false);
        Assert.assertFalse(robotRenderer.isRenderDead());
    }

    @Test
    public void testIsShowStatusBars() {
        RobotRenderer robotRenderer = new RobotRenderer();
        Assert.assertTrue(robotRenderer.isShowStatusBars());
    }

    @Test
    public void testSetShowStatusBars() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setShowStatusBars(false);
        Assert.assertFalse(robotRenderer.isShowStatusBars());
    }

    @Test
    public void testIsShowName() {
        RobotRenderer robotRenderer = new RobotRenderer();
        Assert.assertTrue(robotRenderer.isShowName());
    }

    @Test
    public void testSetShowName() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setShowName(false);
        Assert.assertFalse(robotRenderer.isShowName());
    }

    @Test
    public void testIsFillShield() {
        RobotRenderer robotRenderer = new RobotRenderer();
        Assert.assertTrue(robotRenderer.isFillShield());
    }

    @Test
    public void testSetFillShield() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setFillShield(false);
        Assert.assertFalse(robotRenderer.isFillShield());
    }

    @Test
    public void testPaintSelection() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintSelection(g2d, robotSnapshot);

        Mockito.verify(g2d, Mockito.times(1)).setPaint(Mockito.any(Color.class));
        Mockito.verify(g2d, Mockito.times(1)).fill(Mockito.any(Ellipse2D.Double.class));
    }

    @Test
    public void testPaintShield() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintShield(g2d, robotSnapshot);

        Mockito.verify(g2d, Mockito.times(2)).fill(Mockito.any(Ellipse2D.Double.class));
        Mockito.verify(g2d, Mockito.times(1)).setPaint(Mockito.any(RadialGradientPaint.class));
        Mockito.verify(g2d, Mockito.times(1)).draw(Mockito.any(Ellipse2D.Double.class));
    }

    @Test
    public void testPaintName() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintName(g2d, robotSnapshot);

        Mockito.verify(g2d, Mockito.times(1)).setPaint(Mockito.any(Color.class));
        Mockito.verify(g2d, Mockito.times(1)).drawString(Mockito.anyString(), Mockito.anyFloat(), Mockito.anyFloat());
    }

    @Test
    public void testPaintHeat() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintHeat(g2d, robotSnapshot);

        Mockito.verify(g2d, Mockito.times(1)).setPaint(Mockito.any(GradientPaint.class));
        Mockito.verify(g2d, Mockito.times(1)).fill(Mockito.any(Rectangle2D.Double.class));
        Mockito.verify(g2d, Mockito.times(1)).draw(Mockito.any(Rectangle2D.Double.class));
    }

    @Test
    public void testPaintArmor() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintArmor(g2d, robotSnapshot);

        Mockito.verify(g2d, Mockito.times(1)).setPaint(Mockito.any(Color.class));
        Mockito.verify(g2d, Mockito.times(1)).fill(Mockito.any(Rectangle2D.Double.class));
        Mockito.verify(g2d, Mockito.times(1)).draw(Mockito.any(Rectangle2D.Double.class));
    }

    @Test
    public void testPaintTurret() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintTurret(g2d, robotSnapshot);

        Mockito.verify(g2d, Mockito.times(1)).setPaint(Mockito.any(Color.class));
        Mockito.verify(g2d, Mockito.times(1)).draw(Mockito.any(Line2D.Double.class));
    }

    @Test
    public void testPaintBody() {
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);

        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintBody(g2d, robotSnapshot);

        Mockito.verify(g2d, Mockito.times(1)).setPaint(Mockito.any(Color.class));
        Mockito.verify(g2d, Mockito.times(1)).draw(Mockito.any(BasicStroke.class));
        Mockito.verify(g2d, Mockito.times(1)).draw(Mockito.any(GeneralPath.class));
        Mockito.verify(g2d, Mockito.times(1)).fill(Mockito.any(GeneralPath.class));
    }
}
