package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PosixParser
 */
public class PosixParserTest
{
    /**
     * Test for flatten method with no arguments
     */
    @Test
    public void testFlattenNoArguments()
    {
        String[] args = {};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{}, result);
    }

    /**
     * Test for flatten method with single argument
     */
    @Test
    public void testFlattenSingleArgument()
    {
        String[] args = {"arg1"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"arg1"}, result);
    }

    /**
     * Test for flatten method with option --option
     */
    @Test
    public void testFlattenOption()
    {
        String[] args = {"--option"};
        Options options = new Options();
        options.addOption("option", true, "Option description");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"option"}, result);
    }

    /**
     * Test for flatten method with option --option=value
     */
    @Test
    public void testFlattenOptionWithValue()
    {
        String[] args = {"--option", "value"};
        Options options = new Options();
        options.addOption("option", true, "Option description");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"option", "value"}, result);
    }

    /**
     * Test for flatten method with long option --long-option
     */
    @Test
    public void testFlattenLongOption()
    {
        String[] args = {"--long-option"};
        Options options = new Options();
        options.addOption("longOption", true, "Long option description");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"longOption"}, result);
    }

    /**
     * Test for flatten method with long option --long-option=value
     */
    @Test
    public void testFlattenLongOptionWithValue()
    {
        String[] args = {"--long-option", "value"};
        Options options = new Options();
        options.addOption("longOption", true, "Long option description");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"longOption", "value"}, result);
    }

    /**
     * Test for flatten method with multiple options --option1 --option2
     */
    @Test
    public void testFlattenMultipleOptions()
    {
        String[] args = {"--option1", "--option2"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1 description");
        options.addOption("option2", true, "Option 2 description");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"option1", "option2"}, result);
    }

    /**
     * Test for flatten method with option --option and argument arg
     */
    @Test
    public void testFlattenOptionWithArgument()
    {
        String[] args = {"--option", "arg"};
        Options options = new Options();
        options.addOption("option", true, "Option description");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"option", "arg"}, result);
    }

    /**
     * Test for flatten method with long option --long-option and argument arg
     */
    @Test
    public void testFlattenLongOptionWithArgument()
    {
        String[] args = {"--long-option", "arg"};
        Options options = new Options();
        options.addOption("longOption", true, "Long option description");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"longOption", "arg"}, result);
    }

    /**
     * Test for flatten method with multiple options and arguments
     */
    @Test
    public void testFlattenMultipleOptionsAndArguments()
    {
        String[] args = {"--option1", "arg1", "--option2", "arg2"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1 description");
        options.addOption("option2", true, "Option 2 description");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"option1", "arg1", "option2", "arg2"}, result);
    }

    /**
     * Test for flatten method with stopAtNonOption set to true
     */
    @Test
    public void testFlattenStopAtNonOption()
    {
        String[] args = {"--option", "arg", "nonOption"};
        Options options = new Options();
        options.addOption("option", true, "Option description");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"--option", "arg"}, result);
    }

    /**
     * Test for flatten method with invalid option --invalidOption
     */
    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption()
    {
        String[] args = {"--invalidOption"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    /**
     * Test for flatten method with unsupported token
     */
    @Test(expected = ParseException.class)
    public void testFlattenUnsupportedToken()
    {
        String[] args = {"unsupportedToken"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }
}