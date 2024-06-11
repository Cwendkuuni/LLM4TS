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
        long initial = memory.getInitial();
        assertEquals(Memory.toMegaByte(initial), initial / 1048576.0, 0.1);
    }

    @Test
    public void testGetCurrent() {
        long current = memory.getCurrent();
        assertEquals(Memory.toMegaByte(current), current / 1048576.0, 0.1);
    }

    @Test
    public void testGetMax() {
        long max = memory.getMax();
        assertEquals(Memory.toMegaByte(max), max / 1048576.0, 0.1);
    }

    @Test
    public void testIsOutOfMemory() {
        memory.setCurrent(memory.getMax() - memory.getInitial() - 200000L);
        assertTrue(memory.isOutOfMemory());
    }
    
    // Additional test methods can be added here
    
    @Test
    public void testGetRevision() {
        String revision = memory.getRevision();
        assertNotNull(revision);
    }

    @Test
    public void testMain() {
        // Test main method
    }

    @Test
    public void testShowOutOfMemory() {
        // Test showOutOfMemory method
    }

    @Test
    public void testStopThreads() {
        // Test stopThreads method
    }   

}