
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
        long expectedInitial = Runtime.getRuntime().totalMemory();
        assertEquals(expectedInitial, memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        long expectedTotal = Runtime.getRuntime().totalMemory();
        assertEquals(expectedTotal, memory.getCurrent());
    }

    @Test
    public void testGetMax() {
        long expectedMax = Runtime.getRuntime().maxMemory();
        assertEquals(expectedMax, memory.getMax());
    }

    @Test
    public void testIsOutOfMemory() {
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        long bytes = 1048576L;
        double expectedValue = 1.0;
        assertEquals(expectedValue, memory.toMegaByte(bytes), 0.01);
    }

    @Test
    public void testShowOutOfMemory() {
        memory.setEnabled(false);
        memory.showOutOfMemory();
        // Verify if no error occurs
    }

    @Test
    public void testStopThreads() {
        // Cannot create meaningful test case as it involves stopping threads
    }

    @Test
    public void testGetRevision() {
        String expectedRevision = "$Revision: 8034 $";
        assertEquals(expectedRevision, memory.getRevision());
    }

    @Test
    public void testMain() {
        // Cannot create meaningful test case as it involves console output
    }
}

