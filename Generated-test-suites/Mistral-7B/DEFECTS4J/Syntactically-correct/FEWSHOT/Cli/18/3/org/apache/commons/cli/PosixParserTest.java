package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten_SimpleOptions() {
        Options options = new Options();
        options.addOption("o", "option-o", false, "Option O");
        options.addOption("i", "option-i", true, "Option I with value");
        options.addOption("f", "option-f", false, "Option F");

        String[] arguments = {"-o", "--option-i", "value", "-f"};
        String[] expected = {"option-o", "option-i", "value", "option-F"};

        List<String> result = new PosixParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result.toArray(new String[0]));
    }

    @Test
    public void testFlatten_LongOptions() {
        Options options = new Options();
        options.addOption("long-option-o", "long-option-o", false, "Long Option O");
        options.addOption("long-option-i", "long-option-i", true, "Long Option I with value");

        String[] arguments = {"--long-option-o", "--long-option-i", "value"};
        String[] expected = {"long-option-o", "long-option-i", "value"};

        List<String> result = new PosixParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result.toArray(new String[0]));
    }

    @Test
    public void testFlatten_ShortAndLongOptions() {
        Options options = new Options();
        options.addOption("o", "option-o", false, "Option O");
        options.addOption("i", "option-i", true, "Option I with value");
        options.addOption("l", "long-option-l", false, "Long Option L");

        String[] arguments = {"-o", "--option-i", "value", "--long-option-l"};
        String[] expected = {"option-o", "option-i", "value", "long-option-L"};

        List<String> result = new PosixParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result.toArray(new String[0]));
    }

    @Test
    public void testFlatten_OptionWithArg() {
        Options options = new Options();
        options.addOption("o", "option-o", true, "Option O with value");

        String[] arguments = {"-o", "value"};
        String[] expected = {"option-o", "value"};

        List<String> result = new PosixParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result.toArray(new String[0]));
    }

    @Test
    public void testFlatten_OptionWithMultipleArgs() {
        Options options = new Options();
        options.addOption("o", "option-o", true, "Option O with multiple values");

        String[] arguments = {"-o", "value1", "value2"};
        String[] expected = {"option-o", "value1", "value2"};

        List<String> result = new PosixParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result.toArray(new String[0]));
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        options.addOption("o", "option-o", false, "Option O");
        options.addOption("i", "option-i", true, "Option I with value");
        options.addOption("f", "option-f", false, "Option F");

        String[] arguments = {"-o", "-i", "--non-option", "value", "-f"};
        String[] expected = {"option-o", "-i", "--non-option"};

        List<String> result = new PosixParser().flatten(options, arguments, true);
        assertArrayEquals(expected, result.toArray(new String[0]));
    }

    @Test
    public void testFlatten_EmptyArguments() {
        Options options = new Options();
        String[] arguments = {};

        List<String> result = new PosixParser().flatten(options, arguments, false);
        assertArrayEquals(new String[0], result.toArray(new String[0]));
    }

    @Test
    public void testFlatten_DoubleDash() {
        Options options = new Options();

        String[] arguments = {"--"};

        List<String> result = new PosixParser().flatten(options, arguments, false);
        assertEquals(Arrays.asList(""), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_NullOptions() {
        String[] arguments = {"-o"};
        new PosixParser().flatten(null, arguments, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_NullArguments() {
        Options options = new Options();
        new PosixParser().flatten(options, null, false);
    }
}