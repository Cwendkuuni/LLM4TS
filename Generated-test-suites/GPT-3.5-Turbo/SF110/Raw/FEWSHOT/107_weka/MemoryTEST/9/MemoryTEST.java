
package weka.core;

import static org.junit.Assert.*;

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
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        assertFalse(memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        assertEquals(8034L, memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        long currentMemory = memory.getCurrent();
        assertTrue(currentMemory >= 0);
    }

    @Test
    public void testGetMax() {
        long maxMemory = memory.getMax();
        assertTrue(maxMemory >= 0);
    }

    @Test
    public void testIsOutOfMemory() {
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        long bytes = 1048576;
        double megabytes = Memory.toMegaByte(bytes);
        assertEquals(1.0, megabytes, 0);
    }

    @Test
    public void testShowOutOfMemory() {
        // Not possible to test System.err and JOptionPane.showMessageDialog()
        // Manually confirm that no exceptions are thrown during the method execution
        memory.showOutOfMemory();
    }

    @Test
    public void testStopThreads() {
        // Not possible to test Thread.stop()
        // Manually confirm that no exceptions are thrown during the method execution
        memory.stopThreads();
    }

    @Test
    public void testGetRevision() {
        assertNotNull(memory.getRevision());
    }

    @Test
    public void testMain() {
        // Not possible to test System.out.println()
        // Manually confirm that no exceptions are thrown during the method execution
        Memory.main(new String[] {});
    }
}
