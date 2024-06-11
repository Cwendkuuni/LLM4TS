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
    public void testFlatten() {
        Options options = new Options();
        String[] arguments = { "-a", "-b", "value1", "value2", "-c" };
        String[] expected = { "-a", "-b", "value1", "value2", "-c" };
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        String[] arguments = { "-a", "-b", "value1", "value2", "-c" };
        String[] expected = { "-a", "-b", "value1", "value2", "-c" };
        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }

    @Test
    public void testFlattenWithOptionWithArg() {
        Options options = new Options();
        Option option = new Option("a", true, "Option with argument");
        options.addOption(option);
        String[] arguments = { "-a", "value" };
        String[] expected = { "-a", "value" };
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenWithOptionWithoutArg() {
        Options options = new Options();
        Option option = new Option("a", false, "Option without argument");
        options.addOption(option);
        String[] arguments = { "-a" };
        String[] expected = { "-a" };
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenWithOptionWithManyArgs() {
        Options options = new Options();
        Option option = new Option("a", true, "Option with multiple arguments");
        option.setArgs(3);
        options.addOption(option);
        String[] arguments = { "-a", "value1", "value2", "value3" };
        String[] expected = { "-a", "value1", "value2", "value3" };
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenWithStopOptionExists() {
        Options options = new Options();
        Option option = new Option("a", false, "Stop option");
        options.addOption(option);
        String[] arguments = { "-a", "-b", "value" };
        String[] expected = { "-a", "-b", "value" };
        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }

    @Test
    public void testFlattenWithStopOptionNotExists() {
        Options options = new Options();
        Option option = new Option("a", false, "Stop option");
        options.addOption(option);
        String[] arguments = { "-b", "value" };
        String[] expected = { "--", "-b", "value" };
        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }
}