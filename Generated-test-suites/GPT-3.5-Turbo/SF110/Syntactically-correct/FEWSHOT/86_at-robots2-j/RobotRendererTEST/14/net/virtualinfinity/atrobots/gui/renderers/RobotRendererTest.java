package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;

public class RobotRendererTest {

    private RobotRenderer robotRenderer;

    @Before
    public void setUp() {
        robotRenderer = new RobotRenderer();
    }

    @Test
    public void testRender_RenderDeadTrue_RobotSnapshotIsDead() {
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setDead(true);

        Graphics2D g2d = new Graphics2D() {
            @Override
            public void setPaint(Color paint) {
                assertTrue(paint.equals(new Color(1.0f, 1.0f, 0.0f, 0.25f)));
            }
        };

        Set<Integer> selectedRobotIds = new HashSet<>();

        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
    }

    @Test
    public void testRender_RenderDeadFalse_RobotSnapshotIsDead() {
        robotRenderer.setRenderDead(false);
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setDead(true);

        Graphics2D g2d = new Graphics2D() {
            @Override
            public void setPaint(Color paint) {
                assertFalse(true);
            }
        };

        Set<Integer> selectedRobotIds = new HashSet<>();

        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
    }

    @Test
    public void testRender_RenderDeadTrue_RobotSnapshotIsNotDead() {
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setDead(false);

        Graphics2D g2d = new Graphics2D() {
            @Override
            public void setPaint(Color paint) {
                assertFalse(true);
            }
        };

        Set<Integer> selectedRobotIds = new HashSet<>();

        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
    }

    @Test
    public void testRender_IsShowStatusBarsTrue() {
        Graphics2D g2d = new Graphics2D() {
            @Override
            public void setPaint(Color paint) {
                assertFalse(true);
            }
        };

        Set<Integer> selectedRobotIds = new HashSet<>();

        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setDead(false);

        robotRenderer.setShowStatusBars(true);

        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
    }

    @Test
    public void testRender_IsShowStatusBarsFalse() {
        robotRenderer.setShowStatusBars(false);

        Graphics2D g2d = new Graphics2D() {
            @Override
            public void setPaint(Color paint) {
                assertTrue(paint.equals(new Color(1.0f, 1.0f, 0.0f, 0.25f)));
            }
        };

        Set<Integer> selectedRobotIds = new HashSet<>();

        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setDead(false);

        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
    }

    @Test
    public void testRender_IsShowNameTrue() {
        Graphics2D g2d = new Graphics2D() {
            @Override
            public void setPaint(Color paint) {
                assertFalse(true);
            }

            @Override
            public void drawString(String str, float x, float y) {
                assertTrue(str.equals("Robot1"));
                assertTrue(x == 0.0f);
                assertTrue(y == -10.0f);
            }
        };

        Set<Integer> selectedRobotIds = new HashSet<>();

        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setDead(false);
        robotSnapshot.setName("Robot1");

        robotRenderer.setShowName(true);

        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
    }

    @Test
    public void testRender_IsShowNameFalse() {
        robotRenderer.setShowName(false);

        Graphics2D g2d = new Graphics2D() {
            @Override
            public void setPaint(Color paint) {
                assertTrue(paint.equals(new Color(1.0f, 1.0f, 0.0f, 0.25f)));
            }
        };

        Set<Integer> selectedRobotIds = new HashSet<>();

        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setDead(false);
        robotSnapshot.setName("Robot1");

        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
    }

    @Test
    public void testRender_SelectedRobotIdsContainsRobotId() {
        Graphics2D g2d = new Graphics2D() {
            @Override
            public void setPaint(Color paint) {
                assertTrue(paint.equals(new Color(1.0f, 1.0f, 0.0f, 0.25f)));
            }

            @Override
            public void fill(java.awt.geom.Ellipse2D.Double s) {
                assertTrue(s.width == 60.0);
                assertTrue(s.height == 60.0);
            }
        };

        Set<Integer> selectedRobotIds = new HashSet<>();
        selectedRobotIds.add(1);

        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setDead(false);

        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
    }

    @Test
    public void testIsRenderDead() {
        assertTrue(robotRenderer.isRenderDead());
    }

    @Test
    public void testSetRenderDead() {
        robotRenderer.setRenderDead(false);
        assertFalse(robotRenderer.isRenderDead());
    }

    @Test
    public void testIsShowStatusBars() {
        assertTrue(robotRenderer.isShowStatusBars());
    }

    @Test
    public void testSetShowStatusBars() {
        robotRenderer.setShowStatusBars(false);
        assertFalse(robotRenderer.isShowStatusBars());
    }

    @Test
    public void testIsShowName() {
        assertTrue(robotRenderer.isShowName());
    }

    @Test
    public void testSetShowName() {
        robotRenderer.setShowName(false);
        assertFalse(robotRenderer.isShowName());
    }

    @Test
    public void testIsFillShield() {
        assertTrue(robotRenderer.isFillShield());
    }

    @Test
    public void testSetFillShield() {
        robotRenderer.setFillShield(false);
        assertFalse(robotRenderer.isFillShield());
    }
}