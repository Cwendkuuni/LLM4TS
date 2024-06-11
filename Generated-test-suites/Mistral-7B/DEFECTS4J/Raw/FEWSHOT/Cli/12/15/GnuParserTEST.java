package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlattenEmptyArguments() {
        Options options = new Options();
        String[] arguments = {};
        String[] expected = {};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleArgument() {
        Options options = new Options();
        String[] arguments = {"arg"};
        String[] expected = {"arg"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleOption() {
        Options options = new Options();
        Option opt = Option.builder("o").longOpt("option").build();
        options.addOption(opt);
        String[] arguments = {"-o"};
        String[] expected = {"o"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleOptionWithValue() {
        Options options = new Options();
        Option opt = Option.builder("o").longOpt("option").valueRequired(true).build();
        options.addOption(opt);
        String[] arguments = {"-o", "value"};
        String[] expected = {"o", "value"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptions() {
        Options options = new Options();
        Option opt1 = Option.builder("o1").longOpt("option1").build();
        Option opt2 = Option.builder("o2").longOpt("option2").build();
        options.addOption(opt1);
        options.addOption(opt2);
        String[] arguments = {"-o1", "-o2"};
        String[] expected = {"o1", "o2"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenTwoArgumentsAndOneOption() {
        Options options = new Options();
        Option opt = Option.builder("o").longOpt("option").build();
        options.addOption(opt);
        String[] arguments = {"arg1", "-o", "arg3"};
        String[] expected = {"arg1", "o", "arg3"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenTwoArgumentsAndOneOptionWithValue() {
        Options options = new Options();
        Option opt = Option.builder("o").longOpt("option").valueRequired(true).build();
        options.addOption(opt);
        String[] arguments = {"arg1", "-o", "value", "arg3"};
        String[] expected = {"arg1", "o", "value", "arg3"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenTwoArgumentsAndOneDoubleDashArgument() {
        Options options = new Options();
        String[] arguments = {"arg1", "--arg3"};
        String[] expected = {"arg1", "--arg3"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenTwoArgumentsAndOneDoubleDashArgumentWithArguments() {
        Options options = new Options();
        String[] arguments = {"arg1", "--arg3", "arg4"};
        String[] expected = {"arg1", "--arg3", "arg4"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenTwoArgumentsAndOnePropertyOption() {
        Options options = new Options();
        Option propOpt = Option.builder("P").longOpt("property").hasArg(true).build();
        options.addOption(propOpt);
        String[] arguments = {"arg1", "-P", "value", "arg3"};
        String[] expected = {"arg1", "P", "value", "arg3"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleDashArgument() {
        Options options = new Options();
        String[] arguments = {"-"};
        String[] expected = {};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenTwoSingleDashArguments() {
        Options options = new Options();
        String[] arguments = {"--"};
        String[] expected = {};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenArgumentsWithStopAtNonOption() {
        Options options = new Options();
        Option opt = Option.builder("o").longOpt("option").build();
        options.addOption(opt);
        String[] arguments = {"arg1", "-o", "value", "nonOptionArg"};
        String[] expected = {"arg1", "o"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenArgumentsWithTwoStopAtNonOption() {
        Options options = new Options();
        Option opt1 = Option.builder("o1").longOpt("option1").build();
        Option opt2 = Option.builder("o2").longOpt("option2").build();
        options.addOption(opt1);
        options.addOption(opt2);
        String[] arguments = {"arg1", "-o1", "value1", "-o2", "value2", "nonOptionArg"};
        String[] expected = {"arg1", "o1"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }
}