
package weka.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MemoryTest {
    @Test
    public void testConstructor() {
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());
        long totalMemory = Runtime.getRuntime().totalMemory();
        long maxMemory = Runtime.getRuntime().maxMemory();
        assertEquals(totalMemory, memory.getTotal());
        assertEquals(maxMemory, memory.getMax());

        memory = new Memory(true);
        assertTrue(memory.getUseGUI());
    }

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertTrue(memory.isEnabled());

        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        Memory memory = new Memory();
        assertTrue(memory.isEnabled());

        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
        memory.setEnabled(true);
        assertTrue(memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());

        memory = new Memory(true);
        assertTrue(memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        assertEquals(totalMemory, memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        assertEquals(totalMemory, memory.getCurrent());
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        long maxMemory = Runtime.getRuntime().maxMemory();
        assertEquals(maxMemory, memory.getMax());
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());

        memory.setEnabled(true);
        memory.setInitial(100000L);
        assertTrue(memory.isOutOfMemory());

        memory.setInitial(1000000000L);
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        long bytes = 1048576L;
        double expected = 1.0;
        assertEquals(expected, Memory.toMegaByte(bytes), 0.0);
    }

    @Test
    public void testShowOutOfMemory() {
        // Unable to test this method as it contains console output and GUI interaction
    }

    @Test
    public void testStopThreads() {
        // Unable to test this method as it stops threads
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertEquals("$Revision: 8034 $", memory.getRevision());
    }
}
