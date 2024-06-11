import org.junit.Test;
import static org.junit.Assert.*;

public class MemoryTest {

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        assertTrue(memory.isEnabled());

        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());
        
        memory = new Memory(true);
        assertTrue(memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        long initial = memory.getInitial();
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
        assertTrue(max >= 0);
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        boolean outOfMemory = memory.isOutOfMemory();
        assertFalse(outOfMemory);
    }

    @Test
    public void testToMegaByte() {
        Memory memory = new Memory();
        double megaBytes = memory.toMegaByte(1048576L);
        assertEquals(1.0, megaBytes, 0.001);
    }

    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();
        memory.showOutOfMemory();
        // No assertions as this method only prints out information
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();
        // No assertions as this method only stops threads and performs garbage collection
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        String revision = memory.getRevision();
        assertNotNull(revision);
    }
}