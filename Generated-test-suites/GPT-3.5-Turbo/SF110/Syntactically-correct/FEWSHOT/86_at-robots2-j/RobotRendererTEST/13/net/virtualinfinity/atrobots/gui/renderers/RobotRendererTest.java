package net.virtualinfinity.atrobots.gui.renderers;

import org.junit.Test;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.GradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.geom.Line2D;
import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;
import java.util.Set;
import static org.junit.Assert.*;

public class RobotRendererTest {

    @Test
    public void testRender() {
        RobotRenderer robotRenderer = new RobotRenderer();
        Graphics2D g2d = (Graphics2D) new Object();
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        Set<Integer> selectedRobotIds = new Set<Integer>();

        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        //Assert statements
    }

    @Test
    public void testIsRenderDead() {
        RobotRenderer robotRenderer = new RobotRenderer();

        boolean result = robotRenderer.isRenderDead();

        //Assert statement
    }

    @Test
    public void testSetRenderDead() {
        RobotRenderer robotRenderer = new RobotRenderer();
        boolean renderDead = true;

        robotRenderer.setRenderDead(renderDead);

        //Assert statement
    }

    @Test
    public void testIsShowStatusBars() {
        RobotRenderer robotRenderer = new RobotRenderer();

        boolean result = robotRenderer.isShowStatusBars();

        //Assert statement
    }

    @Test
    public void testSetShowStatusBars() {
        RobotRenderer robotRenderer = new RobotRenderer();
        boolean showStatusBars = true;

        robotRenderer.setShowStatusBars(showStatusBars);

        //Assert statement
    }

    @Test
    public void testIsShowName() {
        RobotRenderer robotRenderer = new RobotRenderer();

        boolean result = robotRenderer.isShowName();

        //Assert statement
    }

    @Test
    public void testSetShowName() {
        RobotRenderer robotRenderer = new RobotRenderer();
        boolean showName = true;

        robotRenderer.setShowName(showName);

        //Assert statement
    }

    @Test
    public void testIsFillShield() {
        RobotRenderer robotRenderer = new RobotRenderer();

        boolean result = robotRenderer.isFillShield();

        //Assert statement
    }

    @Test
    public void testSetFillShield() {
        RobotRenderer robotRenderer = new RobotRenderer();
        boolean fillShield = true;

        robotRenderer.setFillShield(fillShield);

        //Assert statement
    }

    @Test
    public void testPaintSelection() {
        RobotRenderer robotRenderer = new RobotRenderer();
        Graphics2D g2d = (Graphics2D) new Object();
        RobotSnapshot robotSnapshot = new RobotSnapshot();

        robotRenderer.paintSelection(g2d, robotSnapshot);

        //Assert statements
    }

    @Test
    public void testPaintShield() {
        RobotRenderer robotRenderer = new RobotRenderer();
        Graphics2D g2d = (Graphics2D) new Object();
        RobotSnapshot robotSnapshot = new RobotSnapshot();

        robotRenderer.paintShield(g2d, robotSnapshot);

        //Assert statements
    }

    @Test
    public void testPaintName() {
        RobotRenderer robotRenderer = new RobotRenderer();
        Graphics2D g2d = (Graphics2D) new Object();
        RobotSnapshot robotSnapshot = new RobotSnapshot();

        robotRenderer.paintName(g2d, robotSnapshot);

        //Assert statements
    }

    @Test
    public void testPaintHeat() {
        RobotRenderer robotRenderer = new RobotRenderer();
        Graphics2D g2d = (Graphics2D) new Object();
        RobotSnapshot robotSnapshot = new RobotSnapshot();

        robotRenderer.paintHeat(g2d, robotSnapshot);

        //Assert statements
    }

    @Test
    public void testPaintArmor() {
        RobotRenderer robotRenderer = new RobotRenderer();
        Graphics2D g2d = (Graphics2D) new Object();
        RobotSnapshot robotSnapshot = new RobotSnapshot();

        robotRenderer.paintArmor(g2d, robotSnapshot);

        //Assert statements
    }

    @Test
    public void testPaintTurret() {
        RobotRenderer robotRenderer = new RobotRenderer();
        Graphics2D g2d = (Graphics2D) new Object();
        RobotSnapshot robotSnapshot = new RobotSnapshot();

        robotRenderer.paintTurret(g2d, robotSnapshot);

        //Assert statements
    }

    @Test
    public void testPaintBody() {
        RobotRenderer robotRenderer = new RobotRenderer();
        Graphics2D g2d = (Graphics2D) new Object();
        RobotSnapshot robotSnapshot = new RobotSnapshot();

        robotRenderer.paintBody(g2d, robotSnapshot);

        //Assert statements
    }
}