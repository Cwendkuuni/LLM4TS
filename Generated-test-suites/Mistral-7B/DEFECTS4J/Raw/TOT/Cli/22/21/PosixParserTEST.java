package org.apache.commons.cli.test;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the PosixParser class.
 */
public class PosixParserTest
{
    /**
     * Test flattening of a simple command line with no options or arguments.
     */
    @Test
    public void testEmpty()
    {
        String[] args = {};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{}, result);
    }

    /**
     * Test flattening of a command line with a single option.
     */
    @Test
    public void testSingleOption()
    {
        String[] args = {"--option"};
        Options options = new Options();
        options.addOption("option", true, "An option.");
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option"}, result);
    }

    /**
     * Test flattening of a command line with a single option and an argument.
     */
    @Test
    public void testSingleOptionWithArgument()
    {
        String[] args = {"--option", "arg"};
        Options options = new Options();
        options.addOption("option", true, "An option.");

        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option", "arg"}, result);
    }

    /**
     * Test flattening of a command line with multiple options, with and without arguments.
     */
    @Test
    public void testMultipleOptions()
    {
        String[] args = {"--option1", "--option2=value", "--option3"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1.");
        options.addOption("option2", true, "Option 2.");
        options.addOption("option3", false, "Option 3.");

        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option1", "--option2", "value", "--option3"}, result);
    }

    /**
     * Test flattening of a command line with a long option and an argument.
     */
    @Test
    public void testLongOptionWithArgument()
    {
        String[] args = {"--long-option=value"};
        Options options = new Options();
        options.addOption("longOption", true, "A long option.");

        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--long-option", "value"}, result);
    }

    /**
     * Test flattening of a command line with a single hyphen (-) and an option with an argument.
     */
    @Test
    public void testSingleMinusWithOption()
    {
        String[] args = {"-o", "value"};
        Options options = new Options();
        options.addOption("o", true, "An option.");

        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--o", "value"}, result);
    }

    /**
     * Test flattening of a command line with a long option that does not exist.
     */
    @Test
    public void testInvalidLongOption()
    {
        String[] args = {"--invalid-option"};
        Options options = new Options();

        PosixParser parser = new PosixParser();

        try
        {
            parser.flatten(options, args, false);
            Assert.fail("Expected ParseException");
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    /**
     * Test flattening of a command line with multiple non-options.
     */
    @Test
    public void testMultipleNonOptions()
    {
        String[] args = {"arg1", "arg2"};
        Options options = new Options();

        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--", "arg1", "arg2"}, result);
    }

    /**
     * Test flattening of a command line with a non-option and an option.
     */
    @Test
    public void testNonOptionWithOption()
    {
        String[] args = {"arg", "--option"};
        Options options = new Options();
        options.addOption("option", true, "An option.");

        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--", "arg", "--option"}, result);
    }

    /**
     * Test flattening of a command line with a non-option and an option with an argument.
     */
    @Test
    public void testNonOptionWithOptionAndArgument()
    {
        String[] args = {"arg", "--option=value"};
        Options options = new Options();
        options.addOption("option", true, "An option.");

        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--", "arg", "--option", "value"}, result);
    }

    /**
     * Test flattening of a command line with a long option that can take multiple values.
     */
    @Test
    public void testLongOptionWithMultipleValues()
    {
        String[] args = {"--option1", "--option1=value1", "--option1=value2"};
        Options options = new Options();
        options.addOption("option1", true, "An option that can take multiple values.");

        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option1", "value1", "value2"}, result);
    }

    /**
     * Test flattening of a command line with a non-option, a long option that can take multiple values, and another non-option.
     */
    @Test
    public void testNonOptionWithLongOptionWithMultipleValues()
    {
        String[] args = {"arg1", "--option1", "arg2"};
        Options options = new Options();
        options.addOption("option1", true, "An option that can take multiple values.");

        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--", "arg1", "--option1", "arg2"}, result);
    }

    /**
     * Test flattening of a command line with a non-option, a long option, and an option.
     */
    @Test
    public void testNonOptionWithLongOptionAndOption()
    {
        String[] args = {"arg1", "--option", "--option2"};
        Options options = new Options();
        options.addOption("option", true, "An option.");
        options.addOption("option2", false, "Another option.");

        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--", "arg1", "--option", "--option2"}, result);
    }

    /**
     * Test flattening of a command line with multiple long options, some of which have arguments.
     */
    @Test
    public void testMultipleLongOptions()
    {
        String[] args = {"--option1=value1", "--option2", "--option3=value3"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1.");
        options.addOption("option2", false, "Option 2.");
        options.addOption("option3", true, "Option 3.");

        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option1", "value1", "--option2", "--option3", "value3"}, result);
    }

    /**
     * Test flattening of a command line with a long option that requires an argument and an option without an argument.
     */
    @Test
    public void testLongOptionWithArgumentAndNoArgumentOption()
    {
        String[] args = {"--option1=value1", "--option2"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1.");
        options.addOption("option2", false, "Option 2.");

        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option1", "value1", "--option2"}, result);
    }

    /**
     * Test parsing of a command line with a non-option followed by a long option that requires an argument.
     */
    @Test
    public void testNonOptionWithLongOptionRequiringArgument()
    {
        String[] args = {"arg1", "--option1=value1"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1.");

        PosixParser parser = new PosixParser();

        try
        {
            String[] result = parser.flatten(options, args, false);
            Assert.fail("Expected ParseException");
        }
        catch (ParseException e)
        {
            // Expected
        }
    }
}