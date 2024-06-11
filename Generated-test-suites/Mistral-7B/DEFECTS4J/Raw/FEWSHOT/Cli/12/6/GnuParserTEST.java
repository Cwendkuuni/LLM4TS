package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlattenEmptyArguments() {
        Options options = new Options();
        String[] arguments = {};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenSingleArgument() {
        Options options = new Options();
        String[] arguments = {"arg"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[] {"arg"}, result);
    }

    @Test
    public void testFlattenSingleOption() {
        Options options = new Options();
        Option opt = new Option("o", "option", true, "Description");
        options.addOption(opt);
        String[] arguments = {"-o"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[] {"-o"}, result);
        assertArrayEquals(new String[] {"option"}, parser.parse(options, new String[] {"-o", "value"}));
    }

    @Test
    public void testFlattenOptionWithValue() {
        Options options = new Options();
        Option opt = new Option("o", "option", true, "Description");
        options.addOption(opt);
        String[] arguments = {"-o", "value"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[] {"-o", "value"}, result);
        assertArrayEquals(new String[] {"option", "value"}, parser.parse(options, new String[] {"-o", "value"}));
    }

    @Test
    public void testFlattenMultipleOptions() {
        Options options = new Options();
        Option opt1 = new Option("o1", "option1", true, "Description1");
        Option opt2 = new Option("o2", "option2", true, "Description2");
        options.addOption(opt1);
        options.addOption(opt2);
        String[] arguments = {"-o1", "value1", "-o2", "value2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[] {"-o1", "value1", "-o2", "value2"}, result);
        assertArrayEquals(new String[] {"option1", "value1", "option2", "value2"}, parser.parse(options, arguments));
    }

    @Test
    public void testFlattenSpecialOption() {
        Options options = new Options();
        Option opt = new Option("D", "debug", false, "Set debug mode");
        options.addOption(opt);
        String[] arguments = {"--debug"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[] {"--debug"}, result);
        assertArrayEquals(new String[] {"debug"}, parser.parse(options, arguments));
    }

    @Test
    public void testFlattenDoubleDash() {
        Options options = new Options();
        Option opt = new Option("o", "option", true, "Description");
        options.addOption(opt);
        String[] arguments = {"--"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[] {"--"}, result);
        String[] parsed = parser.parse(options, arguments);
        assertNull(parsed);
    }

    @Test
    public void testFlattenDoubleDashWithArgument() {
        Options options = new Options();
        Option opt = new Option("o", "option", true, "Description");
        options.addOption(opt);
        String[] arguments = {"--arg"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[] {"--arg"}, result);
        assertArrayEquals(new String[] {"arg"}, parser.parse(options, arguments));
    }

    @Test
    public void testFlattenDoubleDashWithOptions() {
        Options options = new Options();
        Option opt1 = new Option("o1", "option1", true, "Description1");
        Option opt2 = new Option("o2", "option2", true, "Description2");
        options.addOption(opt1);
        options.addOption(opt2);
        String[] arguments = {"--", "-o1", "value1", "-o2", "value2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[] {"--", "-o1", "value1", "-o2", "value2"}, result);
        assertArrayEquals(new String[] {"option1", "value1", "option2", "value2"}, parser.parse(options, arguments));
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        Option opt = new Option("o", "option", true, "Description");
        options.addOption(opt);
        String[] arguments = {"-o", "val1", "nonOption", "val2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[] {"-o", "val1"}, result);
    }

    @Test
    public void testFlattenWithLongOptions() {
        Options options = new Options();
        Option longOpt = new Option("longoption", "long option", true, "Description");
        options.addOption(longOpt);
        String[] arguments = {"--longoption", "arg"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[] {"--longoption", "arg"}, result);
        assertArrayEquals(new String[] {"long option", "arg"}, parser.parse(options, arguments));
    }

    @Test
    public void testFlattenWithEmptyArguments() {
        Options options = new Options();
        String[] arguments = {"", ""};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenWithTrailingEmptyArgument() {
        Options options = new Options();
        String[] arguments = {"arg1", "arg2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[] {"arg1", "arg2"}, result);
        String[] parsed = parser.parse(options, arguments);
        assertArrayEquals(new String[] {"arg1", "arg2"}, parsed);

        arguments[arguments.length - 1] = "";
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[] {"arg1", "", "arg2"}, result);
        parsed = parser.parse(options, arguments);
        assertArrayEquals(new String[] {"arg1", "", "arg2"}, parsed);
    }
}