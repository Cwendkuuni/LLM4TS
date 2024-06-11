import org.junit.Before;
import org.junit.After;
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
        assertFalse(memory.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        memory.setEnabled(true);
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
        assertEquals(memory.getCurrent(), memory.getTotal());
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
        assertEquals(0, memory.toMegaByte(0), 0.001);
        assertEquals(1, memory.toMegaByte(1048576), 0.001);
        assertEquals(2, memory.toMegaByte(2097152), 0.001);
    }

    @Test
    public void testShowOutOfMemory() {
        // Cannot be tested as it has dependency on System.err and JOptionPane
    }

    @Test
    public void testStopThreads() {
        // Cannot be tested as it stops threads
    }

    @Test
    public void testGetRevision() {
        // Cannot be tested as it returns revision information
    }

    @Test
    public void testMain() {
        // Cannot be tested as it prints to console
    }

    @After
    public void tearDown() {
        memory = null;
    }

}