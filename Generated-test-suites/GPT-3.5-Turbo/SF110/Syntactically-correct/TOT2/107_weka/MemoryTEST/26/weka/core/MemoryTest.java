package weka.core;
import org.junit.Test;
import static org.junit.Assert.*;

public class MemoryTest {

    @Test
    public void testConstructor() {
        Memory memory = new Memory();
        assertNotNull(memory);
        assertFalse(memory.getUseGUI());
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent());
    }

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertTrue(memory.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        Memory memory = new Memory();
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        assertEquals(Memory.m_Initial, new Memory().getInitial());
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        long totalMemory = memory.getCurrent();
        assertEquals(totalMemory, memory.getCurrent());
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        long bytes = 1048576;
        assertEquals(1.0, Memory.toMegaByte(bytes), 0.001);
    }

    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();
        memory.showOutOfMemory();
        // Unable to assert output directy
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();
        // Unable to assert thread stopping directly
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertNotNull(memory.getRevision());
    }

    @Test
    public void testMain() {
        // Unable to test main method directly
    }
}