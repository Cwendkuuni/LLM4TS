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
        assertTrue(memory.getCurrent() > 0);
    }

    @Test
    public void testGetMax() {
        assertTrue(memory.getMax() > 0);
    }

    @Test
    public void testIsOutOfMemory() {
        assertFalse(memory.isOutOfMemory());
        memory.setEnabled(true);
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        assertEquals(0.0, Memory.toMegaByte(0));
        assertEquals(1.0, Memory.toMegaByte(1048576), 0.01);
        assertEquals(2.0, Memory.toMegaByte(2097152), 0.01);
    }

    @Test
    public void testShowOutOfMemory() {
        // Unable to test
    }

    @Test
    public void testStopThreads() {
        // Unable to test
    }

    @Test
    public void testGetRevision() {
        assertEquals("$Revision: 8034 $", memory.getRevision());
    }

    @Test
    public void testMain() {
        // Unable to test
    }
}