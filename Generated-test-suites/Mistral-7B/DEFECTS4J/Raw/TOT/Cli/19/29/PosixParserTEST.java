package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the PosixParser class.
 */
public class PosixParserTest {

    @Test
    public void testSimpleOptions() throws ParseException {
        String[] args = {"-a", "-b=valueB", "--optionC=valueC"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B with value");
        options.addOption("c", true, "Option C with value");

        String[] expectedArgs = {"-a", "-b", "valueB", "--optionC", "valueC"};
        String[] resultArgs = parser.parse(args, options, true);

        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws ParseException {
        String[] args = {"--missingOption"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");

        parser.parse(args, options, false);
    }

    @Test(expected = ParseException.class)
    public void testMissingValue() throws ParseException {
        String[] args = {"-a", "--optionB"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B with value");

        parser.parse(args, options, false);
    }

    @Test
    public void testLongOptions() throws ParseException {
        String[] args = {"--longOptionA=valueA", "--longOptionB=valueB"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOptionA", true, "Long option A with value");
        options.addOption("longOptionB", true, "Long option B with value");

        String[] expectedArgs = {"--longOptionA", "valueA", "--longOptionB", "valueB"};
        String[] resultArgs = parser.parse(args, options, true);

        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test
    public void testMultiValueOption() throws ParseException {
        String[] args = {"-c", "value1", "-c", "value2", "-c", "value3"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("c", true, "Option C with multiple values");

        String[] expectedArgs = {"-c", "value1", "-c", "value2", "-c", "value3"};
        String[] resultArgs = parser.parse(args, options, true);

        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() throws ParseException {
        String[] args = {"--invalidOption"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");

        parser.parse(args, options, false);
    }

    @Test
    public void testBursting() throws ParseException {
        String[] args = {"--longOptionWithValue1Value2"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOptionWithValue", true, "Long option with value");

        String[] expectedArgs = {"--longOptionWithValue", "value1", "value2"};
        String[] resultArgs = parser.parse(args, options, true);

        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test
    public void testStopAtNonOption() throws ParseException {
        String[] args = {"--", "-a", "-b=valueB"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B with value");

        String[] expectedArgs = {"--", "-a", "-b"};
        String[] resultArgs = parser.parse(args, options, true);

        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }
}