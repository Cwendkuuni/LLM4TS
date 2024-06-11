package net.virtualinfinity.atrobots.gui.renderers;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Set;
import java.util.HashSet;
import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;

public class RobotRendererTest {

    private Graphics2D g2d = (Graphics2D) new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB).getGraphics();
    private Set<Integer> selectedRobotIds = new HashSet<>();
    private RobotSnapshot robotSnapshot = new RobotSnapshot();

    @Test
    public void testRender() {
        RobotRenderer robotRenderer = new RobotRenderer();
        
        robotRenderer.setRenderDead(true);
        robotRenderer.setShowStatusBars(true);
        robotRenderer.setShowName(true);
        robotRenderer.setFillShield(true);
        robotSnapshot.setDead(false);
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        robotRenderer.setFillShield(false);
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        selectedRobotIds.add(robotSnapshot.getId());
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        robotSnapshot.setDead(true);
        robotSnapshot.setActiveShield(false);
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        robotSnapshot.setActiveShield(true);
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
    }

    @Test
    public void testSetRenderDead() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setRenderDead(true);
        assertEquals(true, robotRenderer.isRenderDead());
        robotRenderer.setRenderDead(false);
        assertEquals(false, robotRenderer.isRenderDead());
    }

    @Test
    public void testSetShowStatusBars() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setShowStatusBars(true);
        assertEquals(true, robotRenderer.isShowStatusBars());
        robotRenderer.setShowStatusBars(false);
        assertEquals(false, robotRenderer.isShowStatusBars());
    }

    @Test
    public void testSetShowName() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setShowName(true);
        assertEquals(true, robotRenderer.isShowName());
        robotRenderer.setShowName(false);
        assertEquals(false, robotRenderer.isShowName());
    }

    @Test
    public void testSetFillShield() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setFillShield(true);
        assertEquals(true, robotRenderer.isFillShield());
        robotRenderer.setFillShield(false);
        assertEquals(false, robotRenderer.isFillShield());
    }

    @Test
    public void testPaintSelection() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintSelection(g2d, robotSnapshot);
    }

    @Test
    public void testPaintShield() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotSnapshot.setActiveShield(true);
        robotRenderer.setFillShield(true);
        robotRenderer.paintShield(g2d, robotSnapshot);
        
        robotRenderer.setFillShield(false);
        robotRenderer.paintShield(g2d, robotSnapshot);
        
        robotSnapshot.setActiveShield(false);
        robotRenderer.paintShield(g2d, robotSnapshot);
    }

    @Test
    public void testPaintName() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintName(g2d, robotSnapshot);
    }

    @Test
    public void testPaintHeat() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotSnapshot.setDead(false);
        robotRenderer.paintHeat(g2d, robotSnapshot);
        
        robotSnapshot.setDead(true);
        robotRenderer.paintHeat(g2d, robotSnapshot);
    }

    @Test
    public void testPaintArmor() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintArmor(g2d, robotSnapshot);
    }

    @Test
    public void testPaintTurret() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.paintTurret(g2d, robotSnapshot);
    }

    @Test
    public void testPaintBody() {
        RobotRenderer robotRenderer = new RobotRenderer();
        robotSnapshot.setDead(true);
        robotRenderer.paintBody(g2d, robotSnapshot);
        
        robotSnapshot.setDead(false);
        robotRenderer.paintBody(g2d, robotSnapshot);
    }

}