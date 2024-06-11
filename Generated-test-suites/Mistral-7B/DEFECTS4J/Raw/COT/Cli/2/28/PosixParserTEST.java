package org.apache.commons.cli;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * JUnit test cases for PosixParser class.
 */
public class PosixParserTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an integer value");
        options.addOption("c", 2, "Option C with a short value");
        options.addOption("d", 3, "Option D with an int array value");
        options.addOption("e", 4, "Option E with a long value");
    }

    

    @Test
    public void testEmpty() {
        PosixParser parser = new PosixParser();
        String[] args = {};
        String[] expected = {};

        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testSingleHyphen() {
        PosixParser parser = new PosixParser();
        String[] args = {"-"};
        String[] expected = {"-"};

        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testSingleOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a"};
        String[] expected = {"-a"};

        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testOptionWithArgument() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "value"};
        String[] expected = {"-a", "value"};

        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testOptionWithMultipleArguments() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "value1", "value2"};
        String[] expected = {"-a", "value1", "value2"};

        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testMultipleOptions() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "-b", "-c=10"};
        String[] expected = {"-a", "-b", "-c", "10"};

        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testLongOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--option-e", "5"};
        String[] expected = {"--option-e", "5"};

        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testLongOptionWithArgument() {
        PosixParser parser = new PosixParser();
        String[] args = {"--option-d", "1,2,3"};
        String[] expected = {"--option-d", "1,2,3"};

        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testUnknownOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-x"};

        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testUnknownLongOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--unknown-option"};

        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testMissingArgument() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a"};

        parser.flatten(options, args, true);
    }

    @Test(expected = ParseException.class)
    public void testMissingArgumentForLongOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--option-d"};

        parser.flatten(options, args, true);
    }

    @Test(expected = ParseException.class)
    public void testExtraArguments() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "-b", "-c=10", "extra"};

        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testExtraArgumentsForLongOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--option-d", "1,2,3", "extra"};

        parser.flatten(options, args, false);
    }

    
}