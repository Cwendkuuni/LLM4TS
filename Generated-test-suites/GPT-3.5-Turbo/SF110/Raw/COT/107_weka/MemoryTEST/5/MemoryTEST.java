
import org.junit.*;
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
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
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
        long currentMemory = memory.getCurrent();
        assertTrue(currentMemory >= 0);
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
        long bytes = 1048576;
        double megaByte = Memory.toMegaByte(bytes);
        assertEquals(1.0, megaByte, 0);
    }
    
    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();
        // Not feasible to test since it involves system output and GUI interaction
    }
    
    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        // Not feasible to test since it involves stopping threads
    }
    
    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertNotNull(memory.getRevision());
    }
    
    @Test
    public void testMain() {
        // Not feasible to test since it involves system output
    }
    
}
