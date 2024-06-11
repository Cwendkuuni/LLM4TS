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
        
        Memory memoryWithGUI = new Memory(true);
        assertTrue(memoryWithGUI.getUseGUI());
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
        
        memory.setEnabled(true);
        assertTrue(memory.isOutOfMemory());
    }
    
    @Test
    public void testToMegaByte() {
        assertEquals(1.0, Memory.toMegaByte(1048576), 0.0);
    }
    
    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();
        
        // No exception should be thrown
    }
    
    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        
        assertNotNull(memory.getRevision());
    }
}