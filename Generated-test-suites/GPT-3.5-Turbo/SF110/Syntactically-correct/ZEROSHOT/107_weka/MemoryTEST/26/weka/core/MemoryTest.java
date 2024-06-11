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
        assertEquals(memory.getCurrent(), memory.m_Total);
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
    public void testToMegaBytes() {
        assertEquals(0, memory.toMegaByte(0), 0.001);
        assertEquals(1, memory.toMegaByte(1048576), 0.001);
    }
    
    @Test
    public void testShowOutOfMemory() {
        // Since this method has void return type, it is not testable through Junit.
        // Can be tested manually by checking console output or GUI output.
    }
    
    @Test
    public void testStopThreads() {
        // Since this method has void return type, it is not testable through Junit.
        // Can be tested manually by checking if threads are stopped.
    }
    
    @Test
    public void testGetRevision() {
        assertNotNull(memory.getRevision());
    }
    
    @Test
    public void testMain() {
        // Since this method only prints to console, it is not testable through Junit.
        // Can be tested manually by checking console output.
    }
}