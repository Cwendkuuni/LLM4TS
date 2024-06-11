package weka.core;

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
        assertEquals(true, memory.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        memory.setEnabled(false);
        assertEquals(false, memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        assertEquals(false, memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        long expectedInitial = Runtime.getRuntime().totalMemory();
        assertEquals(expectedInitial, memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        long expectedCurrent = Runtime.getRuntime().totalMemory();
        assertEquals(expectedCurrent, memory.getCurrent());
    }

    @Test
    public void testGetMax() {
        long expectedMax = Runtime.getRuntime().maxMemory();
        assertEquals(expectedMax, memory.getMax());
    }

    @Test
    public void testIsOutOfMemory() {
        boolean expectedOutOfMemory = memory.isEnabled() && memory.getMax() - memory.getCurrent() < memory.getInitial() + 200000L;
        assertEquals(expectedOutOfMemory, memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        long bytes = 1048576L;
        double expectedMegaBytes = bytes / 1048576.0;
        assertEquals(expectedMegaBytes, memory.toMegaByte(bytes), 0.01);
    }

    @Test
    public void testShowOutOfMemory() {
        // Test if method executes without errors
        memory.showOutOfMemory();
    }

    @Test
    public void testStopThreads() {
        // Test if method executes without errors
        memory.stopThreads();
    }
}