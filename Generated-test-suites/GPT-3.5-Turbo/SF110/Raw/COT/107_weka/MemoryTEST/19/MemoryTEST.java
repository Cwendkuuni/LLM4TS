
import org.junit.Test;

import static org.junit.Assert.*;

public class MemoryTest {
    /**
     * Test the isEnabled() method
     */
    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertTrue(memory.isEnabled());

        memory.setEnabled(false);
        assertFalse(memory.isEnabled());

        memory.setEnabled(true);
        assertTrue(memory.isEnabled());
    }

    /**
     * Test the setEnabled() method
     */
    @Test
    public void testSetEnabled() {
        Memory memory = new Memory();
        assertTrue(memory.isEnabled());

        memory.setEnabled(false);
        assertFalse(memory.isEnabled());

        memory.setEnabled(true);
        assertTrue(memory.isEnabled());
    }

    /**
     * Test the getUseGUI() method
     */
    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());
    }

    /**
     * Test the getInitial() method
     */
    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        assertEquals(0, memory.getInitial());
    }

    /**
     * Test the getCurrent() method
     */
    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        long current = memory.getCurrent();
        assertTrue(current >= 0);
    }

    /**
     * Test the getMax() method
     */
    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        long max = memory.getMax();
        assertTrue(max >= 0);
    }

    /**
     * Test the isOutOfMemory() method
     */
    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        long initial = memory.getInitial();
        long current = memory.getCurrent();
        long max = memory.getMax();
        assertFalse(memory.isOutOfMemory());

        memory.setEnabled(true);
        memory.setInitial(100000);
        current += 200000;
        assertTrue(memory.isOutOfMemory());

        current -= 100000;
        assertFalse(memory.isOutOfMemory());

        current += 100000;
        assertTrue(memory.isOutOfMemory());
    }

    /**
     * Test the toMegaByte() method
     */
    @Test
    public void testToMegaByte() {
        Memory memory = new Memory();
        long bytes = 1048576;

        assertEquals(1.0, memory.toMegaByte(bytes), 0.001);
    }

    /**
     * Test the showOutOfMemory() method
     * Note: Manual testing required
     */

    /**
     * Test the stopThreads() method
     * Note: Manual testing required
     */
    
    /**
     * Test the getRevision() method
     */
    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertNotNull(memory.getRevision());
    }
}
