package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for PosixParser class.
 */
public class PosixParserTest {

    @Test
    public void testFlattenWithEmptyArguments() {
        String[] arguments = new String[0];
        Options options = new Options();
        PosixParser parser = new PosixParser();

        try {
            String[] flattened = parser.flatten(options, arguments, false);
            Assert.assertArrayEquals(new String[0], flattened);
        } catch (ParseException e) {
            // This test case does not handle parsing exceptions, as
            // there should be none when no arguments are provided.
            Assert.fail("Parsing exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFlattenWithSingleArgument() {
        String[] arguments = new String[]{"arg"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        try {
            String[] flattened = parser.flatten(options, arguments, false);
            Assert.assertArrayEquals(new String[]{"arg"}, flattened);
        } catch (ParseException e) {
            Assert.fail("Parsing exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFlattenWithDoubleDash() {
        String[] arguments = new String[]{"--test"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        try {
            String[] flattened = parser.flatten(options, arguments, false);
            Assert.assertArrayEquals(new String[]{"--test"}, flattened);
        } catch (ParseException e) {
            Assert.fail("Parsing exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFlattenWithSingleHyphen() {
        String[] arguments = new String[]{"-"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        try {
            String[] flattened = parser.flatten(options, arguments, false);
            Assert.assertArrayEquals(new String[]{"-"}, flattened);
        } catch (ParseException e) {
            Assert.fail("Parsing exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFlattenWithOption() {
        String[] arguments = new String[]{"-t", "value"};
        Options options = new Options();
        options.addOption("t", "test", true, "Test value");
        PosixParser parser = new PosixParser();

        try {
            String[] flattened = parser.flatten(options, arguments, false);
            Assert.assertArrayEquals(new String[]{"-t", "value"}, flattened);
        } catch (ParseException e) {
            Assert.fail("Parsing exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFlattenWithOptionWithArgument() {
        String[] arguments = new String[]{"-t", "value1", "value2"};
        Options options = new Options();
        options.addOption("t", "test", true, "Test value");
        PosixParser parser = new PosixParser();

        try {
            String[] flattened = parser.flatten(options, arguments, false);
            Assert.assertArrayEquals(new String[]{"-t", "value1", "value2"}, flattened);
        } catch (ParseException e) {
            Assert.fail("Parsing exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFlattenWithOptionAndArguments() {
        String[] arguments = new String[]{"--test", "value1", "value2"};
        Options options = new Options();
        options.addOption("t", "test", true, "Test value");
        PosixParser parser = new PosixParser();

        try {
            String[] flattened = parser.flatten(options, arguments, false);
            Assert.assertArrayEquals(new String[]{"--test", "value1", "value2"}, flattened);
        } catch (ParseException e) {
            Assert.fail("Parsing exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFlattenWithOptionsAndArguments() {
        String[] arguments = new String[]{"-t", "value", "--option1", "value1"};
        Options options = new Options();
        options.addOption("t", "test", true, "Test value");
        options.addOption("o", "option1", true, "Option 1 value");
        PosixParser parser = new PosixParser();

        try {
            String[] flattened = parser.flatten(options, arguments, false);
            Assert.assertArrayEquals(new String[]{"-t", "value", "--option1", "value1"}, flattened);
        } catch (ParseException e) {
            Assert.fail("Parsing exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFlattenWithMultipleOptions() {
        String[] arguments = new String[]{"-t", "value1", "--option1", "value1", "-t", "value2", "--option2", "value2"};
        Options options = new Options();
        options.addOption("t", "test", true, "Test value");
        options.addOption("o1", "option1", true, "Option 1 value");
        options.addOption("o2", "option2", true, "Option 2 value");
        PosixParser parser = new PosixParser();

        try {
            String[] flattened = parser.flatten(options, arguments, false);
            Assert.assertArrayEquals(new String[]{"-t", "value1", "--option1", "value1", "-t", "value2", "--option2", "value2"}, flattened);
        } catch (ParseException e) {
            Assert.fail("Parsing exception should not be thrown: " + e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() {
        String[] arguments = new String[]{"-invalidOption"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        try {
            parser.flatten(options, arguments, false);
            Assert.fail("Parsing exception should have been thrown.");
        } catch (ParseException e) {
            // Expected exception.
        }
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOptionWithArguments() {
        String[] arguments = new String[]{"-invalidOption", "arg1", "arg2"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        try {
            parser.flatten(options, arguments, false);
            Assert.fail("Parsing exception should have been thrown.");
        } catch (ParseException e) {
            // Expected exception.
        }
    }
}