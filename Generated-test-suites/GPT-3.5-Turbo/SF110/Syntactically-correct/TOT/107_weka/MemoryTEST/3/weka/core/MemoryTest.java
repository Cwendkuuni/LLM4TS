package weka.core;

import static org.junit.Assert.*;
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
        // Test when Memory is enabled
        memory.setEnabled(true);
        assertTrue(memory.isEnabled());

        // Test when Memory is disabled
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        // Test when m_UseGUI is true
        memory = new Memory(true);
        assertTrue(memory.getUseGUI());

        // Test when m_UseGUI is false
        memory = new Memory(false);
        assertFalse(memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        // Initial value is set in the static block, so check if it matches
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        // Check if initial and current memory match
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent());
    }

    @Test
    public void testGetMax() {
        // Check if initial and max memory match
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
    }

    @Test
    public void testIsOutOfMemory() {
        // Test when Memory is enabled and enough space is available
        memory.setEnabled(true);
        assertFalse(memory.isOutOfMemory());

        // Test when Memory is enabled and not enough space is available
        memory.setEnabled(true);
        memory.m_Total = memory.getMax() - memory.getInitial() - 200000L;
        assertTrue(memory.isOutOfMemory());

        // Test when Memory is disabled
        memory.setEnabled(false);
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        // Test conversion of bytes to megabytes
        assertEquals(0.5, Memory.toMegaByte(524288), 0.001);
        assertEquals(1.0, Memory.toMegaByte(1048576), 0.001);
        assertEquals(2.0, Memory.toMegaByte(2097152), 0.001);
    }

    @Test
    public void testShowOutOfMemory() {
        // Cannot directly test this method as it has dependencies on System.gc() and JOptionPane.showMessageDialog()

        // Should test if the method returns immediately when Memory is disabled
        memory.setEnabled(false);
        memory.showOutOfMemory();
        // No way to validate if the method executed successfully, just ensuring it doesn't throw any exceptions
    }

    @Test
    public void testStopThreads() {
        // Cannot directly test this method as it has dependencies on Thread and System.gc()

        // No way to validate if the method executed successfully, just ensuring it doesn't throw any exceptions
        memory.stopThreads();
    }

    @Test
    public void testGetRevision() {
        // Test if the revision string is correctly extracted
        assertEquals("$Revision: 8034 $", memory.getRevision());
    }

    @Test
    public void testMain() {
        // Cannot directly test this method as it has dependencies on System.out.println()

        // No way to validate if the method executed successfully, just ensuring it doesn't throw any exceptions
        Memory.main(new String[]{});
    }
}