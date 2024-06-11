
package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.*;
import java.util.HashSet;
import java.util.Set;
import net.virtualinfinity.atrobots.measures.Angle;
import net.virtualinfinity.atrobots.measures.AbsoluteAngle;
import net.virtualinfinity.atrobots.measures.Metric;
import net.virtualinfinity.atrobots.measures.RelativeAngle;
import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;

public class RobotRendererTest {
    private RobotRenderer robotRenderer;

    @Before
    public void setup() {
        robotRenderer = new RobotRenderer();
    }

    @Test
    public void testDefaultConstructor() {
        assertTrue(robotRenderer.isRenderDead());
        assertTrue(robotRenderer.isShowStatusBars());
        assertTrue(robotRenderer.isShowName());
        assertTrue(robotRenderer.isFillShield());
    }

    @Test
    public void testSetRenderDead() {
        robotRenderer.setRenderDead(false);
        assertFalse(robotRenderer.isRenderDead());
        robotRenderer.setRenderDead(true);
        assertTrue(robotRenderer.isRenderDead());
    }

    @Test
    public void testSetShowStatusBars() {
        robotRenderer.setShowStatusBars(false);
        assertFalse(robotRenderer.isShowStatusBars());
        robotRenderer.setShowStatusBars(true);
        assertTrue(robotRenderer.isShowStatusBars());
    }

    @Test
    public void testSetShowName() {
        robotRenderer.setShowName(false);
        assertFalse(robotRenderer.isShowName());
        robotRenderer.setShowName(true);
        assertTrue(robotRenderer.isShowName());
    }

    @Test
    public void testSetFillShield() {
        robotRenderer.setFillShield(false);
        assertFalse(robotRenderer.isFillShield());
        robotRenderer.setFillShield(true);
        assertTrue(robotRenderer.isFillShield());
    }

    @Test
    public void testRender_NotDead() {
        Graphics2D g2d = (Graphics2D) createMock(Graphics2D.class);
        Set<Integer> selectedRobotIds = new HashSet<>();

        RobotSnapshot robotSnapshot = createMock(RobotSnapshot.class);
        expect(robotSnapshot.isDead()).andReturn(false);

        robotRenderer.paintBody(g2d, robotSnapshot);
        robotRenderer.paintShield(g2d, robotSnapshot);
        robotRenderer.paintTurret(g2d, robotSnapshot);
        robotRenderer.paintArmor(g2d, robotSnapshot);
        robotRenderer.paintHeat(g2d, robotSnapshot);
        robotRenderer.paintName(g2d, robotSnapshot);
        replayAll();

        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        verifyAll();
    }

    @Test
    public void testRender_DeadAndRenderDeadTrue() {
        Graphics2D g2d = (Graphics2D) createMock(Graphics2D.class);
        Set<Integer> selectedRobotIds = new HashSet<>();

        RobotSnapshot robotSnapshot = createMock(RobotSnapshot.class);
        expect(robotSnapshot.isDead()).andReturn(true);

        robotRenderer.setRenderDead(true);

        robotRenderer.paintBody(g2d, robotSnapshot);
        robotRenderer.paintShield(g2d, robotSnapshot);
        robotRenderer.paintTurret(g2d, robotSnapshot);
        robotRenderer.paintArmor(g2d, robotSnapshot);
        robotRenderer.paintHeat(g2d, robotSnapshot);
        robotRenderer.paintName(g2d, robotSnapshot);
        replayAll();

        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        verifyAll();
    }

    @Test
    public void testRender_DeadAndRenderDeadFalse() {
        Graphics2D g2d = (Graphics2D) createMock(Graphics2D.class);
        Set<Integer> selectedRobotIds = new HashSet<>();

        RobotSnapshot robotSnapshot = createMock(RobotSnapshot.class);
        expect(robotSnapshot.isDead()).andReturn(true);

        robotRenderer.setRenderDead(false);

        replayAll();

        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        verifyAll();
    }

    @Test
    public void testRender_SelectedRobot() {
        Graphics2D g2d = (Graphics2D) createMock(Graphics2D.class);
        Set<Integer> selectedRobotIds = new HashSet<>();
        selectedRobotIds.add(1);

        RobotSnapshot robotSnapshot = createMock(RobotSnapshot.class);
        expect(robotSnapshot.isDead()).andReturn(false);
        expect(robotSnapshot.getId()).andReturn(1);

        robotRenderer.paintBody(g2d, robotSnapshot);
        robotRenderer.paintShield(g2d, robotSnapshot);
        robotRenderer.paintTurret(g2d, robotSnapshot);
        robotRenderer.paintArmor(g2d, robotSnapshot);
        robotRenderer.paintHeat(g2d, robotSnapshot);
        robotRenderer.paintName(g2d, robotSnapshot);
        robotRenderer.paintSelection(g2d, robotSnapshot);
        replayAll();

        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        verifyAll();
    }
    
    // TODO: Add test cases for all private paint methods
}
