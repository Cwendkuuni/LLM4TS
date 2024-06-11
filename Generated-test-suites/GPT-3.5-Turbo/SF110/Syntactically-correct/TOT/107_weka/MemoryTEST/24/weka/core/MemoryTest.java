import org.junit.*;
import static org.junit.Assert.*;

public class MemoryTest {

    // Test case for isEnabled() method
    @Test
    public void testIsEnabled() {
        Memory memory = new Memory(false);
        assertFalse(memory.isEnabled());
        memory.setEnabled(true);
        assertTrue(memory.isEnabled());
    }

    // Test case for setEnabled() method
    @Test
    public void testSetEnabled() {
        Memory memory = new Memory();
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
        memory.setEnabled(true);
        assertTrue(memory.isEnabled());
    }

    // Test case for getUseGUI() method
    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory(true);
        assertTrue(memory.getUseGUI());
        memory = new Memory(false);
        assertFalse(memory.getUseGUI());
    }

    // Test case for getInitial() method
    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        assertEquals(memory.getInitial(), Runtime.getRuntime().totalMemory());
    }

    // Test case for getCurrent() method
    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        assertEquals(memory.getCurrent(), Runtime.getRuntime().totalMemory());
    }

    // Test case for getMax() method
    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        assertTrue(memory.getMax() > 0);
    }

    // Test case for isOutOfMemory() method
    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory(false);
        assertFalse(memory.isOutOfMemory());

        memory.setEnabled(true);
        assertFalse(memory.isOutOfMemory());

        memory.setEnabled(true);
        long initial = memory.getInitial();
        long current = memory.getCurrent();
        long max = memory.getMax();
        assertTrue(memory.isOutOfMemory());

        memory.setEnabled(false);
        assertFalse(memory.isOutOfMemory());

        memory.setEnabled(true);
        assertFalse(memory.isOutOfMemory());
    }

    // Test case for toMegaByte() method
    @Test
    public void testToMegaByte() {
        Memory memory = new Memory();
        assertEquals(memory.toMegaByte(1048576), 1.0, 0.01);
        assertEquals(memory.toMegaByte(2097152), 2.0, 0.01);
        assertEquals(memory.toMegaByte(1000000), 0.953674, 0.01);
        assertEquals(memory.toMegaByte(0), 0.0, 0.01);
    }

    // Test case for showOutOfMemory() method
    @Test
    public void testShowOutOfMemory() {
        // As this method prints output to System.err and uses JOptionPane (GUI),
        // it's difficult to write a reliable JUnit test case for this method.
        // Manual testing is recommended to verify the functionality.

        // However, we can test if the method is not throwing any exception
        // when called.
        Memory memory = new Memory();
        memory.setEnabled(true);
        memory.showOutOfMemory();
    }

    // Test case for stopThreads() method
    @Test
    public void testStopThreads() {
        // As this method performs operations like calling the stop() method on threads,
        // it's difficult to write a reliable JUnit test case for this method.
        // Manual testing is recommended to verify the functionality.

        // However, we can test if the method is not throwing any exception
        // when called.
        Memory memory = new Memory();
        memory.stopThreads();
    }

    // Test case for getRevision() method
    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertNotNull(memory.getRevision());
    }

    // Additional test case for main() method
    @Test
    public void testMain() {
        // As this method mainly prints output to System.out,
        // it's difficult to write a reliable JUnit test case for this method.
        // Manual testing is recommended to verify the functionality.

        // However, we can test if the method is not throwing any exception
        // when called.
        Memory.main(new String[]{});
    }

}