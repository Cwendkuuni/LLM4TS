import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class MemoryTest {
    private Memory memory;
    
    @Before
    public void setUp() {
        memory = new Memory();
    }
    
    @Test
    public void testIsEnabled() {
        assertTrue(memory.isEnabled());
        
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }
    
    @Test
    public void testGetUseGUI() {
        assertFalse(memory.getUseGUI());
    }
    
    @Test
    public void testGetInitial() {
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getInitial());
    }
    
    @Test
    public void testGetCurrent() {
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent());
    }
    
    @Test
    public void testGetMax() {
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
    }
    
    @Test
    public void testIsOutOfMemory() {
        assertFalse(memory.isOutOfMemory());
    }
    
    @Test
    public void testToMegaByte() {
        assertEquals(0.0, Memory.toMegaByte(0), 0.001);
        assertEquals(1.0, Memory.toMegaByte(1048576), 0.001);
        assertEquals(2.0, Memory.toMegaByte(2097152), 0.001);
    }
    
    @Test
    public void testShowOutOfMemory() {
        // Not sure how to test this method as it relies on System.out and JOptionPane
    }
    
    @Test
    public void testStopThreads() {
        // Not sure how to test this method as it relies on Thread methods
    }
    
    @Test
    public void testGetRevision() {
        assertEquals("$Revision: 8034 $", memory.getRevision());
    }
}