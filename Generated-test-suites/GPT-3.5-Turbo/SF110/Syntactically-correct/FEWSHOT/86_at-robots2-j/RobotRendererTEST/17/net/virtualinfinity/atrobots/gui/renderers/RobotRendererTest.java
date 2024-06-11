package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.GradientPaint;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;
import net.virtualinfinity.atrobots.snapshots.Vector;
import net.virtualinfinity.atrobots.gui.renderers.RobotRenderer;

public class RobotRendererTest {

    @Test
    public void testRender_RenderDeadFalseRobotDead_DoNotRender() {
        RobotRenderer robotRenderer = new RobotRenderer();
        Graphics2D g2d = createGraphics2D();
        RobotSnapshot robotSnapshot = createRobotSnapshot(true, false);
        Set<Integer> selectedRobotIds = new HashSet<>();
        robotRenderer.setRenderDead(false);

        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        // Assert that nothing is drawn
    }

    @Test
    public void testRender_RenderDeadTrueRobotDead_Render() {
        RobotRenderer robotRenderer = new RobotRenderer();
        Graphics2D g2d = createGraphics2D();
        RobotSnapshot robotSnapshot = createRobotSnapshot(true, false);
        Set<Integer> selectedRobotIds = new HashSet<>();
        robotRenderer.setRenderDead(true);

        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        // Assert that paintBody is called
        // Assert that nothing else is called
    }

    @Test
    public void testRender_RenderDeadTrueRobotAlive_Render() {
        RobotRenderer robotRenderer = new RobotRenderer();
        Graphics2D g2d = createGraphics2D();
        RobotSnapshot robotSnapshot = createRobotSnapshot(false, false);
        Set<Integer> selectedRobotIds = new HashSet<>();
        robotRenderer.setRenderDead(true);

        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        // Assert that paintBody is called
        // Assert that paintShield is called
        // Assert that paintTurret is called
        // Assert that paintArmor is called
        // Assert that paintHeat is called
        // Assert that paintName is called
        // Assert that paintSelection is NOT called
    }

    @Test
    public void testRender_ShowStatusBarsTrue_RenderStatusBars() {
        RobotRenderer robotRenderer = new RobotRenderer();
        Graphics2D g2d = createGraphics2D();
        RobotSnapshot robotSnapshot = createRobotSnapshot(false, false);
        Set<Integer> selectedRobotIds = new HashSet<>();
        robotRenderer.setShowStatusBars(true);

        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        // Assert that paintArmor is called
        // Assert that paintHeat is called
    }

    @Test
    public void testRender_ShowStatusBarsFalse_DoNotRenderStatusBars() {
        RobotRenderer robotRenderer = new RobotRenderer();
        Graphics2D g2d = createGraphics2D();
        RobotSnapshot robotSnapshot = createRobotSnapshot(false, false);
        Set<Integer> selectedRobotIds = new HashSet<>();
        robotRenderer.setShowStatusBars(false);

        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        // Assert that nothing is drawn for armor and heat
    }

    @Test
    public void testRender_ShowNameTrue_RenderName() {
        RobotRenderer robotRenderer = new RobotRenderer();
        Graphics2D g2d = createGraphics2D();
        RobotSnapshot robotSnapshot = createRobotSnapshot(false, false);
        Set<Integer> selectedRobotIds = new HashSet<>();
        robotRenderer.setShowName(true);

        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        // Assert that paintName is called
    }

    @Test
    public void testRender_ShowNameFalse_DoNotRenderName() {
        RobotRenderer robotRenderer = new RobotRenderer();
        Graphics2D g2d = createGraphics2D();
        RobotSnapshot robotSnapshot = createRobotSnapshot(false, false);
        Set<Integer> selectedRobotIds = new HashSet<>();
        robotRenderer.setShowName(false);

        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        // Assert that nothing is drawn for name
    }

    @Test
    public void testRender_SelectedRobot_RenderSelection() {
        RobotRenderer robotRenderer = new RobotRenderer();
        Graphics2D g2d = createGraphics2D();
        RobotSnapshot robotSnapshot = createRobotSnapshot(false, false);
        Set<Integer> selectedRobotIds = new HashSet<>();
        selectedRobotIds.add(robotSnapshot.getId());

        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        // Assert that paintSelection is called
    }

    private Graphics2D createGraphics2D() {
        // TODO: Implement method to create Graphics2D object
        return null;
    }

    private RobotSnapshot createRobotSnapshot(boolean isDead, boolean isActiveShield) {
        int id = 1;
        double x = 0.0;
        double y = 0.0;
        double heading = 0.0;
        double turretHeading = 0.0;
        int armor = 100;
        Temperature temperature = new Temperature(50);
        return new RobotSnapshot(id, x, y, heading, turretHeading, armor, temperature, isActiveShield, isDead);
    }

}