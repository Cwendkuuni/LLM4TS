
import org.junit.Test;

import static org.junit.Assert.*;

public class MemoryTest {

    @Test
    public void testMemoryConstructor1() {
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());
        assertEquals(0, memory.getCurrent());
        assertTrue(memory.getMax() > 0);
    }

    @Test
    public void testMemoryConstructor2() {
        Memory memory = new Memory(true);
        assertTrue(memory.getUseGUI());
        assertEquals(0, memory.getCurrent());
        assertTrue(memory.getMax() > 0);
    }

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertTrue(memory.isEnabled());

        memory.setEnabled(false);
        assertFalse(memory.isEnabled());

        memory.setEnabled(true);
        assertTrue(memory.isEnabled());
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
        assertTrue(memory.getInitial() > 0);
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        assertEquals(memory.getTotal(), memory.getCurrent());
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        assertTrue(memory.getMax() > 0);
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());

        memory.setEnabled(true);
        assertFalse(memory.isOutOfMemory());

        memory.setInitial(200_000);
        assertFalse(memory.isOutOfMemory());

        memory.setMax(1_000_000);
        assertTrue(memory.isOutOfMemory());

        memory.setCurrent(900_000);
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        assertEquals(0, Memory.toMegaByte(0), 0);
        assertEquals(1, Memory.toMegaByte(1_048_576), 0);
        assertEquals(2, Memory.toMegaByte(2_097_152), 0);
    }
    
    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();
        
        memory.setEnabled(true);
        memory.setInitial(100_000);
        memory.setMax(1_000_000);
        memory.setCurrent(900_000);
        
        // Not able to test the output of System.err.println() and JOptionPane.showMessageDialog()
        memory.showOutOfMemory();
    }
    
    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        
        memory.stopThreads();
        
        // Not able to test the behavior of stopping threads
    }
    
    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertNotNull(memory.getRevision());
    }

    @Test
    public void testMain() {
        // Not able to test the output of System.out.println()
        Memory.main(new String[]{});
    }
    
}
