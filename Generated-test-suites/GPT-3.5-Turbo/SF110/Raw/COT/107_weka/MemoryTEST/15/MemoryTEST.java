package weka.core;

import org.junit.*;
import static org.junit.Assert.*;

public class MemoryTest {

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertTrue(memory.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        Memory memory = new Memory();
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        assertEquals(Memory.m_Initial, memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        long currentMemory = memory.getCurrent();
        assertTrue(currentMemory >= 0);
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        long maxMemory = memory.getMax();
        assertTrue(maxMemory >= 0);
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        long bytes = 1048576;
        double expected = 1.0;
        assertEquals(expected, Memory.toMegaByte(bytes), 0.001);
    }

    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        memory.showOutOfMemory();

        // Test the GUI part manually
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();

        // Test the effect of stopping threads manually
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertTrue(memory.getRevision().startsWith("$Revision: "));
    }

    @Test
    public void testMain() {
        // Test the functionality of the main method manually
    }
}