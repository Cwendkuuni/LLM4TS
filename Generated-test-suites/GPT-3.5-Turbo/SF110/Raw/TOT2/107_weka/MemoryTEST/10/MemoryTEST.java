import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

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
    }

    @Test
    public void testGetInitial() {
        assertEquals(Memory.m_Initial, memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        long currentMemory = memory.getCurrent();
        assertTrue(currentMemory > 0);
    }

    @Test
    public void testGetMax() {
        assertTrue(memory.getMax() > 0);
    }

    @Test
    public void testIsOutOfMemory() {
        assertTrue(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        assertEquals(0.0, Memory.toMegaByte(0L), 0.0001);
        assertEquals(1.0, Memory.toMegaByte(1048576L), 0.0001);
    }

    @Test
    public void testShowOutOfMemory() {
        // Not testable as it interacts with System.out and JOptionPane
    }

    @Test
    public void testStopThreads() {
        // Not testable as it stops threads
    }

    @Test
    public void testGetRevision() {
        assertNotNull(memory.getRevision());
    }

    // Add more test cases for edge cases and exception scenarios if needed

}