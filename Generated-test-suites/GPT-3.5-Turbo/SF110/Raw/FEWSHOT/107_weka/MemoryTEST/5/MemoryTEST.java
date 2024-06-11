
package weka.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class MemoryTest {

    @Test
    public void testMemoryEnabled() {
        Memory memory = new Memory();
        assertTrue(memory.isEnabled());
    }

    @Test
    public void testSetMemoryEnabled() {
        Memory memory = new Memory();
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }

    @Test
    public void testMemoryUseGUI() {
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());
    }

    @Test
    public void testMemoryTotalMemory() {
        Memory memory = new Memory();
        assertNotNull(memory.getCurrent());
    }

    @Test
    public void testMemoryMaxMemory() {
        Memory memory = new Memory();
        assertNotNull(memory.getMax());
    }

    @Test
    public void testMemoryOutOfMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testMemoryToMegaByte() {
        long bytes = 1048576;
        double expected = 1.0;
        assertEquals(expected, Memory.toMegaByte(bytes), 0.1);
    }

    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();
        memory.showOutOfMemory();
        // Not testing the actual output as it writes to System.err and uses JOptionPane
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();
        // Not testing the actual output as it stops threads and calls System.gc()
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertNotNull(memory.getRevision());
    }

    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        assertNotNull(memory.getInitial());
    }
}
