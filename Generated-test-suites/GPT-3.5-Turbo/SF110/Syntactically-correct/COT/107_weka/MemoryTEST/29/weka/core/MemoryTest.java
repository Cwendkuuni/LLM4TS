
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import weka.core.Memory;

public class MemoryTest {
    Memory memory;

    @Before
    public void setUp() {
        memory = new Memory();
    }

    @Test
    public void testIsEnabled() {
        // Test initial state
        assertTrue(memory.isEnabled());

        // Test setting enabled to false
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());

        // Test setting enabled back to true
        memory.setEnabled(true);
        assertTrue(memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        // Test initial state
        assertFalse(memory.getUseGUI());

        // Test setting useGUI to true
        memory = new Memory(true);
        assertTrue(memory.getUseGUI());

        // Test setting useGUI back to false
        memory = new Memory(false);
        assertFalse(memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        assertEquals(8034, memory.getInitial()); // Default value, update if necessary
    }

    @Test
    public void testGetCurrent() {
        assertEquals(memory.getInitial(), memory.getCurrent());
        
        // Manually increase memory, then check if getCurrent() reflects the change
        long increasedMemory = memory.getCurrent() + 500000L;
        // Simulating increase in memory usage
        memory.m_Total = increasedMemory;
        assertEquals(increasedMemory, memory.getCurrent());
    }

    @Test
    public void testGetMax() {
        // Test initial state
        assertTrue(memory.getMax() >= memory.getInitial()); // Max should always be greater or equal to Initial value
    }

    @Test
    public void testIsOutOfMemory() {
        // Test initial state
        assertFalse(memory.isOutOfMemory());

        // Test isOutOfMemory when enabled is true and enough memory is not available
        memory.setEnabled(true);
        memory.m_Max = memory.m_Total + memory.getInitial() + 200000L - 1;
        assertTrue(memory.isOutOfMemory());

        // Test isOutOfMemory when enabled is true but enough memory is available
        memory.m_Max = memory.m_Total + memory.getInitial() + 200000L + 1;
        assertFalse(memory.isOutOfMemory());

        // Test isOutOfMemory when enabled is false and enough memory is not available
        memory.setEnabled(false);
        memory.m_Max = memory.m_Total + memory.getInitial() + 200000L - 1;
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        // Test conversion from bytes to megabytes
        assertEquals(2.0, Memory.toMegaByte(2097152L), 0.01); // 2MB
        assertEquals(1.5, Memory.toMegaByte(1572864L), 0.01); // 1.5MB
        assertEquals(0.0, Memory.toMegaByte(0L), 0.01); // 0MB
    }
    
    // Additional test cases for other methods can be added here

}

