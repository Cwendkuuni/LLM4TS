import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for Parser.java
 */
public class ParserTest
{
    @Test
    public void testParseNoOptions() throws ParseException
    {
        String[] arguments = new String[0];
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertFalse(cmd.hasOptions());
    }

    @Test
    public void testParseSimpleOptions() throws ParseException
    {
        String[] arguments = new String[]{"-a", "1", "--b", "2"};
        Options options = new Options();
        Option optionA = new Option("a", "a", false, "Print A");
        Option optionB = new Option("b", "b", false, "Print B");
        options.addOption(optionA);
        options.addOption(optionB);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, arguments);
        List<String> args = cmd.getArgs();
        Assert.assertEquals(0, args.size());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() throws ParseException
    {
        String[] arguments = new String[]{"--c", "3"};
        Options options = new Options();
        Option optionA = new Option("a", "a", false, "Print A");
        Option optionB = new Option("b", "b", false, "Print B");
        options.addOption(optionA);
        options.addOption(optionB);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        parser.parse(options, arguments);
        Assert.fail("MissingOptionException expected");
    }

    @Test
    public void testParseRequiredOptions() throws ParseException
    {
        String[] arguments = new String[]{"--a", "1"};
        Options options = new Options();
        Option optionA = new Option("a", "a", true, "Print A");
        Option optionB = new Option("b", "b", false, "Print B");
        options.addOption(optionA);
        options.addOption(optionB);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertFalse(cmd.hasOption("b"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOptions() throws ParseException, MissingOptionException
    {
        String[] arguments = new String[0];
        Options options = new Options();
        Option optionA = new Option("a", "a", true, "Print A");
        Option optionB = new Option("b", "b", false, "Print B");
        options.addOption(optionA);
        options.addOption(optionB);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, arguments);
        Assert.fail("MissingOptionException expected");
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        String[] arguments = new String[]{"--property1=value1", "--property2=value2"};
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        Options options = new Options();
        Option option1 = new Option("p", "property", true, "property");
        options.addOption(option1);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, arguments, properties, false);
        List<String> args = cmd.getArgs();
        Assert.assertEquals(0, args.size());
        Assert.assertTrue(cmd.hasOption("property"));
        Assert.assertEquals("value1", cmd.getOptionValue("property1"));
        Assert.assertEquals("value2", cmd.getOptionValue("property2"));
    }

    @Test
    public void testParseWithGroups() throws ParseException
    {
        String[] arguments = new String[]{"--group1", "--option1", "arg1", "--group2", "--option2", "arg2"};
        Options options = new Options();

        OptionGroup group1 = new OptionGroup("group1", "group1");
        Option option1 = new Option("o", "option", true, "option");
        group1.addOption(option1);
        options.addOptionGroup(group1);

        OptionGroup group2 = new OptionGroup("group2", "group2");
        Option option2 = new Option("o", "option", true, "option");
        group2.addOption(option2);
        options.addOptionGroup(group2);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, arguments, null, false);
        List<String> args = cmd.getArgs();
        Assert.assertEquals(2, args.size());
        Assert.assertEquals("arg1", args.get(0));
        Assert.assertEquals("arg2", args.get(1));
        Assert.assertTrue(group1.isSelected());
        Assert.assertTrue(group2.isSelected());
        Assert.assertTrue(cmd.hasOption("option"));
        Assert.assertEquals("arg1", cmd.getOptionValue("option", group1));
        Assert.assertEquals("arg2", cmd.getOptionValue("option", group2));
    }

    @Test(expected = ParseException.class)
    public void testParseWithStopAtNonOption() throws ParseException
    {
        String[] arguments = new String[]{"--group1", "--option1", "arg1", "--unknown", "--group2", "--option2", "arg2"};
        Options options = new Options();

        OptionGroup group1 = new OptionGroup("group1", "group1");
        Option option1 = new Option("o", "option", true, "option");
        group1.addOption(option1);
        options.addOptionGroup(group1);

        OptionGroup group2 = new OptionGroup("group2", "group2");
        Option option2 = new Option("o", "option", true, "option");
        group2.addOption(option2);
        options.addOptionGroup(group2);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        parser.parse(options, arguments, null, true);
        Assert.fail("ParseException expected");
    }
}