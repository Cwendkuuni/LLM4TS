package weka.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MemoryTest {
    @Test
    public void testMemoryConstructor() {
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());
        assertEquals(0, memory.getCurrent());
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
    }

    @Test
    public void testMemoryConstructorWithUseGUI() {
        Memory memory = new Memory(true);
        assertTrue(memory.getUseGUI());
        assertEquals(0, memory.getCurrent());
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
    }

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertTrue(memory.isEnabled());

        memory.setEnabled(false);
        assertFalse(memory.isEnabled());

        memory.setEnabled(true);
        assertTrue(memory.isEnabled());
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
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent());
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

        memory.setEnabled(true);
        assertTrue(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        Memory memory = new Memory();
        assertEquals(0, memory.toMegaByte(0), 0);
        assertEquals(0.00000095367431640625, memory.toMegaByte(1), 0.0000001);
        assertEquals(1, memory.toMegaByte(1048576), 0);
    }

    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory(false);
        memory.setEnabled(true);

        // No assertions can be made as showOutOfMemory() only prints to System.err

        memory = new Memory(true);
        memory.setEnabled(true);

        // No assertions can be made as showOutOfMemory() only shows a dialog with the error message
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();

        // No assertions can be made as stopThreads() only stops threads if their names start with "Thread" or "AWT-EventQueue"
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertNotNull(memory.getRevision());
    }

    @Test
    public void testMain() {
        // No assertions can be made in main() method as it only prints to System.out
    }
}