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
        assertEquals(Memory.m_Initial, memory.getInitial());
    }
    
    @Test
    public void testGetCurrent() {
        long current = Runtime.getRuntime().totalMemory();
        assertEquals(current, memory.getCurrent());
    }
    
    @Test
    public void testGetMax() {
        long max = Runtime.getRuntime().maxMemory();
        assertEquals(max, memory.getMax());
    }
    
    @Test
    public void testIsOutOfMemory() {
        boolean outOfMemory = memory.isEnabled() && memory.getMax() - memory.getCurrent() < memory.getInitial() + 200000L;
        assertEquals(outOfMemory, memory.isOutOfMemory());
    }
    
    @Test
    public void testToMegaByte() {
        long bytes = 1048576L;
        double expected = bytes / 1048576.0;
        assertEquals(expected, memory.toMegaByte(bytes), 0);
    }
    
    @Test
    public void testStopThreads() {
        memory.stopThreads();
        // unable to test the effects of stopping threads
        // only checking if the method does not throw any exceptions
    }
    
    @Test
    public void testGetRevision() {
        String expected = "$Revision: 8034 $";
        assertEquals(expected, memory.getRevision());
    }
}