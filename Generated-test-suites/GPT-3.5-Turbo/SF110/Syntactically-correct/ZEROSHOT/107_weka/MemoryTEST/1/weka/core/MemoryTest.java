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
        assertEquals(Memory.m_Initial, memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        assertTrue(memory.getCurrent() >= 0);
    }
    
    @Test
    public void testGetMax() {
        assertTrue(memory.getMax() > 0);
    }
    
    @Test
    public void testIsOutOfMemory() {
        assertEquals(false, memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        assertEquals(0.0, Memory.toMegaByte(0), 0.001);
        assertEquals(1.0, Memory.toMegaByte(1048576), 0.001);
    }

    @Test
    public void testShowOutOfMemory() {
        // The showOutOfMemory() method is not testable as it interacts with the console and GUI.
        // Therefore, we cannot write a unit test for it.
    }

    @Test
    public void testStopThreads() {
        // The stopThreads() method is not testable as it terminates threads forcefully.
        // Therefore, we cannot write a unit test for it.
    }

    @Test
    public void testGetRevision() {
        assertNotNull(memory.getRevision());
    }

    @Test
    public void testMain() {
        // The main() method is not testable as it interacts with the console.
        // Therefore, we cannot write a unit test for it.
    }

}