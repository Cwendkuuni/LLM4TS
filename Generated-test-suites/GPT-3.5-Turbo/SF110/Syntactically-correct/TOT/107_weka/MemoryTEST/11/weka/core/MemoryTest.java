import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MemoryTest {

    private Memory memory;

    @Before
    public void setup() {
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

        Memory memoryWithGUI = new Memory(true);

        assertTrue(memoryWithGUI.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        long initialMemory = Runtime.getRuntime().totalMemory();

        assertEquals(initialMemory, memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        long currentMemory = Runtime.getRuntime().totalMemory();

        assertEquals(currentMemory, memory.getCurrent());
    }

    @Test
    public void testGetMax() {
        long maxMemory = Runtime.getRuntime().maxMemory();

        assertEquals(maxMemory, memory.getMax());
    }

    @Test
    public void testIsOutOfMemory() {
        memory.setEnabled(true);
        memory.setMax(1000000L);
        memory.setInitial(500000L);
        memory.setCurrent(800000L);

        assertTrue(memory.isOutOfMemory());

        memory.setEnabled(false);

        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        long bytes = 1048576L;

        assertEquals(1.0, memory.toMegaByte(bytes), 0.001);
    }

    @Test
    public void testShowOutOfMemory() {
        // Unable to test as it involves console output and interaction with GUI.
        // Manual testing is required.
        // We can add assertions to verify that the method does not throw any exceptions.
        memory.showOutOfMemory();
    }

    @Test
    public void testStopThreads() {
        // Unable to test as it involves stopping active threads and resource clean up.
        // Manual testing is required.
        // We can add assertions to verify that the method does not throw any exceptions.
        memory.stopThreads();
    }

    @Test
    public void testGetRevision() {
        String revision = "$Revision: 8034 $";

        assertEquals(revision, memory.getRevision());
    }
}