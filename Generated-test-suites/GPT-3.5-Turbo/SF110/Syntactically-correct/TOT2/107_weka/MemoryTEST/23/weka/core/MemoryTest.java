package weka.core;
import org.junit.*;
import static org.junit.Assert.*;

public class MemoryTest {

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertFalse(memory.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        assertTrue(memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        assertEquals(0, memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        assertTrue(memory.getCurrent() >= 0);
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        assertTrue(memory.getMax() >= 0);
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        assertEquals(1, Memory.toMegaByte(1048576), 0.01);
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads(); // To be enhanced with more specific tests
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertNotNull(memory.getRevision());
    }

    // Additional tests for showOutOfMemory and main methods can be added here

}