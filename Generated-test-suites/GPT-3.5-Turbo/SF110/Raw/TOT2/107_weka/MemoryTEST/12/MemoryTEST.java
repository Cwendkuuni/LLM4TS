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
        assertTrue(Memory.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        Memory.setEnabled(false);
        assertFalse(Memory.isEnabled());
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
        assertTrue(memory.getMax() >= 0);
    }

    @Test
    public void testIsOutOfMemory() {
        Memory.setEnabled(true);
        memory.getCurrent();
        memory.setMax(1000000);
        memory.setInitial(50000);
        assertTrue(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        assertEquals(1.0, Memory.toMegaByte(1048576), 0);
    }

    @Test
    public void testStopThreads() {
        memory.stopThreads();
        // No exception should be thrown
    }

    @Test
    public void testGetRevision() {
        assertNotNull(memory.getRevision());
    }

}