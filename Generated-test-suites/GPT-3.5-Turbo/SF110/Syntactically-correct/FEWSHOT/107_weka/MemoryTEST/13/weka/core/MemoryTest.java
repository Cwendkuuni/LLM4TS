
package weka.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        assertTrue(memory.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        memory.setEnabled(false);
        assertTrue(!memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        assertTrue(!memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        assertEquals(Memory.toMegaByte(Runtime.getRuntime().totalMemory()), memory.getInitial(), 0.1);
    }

    @Test
    public void testGetCurrent() {
        assertEquals(Memory.toMegaByte(Runtime.getRuntime().totalMemory()), memory.getCurrent(), 0.1);
    }

    @Test
    public void testGetMax() {
        assertEquals(Memory.toMegaByte(Runtime.getRuntime().maxMemory()), memory.getMax(), 0.1);
    }

    @Test
    public void testIsOutOfMemory() {
        // Test case when out of memory is true
        memory.setEnabled(true);
        assertTrue(memory.isOutOfMemory());

        // Test case when out of memory is false
        memory.setEnabled(false);
        assertTrue(!memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        assertEquals(0, Memory.toMegaByte(0), 0.1);
        assertEquals(1, Memory.toMegaByte(1048576), 0.1);
        assertEquals(2, Memory.toMegaByte(2097152), 0.1);
        assertEquals(3.5, Memory.toMegaByte(3670016), 0.1);
    }

    @Test
    public void testShowOutOfMemory() {
        // Test case when not enabled
        memory.setEnabled(false);
        memory.showOutOfMemory();

        // Test case when enabled and use GUI is false
        memory.setEnabled(true);
        memory.setUseGUI(false);
        memory.showOutOfMemory();

        // Test case when enabled and use GUI is true
        memory.setEnabled(true);
        memory.setUseGUI(true);
        memory.showOutOfMemory();
    }

    @Test
    public void testStopThreads() {
        memory.stopThreads();
        // Cannot validate the behavior of stopping threads in a test case
    }

    @Test
    public void testGetRevision() {
        assertEquals("$Revision: 8034 $", memory.getRevision());
    }
}
