package weka.core;
import static org.junit.Assert.*;
import org.junit.Test;

public class MemoryTest {

    @Test
    public void testConstructor() {
        Memory memory = new Memory();
        assertNotNull(memory);
        assertFalse(memory.getUseGUI());
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
        Memory memory = new Memory();
        assertEquals(Memory.m_Initial, memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        long current = memory.getCurrent();
        assertEquals(memory.m_Total, current);
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        assertEquals(memory.m_Max, memory.getMax());
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
        memory.setEnabled(true);
        memory.getCurrent();
        memory.showOutOfMemory();
        // Unable to test System.err output
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();
        // Unable to test thread stopping
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertNotNull(memory.getRevision());
    }

    @Test
    public void testMain() {
        Memory.main(null);
        // Unable to test System.out output
    }

}