
package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RobotRendererTest {
    
    private RobotRenderer robotRenderer;

    @Before
    public void setup() {
        robotRenderer = new RobotRenderer();
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
