package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenWithEmptyArguments() {
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] arguments = {};
        String[] expected = {};

        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithSingleArgument() {
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] arguments = {"arg"};
        String[] expected = {"arg"};

        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithSingleOption() {
        Options options = new Options();
        Parser parser = new PosixParser();
        options.addOption("o", "option", false, "Option description");
        String[] arguments = {"-o"};
        String[] expected = {"-o"};

        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithSingleOptionValue() {
        Options options = new Options();
        Parser parser = new PosixParser();
        options.addOption("o", "option", true, "Option description");
        String[] arguments = {"-o", "value"};
        String[] expected = {"-o", "value"};

        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithMultipleOptions() {
        Options options = new Options();
        Parser parser = new PosixParser();
        options.addOption("o", "option1", false, "Option1 description");
        options.addOption("e", "option2", true, "Option2 description");
        String[] arguments = {"-o", "--option2", "value"};
        String[] expected = {"-o", "--option2", "value"};

        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOption() {
        Options options = new Options();
        Parser parser = new PosixParser();
        options.addOption("longOption", "Long option description", true, "Long option description");
        String[] arguments = {"--longOption", "value"};
        String[] expected = {"--longOption", "value"};

        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithShortAndLongOptions() {
        Options options = new Options();
        Parser parser = new PosixParser();
        options.addOption("s", "shortOption", false, "Short option description");
        options.addOption("l", "longOption", true, "Long option description");
        String[] arguments = {"-s", "--longOption", "value"};
        String[] expected = {"-s", "--longOption", "value"};

        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithMixedArgumentsAndOptions() {
        Options options = new Options();
        Parser parser = new PosixParser();
        options.addOption("o", "option", true, "Option description");
        String[] arguments = {"arg1", "-o", "arg2"};
        String[] expected = {"arg1", "-o", "arg2"};

        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        Parser parser = new PosixParser();
        options.addOption("o", "option", true, "Option description");
        String[] arguments = {"arg1", "-o", "arg2", "--nonOption", "arg3"};
        List<String> expected = Arrays.asList("arg1", "-o", "arg2");

        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithInvalidOption() {
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] arguments = {"-x"};

        parser.flatten(options, arguments, false);
    }
}