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
        assertTrue(memory.isEnabled());
    }
    
    @Test
    public void testSetEnabled() {
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }
    
    @Test
    public void testGetUseGUI() {
        assertFalse(memory.getUseGUI());
    }
    
    @Test
    public void testGetInitial() {
        assertEquals(0, memory.getInitial());
    }
    
    @Test
    public void testGetCurrent() {
        assertTrue(memory.getCurrent() >= 0);
    }
    
    @Test
    public void testGetMax() {
        assertTrue(memory.getMax() > 0);
    }
    
    @Test
    public void testIsOutOfMemory() {
        assertFalse(memory.isOutOfMemory());
    }
    
    @Test
    public void testToMegaByte() {
        assertEquals(1, Memory.toMegaByte(1048576), 0.1);
    }
    
    @Test
    public void testShowOutOfMemory() {
        // Not easily testable
    }
    
    @Test
    public void testStopThreads() {
        // Not easily testable
    }
    
    @Test
    public void testGetRevision() {
        assertNotNull(memory.getRevision());
    }
    
}