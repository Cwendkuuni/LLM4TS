
package weka.core;

import static org.junit.Assert.*;
import org.junit.Test;

public class MemoryTest {
    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertTrue(memory.isEnabled());
        
        memory.setEnabled(false);
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
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getInitial());
    }
    
    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent());
    }
    
    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
    }
    
    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());
        
        // Simulate being out of memory by setting current memory close to the max
        long currentMemory = Runtime.getRuntime().maxMemory() - memory.getInitial() - 200000L;
        memory.m_Total = currentMemory;
        assertTrue(memory.isOutOfMemory());
    }
    
    @Test
    public void testToMegaByte() {
        long bytes = 1048576;
        double expected = 1.0;
        assertEquals(expected, Memory.toMegaByte(bytes), 0.001);
    }
    
    @Test
    public void testShowOutOfMemory() {
        // Not possible to test this method as it prints to console and shows a GUI message box.
        // We can only verify that the code doesn't throw any exceptions.
        Memory memory = new Memory();
        memory.showOutOfMemory();
    }
    
    @Test
    public void testStopThreads() {
        // Not possible to test this method as it forcefully stops threads.
        // We can only verify that the code doesn't throw any exceptions.
        Memory memory = new Memory();
        memory.stopThreads();
    }
    
    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertNotNull(memory.getRevision());
    }
    
    @Test
    public void testMain() {
        // Not possible to test this method as it only prints to console.
        // We can only verify that the code doesn't throw any exceptions.
        Memory.main(new String[0]);
    }
}
