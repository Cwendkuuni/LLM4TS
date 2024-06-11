package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlattenWithEmptyArguments() {
        Options options = new Options();
        String[] arguments = {};
        String[] expected = {};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithSingleHyphenArgument() {
        Options options = new Options();
        String[] arguments = {"-"};
        String[] expected = {"-"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithSingleOption() {
        Options options = new Options();
        Option option = new Option("d", "debug", false, "Prints debug information.");
        options.addOption(option);
        String[] arguments = {"-d"};
        String[] expected = {"-d"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithOptionAndValue() {
        Options options = new Options();
        Option option = new Option("d", "debug", false, "Prints debug information.");
        options.addOption(option);
        String[] arguments = {"-d", "value"};
        String[] expected = {"-d", "value"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithOptionWithLongName() {
        Options options = new Options();
        Option option = new Option("longOptionName", "longOptionDescription", false, "A long option description.");
        options.addOption(option);
        String[] arguments = {"--longOptionName"};
        String[] expected = {"--longOptionName"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithDoubleDashOption() {
        Options options = new Options();
        String[] arguments = {"--"};
        String[] expected = {"--"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithDoubleDashAndFollowingOption() {
        Options options = new Options();
        Option option = new Option("d", "debug", false, "Prints debug information.");
        options.addOption(option);
        String[] arguments = {"--", "-d"};
        String[] expected = {"--", "-d"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithDoubleDashAndFollowingArguments() {
        Options options = new Options();
        String[] arguments = {"--", "arg1", "arg2"};
        String[] expected = {"--", "arg1", "arg2"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithDoubleDashAndFollowingOptionWithValue() {
        Options options = new Options();
        Option option = new Option("d", "debug", false, "Prints debug information.");
        options.addOption(option);
        String[] arguments = {"--", "-d", "value"};
        String[] expected = {"--", "-d", "value"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithDoubleDashAndFollowingArgumentsAndOption() {
        Options options = new Options();
        Option option = new Option("d", "debug", false, "Prints debug information.");
        options.addOption(option);
        String[] arguments = {"--", "-d", "arg1", "arg2"};
        String[] expected = {"--", "-d", "arg1", "arg2"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithDoubleDashStoppingAtNonOption() {
        Options options = new Options();
        Option option = new Option("d", "debug", false, "Prints debug information.");
        options.addOption(option);
        String[] arguments = {"--", "-d", "arg1", "nonOption"};
        String[] expected = {"--", "-d"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptionNameAndValue() {
        Options options = new Options();
        Option option = new Option("longOptionName", "longOptionDescription", false, "A long option description.");
        options.addOption(option);
        String[] arguments = {"--longOptionName=value"};
        String[] expected = {"--longOptionName", "value"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptionNameAndValueAndArguments() {
        Options options = new Options();
        Option option = new Option("longOptionName", "longOptionDescription", false, "A long option description.");
        options.addOption(option);
        String[] arguments = {"--longOptionName=value", "arg1", "arg2"};
        String[] expected = {"--longOptionName", "value", "arg1", "arg2"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptionNameAndValueAndArgumentsStoppingAtNonOption() {
        Options options = new Options();
        Option option = new Option("longOptionName", "longOptionDescription", false, "A long option description.");
        options.addOption(option);
        String[] arguments = {"--longOptionName=value", "arg1", "nonOption"};
        String[] expected = {"--longOptionName", "value"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithMultipleOptionsAndArguments() {
        Options options = new Options();
        Option option1 = new Option("d", "debug", false, "Prints debug information.");
        Option option2 = new Option("l", "list", false, "Prints a list of available options.");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {"-d", "-l", "arg1", "arg2"};
        String[] expected = {"-d", "-l", "arg1", "arg2"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithSingleOptionWithValueAndArguments() {
        Options options = new Options();
        Option option = new Option("d", "debug", false, "Prints debug information.");
        Option optionWithValue = new Option("o", "output", true, "Specifies the output file.");
        options.addOption(option);
        options.addOption(optionWithValue);
        String[] arguments = {"-d", "value", "arg1", "arg2"};
        String[] expected = {"-d", "value", "arg1", "arg2"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithSingleOptionWithValueAndArgumentsStoppingAtNonOption() {
        Options options = new Options();
        Option option = new Option("d", "debug", false, "Prints debug information.");
        Option optionWithValue = new Option("o", "output", true, "Specifies the output file.");
        options.addOption(option);
        options.addOption(optionWithValue);
        String[] arguments = {"-d", "value", "arg1", "nonOption"};
        String[] expected = {"-d", "value"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }
}