
package weka.core;

import javax.swing.*;
import org.junit.Test;
import java.awt.*;
import static org.junit.Assert.*;

public class MemoryTest {

    @Test
    public void testConstructor() {
        Memory memory1 = new Memory();
        assertFalse(memory1.getUseGUI());
        
        Memory memory2 = new Memory(true);
        assertTrue(memory2.getUseGUI());
    }

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
        Memory memory1 = new Memory();
        assertFalse(memory1.getUseGUI());
        
        Memory memory2 = new Memory(true);
        assertTrue(memory2.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        long initial = memory.getInitial();
        
        // Assert that initial value is not zero
        assertNotEquals(0, initial);
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        long current = memory.getCurrent();
        
        // Assert that current value is not zero
        assertNotEquals(0, current);
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        long max = memory.getMax();
        
        // Assert that max value is not zero
        assertNotEquals(0, max);
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        long bytes1 = 1048576;
        double result1 = bytes1 / 1048576.0;
        assertEquals(result1, Memory.toMegaByte(bytes1), 0.001);

        long bytes2 = 2097152;
        double result2 = bytes2 / 1048576.0;
        assertEquals(result2, Memory.toMegaByte(bytes2), 0.001);
    }

    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();
        memory.showOutOfMemory();
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertNotNull(memory.getRevision());
    }

    @Test
    public void testMain() {
        Memory memory = new Memory();
        memory.main(new String[]{});
    }
}
