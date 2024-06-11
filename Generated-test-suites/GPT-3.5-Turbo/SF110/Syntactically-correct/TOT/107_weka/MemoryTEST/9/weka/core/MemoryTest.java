import org.junit.Test;
import static org.junit.Assert.*;

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
        
        memory = new Memory(true);
        assertTrue(memory.getUseGUI());
    }
    
    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        
        // Initial memory should be greater than or equal to 0
        assertTrue(memory.getInitial() >= 0);
    }
    
    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        
        // Current memory should be greater than or equal to 0
        assertTrue(memory.getCurrent() >= 0);
    }
    
    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        
        // Max memory should be greater than or equal to 0
        assertTrue(memory.getMax() >= 0);
    }
    
    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        
        memory.setEnabled(true);
        
        // Test when current memory is greater than max memory
        memory.m_Total = 100000000L;
        memory.m_Max = 100L;
        assertTrue(memory.isOutOfMemory());
        
        // Test when current memory is equal to max memory
        memory.m_Total = 100L;
        memory.m_Max = 100L;
        assertFalse(memory.isOutOfMemory());
        
        // Test when current memory is less than max memory
        memory.m_Total = 100L;
        memory.m_Max = 100000000L;
        assertFalse(memory.isOutOfMemory());
    }
    
    @Test
    public void testToMegaByte() {
        Memory memory = new Memory();
        
        // Test converting bytes to mega bytes
        assertEquals(1.0, memory.toMegaByte(1048576L), 0.001);
        assertEquals(2.0, memory.toMegaByte(2097152L), 0.001);
        assertEquals(0.5, memory.toMegaByte(524288L), 0.001);
    }
    
    @Test
    public void testShowOutOfMemory() {
        // Unable to test this method as it uses System.err and JOptionPane
        
        // Possible future improvement: Mock System.err and JOptionPane and assert their interactions
    }
    
    @Test
    public void testStopThreads() {
        // Unable to test this method as it alters the behavior of running threads
        
        // Possible future improvement: Mock Thread and assert that the correct threads are stopped
    }
    
    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        
        // Test that the revision matches the expected format
        assertTrue(memory.getRevision().matches("\\$Revision: \\d+ \\$"));
    }
    
    // Add additional test methods for public static void main(String[] args) if necessary
    
}