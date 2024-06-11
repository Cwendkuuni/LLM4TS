
package weka.core;

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
        
        memory = new Memory(true);
        assertTrue(memory.getUseGUI());
    }
    
    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        assertEquals(8034, memory.getInitial());
    }
    
    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        Runtime runtime = Runtime.getRuntime();
        assertEquals(runtime.totalMemory(), memory.getCurrent());
    }
    
    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        Runtime runtime = Runtime.getRuntime();
        assertEquals(runtime.maxMemory(), memory.getMax());
    }
    
    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        memory.setInitial(1000000L);
        memory.setEnabled(true);
        
        assertFalse(memory.isOutOfMemory());
        
        memory.setInitial(100000000L);
        assertTrue(memory.isOutOfMemory());
    }
    
    @Test
    public void testToMegaByte() {
        assertEquals(0.0, Memory.toMegaByte(0), 0.001);
        assertEquals(1.0, Memory.toMegaByte(1048576L), 0.001);
        assertEquals(2.0, Memory.toMegaByte(2097152L), 0.001);
    }
    
    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();
        
        // Required test condition missing as the method implementation is empty
        assertFalse(true);
    }
    
    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertEquals("$Revision: 8034 $", memory.getRevision());
    }
    
    @Test
    public void testMain() {
        // Required test condition missing as the method implementation is specific to console output
        assertFalse(true);
    }
}
