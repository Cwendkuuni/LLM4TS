package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    private Options options;
    private PosixParser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new PosixParser();
    }

    @Test
    public void testFlatten() {
        String[] args = { "-a", "value", "-b", "-c", "--option", "argument", "arg1", "arg2" };

        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");
        options.addOption("c", false, "option c");
        options.addOption("option", true, "option with argument");

        String[] result = parser.flatten(options, args, false);

        assertEquals(8, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("-c", result[3]);
        assertEquals("--option", result[4]);
        assertEquals("argument", result[5]);
        assertEquals("arg1", result[6]);
        assertEquals("arg2", result[7]);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] args = { "-a", "value", "-b", "-c", "--option", "argument", "arg1", "arg2" };

        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");
        options.addOption("c", false, "option c");
        options.addOption("option", true, "option with argument");

        String[] result = parser.flatten(options, args, true);

        assertEquals(7, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("-c", result[3]);
        assertEquals("--option", result[4]);
        assertEquals("argument", result[5]);
        assertEquals("arg1", result[6]);
    }

    @Test
    public void testFlattenWithNoArguments() {
        String[] args = {};

        String[] result = parser.flatten(options, args, false);

        assertEquals(0, result.length);
    }

    @Test
    public void testFlattenWithNullArguments() {
        String[] args = null;

        String[] result = parser.flatten(options, args, false);

        assertNull(result);
    }
}