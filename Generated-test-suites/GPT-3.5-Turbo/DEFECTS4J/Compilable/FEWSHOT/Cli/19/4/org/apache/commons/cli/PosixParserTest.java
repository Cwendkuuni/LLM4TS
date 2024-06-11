package org.apache.commons.cli;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("f", false, "Option f");
        options.addOption("o", true, "Option o");
        options.addOption("p", false, "Option p");

        String[] arguments = {"-f", "-o", "value", "arg1", "-p"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(7, result.length);
        assertEquals("-f", result[0]);
        assertEquals("-o", result[1]);
        assertEquals("value", result[2]);
        assertEquals("arg1", result[3]);
        assertEquals("-p", result[4]);

    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("f", false, "Option f");
        options.addOption("o", true, "Option o");
        options.addOption("p", false, "Option p");

        String[] arguments = {"-f", "-o", "value", "arg1", "-p"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(5, result.length);
        assertEquals("-f", result[0]);
        assertEquals("-o", result[1]);
        assertEquals("value", result[2]);
        assertEquals("arg1", result[3]);
        assertEquals("-p", result[4]);
    }
}