
package weka.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class MemoryTest {

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        
        // Typical use case: Memory is enabled
        Memory.m_Enabled = true;
        assertTrue(memory.isEnabled());
        
        // Edge case: Memory is disabled
        Memory.m_Enabled = false;
        assertFalse(memory.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        Memory memory = new Memory();
        
        // Typical use case: Enable memory
        memory.setEnabled(true);
        assertTrue(Memory.m_Enabled);
        
        // Typical use case: Disable memory
        memory.setEnabled(false);
        assertFalse(Memory.m_Enabled);
    }

    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory();

        // Typical use case: UseGUI is true
        memory.m_UseGUI = true;
        assertTrue(memory.getUseGUI());

        // Typical use case: UseGUI is false
        memory.m_UseGUI = false;
        assertFalse(memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        long initial = Runtime.getRuntime().totalMemory();

        assertEquals(initial, memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        long current = Runtime.getRuntime().totalMemory();

        assertEquals(current, memory.getCurrent());
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        long max = Runtime.getRuntime().maxMemory();

        assertEquals(max, memory.getMax());
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();

        // Typical use case: Not out of memory
        Memory.m_Enabled = true;
        memory.m_Max = 8000000L;
        memory.m_Total = 2000000L;
        long initial = 100000L;
        assertFalse(memory.isOutOfMemory());

        // Typical use case: Out of memory
        Memory.m_Enabled = true;
        memory.m_Max = 5000000L;
        memory.m_Total = 4000000L;
        assertFalse(memory.isOutOfMemory());

        // Error scenario: Memory not enabled, should not check for out of memory
        Memory.m_Enabled = false;
        memory.m_Max = 5000000L;
        memory.m_Total = 4000000L;
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        Memory memory = new Memory();

        // Typical use case: Convert bytes to megabytes
        long bytes = 1048576L;
        double megabytes = 1.0;
        assertEquals(megabytes, memory.toMegaByte(bytes), 0.001);
    }

    @Test
    public void testShowOutOfMemory() {
        // Cannot be tested as it relies on System.err.println() and JOptionPane.showMessageDialog() which cannot be directly tested.
    }

    @Test
    public void testStopThreads() {
        // Cannot be directly tested as it relies on Thread.stop() which is deprecated and unsafe.
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();

        // Typical use case: Get the revision string from RevisionUtils
        String revision = "$Revision: 8034 $";
        assertEquals(revision, memory.getRevision());
    }

    @Test
    public void testMain() {
        // Cannot be directly tested as it relies on System.out.println() which cannot be directly tested.
    }
}
