import org.junit.*;
import static org.junit.Assert.*;

public class MemoryTest {

    @Test
    public void testIsEnabled() {
        // Typical use case
        Memory memory = new Memory();
        assertTrue(memory.isEnabled());

        // Edge case
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        // Typical use case
        Memory memory = new Memory();
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());

        // Edge case
        memory.setEnabled(true);
        assertTrue(memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        // Typical use case
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());

        // Edge case
        memory = new Memory(true);
        assertTrue(memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        // Typical use case
        Memory memory = new Memory();
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getInitial());

        // Edge case
        Runtime.getRuntime().gc();
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        // Typical use case
        Memory memory = new Memory();
        long currentMemory = Runtime.getRuntime().totalMemory();
        assertEquals(currentMemory, memory.getCurrent());

        // Edge case
        Runtime.getRuntime().gc();
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent());
    }

    @Test
    public void testGetMax() {
        // Typical use case
        Memory memory = new Memory();
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());

        // Edge case
        Runtime.getRuntime().gc();
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
    }

    @Test
    public void testIsOutOfMemory() {
        // Typical use case
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());

        // Edge case
        memory.m_Max = memory.m_Total;
        assertTrue(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        // Typical use case
        Memory memory = new Memory();
        assertEquals(0.0, memory.toMegaByte(0L), 0.0);
        assertEquals(1.0, memory.toMegaByte(1048576L), 0.0);

        // Edge case
        assertEquals(Double.POSITIVE_INFINITY, memory.toMegaByte(Long.MAX_VALUE), 0.0);
    }

    @Test
    public void testShowOutOfMemory() {
        // Not possible to test as it has dependency on external classes and Java Swing GUI
        // This method needs to be manually tested
    }

    @Test
    public void testStopThreads() {
        // Not possible to test as it forcefully stops threads and can cause system instability
        // This method needs to be manually tested
    }

    @Test
    public void testGetRevision() {
        // Typical use case
        Memory memory = new Memory();
        assertNotNull(memory.getRevision());

        // Edge case
        memory = null;
        assertNull(memory.getRevision());
    }

    @Test
    public void testMain() {
        // Not possible to test as it is a main method and prints output to console
        // This method needs to be manually tested
    }
}