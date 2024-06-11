package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B with argument");
        options.addOption("c", true, "Option C with multiple arguments");
        parser = new PosixParser();
    }

    @Test
    public void testFlattenEmpty() {
        String[] arguments = {};
        String[] expected = {};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenSimple() {
        String[] arguments = {"arg1", "arg2"};
        String[] expected = {"arg1", "arg2"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenSpecial() {
        String[] arguments = {"--foo", "arg1", "--bar", "arg2"};
        String[] expected = {"--foo", "arg1", "--bar", "arg2"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenSingleHyphen() {
        String[] arguments = {"-f"};
        String[] expected = {"-f"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenTwoCharacterOption() {
        String[] arguments = {"-ab"};
        String[] expected = {"-a", "-b"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenTwoCharacterOptionWithArgument() {
        String[] arguments = {"-ab=value"};
        String[] expected = {"-a", "-b", "value"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenOptionWithArgument() {
        String[] arguments = {"--option-with-argument=value"};
        String[] expected = {"--option-with-argument", "value"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenLongOption() {
        String[] arguments = {"--long-option-with-multiple-arguments=value1 value2"};
        String[] expected = {"--long-option-with-multiple-arguments", "value1", "value2"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenOptionWithMultipleArguments() {
        String[] arguments = {"-c", "arg1", "arg2", "arg3"};
        String[] expected = {"-c", "arg1", "arg2", "arg3"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] arguments = {"arg1", "--non-option", "arg2"};
        String[] expected = {"arg1", "--non-option", "arg2"};
        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }

    @Test
    public void testFlattenStopAtNonOptionWithBurstToken() {
        String[] arguments = {"--no-option-a", "-c", "arg1", "arg2"};
        String[] expected = {"--no-option-a", "-c", "arg1", "arg2"};
        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }

    @Test
    public void testFlattenBurstToken() {
        String[] arguments = {"--no-option-ac"};
        String[] expected = {"-a", "-c"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenBurstTokenWithStopAtNonOption() {
        String[] arguments = {"--no-option-ac"};
        String[] expected = {"--no-option-a", null};
        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }

    @Test
    public void testFlattenBurstTokenWithArgument() {
        String[] arguments = {"--no-option-ac=value"};
        String[] expected = {"--no-option-a", "value"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenBurstTokenWithArgumentAndStopAtNonOption() {
        String[] arguments = {"--no-option-ac=value"};
        String[] expected = {"--no-option-a", null};
        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }
}