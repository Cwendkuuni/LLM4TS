package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", true, "A boolean option");
        options.addOption("b", 1, "An int option with an argument");
        options.addOption("c", 3, "An int option with an optional argument");

        String[] args = {"--a", "-b", "1", "--c", "-d", "3", "e"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "--a",
                "-b",
                "1",
                "--c",
                "-d",
                "3"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), parsedArgs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithInvalidOption() {
        Options options = new Options();

        String[] args = {"--invalid"};
        PosixParser parser = new PosixParser();

        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenWithLongOptionValue() {
        Options options = new Options();
        options.addLongOption("longOption", true, "A long option");

        String[] args = {"--longOption=longOptionValue"};
        PosixParser parser = new PosixParser();

        String[] parsedArgs = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "--longOption",
                "longOptionValue"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), parsedArgs);
    }

    @Test
    public void testFlattenWithMultipleLongOptions() {
        Options options = new Options();
        options.addLongOption("longOption1", true, "A long option 1");
        options.addLongOption("longOption2", true, "A long option 2");

        String[] args = {"--longOption1", "--longOption2"};
        PosixParser parser = new PosixParser();

        String[] parsedArgs = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "--longOption1",
                "--longOption2"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), parsedArgs);
    }

    @Test
    public void testFlattenWithMultipleArgumentsForAnOption() {
        Options options = new Options();
        options.addOption("a", 3, "An option with three arguments");

        String[] args = {"-a", "arg1", "arg2", "arg3"};
        PosixParser parser = new PosixParser();

        String[] parsedArgs = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "-a",
                "arg1",
                "arg2",
                "arg3"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), parsedArgs);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "A boolean option");

        String[] args = {"--a", "-b", "invalidOption"};
        PosixParser parser = new PosixParser();

        String[] parsedArgs = parser.flatten(options, args, true);

        List<String> expected = Arrays.asList(
                "--a",
                "-b",
                "invalidOption"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), parsedArgs);
    }

    @Test
    public void testFlattenWithEmptyArguments() {
        Options options = new Options();

        String[] args = {};
        PosixParser parser = new PosixParser();

        String[] parsedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[0], parsedArgs);
    }
}