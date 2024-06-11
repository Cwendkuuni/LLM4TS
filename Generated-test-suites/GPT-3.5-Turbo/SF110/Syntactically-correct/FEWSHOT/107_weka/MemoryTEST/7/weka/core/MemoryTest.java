
package weka.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class MemoryTest {

    private Memory memory;

    @Before
    public void setUp() {
        memory = new Memory();
    }

    @Test
    public void isEnabled() {
        assertTrue(memory.isEnabled());
    }

    @Test
    public void setEnabled() {
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }

    @Test
    public void getUseGUI() {
        assertFalse(memory.getUseGUI());
    }

    @Test
    public void getInitial() {
        assertEquals(8034, memory.getInitial());
    }

    @Test
    public void getCurrent() {
        assertEquals(memory.getTotal(), memory.getCurrent());
    }

    @Test
    public void getMax() {
        assertEquals(8034, memory.getMax());
    }

    @Test
    public void isOutOfMemory() {
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void toMegaByte() {
        assertEquals(0.0, Memory.toMegaByte(0), 0.001);
        assertEquals(1.0, Memory.toMegaByte(1048576), 0.001);
    }

    @Test
    public void showOutOfMemory() {
        // Test the output format of showOutOfMemory method indirectly
        assertNull(memory.showOutOfMemory());
    }

    @Test
    public void stopThreads() {
        assertNull(memory.stopThreads());
    }

    @Test
    public void getRevision() {
        assertEquals("$Revision: 8034 $", memory.getRevision());
    }

    @Test
    public void main() {
        assertNull(Memory.main(new String[0]));
    }

}
