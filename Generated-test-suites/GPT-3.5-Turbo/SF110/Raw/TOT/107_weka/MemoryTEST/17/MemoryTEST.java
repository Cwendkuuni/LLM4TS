import static org.junit.Assert.*;
import org.junit.Test;

public class MemoryTest {

    @Test
    public void testIsEnabled() {
        // Test when isEnabled() is true
        Memory memory = new Memory();
        memory.setEnabled(true);
        assertTrue(memory.isEnabled());
        
        // Test when isEnabled() is false
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }
    
    @Test
    public void testSetEnabled() {
        // Test when setEnabled() is true
        Memory memory = new Memory();
        memory.setEnabled(true);
        assertTrue(memory.isEnabled());
        
        // Test when setEnabled() is false
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }
    
    @Test
    public void testGetUseGUI() {
        // Test when useGUI is true
        Memory memory = new Memory(true);
        assertTrue(memory.getUseGUI());
        
        // Test when useGUI is false
        memory = new Memory(false);
        assertFalse(memory.getUseGUI());
    }
    
    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        long initial = memory.getInitial();
        
        // Initial memory should be greater than 0
        assertTrue(initial > 0);
    }
    
    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        long current = memory.getCurrent();
        
        // Current memory should be greater than or equal to initial memory
        assertTrue(current >= memory.getInitial());
    }
    
    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        long max = memory.getMax();
        
        // Max memory should be greater than or equal to current memory
        assertTrue(max >= memory.getCurrent());
    }
    
    @Test
    public void testIsOutOfMemory() {
        // Test when enabled and max - current < initial + 200000L
        Memory memory = new Memory();
        memory.setEnabled(true);
        long max = memory.getMax();
        long current = memory.getCurrent();
        long initial = memory.getInitial();
        assertTrue(memory.isOutOfMemory());
        
        // Test when not enabled
        memory.setEnabled(false);
        assertFalse(memory.isOutOfMemory());
        
        // Test when max - current > initial + 200000L
        memory.setEnabled(true);
        memory.showOutOfMemory();
        assertFalse(memory.isOutOfMemory());
    }
    
    @Test
    public void testToMegaByte() {
        assertEquals(0, Memory.toMegaByte(0), 0.1);
        assertEquals(1, Memory.toMegaByte(1048576), 0.1);
    }
    
    @Test
    public void testShowOutOfMemory() {
        // Test when enabled and useGUI is false
        Memory memory = new Memory();
        memory.setEnabled(true);
        memory.showOutOfMemory();
        
        // Test when enabled and useGUI is true
        memory = new Memory(true);
        memory.showOutOfMemory();
        
        // Test when not enabled
        memory.setEnabled(false);
        memory.showOutOfMemory();
    }
    
    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();
        
        // No assertion, just testing the method
    }
    
    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertNotNull(memory.getRevision());
    }
    
    @Test
    public void testMain() {
        // No assertion, just testing the method
        Memory.main(null);
    }
}