package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest
{
    @Test
    public void testSimpleOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("o", "output", true, "Output file path");

        String[] args = {"--output", "output.txt"};
        CommandLine cmd = new DefaultParser().parse(options, args, false);

        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
    }

    @Test
    public void testSimpleRequiredOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("o", "output", true, "Output file path").isRequired();

        String[] args = {"--input", "input.txt"};
        try
        {
            new DefaultParser().parse(options, args, false);
            Assert.fail("An exception should have been thrown due to the missing required option");
        }
        catch (MissingOptionException e)
        {
            Assert.assertEquals("The following required options do not have a value: [output]", e.getMessage());
        }

        args = new String[]{"--input", "input.txt", "--output", "output.txt"};
        CommandLine cmd = new DefaultParser().parse(options, args, false);

        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
    }

    @Test
    public void testSimpleOptionalArg() throws ParseException
    {
        Options options = new Options();
        options.addOption("o", "output", true, "Output file path (default: output.txt)");

        String[] args = {"--input", "input.txt"};
        CommandLine cmd = new DefaultParser().parse(options, args, false);

        Assert.assertNull(cmd.getOptionValue("output"));

        args = new String[]{"--input", "input.txt", "--output", "output.txt"};
        cmd = new DefaultParser().parse(options, args, false);

        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
    }

    @Test
    public void testMultipleOptions() throws ParseException
    {
        Options options = new Options();
        options.addOption("o", "output", true, "Output file path");
        options.addOption("i", "input", true, "Input file path");

        String[] args = {"--output", "output.txt", "--input", "input.txt"};
        CommandLine cmd = new DefaultParser().parse(options, args, false);

        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
    }

    @Test
    public void testMultipleRequiredOptions() throws ParseException
    {
        Options options = new Options();
        options.addOption("o1", "opt1", true, "Required option 1").isRequired();
        options.addOption("o2", "opt2", true, "Required option 2").isRequired();

        try
        {
            new DefaultParser().parse(options, new String[0], false);
            Assert.fail("An exception should have been thrown due to the missing required options");
        }
        catch (MissingOptionException e)
        {
            Assert.assertEquals("The following required options do not have a value: [opt1, opt2]", e.getMessage());
        }

        String[] args = {"--opt1", "opt1.txt", "--opt2", "opt2.txt"};
        CommandLine cmd = new DefaultParser().parse(options, args, false);

        Assert.assertEquals("opt1.txt", cmd.getOptionValue("opt1"));
        Assert.assertEquals("opt2.txt", cmd.getOptionValue("opt2"));
    }

    @Test
    public void testMultipleOptionalArgs() throws ParseException
    {
        Options options = new Options();
        options.addOption("o", "output", true, "Output file path");
        options.addOption("i", "input", true, "Input file path");

        String[] args = {"--input", "input.txt", "--output", "output.txt", "--input", "input2.txt"};
        CommandLine cmd = new DefaultParser().parse(options, args, false);

        Assert.assertEquals(2, cmd.getNumArgs());
        Assert.assertEquals("input.txt", cmd.getArg(0));
        Assert.assertEquals("output.txt", cmd.getArg(1));
    }

    @Test
    public void testUnrecognizedOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("o", "output", true, "Output file path");

        String[] args = {"--unrecognized-option"};
        try
        {
            new DefaultParser().parse(options, args, false);
            Assert.fail("An exception should have been thrown due to the unrecognized option");
        }
        catch (UnrecognizedOptionException e)
        {
            Assert.assertEquals("Unrecognized option: unrecognized-option", e.getMessage());
        }
    }

    @Test
    public void testInvalidBooleanOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("b", "boolean", false, "Boolean option");

        String[] args = {"--boolean", "invalid-value"};
        try
        {
            new DefaultParser().parse(options, args, false);
            Assert.fail("An exception should have been thrown due to the invalid boolean option value");
        }
        catch (ParseException e)
        {
            Assert.assertEquals("Invalid boolean option value: invalid-value", e.getMessage());
        }

        args = new String[]{"--boolean"};
        CommandLine cmd = new DefaultParser().parse(options, args, false);

        Assert.assertFalse(cmd.hasOption("boolean"));

        args = new String[]{"--boolean"};
        cmd = new DefaultParser().parse(options, args, true);

        Assert.assertTrue(cmd.hasOption("boolean"));
    }

    @Test
    public void testPropertyValues() throws ParseException
    {
        Options options = new Options();
        options.addOption("p", "property", true, "Property file path");

        Properties props = new Properties();
        props.setProperty("property", "property-value");

        String[] args = {"--property", "property.txt", "--property-file", "property.properties"};
        CommandLine cmd = new DefaultParser().parse(options, args, false);

        Assert.assertEquals("property-value", cmd.getOptionValue("property"));

        cmd = new DefaultParser().parse(options, args, true);
        Assert.assertEquals("property-value", cmd.getOptionValue("property"));

        args = new String[]{"--property-file", "property.properties"};
        cmd = new DefaultParser().parse(options, args, false);

        Assert.assertNotNull(cmd.getOptions().getOptionGroup("property-group"));
        Assert.assertNull(cmd.getOptionValue("property"));

        cmd = new DefaultParser().parse(options, args, true);
        Assert.assertNotNull(cmd.getOptions().getOptionGroup("property-group"));
        Assert.assertEquals("property-value", cmd.getOptionValue("property"));
    }

    @Test
    public void testInvalidPropertyFile() throws ParseException
    {
        Options options = new Options();
        options.addOption("p", "property", true, "Property file path");

        Properties props = new Properties();
        props.setProperty("property", "property-value");

        String[] args = {"--property-file", "../../property.properties"};
        try
        {
            new DefaultParser().parse(options, args, false);
            Assert.fail("An exception should have been thrown due to the invalid property file path");
        }
        catch (ParseException e)
        {
            Assert.assertTrue(e.getMessage().contains("File ../../property.properties does not exist"));
        }

        args = new String[]{"--property-file", "property.properties"};
        CommandLine cmd = new DefaultParser().parse(options, args, false);

        Assert.assertNotNull(cmd.getOptions().getOptionGroup("property-group"));
        Assert.assertEquals("property-value", cmd.getOptionValue("property"));
    }

    @Test
    public void testOptionGroup() throws ParseException
    {
        Options options = new Options();
        OptionGroup group = new OptionGroup("property-group", "Property group");
        group.setRequired(false);

        Option opt1 = new Option("p1", "property1", true, "Property 1");
        opt1.setLongOpt("long-property1");
        group.addOption(opt1);

        Option opt2 = new Option("p2", "property2", true, "Property 2");
        opt2.setLongOpt("long-property2");
        group.addOption(opt2);

        options.addOptionGroup(group);

        Properties props = new Properties();
        props.setProperty("property1", "property1-value");
        props.setProperty("property2", "property2-value");

        String[] args = {"--property-group", "property.properties"};
        CommandLine cmd = new DefaultParser().parse(options, args, false);

        Assert.assertNotNull(cmd.getOptions().getOptionGroup("property-group"));
        Assert.assertEquals("property1-value", cmd.getOptionValue("property1"));
        Assert.assertEquals("property2-value", cmd.getOptionValue("property2"));

        cmd = new DefaultParser().parse(options, args, true);
        Assert.assertNotNull(cmd.getOptions().getOptionGroup("property-group"));
        Assert.assertEquals("property1-value", cmd.getOptionValue("property1"));
        Assert.assertEquals("property2-value", cmd.getOptionValue("property2"));
    }

    @Test
    public void testMissingOptionGroup() throws ParseException
    {
        Options options = new Options();
        OptionGroup group = new OptionGroup("property-group", "Property group");
        group.setRequired(false);

        Option opt1 = new Option("p1", "property1", true, "Property 1");
        opt1.setLongOpt("long-property1");
        group.addOption(opt1);

        Option opt2 = new Option("p2", "property2", true, "Property 2");
        opt2.setLongOpt("long-property2");
        group.addOption(opt2);

        options.addOptionGroup(group);

        Properties props = new Properties();
        props.setProperty("property1", "property1-value");

        String[] args = {"--property-group", "property.properties"};
        try
        {
            new DefaultParser().parse(options, args, false);
            Assert.fail("An exception should have been thrown due to the missing property group option");
        }
        catch (MissingOptionException e)
        {
            Assert.assertEquals("The following required option groups were not found: [property-group]", e.getMessage());
        }

        args = new String[]{"--property-group", "property1.properties"};
        try
        {
            new DefaultParser().parse(options, args, false);
            Assert.fail("An exception should have been thrown due to the missing property group option");
        }
        catch (MissingOptionException e)
        {
            Assert.assertEquals("The following required option groups were not found: [property-group]", e.getMessage());
        }

        args = new String[]{"--property-group", "property.properties", "--property1", "property1-value"};
        CommandLine cmd = new DefaultParser().parse(options, args, false);

        Assert.assertNotNull(cmd.getOptions().getOptionGroup("property-group"));
        Assert.assertEquals("property1-value", cmd.getOptionValue("property1"));
    }

    @Test
    public void testOptionGroupWithRequiredOptions() throws ParseException
    {
        Options options = new Options();
        OptionGroup group = new OptionGroup("property-group", "Property group");
        group.setRequired(true);

        Option opt1 = new Option("p1", "property1", true, "Property 1");
        opt1.setLongOpt("long-property1");
        group.addOption(opt1);

        Option opt2 = new Option("p2", "property2", true, "Property 2");
        opt2.setLongOpt("long-property2");
        group.addOption(opt2);

        options.addOptionGroup(group);

        Properties props = new Properties();
        props.setProperty("property1", "property1-value");
        props.setProperty("property2", "property2-value");

        String[] args = {"--property-group", "property.properties"};
        try
        {
            new DefaultParser().parse(options, args, false);
            Assert.fail("An exception should have been thrown due to the missing required option group");
        }
        catch (MissingOptionException e)
        {
            Assert.assertEquals("The following required option groups were not found: [property-group]", e.getMessage());
        }

        args = new String[]{"--property-group", "property1.properties"};
        try
        {
            new DefaultParser().parse(options, args, false);
            Assert.fail("An exception should have been thrown due to the missing required option 'property2'");
        }
        catch (MissingArgumentException e)
        {
            Assert.assertEquals("The following required arguments are missing: [property2]", e.getMessage());
        }

        args = new String[]{"--property-group", "property1.properties", "--property2", "property2-value"};
        CommandLine cmd = new DefaultParser().parse(options, args, false);

        Assert.assertNotNull(cmd.getOptions().getOptionGroup("property-group"));
        Assert.assertEquals("property1-value", cmd.getOptionValue("property1"));
        Assert.assertEquals("property2-value", cmd.getOptionValue("property2"));
    }
}