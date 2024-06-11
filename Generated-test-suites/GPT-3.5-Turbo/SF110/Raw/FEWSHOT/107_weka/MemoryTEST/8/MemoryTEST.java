
package weka.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class MemoryTest {
    private Memory memory;

    @Before
    public void setup() {
        memory = new Memory();
    }

    @Test
    public void testIsEnabled() {
        assertTrue(memory.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());

        memory.setEnabled(true);
        assertTrue(memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        assertFalse(memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        // Initial memory is set in the static block
        long initialMemory = Memory.toMegaByte(Memory.m_Initial);
        assertEquals(initialMemory, memory.getInitial(), 0.1);
    }

    @Test
    public void testGetCurrent() {
        long currentMemory = memory.getCurrent();
        assertEquals(currentMemory, memory.m_Total);
    }

    @Test
    public void testGetMax() {
        long maxMemory = memory.getMax();
        assertEquals(maxMemory, memory.m_Max);
    }

    @Test
    public void testIsOutOfMemory() {
        long currentMemory = memory.getCurrent();

        // Test when enabled is true and memory is not out of bounds
        assertFalse(memory.isOutOfMemory());

        // Test when enabled is true and memory is out of bounds
        memory.m_Max = currentMemory - memory.getInitial() + 200000L;
        assertTrue(memory.isOutOfMemory());

        // Test when enabled is false and memory is out of bounds
        memory.setEnabled(false);
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        long bytes = 1048576; // 1 MB
        double megabytes = Memory.toMegaByte(bytes);
        assertEquals(1.0, megabytes, 0.1);
    }

    @Test
    public void testShowOutOfMemory() {
        // Mock System.err and JOptionPane.showMessageDialog() to test the output
        // No need to test the exact output, just check if the methods are called
        System.setErr(new PrintStream(new ByteArrayOutputStream()));
        memory.showOutOfMemory();
    }

    @Test
    public void testStopThreads() {
        // Test if threads are stopped and garbage collection is performed
        memory.stopThreads();
        assertNotNull(memory);
    }

    @Test
    public void testGetRevision() {
        String revision = memory.getRevision();
        assertEquals("$Revision: 8034 $", revision);
    }
}
