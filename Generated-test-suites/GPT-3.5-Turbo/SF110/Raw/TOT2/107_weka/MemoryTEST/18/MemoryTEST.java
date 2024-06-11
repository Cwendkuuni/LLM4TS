import static org.junit.Assert.*;
import org.junit.Test;

public class MemoryTest {

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertFalse(memory.isEnabled());
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
        assertEquals(0, Memory.m_Initial);
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
        Memory memory = new Memory(false);
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        assertEquals(0.0, Memory.toMegaByte(0), 0.0001);
    }

    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();
        memory.showOutOfMemory(); // Cannot test output
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads(); // Cannot test stopping threads
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertNotNull(memory.getRevision());
    }

    // Add more test cases for edge cases and error scenarios if required

}