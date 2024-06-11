import static org.junit.Assert.*;
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
        assertFalse(memory.isOutOfMemory());
        memory.setCurrent(memory.getMax() - (memory.getInitial() + 200000L));
        assertTrue(memory.isOutOfMemory());
    }
    
    @Test
    public void testToMegaByte() {
        assertEquals(2.0, Memory.toMegaByte(2097152L), 0.01);
    }
    
    @Test
    public void testStopThreads() {
        memory.stopThreads();
        // Verify the stopping of threads, if any
    }
    
    @Test
    public void testGetRevision() {
        String revision = RevisionUtils.extract("$Revision: 8034 $");
        assertEquals(revision, memory.getRevision());
    }

}