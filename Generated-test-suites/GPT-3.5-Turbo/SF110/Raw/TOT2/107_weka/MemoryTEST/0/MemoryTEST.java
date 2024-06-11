import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MemoryTest {

    private Memory memory;

    @Before
    public void setUp() {
        memory = new Memory(true);
    }

    @Test
    public void testIsEnabled() {
        assertTrue(memory.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        assertTrue(memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        assertEquals(0, memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        assertTrue(memory.getCurrent() >= 0);
    }

    @Test
    public void testGetMax() {
        assertTrue(memory.getMax() > 0);
    }

    @Test
    public void testIsOutOfMemory() {
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        assertEquals(2.0, Memory.toMegaByte(2097152), 0.0);
    }

    @Test
    public void testShowOutOfMemory() {
        memory.showOutOfMemory();
        // Check console output or GUI
    }

    @Test
    public void testStopThreads() {
        memory.stopThreads();
        // Check if threads are stopped
    }

    @Test
    public void testGetRevision() {
        assertNotNull(memory.getRevision());
    }

    // Add more test cases for specific edge cases and error scenarios

}