import org.junit.Test;
import static org.junit.Assert.*;

public class MemoryTest {

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertTrue(memory.isEnabled());
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
        assertEquals(983040L, memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        assertTrue(memory.getCurrent() <= memory.getMax());
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        assertTrue(memory.getMax() >= memory.getInitial());
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        Memory memory = new Memory();
        assertEquals(0.0, memory.toMegaByte(0L), 0.0001);
        assertEquals(0.0009536743, memory.toMegaByte(1L), 0.0001);
        assertEquals(1.0, memory.toMegaByte(1048576L), 0.0001);
    }
    
    @Test
    public void testShowOutOfMemory() {
        // Not easy to test since it involves system output and GUI interaction
        // Manual testing required
    }
    
    @Test
    public void testStopThreads() {
        // Not easy to test since it involves stopping threads
        // Manual testing required
    }
    
    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertNotNull(memory.getRevision());
    }

    @Test
    public void testMain() {
        // Need to manually check the console output
    }
}