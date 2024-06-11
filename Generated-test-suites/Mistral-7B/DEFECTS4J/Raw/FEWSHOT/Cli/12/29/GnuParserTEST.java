package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlatten_EmptyArguments() {
        Options options = new Options();
        String[] arguments = {};
        String[] expected = {};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleDash() {
        Options options = new Options();
        String[] arguments = {"-"};
        String[] expected = {"-"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleOption() {
        Options options = new Options();
        Option option = new Option("D", "debug", false, "Print debug information.");
        options.addOption(option);
        String[] arguments = {"-D"};
        String[] expected = {"-D"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionWithValue() {
        Options options = new Options();
        Option option = new Option("D", "debug", false, "Print debug information.");
        Option valueOption = new Option("value", "v", true, "Set the debug value.");
        options.addOption(option);
        options.addOption(valueOption);
        String[] arguments = {"-D", "--value", "INFO"};
        String[] expected = {"-D", "--value", "INFO"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOption() {
        Options options = new Options();
        Option option = new Option("longOption", "long", false, "Print long information.");
        options.addOption(option);
        String[] arguments = {"--longOption"};
        String[] expected = {"--longOption"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleShortOptions() {
        Options options = new Options();
        Option option1 = new Option("o", "option1", false, "Print option1 information.");
        Option option2 = new Option("o", "option2", false, "Print option2 information.");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {"-o", "-o"};
        String[] expected = {"-o", "-o"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ShortOptionWithValue() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Print option information.");
        Option valueOption = new Option("value", "v", true, "Set the option value.");
        options.addOption(option);
        options.addOption(valueOption);
        String[] arguments = {"-o", "--value", "INFO"};
        String[] expected = {"-o", "--value", "INFO"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionWithValueAndLongOption() {
        Options options = new Options();
        Option option1 = new Option("o", "option1", false, "Print option1 information.");
        Option option2 = new Option("l", "longOption", false, "Print long option information.");
        Option valueOption = new Option("value", "v", true, "Set the option value.");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(valueOption);
        String[] arguments = {"-o", "--longOption", "--value", "INFO"};
        String[] expected = {"-o", "--longOption", "--value", "INFO"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", false, "Print option1 information.");
        Option option2 = new Option("o2", "option2", false, "Print option2 information.");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {"-o1", "-o2"};
        String[] expected = {"-o1", "-o2"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionWithLongOption() {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", false, "Print option1 information.");
        Option option2 = new Option("o", "option", false, "Print option information.");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {"--option1", "-o"};
        String[] expected = {"--option1", "-o"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        Options options = new Options();
        Option option = new Option("l", "longOption", false, "Print long option information.");
        Option valueOption = new Option("value", "v", true, "Set the option value.");
        options.addOption(option);
        options.addOption(valueOption);
        String[] arguments = {"--longOption", "--value", "INFO"};
        String[] expected = {"--longOption", "--value", "INFO"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleLongOptions() {
        Options options = new Options();
        Option option1 = new Option("l1", "longOption1", false, "Print long option1 information.");
        Option option2 = new Option("l2", "longOption2", false, "Print long option2 information.");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {"--longOption1", "--longOption2"};
        String[] expected = {"--longOption1", "--longOption2"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithValueAndAnotherLongOption() {
        Options options = new Options();
        Option option1 = new Option("l1", "longOption1", false, "Print long option1 information.");
        Option option2 = new Option("l2", "longOption2", false, "Print long option2 information.");
        Option valueOption = new Option("value", "v", true, "Set the option value.");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(valueOption);
        String[] arguments = {"--longOption1", "--longOption2", "--value", "INFO"};
        String[] expected = {"--longOption1", "--longOption2", "--value", "INFO"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Print option information.");
        String[] arguments = {"-o", "nonOption"};
        String[] expected = {"-o"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_DashDash() {
        Options options = new Options();
        String[] arguments = {"--"};
        String[] expected = {"--"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }
}