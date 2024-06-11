package weka.core;
import static org.junit.Assert.*;
import org.junit.Test;

public class MemoryTest {
    Memory memory = new Memory();

    @Test
    public void testIsEnabled() {
        assertFalse(memory.isEnabled());
        memory.setEnabled(true);
        assertTrue(memory.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        assertFalse(memory.getUseGUI());
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
        assertTrue(memory.getMax() >= 0);
    }

    @Test
    public void testIsOutOfMemory() {
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        assertEquals(0.0, Memory.toMegaByte(0), 0.001);
    }

    @Test
    public void testShowOutOfMemory() {
        memory.showOutOfMemory();
        // For GUI disabled scenario test
    }

    @Test
    public void testStopThreads() {
        // For stopping threads
    }

    @Test
    public void testGetRevision() {
        assertNotNull(memory.getRevision());
    }
}