import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MemoryTest {

    private Memory memory;

    @Before
    public void setUp() {
        memory = new Memory();
    }

    // Test case for isEnabled() method
    @Test
    public void testIsEnabled() {
        assertTrue(memory.isEnabled());
    }

    // Test case for setEnabled() method
    @Test
    public void testSetEnabled() {
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }

    // Test case for getUseGUI() method
    @Test
    public void testGetUseGUI() {
        assertFalse(memory.getUseGUI());
    }

    // Test case for getInitial() method
    @Test
    public void testGetInitial() {
        assertEquals(62914560, memory.getInitial());
    }

    // Test case for getCurrent() method
    @Test
    public void testGetCurrent() {
        assertEquals(62914560, memory.getCurrent());
    }

    // Test case for getMax() method
    @Test
    public void testGetMax() {
        assertTrue(memory.getMax() > 0);
    }

    // Test case for isOutOfMemory() method
    @Test
    public void testIsOutOfMemory() {
        assertFalse(memory.isOutOfMemory());
    }

    // Test case for toMegaByte() method
    @Test
    public void testToMegaByte() {
        assertEquals(0.0, memory.toMegaByte(0), 0.001);
        assertEquals(1.0, memory.toMegaByte(1048576), 0.001);
        assertEquals(2.0, memory.toMegaByte(2097152), 0.001);
    }

    // Test case for showOutOfMemory() method
    @Test
    public void testShowOutOfMemory() {
        // No assertion possible as the method only performs system print and dialog display
        memory.showOutOfMemory();
    }

    // Test case for stopThreads() method
    @Test
    public void testStopThreads() {
        // No assertion possible as the method only stops threads and performs system actions
        memory.stopThreads();
    }

    // Test case for getRevision() method
    @Test
    public void testGetRevision() {
        assertNotNull(memory.getRevision());
    }

    // Test case for main() method
    @Test
    public void testMain() {
        // No assertion possible as main method only performs system print
        Memory.main(new String[]{});
    }
}