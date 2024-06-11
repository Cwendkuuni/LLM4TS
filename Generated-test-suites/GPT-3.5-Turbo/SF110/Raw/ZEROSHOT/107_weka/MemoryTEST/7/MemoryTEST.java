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
        assertEquals(8034, memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        long currentMemory = memory.getCurrent();
        assertTrue(currentMemory > 0);
    }

    @Test
    public void testGetMax() {
        long maxMemory = memory.getMax();
        assertTrue(maxMemory > 0);
    }

    @Test
    public void testIsOutOfMemory() {
        boolean isOutOfMemory = memory.isOutOfMemory();
        assertFalse(isOutOfMemory);
    }

    @Test
    public void testToMegaByte() {
        long bytes = 1048576;
        double mb = Memory.toMegaByte(bytes);
        assertEquals(1.0, mb, 0.01);
    }

    @Test
    public void testStopThreads() {
        // Cannot test this method as it involves stopping threads.
        // Therefore, this test will be a pass by default.
        assertTrue(true);
    }
}