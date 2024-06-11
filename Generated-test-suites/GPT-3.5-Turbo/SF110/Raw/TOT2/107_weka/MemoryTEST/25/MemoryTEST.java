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
        assertEquals(0, memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        assertTrue(memory.getCurrent() > 0);
    }

    @Test
    public void testGetMax() {
        assertTrue(memory.getMax() > 0);
    }

    @Test
    public void testIsOutOfMemory() {
        assertFalse(memory.isOutOfMemory());
        memory.maxMemory = memory.totalMemory + 1000L;
        assertTrue(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        double result = memory.toMegaByte(1048576L);
        assertEquals(1.0, result, 0.001);
    }

    @Test
    public void testStopThreads() {
        memory.stopThreads();
        // No assertion can be made as it's a void method
    }

    // Add more test cases for edge cases and error scenarios as needed

}