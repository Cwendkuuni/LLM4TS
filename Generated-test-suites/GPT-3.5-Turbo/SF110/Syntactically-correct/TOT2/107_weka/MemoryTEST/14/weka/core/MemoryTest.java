package weka.core;
import org.junit.Test;
import static org.junit.Assert.*;

public class MemoryTest {

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertEquals(true, memory.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        Memory memory = new Memory();
        memory.setEnabled(false);
        assertEquals(false, memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory();
        assertEquals(false, memory.getUseGUI());
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
        assertEquals(1.0, Memory.toMegaByte(1048576), 0.001);
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();
        // Need to assert behavior, can't just assert return values
    }

    // Additional test cases for edge cases and error scenarios to be added here

}