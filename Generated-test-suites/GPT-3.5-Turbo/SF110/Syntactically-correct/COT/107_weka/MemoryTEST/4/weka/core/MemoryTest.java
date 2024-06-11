import org.junit.Test;
import static org.junit.Assert.*;

public class MemoryTest {

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertFalse(memory.isEnabled());
        
        memory.setEnabled(true);
        assertTrue(memory.isEnabled());
        
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
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
        
        Memory memoryWithInitial = new Memory(true);
        assertEquals(0, memoryWithInitial.getInitial());
    }
    
    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        assertNotNull(memory.getCurrent());
    }
    
    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        assertNotNull(memory.getMax());
    }
    
    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());
        
        Memory memoryWithOutOfMemory = new Memory(true);
        // TODO: Test for the edge case scenario of out of memory
    }
    
    @Test
    public void testToMegaByte() {
        Memory memory = new Memory();
        assertEquals(0.0, memory.toMegaByte(0));
        
        assertEquals(1.0, memory.toMegaByte(1048576));
    }
    
    @Test
    public void testShowOutOfMemory() {
        // TODO: Test for the scenario of showing out of memory message (if possible)
    }
    
    @Test
    public void testStopThreads() {
        // TODO: Test for the scenario of stopping threads (if possible)
    }
    
    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertNotNull(memory.getRevision());
    }
}