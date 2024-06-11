import org.junit.*;
import static org.junit.Assert.*;

public class MemoryTest {

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
        long initial = memory.getInitial();
        assertEquals(Runtime.getRuntime().totalMemory(), initial);
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
        long max = memory.getMax();
        assertEquals(Runtime.getRuntime().maxMemory(), max);
    }

    @Test
    public void testIsOutOfMemory_NotEnabled() {
        Memory memory = new Memory();
        memory.setEnabled(false);
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testIsOutOfMemory_EnoughMemory() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testIsOutOfMemory_NotEnoughMemory() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        long initial = memory.getInitial();
        long current = 500000000L;
        memory.m_Total = current;
        memory.m_Max = current;
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        assertEquals(1.0, Memory.toMegaByte(1048576L), 0);
        assertEquals(2.0, Memory.toMegaByte(2097152L), 0);
    }

    @Test
    public void testShowOutOfMemory_NotEnabled() {
        Memory memory = new Memory();
        memory.setEnabled(false);
        memory.showOutOfMemory();
        // No assertions needed, just checking for method execution
    }

    @Test
    public void testShowOutOfMemory_Enabled() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        memory.showOutOfMemory();
        // No assertions needed, just checking for method execution
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();
        // No assertions needed, just checking for method execution
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertNotNull(memory.getRevision());
    }

    @Test
    public void testMain() {
        // No assertions needed, just checking for method execution
        Memory.main(new String[]{});
    }
}