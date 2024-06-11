package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    @Test
    public void testEmptyArguments() {
        String[] emptyArguments = {};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] parsedArguments;

        try {
            parsedArguments = parser.flatten(options, emptyArguments, false);
            Assert.assertArrayEquals(new String[0], parsedArguments);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testNormalOption() {
        String[] arguments = {"-a", "--optionA", "--optionB=valueB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("optionB", true, "Option B");

        PosixParser parser = new PosixParser();
        String[] parsedArguments;

        try {
            parsedArguments = parser.flatten(options, arguments, false);
            Assert.assertArrayEquals(new String[]{"-a", "--optionB=valueB"}, parsedArguments);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testLongOptionWithValue() {
        String[] arguments = {"--optionA=valueA"};
        Options options = new Options();
        options.addOption("optionA", true, "Option A");

        PosixParser parser = new PosixParser();
        String[] parsedArguments;

        try {
            parsedArguments = parser.flatten(options, arguments, false);
            Assert.assertArrayEquals(new String[]{"--optionA=valueA"}, parsedArguments);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testShortOptionWithValue() {
        String[] arguments = {"-a=valueA"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        PosixParser parser = new PosixParser();
        String[] parsedArguments;

        try {
            parsedArguments = parser.flatten(options, arguments, false);
            Assert.assertArrayEquals(new String[]{"-a=valueA"}, parsedArguments);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testLongOptionWithValueAndArg() {
        String[] arguments = {"--optionA=valueA --optionB"};
        Options options = new Options();
        options.addOption("optionA", true, "Option A");
        options.addOption("optionB", true, "Option B");

        PosixParser parser = new PosixParser();
        String[] parsedArguments;

        try {
            parsedArguments = parser.flatten(options, arguments, false);
            Assert.assertArrayEquals(new String[]{"--optionA=valueA"}, parsedArguments);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testUnknownOption() {
        String[] arguments = {"--unknownOption"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] parsedArguments;

        try {
            parsedArguments = parser.flatten(options, arguments, false);
            Assert.assertArrayEquals(new String[]{"--unknownOption"}, parsedArguments);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() {
        String[] arguments = {"--missingOption"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] parsedArguments;

        try {
            parsedArguments = parser.flatten(options, arguments, false);
        } catch (ParseException e) {
            // Expected
        }

        Assert.fail("ParseException not thrown");
    }

    @Test
    public void testMultipleShortOptions() {
        String[] arguments = {"-a", "-b", "-c"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        PosixParser parser = new PosixParser();
        String[] parsedArguments;

        try {
            parsedArguments = parser.flatten(options, arguments, false);
            Assert.assertArrayEquals(new String[]{"-a", "-b", "-c"}, parsedArguments);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testShortOptionWithLongOption() {
        String[] arguments = {"-a", "--optionA"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("optionA", true, "Option A");

        PosixParser parser = new PosixParser();
        String[] parsedArguments;

        try {
            parsedArguments = parser.flatten(options, arguments, false);
            Assert.assertArrayEquals(new String[]{"--optionA"}, parsedArguments);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() {
        String[] arguments = {"-x"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] parsedArguments;

        try {
            parsedArguments = parser.flatten(options, arguments, false);
        } catch (ParseException e) {
            // Expected
        }

        Assert.fail("ParseException not thrown");
    }

    @Test(expected = ParseException.class)
    public void testMixedLongAndShortOptions() {
        String[] arguments = {"--optionA", "-b", "--optionB"};
        Options options = new Options();
        options.addOption("optionA", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("optionB", true, "Option B");

        PosixParser parser = new PosixParser();
        String[] parsedArguments;

        try {
            parsedArguments = parser.flatten(options, arguments, false);
        } catch (ParseException e) {
            // Expected
        }

        Assert.fail("ParseException not thrown");
    }

    @Test
    public void testStopAtNonOption() {
        String[] arguments = {"--", "-a", "-b"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        PosixParser parser = new PosixParser();
        String[] parsedArguments;

        try {
            parsedArguments = parser.flatten(options, arguments, true);
            Assert.assertArrayEquals(new String[]{"--"}, parsedArguments);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
}