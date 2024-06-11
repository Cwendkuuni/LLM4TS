package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import org.junit.Assert;

import java.util.Arrays;

/**
 * Test class for {@link PosixParser} class.
 */
public class PosixParserTest {

    /**
     * Test flattening of simple options.
     */
    @Test
    public void testSimpleOptions() throws ParseException {
        String[] args = {"-a", "-b", "-c"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "-b", "-c"}, flattenedArgs);
    }

    /**
     * Test flattening of options with long names.
     */
    @Test
    public void testLongOptions() throws ParseException {
        String[] args = {"--long-option-a", "--long-option-b=value", "--long-option-c"};
        Options options = new Options();
        options.addOption("longOptionA", true, "Long Option A");
        options.addOption("longOptionB", true, "Long Option B");
        options.addOption("longOptionC", true, "Long Option C");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--long-option-a", "--long-option-b", "value", "--long-option-c"}, flattenedArgs);
    }

    /**
     * Test flattening of options with short and long names.
     */
    @Test
    public void testShortAndLongOptions() throws ParseException {
        String[] args = {"-a", "--long-option-b", "-c", "--long-option-b=value", "--", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "--long-option-b", null, null, "--long-option-b", "value", "--", "arg1", "arg2"}, flattenedArgs);
    }

    /**
     * Test handling of multiple occurrences of the same option.
     */
    @Test
    public void testMultipleOccurrences() throws ParseException {
        String[] args = {"-a", "-a"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "-a"}, flattenedArgs);
    }

    /**
     * Test handling of options with arguments.
     */
    @Test
    public void testOptionsWithArguments() throws ParseException {
        String[] args = {"-a", "value1", "-b", "value2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "value1", "-b", "value2"}, flattenedArgs);
    }

    /**
     * Test handling of options with variable number of arguments.
     */
    @Test
    public void testOptionsWithVariableArguments() throws ParseException {
        String[] args = {"-a", "value1", "-a", "value2", "value3"};
        Options options = new Options();
        options.addOption("a", true, "Option A", 1);

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "value1"}, flattenedArgs);
        Assert.assertArrayEquals(Arrays.copyOfRange(flattenedArgs, 1, flattenedArgs.length), new String[]{"value2", "value3"});
    }

    /**
     * Test handling of invalid options.
     */
    @Test(expected = ParseException.class)
    public void testInvalidOptions() throws ParseException {
        String[] args = {"--invalid-option", "value"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    /**
     * Test handling of stopAtNonOption
     */
    @Test
    public void testStopAtNonOption() throws ParseException {
        String[] args = {"--invalid-option", "value"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, true);

        Assert.assertArrayEquals(new String[]{"--invalid-option"}, flattenedArgs);
    }

    /**
     * Test handling of long options with arguments.
     */
    @Test
    public void testLongOptionsWithArguments() throws ParseException {
        String[] args = {"--long-option-a", "value1", "--long-option-b=value2"};
        Options options = new Options();
        options.addOption("longOptionA", true, "Long Option A");
        options.addOption("longOptionB", true, "Long Option B");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--long-option-a", "value1", "--long-option-b", "value2"}, flattenedArgs);
    }

    /**
     * Test handling of options with no arguments.
     */
    @Test
    public void testOptionsWithNoArguments() throws ParseException {
        String[] args = {"-a", "-b"};
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "-b"}, flattenedArgs);
    }
}