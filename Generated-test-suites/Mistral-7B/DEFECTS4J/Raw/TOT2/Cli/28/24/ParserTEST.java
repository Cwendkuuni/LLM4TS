import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest
{
    private Options options;
    private Parser parser;

    @Before
    public void setUp()
    {
        options = new Options();
        parser = new DefaultParser();
    }

    @Test
    public void testParseWithSimpleArguments() throws ParseException
    {
        options.addOption("a", true, "A");
        options.addOption("b", true, "B");
        options.addOption("c", 3, "C");

        String[] arguments = {"-a", "1", "-b", "2", "--c", "3"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertEquals("3", cmd.getOptionValue("c"));
        Assert.assertNull(cmd.getOptionValue("d"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException
    {
        options.addOption("a", true, "A");
        options.addOption("b", true, "B");

        String[] arguments = {"-b"};
        parser.parse(options, arguments);

        // This statement should not be reached if the test case is correct
        Assert.fail("Missing required option A");
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws ParseException
    {
        options.addOption("a", true, "A");
        options.addOption("b", 2, "B");

        String[] arguments = {"-a"};
        parser.parse(options, arguments);

        // This statement should not be reached if the test case is correct
        Assert.fail("Missing argument for option B");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException
    {
        options.addOption("a", true, "A");

        String[] arguments = {"-d"};
        parser.parse(options, arguments);

        // This statement should not be reached if the test case is correct
        Assert.fail("Unrecognized option: d");
    }

    @Test
    public void testParseWithLongOptions() throws ParseException
    {
        options.addOption("long-option-a", true, "A");
        options.addOption("long-option-b", 2, "B");

        String[] arguments = {"--long-option-a", "1", "--long-option-b", "2"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals("1", cmd.getOptionValue("long-option-a"));
        Assert.assertEquals("2", cmd.getOptionValue("long-option-b"));
        Assert.assertNull(cmd.getOptionValue("a"));
        Assert.assertNull(cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        options.addOption("a", true, "A");
        options.addOption("b", 2, "B");

        Properties properties = new Properties();
        properties.setProperty("a", "1");
        properties.setProperty("b", "2");

        CommandLine cmd = parser.parse(options, null, properties);

        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertNull(cmd.getOptionValue("d"));
    }

    @Test
    public void testParseWithGroup() throws ParseException
    {
        OptionGroup group = new OptionGroup("group");
        group.setRequired(false);

        options.addOptionGroup(group);
        group.addOption("a", true, "A");
        group.addOption("b", true, "B");

        String[] arguments = {"--group", "-a"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals(true, cmd.hasOption("b"));
        Assert.assertFalse(group.isSelected());
    }

    @Test
    public void testParseWithGroupSelected() throws ParseException
    {
        OptionGroup group = new OptionGroup("group");
        group.setRequired(false);

        options.addOptionGroup(group);
        group.addOption("a", true, "A");
        group.addOption("b", true, "B");

        String[] arguments = {"--group", "--group", "-a"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals(true, cmd.hasOption("b"));
        Assert.assertTrue(group.isSelected());
    }
}