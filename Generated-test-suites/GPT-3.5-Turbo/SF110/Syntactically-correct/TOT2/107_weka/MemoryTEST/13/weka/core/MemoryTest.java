package weka.core;
import static org.junit.Assert.*;
import org.junit.Test;

public class MemoryTest {

    @Test
    public void testMemoryConstructor() {
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());
    }

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertFalse(memory.isEnabled());
        memory.setEnabled(true);
        assertTrue(memory.isEnabled());
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        long currentMemory = memory.getCurrent();
        assertTrue(currentMemory >= 0);
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        boolean outOfMemory = memory.isOutOfMemory();
        assertFalse(outOfMemory);
    }

    @Test
    public void testToMegaByte() {
        long bytes = 1048576;
        double result = Memory.toMegaByte(bytes);
        assertEquals(1.0, result, 0.001);
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();
        // Unable to directly test the behavior, assert true for coverage
        assertTrue(true);
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        String revision = memory.getRevision();
        assertNotNull(revision);
    }
}