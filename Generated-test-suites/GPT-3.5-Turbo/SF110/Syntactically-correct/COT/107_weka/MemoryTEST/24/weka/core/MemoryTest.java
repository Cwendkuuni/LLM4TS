
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import weka.core.Memory;

public class MemoryTest {
    
    private Memory memory;
    
    @Before
    public void setUp() {
        memory = new Memory();
    }
    
    @After
    public void tearDown() {
        memory = null;
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
        long currentMemory = memory.getCurrent();
        assertTrue(currentMemory >= 0);
    }
    
    @Test
    public void testGetMax() {
        long maxMemory = memory.getMax();
        assertTrue(maxMemory > 0);
    }
    
    @Test
    public void testIsOutOfMemory() {
        assertFalse(memory.isOutOfMemory());
    }
    
    @Test
    public void testToMegaByte() {
        double megaByte = Memory.toMegaByte(1048576);
        assertEquals(1.0, megaByte, 0.0001);
    }
    
    @Test
    public void testShowOutOfMemory() {
        memory.setEnabled(false);
        memory.showOutOfMemory();
    }
    
    @Test
    public void testStopThreads() {
        memory.stopThreads();
    }
    
    @Test
    public void testGetRevision() {
        assertNotNull(memory.getRevision());
    }
}
