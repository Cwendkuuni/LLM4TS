package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest
{
    @Test
    public void testParseWithNoOptions() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = new String[0];

        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertNull(parser.getOptions());
    }

    @Test
    public void testParseWithSingleRequiredOption() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addRequiredOption("a", "optionA", true, "An option A");
        String[] arguments = new String[1];
        arguments[0] = "--optionA";

        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals("An option A", cmd.getOptionValue("a"));
        Assert.assertNotNull(parser.getOptions());
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addRequiredOption("a", "optionA", true, "An option A");
        String[] arguments = new String[0];

        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithSingleOptionalOption() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", "optionA", true, "An option A");
        String[] arguments = new String[1];
        arguments[0] = "--optionA=value";

        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals("value", cmd.getOptionValue("a"));
        Assert.assertNotNull(parser.getOptions());
    }

    @Test
    public void testParseWithMultipleOptionalOptions() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", "optionA", true, "An option A");
        options.addOption("b", "optionB", true, "An option B");
        String[] arguments = new String[2];
        arguments[0] = "--optionA=valueA";
        arguments[1] = "--optionB=valueB";

        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals("valueA", cmd.getOptionValue("a"));
        Assert.assertEquals("valueB", cmd.getOptionValue("b"));
        Assert.assertNotNull(parser.getOptions());
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingOptionalArg() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", "optionA", true, "An option A");
        String[] arguments = new String[1];
        arguments[0] = "--optionA";

        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithSingleValueOption() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", "optionA", true, "An option A");
        String[] arguments = new String[1];
        arguments[0] = "--optionA=value";

        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals("value", cmd.getOptionValue("a"));
        Assert.assertNull(parser.getOptions());
    }

    @Test
    public void testParseWithMultiValueOption() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", "optionA", true, "An option A");
        String[] arguments = new String[2];
        arguments[0] = "--optionA=value1";
        arguments[1] = "--optionA=value2";

        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertArrayEquals(new String[]{"value1", "value2"}, cmd.getOptionValues("a"));
        Assert.assertNull(parser.getOptions());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = new String[1];
        arguments[0] = "--unrecognizedOption";

        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithLongOption() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("longOption", "longOption", true, "A long option");
        String[] arguments = new String[1];
        arguments[0] = "--longOption=value";

        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals("value", cmd.getOptionValue("longOption"));
        Assert.assertNull(parser.getOptions());
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidLongOption() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = new String[1];
        arguments[0] = "--invalidLongOption";

        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithMultipleOptions() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", "optionA", true, "An option A");
        options.addOption("b", "optionB", true, "An option B");
        String[] arguments = new String[3];
        arguments[0] = "--optionA=valueA";
        arguments[1] = "--optionB=valueB";
        arguments[2] = "--optionA";

        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals("valueA", cmd.getOptionValue("a"));
        Assert.assertEquals("valueB", cmd.getOptionValue("b"));
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("", cmd.getArgs().get(0));
        Assert.assertNull(parser.getOptions());
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Properties properties = new Properties();
        properties.setProperty("optionA", "valueA");

        CommandLine cmd = parser.parse(options, null, properties, false);

        Assert.assertEquals("valueA", cmd.getOptionValue("a"));
        Assert.assertNull(parser.getOptions());
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addRequiredOption("a", "optionA", true, "An option A");
        String[] arguments = new String[2];
        arguments[0] = "--optionA";
        arguments[1] = "--nonOption";

        try
        {
            parser.parse(options, arguments, true);
        }
        catch (ParseException e)
        {
            Assert.assertEquals("Unrecognized option: nonOption", e.getMessage());
        }

        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("--nonOption", cmd.getArgs().get(0));
        Assert.assertNull(parser.getOptions());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullArguments()
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = null;

        try
        {
            parser.parse(options, arguments);
        }
        catch (ParseException e)
        {
            Assert.fail("ParseException expected, but got " + e.getClass().getName());
        }
        catch (IllegalArgumentException ex)
        {
            // expected
        }

        Assert.assertNull(parser.getOptions());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithEmptyArguments()
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = new String[0];

        try
        {
            parser.parse(options, arguments);
        }
        catch (ParseException e)
        {
            Assert.fail("ParseException expected, but got " + e.getClass().getName());
        }
        catch (IllegalArgumentException ex)
        {
            // expected
        }

        Assert.assertNull(parser.getOptions());
    }

    @Test(expected = ParseException.class)
    public void testParseWithEmptyOptions()
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = new String[0];

        try
        {
            parser.parse(options, arguments);
        }
        catch (IllegalArgumentException e)
        {
            Assert.fail("IllegalArgumentException expected, but got " + e.getClass().getName());
        }
        catch (ParseException ex)
        {
            // expected
        }

        Assert.assertNull(parser.getOptions());
    }

    @Test
    public void testParseWithMultipleValuesForSingleValueOption() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", "optionA", true, "An option A");
        String[] arguments = new String[2];
        arguments[0] = "--optionA=value1";
        arguments[1] = "--optionA=value2";

        try
        {
            parser.parse(options, arguments);
            Assert.fail("ParseException expected, but no exception was thrown.");
        }
        catch (ParseException ex)
        {
            Assert.assertEquals("Can not parse value for option: optionA. Multiple values provided for a single-valued option.", ex.getMessage());
        }

        Assert.assertNull(parser.getOptions());
    }
}