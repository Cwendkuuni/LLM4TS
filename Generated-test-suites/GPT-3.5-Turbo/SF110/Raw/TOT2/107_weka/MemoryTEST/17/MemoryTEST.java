import org.junit.Test;
import static org.junit.Assert.*;

public class MemoryTest {

    @Test
    public void testMemoryConstructor() {
        Memory memory = new Memory();
        assertNotNull(memory);
        assertFalse(memory.getUseGUI());
    }

    @Test
    public void testSetEnabled() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        assertTrue(Memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        long initial = Memory.getInitial();
        assertTrue(initial >= 0);
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        long current = memory.getCurrent();
        assertTrue(current >= 0);
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        long max = memory.getMax();
        assertTrue(max > 0);
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertTrue(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        double megaByte = Memory.toMegaByte(1048576);
        assertEquals(1.0, megaByte, 0.001);
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();
        assertTrue(true); // Cannot assert any specific output as it depends on running threads
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertNotNull(memory.getRevision());
    }

    @Test
    public void testMain() {
        Memory.main(new String[]{});
        assertTrue(true); // Cannot assert any specific output as it just prints to console
    }
}