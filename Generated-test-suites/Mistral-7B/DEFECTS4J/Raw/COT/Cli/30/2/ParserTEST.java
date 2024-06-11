import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParserTest
{
    private Parser parser;
    private Options options;

    @Before
    public void setUp()
    {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOptionGroup(new OptionGroup("group1", "Group 1"));
        options.addOptionGroup(new OptionGroup("group2", "Group 2"));
        options.addOption("c", 3, "Option C", "group1", "Optional Option C");
        parser = new DefaultParser();
        ((DefaultParser) parser).setOptions(options);
    }

    @Test
    public void testParseWithArguments() throws ParseException
    {
        String[] arguments = new String[]{"--a", "argA", "--b=valueB", "--c=valueC1", "--c=valueC2"};
        CommandLine commandLine = parser.parse(options, arguments);

        Assert.assertEquals(3, commandLine.getArgs().size());
        Assert.assertEquals("argA", commandLine.getOptionValue("a"));
        Assert.assertEquals("valueB", commandLine.getOptionValue("b"));
        Assert.assertEquals("valueC1", commandLine.getOptionValue("c"));
        Assert.assertEquals("valueC2", commandLine.getOptionValue("c"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingOption() throws ParseException
    {
        String[] arguments = new String[]{"--x"};
        parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException
    {
        String[] arguments = new String[]{"--a"};
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithMissingGroupOption() throws ParseException
    {
        String[] arguments = new String[]{"--group1=value"};
        CommandLine commandLine = parser.parse(options, arguments);

        Assert.assertNull(commandLine.getOptionValue("group1"));
    }

    @Test
    public void testParseWithMissingOptionGroup() throws ParseException
    {
        String[] arguments = new String[]{"--group3=value"};
        CommandLine commandLine = parser.parse(options, arguments);

        Assert.assertEquals(0, commandLine.getOptions().size());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException
    {
        String[] arguments = new String[]{"--x"};
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        Properties properties = new Properties();
        properties.setProperty("a", "valueA");
        properties.setProperty("c", "valueC");

        CommandLine commandLine = parser.parse(options, new String[0], properties, true);

        Assert.assertEquals(2, commandLine.getOptions().size());
        Assert.assertEquals("valueA", commandLine.getOptionValue("a"));
        Assert.assertEquals("valueC", commandLine.getOptionValue("c"));
    }
}