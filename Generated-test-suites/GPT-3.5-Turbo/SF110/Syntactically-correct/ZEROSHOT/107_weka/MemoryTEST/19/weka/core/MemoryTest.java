import org.junit.*;
import static org.junit.Assert.*;

public class MemoryTest {

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertFalse(memory.isEnabled());
        memory.setEnabled(true);
        assertTrue(memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        long initial = memory.getInitial();
        assertEquals(Runtime.getRuntime().totalMemory(), initial);
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        long current = memory.getCurrent();
        assertEquals(Runtime.getRuntime().totalMemory(), current);
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        long max = memory.getMax();
        assertEquals(Runtime.getRuntime().maxMemory(), max);
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());
        memory.setEnabled(true);
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        Memory memory = new Memory();
        long bytes = 1048576L;
        double expected = 1.0;
        double result = memory.toMegaByte(bytes);
        assertEquals(expected, result, 0.0);
    }

    @Test
    public void testShowOutOfMemory() {
        // TODO: Implement this test
    }

    @Test
    public void testStopThreads() {
        // TODO: Implement this test
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertNotNull(memory.getRevision());
    }

    @Test
    public void testMain() {
        // TODO: Implement this test
    }
}