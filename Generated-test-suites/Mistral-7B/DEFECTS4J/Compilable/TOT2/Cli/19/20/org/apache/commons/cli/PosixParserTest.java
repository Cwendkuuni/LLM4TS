package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.PosixParser;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testFlatten_SimpleOptionWithNoArgument() {
        Options options = new Options();
        options.addOption("a", false, "Option A");

        String[] args = {"-a"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a"}, result);
    }

    @Test
    public void testFlatten_SimpleOptionWithArgument() {
        Options options = new Options();
        options.addOption("a", true, "Option A with argument");

        String[] args = {"-a", "arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "arg"}, result);
    }

    @Test
    public void testFlatten_MultipleOptionsWithNoArguments() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");

        String[] args = {"-a", "-b"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "-b"}, result);
    }

    @Test
    public void testFlatten_MultipleOptionsWithArguments() {
        Options options = new Options();
        options.addOption("a", true, "Option A with argument");
        options.addOption("b", true, "Option B with argument");

        String[] args = {"-a", "argA", "-b", "argB"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "argA", "-b", "argB"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_UnknownOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");

        String[] args = {"-c", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.fail("Unexpected result: " + Arrays.toString(result));
    }

    @Test
    public void testFlatten_LongOption() {
        Options options = new Options();
        options.addOption("longOption", false, "Long Option");

        String[] args = {"--longOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--longOption"}, result);
    }

    @Test
    public void testFlatten_LongOptionWithArgument() {
        Options options = new Options();
        options.addOption("longOption", true, "Long Option with argument");

        String[] args = {"--longOption", "arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--longOption", "arg"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_LongOptionWithTwoArguments() {
        Options options = new Options();
        options.addOption("longOption", true, "Long Option with two arguments");

        String[] args = {"--longOption", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.fail("Unexpected result: " + Arrays.toString(result));
    }

    @Test
    public void testFlatten_ShortAndLongOptionWithSameName() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("a", false, "Option A (short)");

        String[] args = {"-a", "--a"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "--a"}, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");

        String[] args = {"-a", "--nonOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        Assert.assertArrayEquals(new String[]{"-a", "--nonOption"}, result);
    }

    @Test
    public void testFlatten_EmptyArgs() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, new String[0], false);

        Assert.assertArrayEquals(new String[0], result);
    }
}