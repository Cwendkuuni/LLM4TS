import static org.junit.Assert.*;
import org.junit.Test;

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
        assertEquals(Memory.m_Initial, memory.getInitial());
    }
    
    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        assertEquals(memory.m_Total, memory.getCurrent());
    }
    
    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        assertEquals(memory.m_Max, memory.getMax());
    }
    
    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());
    }
    
    @Test
    public void testToMegaByte() {
        long bytes = 1048576;
        double expected = 1.0;
        assertEquals(expected, Memory.toMegaByte(bytes), 0.001);
    }
    
    @Test
    public void testShowOutOfMemory() {
        // Unable to test without modifying system out and err
    }
    
    @Test
    public void testStopThreads() {
        // Unable to test without stopping actual threads
    }
    
    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertEquals("$Revision: 8034 $", memory.getRevision());
    }
    
    @Test
    public void testMain() {
        // Unable to test without modifying system out
    }
}