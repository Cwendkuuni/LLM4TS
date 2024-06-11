package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten_SimpleOption() {
        Options options = new Options();
        options.addOption("a", true, "Description for option a");

        String[] arguments = {"-a"};
        String[] expected = {"-a"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionWithValue() {
        Options options = new Options();
        options.addOption("a", true, "Description for option a");
        options.addOption("b", 1, "Description for option b");

        String[] arguments = {"-a", "valueA", "-b", "valueB"};
        String[] expected = {"-a", "valueA", "-b", "valueB"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionWithMultipleValues() {
        Options options = new Options();
        options.addOption("a", true, "Description for option a");
        options.addOption("b", 1, "Description for option b");

        String[] arguments = {"-a", "--b", "valueB1", "--b", "valueB2"};
        String[] expected = {"-a", "--b", "valueB1", "--b", "valueB2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        Options options = new Options();
        options.addOption("a", true, "Description for option a");
        options.addOption("b", true, "Description for option b");

        String[] arguments = {"-a", "-b"};
        String[] expected = {"-a", "-b"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOption() {
        Options options = new Options();
        options.addOption("longOption", true, "Description for long option");

        String[] arguments = {"--longOption"};
        String[] expected = {"--longOption"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        Options options = new Options();
        options.addOption("longOption", true, "Description for long option");
        options.addOption("longOptionValue", 1, "Description for long option value");

        String[] arguments = {"--longOption", "value"};
        String[] expected = {"--longOption", "value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithMultipleValues() {
        Options options = new Options();
        options.addOption("longOption", true, "Description for long option");

        String[] arguments = {"--longOption", "--longOption", "value1", "--longOption", "value2"};
        String[] expected = {"--longOption", "--longOption", "value1", "--longOption", "value2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_UnrecognizedOption() {
        Options options = new Options();
        options.addOption("a", true, "Description for option a");

        String[] arguments = {"--unknownOption"};
        String[] expected = { "--unknownOption" };

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Description for option a");

        String[] arguments = {"--unknownOption", "-a"};
        String[] expected = {"--unknownOption"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOptionWithValues() {
        Options options = new Options();
        options.addOption("a", true, "Description for option a");
        options.addOption("b", 1, "Description for option b");

        String[] arguments = {"--unknownOption", "-a", "--optionB", "value"};
        String[] expected = {"--unknownOption", "-a", "--optionB", "value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionWithArg() {
        Options options = new Options();
        options.addOption("a", true, "Description for option a");
        options.addOption("b", 1, "Description for option b");

        String[] arguments = {"-a", "valueA", "-b", "valueB"};
        String[] expected = {"-a", "valueA", "-b", "valueB"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);

        assertEquals(null, parser.currentOption);
        assertEquals(0, parser.tokens.size());
    }

    @Test
    public void testFlatten_OptionWithArgs() {
        Options options = new Options();
        options.addOption("a", true, "Description for option a");
        options.addOption("b", 1, "Description for option b");

        String[] arguments = {"-a", "--b", "valueB1", "-b", "valueB2"};
        String[] expected = {"-a", "--b", "valueB1", "-b", "valueB2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);

        assertSame(options.findOption("b"), parser.currentOption);
        assertEquals(2, parser.tokens.size());
    }

    @Test
    public void testFlatten_ComplexArguments() {
        Options options = new Options();
        options.addOption("a", true, "Description for option a");
        options.addOption("b", 1, "Description for option b");

        String[] arguments = {"--optionB", "valueB1", "--optionB", "valueB2", "-a", "valueA"};
        String[] expected = {"--optionB", "valueB1", "--optionB", "valueB2", "-a", "valueA"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);

        assertSame(options.findOption("b"), parser.currentOption);
        assertEquals(2, parser.tokens.size());
    }

    @Test
    public void testFlatten_EmptyArguments() {
        Options options = new Options();

        String[] arguments = {};
        String[] expected = {};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);

        assertEquals(null, parser.currentOption);
        assertEquals(0, parser.tokens.size());
    }
}