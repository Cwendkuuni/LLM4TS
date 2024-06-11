import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
        assertEquals(8034, memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        assertEquals(memory.getTotal(), memory.getCurrent());
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
        assertEquals(0.0, Memory.toMegaByte(0L), 0.0);
        assertEquals(1.0, Memory.toMegaByte(1048576L), 0.0);
        assertEquals(2.5, Memory.toMegaByte(2621440L), 0.0);
    }

    @Test
    public void testShowOutOfMemory() {
        memory.showOutOfMemory();
        // Cannot be tested as it prints to System.err and shows a MessageDialog
    }

    @Test
    public void testStopThreads() {
        memory.stopThreads();
        // Cannot be tested as it stops multiple threads
    }

    @Test
    public void testGetRevision() {
        assertEquals("$Revision: 8034 $", memory.getRevision());
    }
}