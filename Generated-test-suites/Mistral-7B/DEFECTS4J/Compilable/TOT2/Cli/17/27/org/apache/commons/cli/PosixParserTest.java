package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        Options options = new Options();
        String[] arguments = {"--foo", "-a", "--bar=baz", "-c", "--baz", "--", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"--foo", "--", "-a", "--bar", "baz", "--", "--baz", "--", "arg1", "arg2"}, parsedArguments);
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() throws ParseException {
        Options options = new Options();
        String[] arguments = {"--invalid-option"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, true);
    }

    @Test(expected = ParseException.class)
    public void testInvalidTwoCharacterOption() throws ParseException {
        Options options = new Options();
        String[] arguments = {"--.."};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, true);
    }

    @Test
    public void testBursting() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Long Option A");
        options.addOption("b", false, "Short Option B");
        String[] arguments = {"-ab"};
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"-a", "--", "-b"}, parsedArguments);
    }

    @Test
    public void testBurstingWithArgument() throws ParseException {
        Options options = new Options();
        options.addOption("x", true, "Long Option X with argument");
        String[] arguments = {"-xarg1"};
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"-x", "--", "arg1"}, parsedArguments);
    }

    @Test
    public void testBurstingWithNoMatchingOption() throws ParseException {
        Options options = new Options();
        String[] arguments = {"--no-matching-option"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, true);
    }

    @Test(expected = ParseException.class)
    public void testBurstingWithTwoCharacterOptionWithoutArgument() throws ParseException {
        Options options = new Options();
        String[] arguments = {"--ab"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, true);
    }

    @Test
    public void testBurstingWithTwoCharacterOptionWithArgument() throws ParseException {
        Options options = new Options();
        options.addOption("ab", true, "Two character option AB with argument");
        String[] arguments = {"--abarg1"};
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"--ab", "--", "arg1"}, parsedArguments);
    }
}