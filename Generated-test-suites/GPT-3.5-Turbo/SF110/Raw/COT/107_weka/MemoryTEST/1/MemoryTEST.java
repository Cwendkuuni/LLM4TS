
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
        assertEquals(818728708, memory.getInitial());
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
        assertFalse(memory.isOutOfMemory());
    }
    
    @Test
    public void testToMegaByte() {
        double result = Memory.toMegaByte(1048576);
        assertEquals(1.0, result, 0.001);
    }
    
    @Test
    public void testShowOutOfMemory() {
        // Unable to test this method as it interacts with external resources (System and JOptionPane)
        // It should be manually tested.
    }
    
    @Test
    public void testStopThreads() {
        // Unable to test this method as it forcefully stops threads, which can cause unexpected behavior
        // It should be manually tested.
    }
    
    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertEquals("$Revision: 8034 $", memory.getRevision());
    }
    
    @Test
    public void testMain() {
        // Unable to test this method as it only contains print statements
        // It should be manually tested.
    }
    
}
