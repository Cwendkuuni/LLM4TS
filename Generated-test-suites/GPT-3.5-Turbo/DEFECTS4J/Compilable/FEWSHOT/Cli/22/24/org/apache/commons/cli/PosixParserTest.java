package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
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
    public void testFlattenNoOptions() {
        String[] args = {"arg1", "arg2"};
        String[] result = parser.flatten(options, args, true);

        assertEquals(2, result.length);
        assertEquals("arg1", result[0]);
        assertEquals("arg2", result[1]);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        options.addOption(new Option("f", false, "Test option"));
        options.addOption(new Option("o", true, "Test option"));

        String[] args = {"-f", "arg1", "-o", "value"};
        String[] result = parser.flatten(options, args, true);

        assertEquals(2, result.length);
        assertEquals("arg1", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlattenWithEmptyOption() {
        options.addOption(new Option("f", false, "Test option"));
        options.addOption(new Option("o", true, "Test option"));

        String[] args = {"-f", "-o", "", "arg1"};
        String[] result = parser.flatten(options, args, false);

        assertEquals(3, result.length);
        assertEquals("-f", result[0]);
        assertEquals("-o", result[1]);
        assertEquals("arg1", result[2]);
    }

    @Test
    public void testFlattenLongOption() {
        options.addOption(new Option("f", false, "Test option"));
        options.addOption(new Option("o", true, "Test option"));

        String[] args = {"--f", "--o=value"};
        String[] result = parser.flatten(options, args, false);

        assertEquals(2, result.length);
        assertEquals("--f", result[0]);
        assertEquals("--o=value", result[1]);
    }

    @Test
    public void testFlattenSingleHyphen() {
        options.addOption(new Option("f", false, "Test option"));
        options.addOption(new Option("o", true, "Test option"));

        String[] args = {"-"};
        String[] result = parser.flatten(options, args, false);

        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlattenOptionWithoutArgument() {
        options.addOption(new Option("f", false, "Test option"));
        options.addOption(new Option("o", true, "Test option"));

        String[] args = {"-f", "-o"};
        String[] result = parser.flatten(options, args, false);

        assertEquals(2, result.length);
        assertEquals("-f", result[0]);
        assertEquals("-o", result[1]);
    }

    @Test
    public void testFlattenOptionWithArgument() {
        options.addOption(new Option("f", false, "Test option"));
        options.addOption(new Option("o", true, "Test option"));

        String[] args = {"-o=value"};
        String[] result = parser.flatten(options, args, false);

        assertEquals(1, result.length);
        assertEquals("-o=value", result[0]);
    }

    @Test
    public void testFlattenOptionWithArgumentAndNonOptionStop() {
        options.addOption(new Option("f", false, "Test option"));
        options.addOption(new Option("o", true, "Test option"));

        String[] args = {"-o=value", "arg1"};
        String[] result = parser.flatten(options, args, true);

        assertEquals(2, result.length);
        assertEquals("arg1", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlattenInvalidOptionAndNonOptionStop() {
        options.addOption(new Option("f", false, "Test option"));
        options.addOption(new Option("o", true, "Test option"));

        String[] args = {"-xvalue", "arg1"};
        String[] result = parser.flatten(options, args, true);

        assertEquals(2, result.length);
        assertEquals("arg1", result[0]);
        assertEquals("-xvalue", result[1]);
    }

    @Test
    public void testFlattenInvalidOptionNoStop() {
        options.addOption(new Option("f", false, "Test option"));
        options.addOption(new Option("o", true, "Test option"));

        String[] args = {"-xvalue"};
        String[] result = parser.flatten(options, args, false);

        assertEquals(1, result.length);
        assertEquals("-xvalue", result[0]);
    }
}