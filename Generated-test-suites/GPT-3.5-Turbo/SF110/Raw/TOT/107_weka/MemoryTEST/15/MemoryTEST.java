import org.junit.Test;
import static org.junit.Assert.*;

public class MemoryTest {

    @Test
    public void testIsEnabled_WhenEnabled_ReturnsTrue() {
        Memory memory = new Memory();
        boolean expected = true;
        boolean actual = memory.isEnabled();
        assertEquals(expected, actual);    
    }

    @Test
    public void testSetEnabled_WhenValueIsTrue_EnablesMemory() {
        Memory memory = new Memory();
        boolean expected = true;
        memory.setEnabled(expected);
        boolean actual = memory.isEnabled();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetEnabled_WhenValueIsFalse_DisablesMemory() {
        Memory memory = new Memory();
        boolean expected = false;
        memory.setEnabled(expected);
        boolean actual = memory.isEnabled();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetUseGUI_WhenUseGUIIsFalse_ReturnsFalse() {
        Memory memory = new Memory();
        boolean expected = false;
        boolean actual = memory.getUseGUI();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetUseGUI_WhenUseGUIIsTrue_ReturnsTrue() {
        Memory memory = new Memory(true);
        boolean expected = true;
        boolean actual = memory.getUseGUI();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetInitial_ReturnsCorrectValue() {
        Memory memory = new Memory();
        long expected = Runtime.getRuntime().totalMemory();
        long actual = memory.getInitial();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCurrent_ReturnsCorrectValue() {
        Memory memory = new Memory();
        long expected = Runtime.getRuntime().totalMemory();
        long actual = memory.getCurrent();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMax_ReturnsCorrectValue() {
        Memory memory = new Memory();
        long expected = Runtime.getRuntime().maxMemory();
        long actual = memory.getMax();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsOutOfMemory_WhenEnabledAndOutOfMemory_ReturnsTrue() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        memory.setMax(1000);
        memory.setCurrent(100);
        memory.setInitial(100);
        boolean expected = true;
        boolean actual = memory.isOutOfMemory();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsOutOfMemory_WhenEnabledAndNotOutOfMemory_ReturnsFalse() {
        Memory memory = new Memory(true);
        memory.setMax(2000);
        memory.setCurrent(100);
        memory.setInitial(100);
        boolean expected = false;
        boolean actual = memory.isOutOfMemory();
        assertEquals(expected, actual);
    }

    @Test
    public void testToMegaByte_ReturnsCorrectValue() {
        long bytes = 1048576;
        double expected = 1.0;
        double actual = Memory.toMegaByte(bytes);
        assertEquals(expected, actual, 0.0001);
    }
    

    @Test
    public void testStopThreads_WhenThreadsExist_ThreadsAreStopped() {
        // Test logic for stopping threads
    }

    @Test
    public void testGetRevision_ReturnsCorrectRevision() {
        Memory memory = new Memory();
        String expected = "$Revision: 8034 $";
        String actual = memory.getRevision();
        assertEquals(expected, actual);
    }

    @Test
    public void testMain_WhenCalled_InitialAndMaxMemoryArePrinted() {
        // Test logic for checking printed memory values
    }
}