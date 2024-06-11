import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class MemoryTest {

    private static Memory memory;

    @BeforeClass
    public static void setUp() {
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
        memory = new Memory(true);
        assertTrue(memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        assertTrue(memory.getInitial() > 0);
    }

    @Test
    public void testGetCurrent() {
        assertTrue(memory.getCurrent() >= memory.getInitial());
    }

    @Test
    public void testGetMax() {
        assertTrue(memory.getMax() > 0);
    }

    @Test
    public void testIsOutOfMemory() {
        assertFalse(memory.isOutOfMemory());
        memory.setEnabled(true);
        memory.setCurrent(memory.getMax() - memory.getInitial() - 200000L);
        assertFalse(memory.isOutOfMemory());
        memory.setCurrent(memory.getMax() - memory.getInitial() - 199999L);
        assertTrue(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        assertEquals(0.0, memory.toMegaByte(0), 0.01);
        assertEquals(1.0, memory.toMegaByte(1048576), 0.01);
        assertEquals(2.0, memory.toMegaByte(2097152), 0.01);
    }

    @Test
    public void testShowOutOfMemory() {
        // Unable to test System.err and JOptionPane.showMessageDialog
        // as they rely on system output and input.
    }

    @Test
    public void testStopThreads() {
        // Unable to test Thread.stop() method as it is deprecated
    }

    @Test
    public void testGetRevision() {
        assertNotNull(memory.getRevision());
    }
}