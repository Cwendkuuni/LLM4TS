package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten_StopAtNonOption_True() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] arguments = { "-a", "value1", "--", "-b", "value2" };

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("--", result[2]);
    }

    @Test
    public void testFlatten_StopAtNonOption_False() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] arguments = { "-a", "value1", "--", "-b", "value2" };

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("--", result[2]);
        assertEquals("-b", result[3]);
        assertEquals("value2", result[4]);
    }

    @Test
    public void testFlatten_HyphenArgument() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String[] arguments = { "-", "--", "-a" };

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-", result[0]);
        assertEquals("--", result[1]);
        assertEquals("-a", result[2]);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String[] arguments = { "-a", "-", "--", "-a" };

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-", result[1]);
        assertEquals("--", result[2]);
        assertEquals("-a", result[3]);
        assertEquals("-", result[4]);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String[] arguments = { "--option=value", "-a" };

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("--option", result[0]);
        assertEquals("value", result[1]);
        assertEquals("-a", result[2]);
    }

    @Test
    public void testFlatten_LongOptionWithoutValue() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String[] arguments = { "--option", "-a" };

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("--option", result[0]);
        assertEquals("-a", result[1]);
    }

    @Test
    public void testFlatten_UnrecognizedOption_StopAtNonOption_True() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String[] arguments = { "-a", "-b", "value" };

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_UnrecognizedOption_StopAtNonOption_False() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String[] arguments = { "-a", "-b", "value" };

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
    }

}