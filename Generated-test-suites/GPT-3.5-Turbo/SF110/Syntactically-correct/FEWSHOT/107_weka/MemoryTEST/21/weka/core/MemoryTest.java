
package weka.core;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MemoryTest {

    private Memory memory;

    @Before
    public void setUp() {
        memory = new Memory();
    }

    @Test
    public void testIsEnabled() {
        Assert.assertTrue(memory.isEnabled());
        memory.setEnabled(false);
        Assert.assertFalse(memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        Assert.assertFalse(memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        Assert.assertEquals(536870912, memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        Assert.assertEquals(memory.m_Total, memory.getCurrent());
    }

    @Test
    public void testGetMax() {
        Assert.assertEquals(1073741824, memory.getMax());
    }

    @Test
    public void testIsOutOfMemory() {
        memory.setEnabled(false);
        Assert.assertFalse(memory.isOutOfMemory());
        memory.setEnabled(true);
        Assert.assertTrue(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        Assert.assertEquals(0.5, memory.toMegaByte(524288), 0.001);
    }

    @Test
    public void testShowOutOfMemory() {
        // Can't really test console output in JUnit, so just calling the method to make sure it doesn't throw an exception
        memory.showOutOfMemory();
    }

    @Test
    public void testStopThreads() {
        // Can't really test thread stopping in JUnit, so just calling the method to make sure it doesn't throw an exception
        memory.stopThreads();
    }

    @Test
    public void testGetRevision() {
        Assert.assertEquals("$Revision: 8034 $", memory.getRevision());
    }
}
