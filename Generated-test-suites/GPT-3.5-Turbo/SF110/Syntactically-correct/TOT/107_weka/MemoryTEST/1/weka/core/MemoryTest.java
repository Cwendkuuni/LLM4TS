
import org.junit.Test;
import static org.junit.Assert.*;

public class MemoryTest {

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertEquals(true, memory.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        Memory memory = new Memory();
        memory.setEnabled(false);
        assertEquals(false, memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory(true);
        assertEquals(true, memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        memory.getCurrent();
        // It is difficult to assert the exact value here, so we can only check that there was no error
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        memory.m_Total = memory.getMax() - memory.getInitial() + 200000L;
        assertEquals(true, memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        Memory memory = new Memory();
        assertEquals(1.0, memory.toMegaByte(1048576L), 0.01);
    }
}
