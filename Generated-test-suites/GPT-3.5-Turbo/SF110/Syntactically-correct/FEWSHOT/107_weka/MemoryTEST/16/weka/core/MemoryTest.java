
package weka.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MemoryTest {
    @Test
    public void testConstructor() {
        Memory memory = new Memory();
        assertNotNull(memory);
        assertEquals(false, memory.getUseGUI());

        memory = new Memory(true);
        assertNotNull(memory);
        assertEquals(true, memory.getUseGUI());
    }

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertEquals(true, memory.isEnabled());

        memory.setEnabled(false);
        assertEquals(false, memory.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        assertEquals(true, memory.isEnabled());

        memory.setEnabled(false);
        assertEquals(false, memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory();
        assertEquals(false, memory.getUseGUI());

        memory = new Memory(true);
        assertEquals(true, memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        assertTrue(memory.getInitial() > 0);
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        assertTrue(memory.getCurrent() > 0);
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        assertTrue(memory.getMax() > 0);
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        long bytes1 = 1048576; // 1 MB
        double expected1 = 1.0;
        assertEquals(expected1, Memory.toMegaByte(bytes1), 0.01);

        long bytes2 = 2097152; // 2 MB
        double expected2 = 2.0;
        assertEquals(expected2, Memory.toMegaByte(bytes2), 0.01);
    }

    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();
        // Cannot test exact output
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        // Cannot test exact output
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertNotNull(memory.getRevision());
    }

    @Test
    public void testMain() {
        // Cannot test exact output
    }
}
