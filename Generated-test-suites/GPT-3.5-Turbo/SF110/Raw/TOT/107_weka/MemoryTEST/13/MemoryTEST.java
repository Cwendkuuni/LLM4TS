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
        assertFalse(memory.isEnabled());

        memory.setEnabled(true);
        assertTrue(memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        assertFalse(memory.getUseGUI());

        memory = new Memory(true);
        assertTrue(memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        long initial = memory.getInitial();
        assertEquals(initial, Memory.m_Initial);
    }

    @Test
    public void testGetCurrent() {
        long current = memory.getCurrent();
        
        // Ensure that current memory usage is greater than or equal to the initial memory
        assertTrue(current >= memory.getInitial());
        
        // Ensure that current memory usage is less than or equal to the max memory
        assertTrue(current <= memory.getMax());
    }

    @Test
    public void testGetMax() {
        long max = memory.getMax();
        assertTrue(max > 0);
    }

    @Test
    public void testIsOutOfMemory() {
        assertFalse(memory.isOutOfMemory());

        // Set max memory to a small value
        memory.m_Max = 100000L;

        assertTrue(memory.isOutOfMemory());

        // Set max memory to a larger value
        memory.m_Max = 100000000L;

        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        long bytes = 1048576L;
        double expected = 1.0;
        assertEquals(expected, memory.toMegaByte(bytes), 0.001);

        bytes = 2048576L;
        expected = 1.953125;
        assertEquals(expected, memory.toMegaByte(bytes), 0.001);
    }

    @Test
    public void testStopThreads() {
        // This test is difficult to validate as it involves stopping threads
        // We can only check for exceptions and verify that the method does not throw any
        assertDoesNotThrow(() -> memory.stopThreads());
    }

    @Test
    public void testGetRevision() {
        String revision = memory.getRevision();
        assertNotNull(revision);
        assertFalse(revision.isEmpty());
    }
}