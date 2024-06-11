package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest
{
    @Test
    public void testSimpleOption() throws ParseException
    {
        String[] args = new String[]{"app.jar", "--output", "output"};

        Options options = new Options();
        options.addOption("o", "output", true, "output directory");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args, null);

        Assert.assertEquals("output", cmd.getOptionValue("output"));
    }

    @Test
    public void testRequiredOption() throws ParseException
    {
        String[] args = new String[]{"app.jar", "--input", "input.txt"};

        Options options = new Options();
        options.addRequiredOption("i", "input", true, "input file");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        try
        {
            parser.parse(options, args, null);
            Assert.fail("Expected ParseException for missing required option");
        }
        catch (ParseException e)
        {
            Assert.assertEquals("The following required options do not have a value: [input]", e.getMessage());
        }

        String[] argsWithInput = new String[]{"app.jar", "--input", "input.txt"};
        CommandLine cmd = parser.parse(options, argsWithInput, null);

        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
    }

    @Test
    public void testMultipleOptions() throws ParseException
    {
        String[] args = new String[]{"app.jar", "--output", "output", "--input", "input.txt"};

        Options options = new Options();
        options.addOption("o", "output", true, "output directory");
        options.addOption("i", "input", true, "input file");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args, null);

        Assert.assertEquals("output", cmd.getOptionValue("output"));
        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() throws ParseException
    {
        String[] args = new String[]{"app.jar", "--invalid"};

        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(options, args, null);

        Assert.fail("Expected ParseException for invalid option");
    }

    @Test(expected = ParseException.class)
    public void testMissingOptionValue() throws ParseException
    {
        String[] args = new String[]{"app.jar", "--output"};

        Options options = new Options();
        options.addOption("o", "output", true, "output directory");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        try
        {
            parser.parse(options, args, null);
            Assert.fail("Expected ParseException for missing option value");
        }
        catch (ParseException e)
        {
            Assert.assertEquals("The following required options do not have a value: [output]", e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testUnrecognizedOption() throws ParseException
    {
        String[] args = new String[]{"app.jar", "--unknown"};

        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        try
        {
            parser.parse(options, args, null);
            Assert.fail("Expected ParseException for unrecognized option");
        }
        catch (ParseException e)
        {
            Assert.assertEquals("Unrecognized option: unknown", e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredGroup() throws ParseException
    {
        String[] args = new String[]{"app.jar", "--group"};

        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group");
        options.addOptionGroup(group);
        group.addOption(new Option("g", "group", true, "Group option"));

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        try
        {
            parser.parse(options, args, null);
            Assert.fail("Expected ParseException for missing required group");
        }
        catch (ParseException e)
        {
            Assert.assertEquals("The following required option groups did not have a value: [group]", e.getMessage());
        }
    }

    @Test
    public void testProperties() throws ParseException
    {
        String[] args = new String[]{"app.jar", "--property1=value1", "--property2=value2"};

        Options options = new Options();
        OptionPropertyConfigurer configurer = new OptionPropertyConfigurer();
        configurer.configure(options);

        options.addOption("p1", true, "property1", "Property 1 value");
        options.addOption("p2", true, "property2", "Property 2 value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args, null);

        Assert.assertEquals("value1", cmd.getOptionValue("property1"));
        Assert.assertEquals("value2", cmd.getOptionValue("property2"));
    }

    @Test(expected = ParseException.class)
    public void testInvalidProperty() throws ParseException
    {
        String[] args = new String[]{"app.jar", "--property=value"};

        Options options = new Options();
        OptionPropertyConfigurer configurer = new OptionPropertyConfigurer();
        configurer.configure(options);

        options.addOption("p", true, "property", "Property value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        try
        {
            parser.parse(options, args, null);
            Assert.fail("Expected ParseException for invalid property");
        }
        catch (ParseException e)
        {
            Assert.assertEquals("Invalid property: property", e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testMissingProperty() throws ParseException
    {
        String[] args = new String[]{"app.jar", "--property"};

        Options options = new Options();
        OptionPropertyConfigurer configurer = new OptionPropertyConfigurer();
        configurer.configure(options);

        options.addOption("p", true, "property", "Property value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        try
        {
            parser.parse(options, args, null);
            Assert.fail("Expected ParseException for missing property");
        }
        catch (ParseException e)
        {
            Assert.assertEquals("The following required properties did not have a value: [property]", e.getMessage());
        }
    }
}