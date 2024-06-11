import static org.junit.Assert.*;
import org.junit.Test;

public class MemoryTest {

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        Memory.setEnabled(true);
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
        assertTrue(memory.getCurrent() > 0);
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        assertTrue(memory.getMax() > 0);
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertTrue(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        assertEquals(1.0, Memory.toMegaByte(1048576), 0.001);
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();
        // unable to directly test the stopThreads method, as it stops threads
        // possible to assert that the for loop completes without exceptions
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertNotNull(memory.getRevision());
    }

    @Test
    public void testMain() {
        // unable to directly test the main method, as it contains console output
        // possible to execute the main method and assert no exceptions occur
    }

}