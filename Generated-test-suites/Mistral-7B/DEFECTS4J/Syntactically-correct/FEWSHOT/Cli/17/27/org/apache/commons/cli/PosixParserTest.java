package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten_EmptyArgs() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {};
        String[] expected = {};

        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleOption() {
        Options options = new Options();
        options.addOption("a", true, "a description");
        PosixParser parser = new PosixParser();
        String[] args = {"-a"};
        String[] expected = {"-a"};

        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleLongOption() {
        Options options = new Options();
        options.addOption("longOption", true, "a description");
        PosixParser parser = new PosixParser();
        String[] args = {"--longOption"};
        String[] expected = {"--longOption"};

        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionWithValue() {
        Options options = new Options();
        options.addOption("a", true, "a description");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "value"};
        String[] expected = {"-a", "value"};

        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        Options options = new Options();
        options.addOption("longOption", true, "a description");
        PosixParser parser = new PosixParser();
        String[] args = {"--longOption", "value"};
        String[] expected = {"--longOption", "value"};

        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionWithValueAndOtherArgs() {
        Options options = new Options();
        options.addOption("a", true, "a description");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "value", "arg1", "arg2"};
        String[] expected = {"-a", "value", "arg1", "arg2"};

        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithValueAndOtherArgs() {
        Options options = new Options();
        options.addOption("longOption", true, "a description");
        PosixParser parser = new PosixParser();
        String[] args = {"--longOption", "value", "arg1", "arg2"};
        String[] expected = {"--longOption", "value", "arg1", "arg2"};

        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_GroupedOptions() {
        Options options = new Options();
        Option group = new Option("g", "group", false, "group description");
        options.addOptionGroup(group);
        group.addOption("a", true, "a description");
        Option longOption = new Option("longOption", true, "a long description");
        group.addOption(longOption);
        PosixParser parser = new PosixParser();
        String[] args = {"-g", "-a", "--longOption"};
        String[] expected = {"-g", "-a", "--longOption"};

        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_CombinedOptions() {
        Options options = new Options();
        Option group = new Option("g", "group", false, "group description");
        options.addOptionGroup(group);
        group.addOption("a", true, "a description");
        group.addOption("longOption", false, "a long description");
        PosixParser parser = new PosixParser();
        String[] args = {"-g", "--longOption", "arg"};
        String[] expected = {"-g", "--longOption"};

        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_NoOptions() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "arg2"};
        String[] expected = {"arg1", "arg2"};

        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "a description");
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "-a", "arg3"};
        String[] expected = {"arg1", "-a", "arg3"};

        String[] result = parser.flatten(options, args, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOptionWithLongOptions() {
        Options options = new Options();
        options.addOption("longOption", true, "a description");
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "--longOption", "arg3"};
        String[] expected = {"arg1", "--longOption", "arg3"};

        String[] result = parser.flatten(options, args, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOptionWithGroupedOptions() {
        Options options = new Options();
        Option group = new Option("g", "group", false, "group description");
        options.addOptionGroup(group);
        group.addOption("a", true, "a description");
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "-g", "arg3"};
        String[] expected = {"arg1", "-g"};

        String[] result = parser.flatten(options, args, true);

        assertArrayEquals(expected, result);
    }
}