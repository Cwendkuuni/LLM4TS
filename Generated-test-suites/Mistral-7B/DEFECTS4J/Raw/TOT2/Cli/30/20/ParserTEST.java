package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Test class for Parser.
 */
public class ParserTest
{
    private CommandLineParser parser;
    private Options options;

    @Before
    public void setup()
    {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");
        options.addOptionGroup(new OptionGroup("group", "Group"));
        options.addOption("c", true, "Option C", true, "valueC");
        options.addOption("d", false, "Option D");
        options.addOption("e", "1,2,3,4", "Option E");
    }

    @Test
    public void testParse_Basic() throws ParseException
    {
        parser = new DefaultParser();
        String[] arguments = {"--a", "--b", "--c=valueC"};
        CommandLine cmd = parser.parse(options, arguments, false);

        Assert.assertEquals(Arrays.asList("--a", "--b", "--c=valueC"), cmd.getArgs());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertTrue(cmd.hasOption("c"));
        Assert.assertEquals("valueC", cmd.getOptionValue("c"));
        Assert.assertFalse(cmd.hasOption("d"));
    }

    @Test(expected = ParseException.class)
    public void testParse_MissingRequired() throws ParseException
    {
        parser = new DefaultParser();
        String[] arguments = {"--a"};
        parser.parse(options, arguments, false);
        Assert.fail();
    }

    @Test
    public void testParse_MissingOption() throws ParseException
    {
        parser = new DefaultParser();
        String[] arguments = {"--x"};
        CommandLine cmd = parser.parse(options, arguments, false);

        Assert.assertFalse(cmd.hasOption("x"));
        Assert.assertFalse(cmd.hasOption("a"));
        Assert.assertFalse(cmd.hasOption("b"));
        Assert.assertFalse(cmd.hasOption("c"));
        Assert.assertFalse(cmd.hasOption("d"));
        Assert.assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParse_WithProperties() throws ParseException
    {
        parser = new DefaultParser();
        String[] arguments = {"--a", "--b", "--c=valueC"};
        Properties properties = new Properties();
        properties.setProperty("a", "valueA");
        CommandLine cmd = parser.parse(options, arguments, properties, false);

        Assert.assertEquals(Arrays.asList("--a", "--b", "--c=valueC"), cmd.getArgs());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertTrue(cmd.hasOption("c"));
        Assert.assertEquals("valueC", cmd.getOptionValue("c"));
        Assert.assertEquals("valueA", cmd.getOptionValue("a"));
        Assert.assertFalse(cmd.hasOption("d"));
        Assert.assertTrue(cmd.getOptions().getOptionGroup("group").isSelected());
    }

    @Test(expected = ParseException.class)
    public void testParse_WithMissingRequired() throws ParseException
    {
        parser = new DefaultParser();
        String[] arguments = {"--a"};
        Properties properties = new Properties();
        properties.setProperty("a", "valueA");
        CommandLine cmd = parser.parse(options, arguments, properties, false);
        Assert.fail();
    }

    @Test
    public void testParse_WithExtraArguments() throws ParseException
    {
        parser = new DefaultParser();
        String[] arguments = {"--a", "--b", "--c=valueC", "arg1", "arg2"};
        CommandLine cmd = parser.parse(options, arguments, false);

        Assert.assertEquals(Arrays.asList("--a", "--b", "--c=valueC"), cmd.getArgs().subList(0, 3));
        Assert.assertEquals(new ArrayList<String>(Arrays.asList("arg1", "arg2")), cmd.getArgs().subList(3, cmd.getArgs().size()));
    }

    @Test
    public void testParse_WithGroupOption() throws ParseException
    {
        parser = new DefaultParser();
        String[] arguments = {"--group", "--c=valueC"};
        CommandLine cmd = parser.parse(options, arguments, false);

        Assert.assertTrue(cmd.getOptions().getOptionGroup("group").isSelected());
        Assert.assertTrue(cmd.hasOption("c"));
        Assert.assertEquals("valueC", cmd.getOptionValue("c"));
        Assert.assertFalse(cmd.hasOption("a"));
        Assert.assertFalse(cmd.hasOption("b"));
        Assert.assertFalse(cmd.hasOption("d"));
        Assert.assertTrue(cmd.getArgs().isEmpty());
    }

    @Test(expected = ParseException.class)
    public void testParse_WithInvalidOption() throws ParseException
    {
        parser = new DefaultParser();
        String[] arguments = {"--invalid"};
        CommandLine cmd = parser.parse(options, arguments, false);
        Assert.fail();
    }

    @Test(expected = ParseException.class)
    public void testParse_WithInvalidArgument() throws ParseException
    {
        parser = new DefaultParser();
        String[] arguments = {"--a=invalid"};
        CommandLine cmd = parser.parse(options, arguments, false);
        Assert.fail();
    }

    @Test(expected = ParseException.class)
    public void testParse_WithMismatchedArguments() throws ParseException
    {
        parser = new DefaultParser();
        String[] arguments = {"--a", "--b=valueB", "--c"};
        CommandLine cmd = parser.parse(options, arguments, false);
        Assert.fail();
    }

    @Test
    public void testFlatten() throws ParseException
    {
        parser = new DefaultParser();
        String[] arguments = {"--a", "--b=valueB", "--c=valueC", "--d", "--e=1,2,3,4", "arg1", "arg2"};
        List<String> flattenedArguments = Arrays.asList(parser.parse(options, arguments, false).getArgs());

        List<String> expectedArguments = Arrays.asList(
                "--a",
                "--b=valueB",
                "--c=valueC",
                "--d",
                "--e",
                "1",
                "2",
                "3",
                "4",
                "arg1",
                "arg2"
        );

        Assert.assertEquals(expectedArguments, flattenedArguments);
    }
}