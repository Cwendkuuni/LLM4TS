import org.junit.*;
import static org.junit.Assert.*;

public class MemoryTest {

    // Test case for Memory constructor
    @Test
    public void testMemoryConstructor() {
        Memory memory = new Memory();
        assertNotNull(memory);
    }

    // Test case for Memory constructor with useGUI parameter
    @Test
    public void testMemoryConstructorWithUseGUI() {
        Memory memory = new Memory(true);
        assertTrue(memory.getUseGUI());
    }

    // Test case for isEnabled method
    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertTrue(memory.isEnabled());
    }

    // Test case for setEnabled method
    @Test
    public void testSetEnabled() {
        Memory memory = new Memory();
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }

    // Test case for getUseGUI method
    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());
    }

    // Test case for getInitial method
    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        long initialMemory = memory.getInitial();
        assertTrue(initialMemory > 0);
    }

    // Test case for getCurrent method
    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        long currentMemory = memory.getCurrent();
        assertTrue(currentMemory > 0);
    }

    // Test case for getMax method
    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        long maxMemory = memory.getMax();
        assertTrue(maxMemory > 0);
    }

    // Test case for isOutOfMemory method
    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());
    }

    // Test case for toMegaByte method
    @Test
    public void testToMegaByte() {
        long bytes = 1048576;
        double megaBytes = Memory.toMegaByte(bytes);
        assertTrue(megaBytes > 0);
    }

    // Test case for showOutOfMemory method
    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();
        memory.showOutOfMemory();

        // Verify that no exception is thrown
        // and the method does not cause any errors
        assertTrue(true);
    }

    // Test case for stopThreads method
    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();

        // Verify that no exception is thrown
        // and the method does not cause any errors
        assertTrue(true);
    }

}