package weka.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.Assert.*;

public class MemoryTest {

    private Memory memory;

    @Before
    public void setUp() {
        memory = new Memory();
    }

    @After
    public void tearDown() {
        memory = null;
    }

    @Test
    public void testIsEnabled() {
        assertFalse(memory.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        memory.setEnabled(true);
        assertTrue(memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        assertFalse(memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        assertEquals(Memory.m_Initial, memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        long currentMemory = memory.getCurrent();

        assertEquals(Runtime.getRuntime().totalMemory(), currentMemory);
    }

    @Test
    public void testGetMax() {
        assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
    }

    @Test
    public void testIsOutOfMemory() {
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        long bytes = 1048576;
        double expected = 1.0;

        assertEquals(expected, Memory.toMegaByte(bytes), 0.001);
    }

    @Test
    public void testShowOutOfMemory() {
        // Set up output stream capturing
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outputStream));

        // Invoke the method
        memory.showOutOfMemory();

        // Check the output
        String expected = "Not enough memory. Please load a smaller dataset or use larger heap size.\n" +
                "- initial JVM size:   " + Utils.doubleToString(Memory.toMegaByte(Memory.m_Initial), 1) + "MB\n" +
                "- total memory used:  " + Utils.doubleToString(Memory.toMegaByte(memory.m_Total), 1) + "MB\n" +
                "- max. memory avail.: " + Utils.doubleToString(Memory.toMegaByte(memory.m_Max), 1) + "MB\n" +
                "\n" +
                "Note:\n" +
                "The Java heap size can be specified with the -Xmx option.\n" +
                "E.g., to use 128MB as heap size, the command line looks like this:\n" +
                "   java -Xmx128m -classpath ...\n" +
                "This does NOT work in the SimpleCLI, the java command refers\n" +
                "to the one with which Weka is started.\n";

        assertEquals(expected, outputStream.toString());

        // Reset output stream
        System.setErr(System.err);
    }

    @Test
    public void testStopThreads() {
        memory.stopThreads();

        // Verify that all relevant threads are stopped
        Thread[] threads = new Thread[Thread.activeCount()];
        Thread.enumerate(threads);

        for (Thread thread : threads) {
            if (thread != null) {
                assertTrue(thread.isInterrupted());
            }
        }
    }

    @Test
    public void testGetRevision() {
        String revision = RevisionUtils.extract("$Revision: 8034 $");
        assertEquals(revision, memory.getRevision());
    }

    @Test
    public void testMain() {
        // Set up output stream capturing
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Invoke the method
        Memory.main(new String[0]);

        // Check the output
        String initialMemory = Utils.doubleToString(Memory.toMegaByte(memory.getInitial()), 1) + "MB" +
                " (" + memory.getInitial() + ")";
        String maxMemory = Utils.doubleToString(Memory.toMegaByte(memory.getMax()), 1) + "MB" +
                " (" + memory.getMax() + ")";

        assertEquals("Initial memory: " + initialMemory + "\n" +
                "Max memory: " + maxMemory + "\n", outputStream.toString());

        // Reset output stream
        System.setOut(System.out);
    }
}