
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MemoryTest {

    private Memory memory;

    @Before
    public void setUp() {
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
        assertEquals(1.0, megabytes, 0.001);
    }

    @Test
    public void testShowOutOfMemory() {
        // No assertion possible, method only prints information
        memory.showOutOfMemory();
    }

    @Test
    public void testStopThreads() {
        // No assertion possible, method stops threads
        memory.stopThreads();
    }
}
