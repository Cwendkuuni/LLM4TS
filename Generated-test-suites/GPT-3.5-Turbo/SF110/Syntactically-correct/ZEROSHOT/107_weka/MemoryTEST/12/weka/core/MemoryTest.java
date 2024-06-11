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
        memory.setEnabled(true);
        assertTrue(memory.isEnabled());
        
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
        long currentMemory = memory.getCurrent();
        assertEquals(memory.m_Total, currentMemory);
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
        
        memory.setEnabled(true);
        boolean isOutOfMemory = memory.getMax() - memory.getCurrent() < memory.getInitial() + 200000L;
        assertEquals(isOutOfMemory, memory.isOutOfMemory());
        
        memory.setEnabled(false);
        assertFalse(memory.isOutOfMemory());
    }
    
    @Test
    public void testToMegaByte() {
        long bytes = 1048576L;
        double megabytes = bytes / 1048576.0;
        assertEquals(megabytes, Memory.toMegaByte(bytes), 0);
    }
    
    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();
        memory.showOutOfMemory();
        
        memory.setEnabled(true);
        memory.showOutOfMemory();
        
        memory.setEnabled(false);
        memory.showOutOfMemory();
    }
    
    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();
    }
    
    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertNotNull(memory.getRevision());
    }
}