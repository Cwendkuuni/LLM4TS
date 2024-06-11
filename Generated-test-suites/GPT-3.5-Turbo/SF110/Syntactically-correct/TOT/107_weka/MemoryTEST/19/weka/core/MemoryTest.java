import static org.junit.Assert.*;
import org.junit.*;

public class MemoryTest {

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertFalse(memory.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        Memory memory = new Memory();
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
        assertEquals(4194304, memory.getInitial()); // Assuming the initial memory is always 4 MB
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        long currentMemory = memory.getCurrent();
        assertTrue(currentMemory > 0);
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        assertTrue(memory.getMax() > 0);
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());

        memory.setEnabled(true);
        memory.m_Max = memory.getInitial() + 200000L - 1;
        assertTrue(memory.isOutOfMemory());

        memory.m_Max = memory.getInitial() + 200000L;
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        Memory memory = new Memory();
        double megaBytes = memory.toMegaByte(1048576);
        assertEquals(1.0, megaBytes, 0.0);
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();
        // Assertion not possible as the test depends on the number and state of existing threads
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        String revision = memory.getRevision();
        assertNotNull(revision);
        assertTrue(revision.startsWith("$Revision:"));
        assertTrue(revision.endsWith("$"));
    }
}