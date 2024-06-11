import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MemoryTest {
    
    private Memory memory;
    
    @Before
    public void setUp() {
        memory = new Memory();
    }
    
    @Test
    public void testIsEnabled() {
        assertFalse(memory.isEnabled());
        memory.setEnabled(true);
        assertTrue(memory.isEnabled());
    }
    
    @Test
    public void testGetUseGUI() {
        assertFalse(memory.getUseGUI());
        Memory memoryWithGUI = new Memory(true);
        assertTrue(memoryWithGUI.getUseGUI());
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
        memory.setEnabled(true);
        memory.setCurrent(memory.getMax() - memory.getInitial() - 200000L);
        assertTrue(memory.isOutOfMemory());
    }
    
    @Test
    public void testToMegaByte() {
        assertEquals(0.0, memory.toMegaByte(0L), 0.0001);
        assertEquals(1.0, memory.toMegaByte(1048576L), 0.0001);
        assertEquals(2.5, memory.toMegaByte(2621440L), 0.0001);
    }
    
    @Test
    public void testStopThreads() {
        // Cannot test this method as it stops threads
        // and might affect the whole system.
    }
    
    @Test
    public void testGetRevision() {
        assertNotNull(memory.getRevision());
    }
    
    @Test
    public void testMain() {
        // Cannot test this method as it only prints to System.out.
    }
    
}