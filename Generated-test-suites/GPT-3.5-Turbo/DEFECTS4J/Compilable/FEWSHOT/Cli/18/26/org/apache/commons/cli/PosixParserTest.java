package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten_StopAtNonOptionTrue() {
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");

        String[] arguments = {"-a", "valueA", "-b", "valueB", "nonOption"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("valueA", result[1]);
        assertEquals("nonOption", result[2]);
    }

    @Test
    public void testFlatten_StopAtNonOptionFalse() {
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");

        String[] arguments = {"-a", "valueA", "-b", "valueB", "nonOption"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(6, result.length);
        assertEquals("-a", result[0]);
        assertEquals("valueA", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("valueB", result[3]);
        assertEquals("nonOption", result[4]);
        assertEquals("", result[5]);
    }

    @Test
    public void testFlatten_NoOptions() {
        String[] arguments = {"nonOption1", "nonOption2"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("nonOption1", result[0]);
        assertEquals("nonOption2", result[1]);
    }
}