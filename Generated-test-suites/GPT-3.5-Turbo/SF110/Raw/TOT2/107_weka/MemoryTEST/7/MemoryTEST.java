import org.junit.Test;
import static org.junit.Assert.*;

public class MemoryTest {

    @Test
    public void testConstructor() {
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());
    }

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertTrue(memory.isEnabled());
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory(true);
        assertTrue(memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        assertEquals(Memory.m_Initial, new Memory().getInitial());
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        assertTrue(memory.getCurrent() > 0);
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        assertTrue(memory.getMax() > 0);
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        
        memory.setEnabled(true);
        long initial = memory.getInitial();
        long max = memory.getMax();
        long current = memory.getCurrent();
        assertFalse(memory.isOutOfMemory());
        
        // Test edge case where memory is out
        memory.setCurrent(max - initial - 199999L);
        assertTrue(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        long bytes = 1048576;
        assertEquals(1.0, Memory.toMegaByte(bytes), 0.01);
    }

    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        memory.setCurrent(memory.getMax() - memory.getInitial() - 200000L);
        memory.showOutOfMemory(); // Just test if it runs without exceptions
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads(); // Just test if it runs without exceptions
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertNotNull(memory.getRevision());
    }
}