
package weka.core;

import static org.junit.Assert.*;

import javax.swing.JOptionPane;

import org.junit.Test;

public class MemoryTest {

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertTrue(memory.isEnabled());
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());
        memory = new Memory(true);
        assertTrue(memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        assertNotNull(memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        assertNotNull(memory.getCurrent());
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        assertNotNull(memory.getMax());
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        long bytes = 1048576;
        double expected = 1.0;
        assertEquals(expected, Memory.toMegaByte(bytes), 0.0);
    }

    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();
        memory.setEnabled(true);
    
        // Not enough memory, alert should be shown
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        memory.showOutOfMemory();
        String expectedOutput = "Not enough memory. Please load a smaller dataset or use larger heap size.\n" +
                "- initial JVM size:   0.0MB\n" +
                "- total memory used:  0.0MB\n" +
                "- max. memory avail.: 0.0MB\n" +
                "\n" +
                "Note:\n" +
                "The Java heap size can be specified with the -Xmx option.\n" +
                "E.g., to use 128MB as heap size, the command line looks like this:\n" +
                "   java -Xmx128m -classpath ...\n" +
                "This does NOT work in the SimpleCLI, the java command refers\n" +
                "to the one with which Weka is started.\n";
        assertEquals(expectedOutput, outContent.toString());
    
        // Enough memory, no alert should be shown
        outContent.reset();
        memory.setEnabled(false);
        memory.showOutOfMemory();
        assertEquals("", outContent.toString());
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();
        // not possible to assert, as it depends on the running threads
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertNotNull(memory.getRevision());
    }

    @Test
    public void testMain() {
        // not possible to assert, as it only prints to stdout
    }
}
