package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten_SimpleOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] arguments = {"-a"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List expected = Arrays.asList("-a");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlatten_OptionWithArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = {"-a", "argA", "--", "-b", "argB"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List expected = Arrays.asList("-a", "--", "-b");
        expected.add("argB");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_UnrecognizedOption() throws ParseException {
        Options options = new Options();
        String[] arguments = {"--x"};
        PosixParser parser = new PosixParser();

        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlatten_LongOption() throws ParseException {
        Options options = new Options();
        options.addOption("longOption", true, "Long Option");

        String[] arguments = {"--longOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List expected = Arrays.asList("--longOption");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlatten_LongOptionWithArgument() throws ParseException {
        Options options = new Options();
        options.addOption("longOption", true, "Long Option");
        options.addOption("b", true, "Option B");

        String[] arguments = {"--longOption=arg", "--", "-b", "argB"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List expected = Arrays.asList("--longOption=arg", "--", "-b");
        expected.add("argB");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_LongOptionWithInvalidArgument() throws ParseException {
        Options options = new Options();
        options.addOption("longOption", true, "Long Option");

        String[] arguments = {"--longOption=invalidArg"};
        PosixParser parser = new PosixParser();

        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlatten_MultipleOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = {"-a", "--longOption=arg", "-b"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List expected = Arrays.asList("-a", "--longOption=arg", "-b");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlatten_StopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] arguments = {"-a", "--", "nonOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        List expected = Arrays.asList("-a", "--", "nonOption");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlatten_BurstingSimpleToken() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] arguments = {"-aa"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List expected = Arrays.asList("-a", "-a");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlatten_BurstingOptionToken() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = {"-ab"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List expected = Arrays.asList("-a", "-b");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlatten_BurstingLongOptionToken() throws ParseException {
        Options options = new Options();
        options.addOption("longOption", true, "Long Option");

        String[] arguments = {"--longOption-l"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List expected = Arrays.asList("--longOption", "--longOption");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlatten_BurstingOptionWithArgument() throws ParseException {
        Options options = new Options();
        options.addOption("longOption", true, "Long Option");
        options.addOption("b", true, "Option B");

        String[] arguments = {"--longOption-lb"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List expected = Arrays.asList("--longOption=lb", "--longOption");
        Assert.assertArrayEquals(expected.toArray(), result);
    }
}