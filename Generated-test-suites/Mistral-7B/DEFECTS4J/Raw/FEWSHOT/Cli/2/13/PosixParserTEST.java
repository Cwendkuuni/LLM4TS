package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten_EmptyArgs() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {};
        String[] expected = {};

        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlatten_SingleArg() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"arg1"};
        String[] expected = {"arg1"};

        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlatten_DoubleDash() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"--foo"};
        String[] expected = {"--foo"};

        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlatten_SingleHyphen() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"-"};
        String[] expected = {"-"};

        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlatten_OptionWithNoArg() {
        Options options = new Options();
        Option option = new Option("a", "option-a");
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String[] args = {"-a"};
        String[] expected = {"-a"};

        assertArrayEquals(expected, parser.flatten(options, args, false));
        assertSame(option, parser.getCurrentOption());
    }

    @Test
    public void testFlatten_OptionWithArg() {
        Options options = new Options();
        Option option = new Option("a", "option-a", true);
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "arg"};
        String[] expected = {"-a", "arg"};

        assertArrayEquals(expected, parser.flatten(options, args, false));
        assertSame(option, parser.getCurrentOption());
    }

    @Test
    public void testFlatten_OptionWithLongArg() {
        Options options = new Options();
        Option option = new Option("a", "option-a", true, "long-arg");
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "long-arg"};
        String[] expected = {"-a", "long-arg"};

        assertArrayEquals(expected, parser.flatten(options, args, false));
        assertSame(option, parser.getCurrentOption());
    }

    @Test
    public void testFlatten_OptionWithArgAndIteratedArgs() {
        Options options = new Options();
        Option option = new Option("a", "option-a", true);
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "arg1", "arg2"};
        String[] expected = {"-a", "arg1", "arg2"};

        assertArrayEquals(expected, parser.flatten(options, args, false));
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlatten_OptionWithNoArgAndIteratedArgs() {
        Options options = new Options();
        Option option = new Option("a", "option-a");
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "arg1", "arg2"};
        String[] expected = {"-a", "arg1", "arg2"};

        parser.setStopAtNonOption(true);

        assertArrayEquals(expected, parser.flatten(options, args, true));
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlatten_OptionWithLongArgAndIteratedArgs() {
        Options options = new Options();
        Option option = new Option("a", "option-a", true, "long-arg");
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "long-arg1", "long-arg2"};
        String[] expected = {"-a", "long-arg1", "long-arg2"};

        parser.setStopAtNonOption(true);

        assertArrayEquals(expected, parser.flatten(options, args, true));
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlatten_MultiOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "option-a");
        Option option2 = new Option("b", "option-b");
        options.addOption(option1);
        options.addOption(option2);
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "-b"};
        String[] expected = {"-a", "-b"};

        assertArrayEquals(expected, parser.flatten(options, args, false));
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlatten_MultiOptionsWithIteratedArgs() {
        Options options = new Options();
        Option option1 = new Option("a", "option-a", true);
        Option option2 = new Option("b", "option-b", true);
        options.addOption(option1);
        options.addOption(option2);
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "arg1", "-b", "arg2"};
        String[] expected = {"-a", "arg1", "-b", "arg2"};

        assertArrayEquals(expected, parser.flatten(options, args, false));
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlatten_LongOption() {
        Options options = new Options();
        Option option = new Option("a", "long-option-name");
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String[] args = {"--long-option-name"};
        String[] expected = {"--long-option-name"};

        assertArrayEquals(expected, parser.flatten(options, args, false));
        assertSame(option, parser.getCurrentOption());
    }

    @Test
    public void testFlatten_LongOptionWithSingleValueArg() {
        Options options = new Options();
        Option option = new Option("a", "long-option-name", true, "value");
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String[] args = {"--long-option-name=value"};
        String[] expected = {"--long-option-name", "value"};

        assertArrayEquals(expected, parser.flatten(options, args, false));
        assertSame(option, parser.getCurrentOption());
    }

    @Test
    public void testFlatten_LongOptionWithMultipleValueArgs() {
        Options options = new Options();
        Option option = new Option("a", "long-option-name", true, "value1 value2");
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String[] args = {"--long-option-name=value1 value2"};
        String[] expected = {"--long-option-name", "value1", "value2"};

        assertArrayEquals(expected, parser.flatten(options, args, false));
        assertSame(option, parser.getCurrentOption());
    }

    @Test
    public void testFlatten_LongOptionWithNoValueArgs() {
        Options options = new Options();
        Option option = new Option("a", "long-option-name");
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String[] args = {"--long-option-name"};
        String[] expected = {"--long-option-name"};

        assertArrayEquals(expected, parser.flatten(options, args, false));
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlatten_LongOptionWithValueAndIteratedArgs() {
        Options options = new Options();
        Option option = new Option("a", "long-option-name", true);
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String[] args = {"--long-option-name=value1", "value2"};
        String[] expected = {"--long-option-name", "value1", "value2"};

        assertArrayEquals(expected, parser.flatten(options, args, false));
        assertSame(option, parser.getCurrentOption());
    }

    @Test
    public void testFlatten_LongOptionWithValueIteratedArgsAndSingleValueArg() {
        Options options = new Options();
        Option option = new Option("a", "long-option-name", true);
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String[] args = {"--long-option-name=value1", "value2"};
        String[] expected = {"--long-option-name", "value1", "value2"};

        parser.setStopAtNonOption(true);

        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(Arrays.asList("--long-option-name", "value1", "value2").toArray(new String[0]), result);
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlatten_LongOptionWithValueIteratedArgsAndLongValueArg() {
        Options options = new Options();
        Option option = new Option("a", "long-option-name", true);
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String[] args = {"--long-option-name=value1 value2"};
        String[] expected = {"--long-option-name", "value1", "value2"};

        parser.setStopAtNonOption(true);

        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(Arrays.asList("--long-option-name", "value1", "value2").toArray(new String[0]), result);
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlatten_LongOptionWithValueIteratedArgsAndLongValueArgs() {
        Options options = new Options();
        Option option = new Option("a", "long-option-name", true);
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String[] args = {"--long-option-name=value1 value2 value3"};
        String[] expected = {"--long-option-name", "value1", "value2", "value3"};

        parser.setStopAtNonOption(true);

        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(Arrays.asList("--long-option-name", "value1", "value2", "value3").toArray(new String[0]), result);
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlatten_EmptyOptionDefinition() {
        Options options = new Options();
        Option option = new Option("a", "");
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String[] args = {"-a"};
        String[] expected = {"-a"};

        assertArrayEquals(expected, parser.flatten(options, args, false));
        assertSame(option, parser.getCurrentOption());
    }

    @Test
    public void testFlatten_EmptyOptionDefinitionWithIteratedArgs() {
        Options options = new Options();
        Option option = new Option("a", "");
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "arg1", "arg2"};
        String[] expected = {"-a", "arg1", "arg2"};

        parser.setStopAtNonOption(true);

        assertArrayEquals(expected, parser.flatten(options, args, true));
        assertNull(parser.getCurrentOption());
    }
}