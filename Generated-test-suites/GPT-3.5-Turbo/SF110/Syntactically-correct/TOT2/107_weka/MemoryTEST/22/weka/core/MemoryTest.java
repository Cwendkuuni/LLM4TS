package weka.core;
import org.junit.*;
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
        assertEquals(0, memory.getInitial());
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
        assertEquals(false, memory.isOutOfMemory());
    }
    
    @Test
    public void testToMegaByte() {
        assertEquals(0.0, Memory.toMegaByte(0));
    }
    
    // Additional test cases for edge cases and error scenarios can be added here
}