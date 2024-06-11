package weka.core;

import org.junit.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.Assert.*;

public class MemoryTest {

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
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        assertEquals(202482944, memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        assertEquals(16777216, memory.getCurrent());
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        assertEquals(268435456, memory.getMax());
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        assertEquals(0.5, Memory.toMegaByte(524288), 0.001);
    }

    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        memory.showOutOfMemory();
        String expectedOutput = "Not enough memory. Please load a smaller dataset or use larger heap size.\n" +
                "- initial JVM size:   192.0MB\n" +
                "- total memory used:  16.0MB\n" +
                "- max. memory avail.: 256.0MB\n" +
                "\n" +
                "Note:\n" +
                "The Java heap size can be specified with the -Xmx option.\n" +
                "E.g., to use 128MB as heap size, the command line looks like this:\n" +
                "   java -Xmx128m -classpath ...\n" +
                "This does NOT work in the SimpleCLI, the java command refers\n" +
                "to the one with which Weka is started.\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();
        // No assertion to be made, as this method is meant to stop threads
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertEquals("$Revision: 8034 $", memory.getRevision());
    }

    @Test
    public void testMain() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Memory.main(new String[]{});
        String expectedOutput = "Initial memory: 13.6MB (14221312)\n" +
                "Max memory: 256.0MB (268435456)\n";
        assertEquals(expectedOutput, outContent.toString());
    }
}