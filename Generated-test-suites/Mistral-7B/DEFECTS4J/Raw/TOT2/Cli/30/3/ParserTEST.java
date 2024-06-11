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

public class ParserTest
{
    private CommandLineParser parser;
    private Options options;

    @Before
    public void setUp()
    {
        options = new Options();
        options.addOption("a", "alpha", true, "an option with a long description");
        options.addOption("b", "beta", false, "an option that doesn't take an argument");
        options.addOption("c", "char", "c", "a character option that takes an argument");
        options.addOption("d", "debug", false, "an option that doesn't take an argument but can be set multiple times");
        options.addOptionGroup(new OptionGroup("group1", "Group 1"));
        options.addOptionGroup(new OptionGroup("group2", "Group 2"));
        options.addOption("e", "error", true, "an option that should throw an error");

        parser = new DefaultParser();
    }

    @Test
    public void testParseWithValidArguments() throws ParseException
    {
        String[] args = {"-a", "valueA", "-b", "--group1", "-c", "valueC"};
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals("-a", cmd.getOptionValue("a"));
        Assert.assertNull(cmd.getOptionValue("b"));
        Assert.assertNull(cmd.getOptionValue("d"));
        Assert.assertEquals("valueC", cmd.getOptionValue("c"));
        Assert.assertTrue(options.isSetGroup("group1"));
        Assert.assertFalse(options.isSetGroup("group2"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidArgument() throws ParseException
    {
        String[] args = {"--invalid"};
        parser.parse(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingOption() throws ParseException
    {
        String[] args = {"-a", "valueA"};
        parser.parse(options, args, false);

        // since the 'a' option is set to be required, this test should fail
        Assert.fail("Missing option: b");
    }

    @Test
    public void testParseWithMultipleDoubledashes() throws ParseException
    {
        String[] args = {"--", "--group1", "--group2"};
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertTrue(options.isSetGroup("group1"));
        Assert.assertTrue(options.isSetGroup("group2"));
    }

    @Test
    public void testParseWithMissingGroup() throws ParseException
    {
        String[] args = {"--group3"};
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertFalse(options.isSetGroup("group3"));
    }

    @Test
    public void testParseWithMultipleDoubledashAndGroup() throws ParseException
    {
        String[] args = {"--", "--group1", "--group2--"};
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertTrue(options.isSetGroup("group1"));
        Assert.assertTrue(options.isSetGroup("group2"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException
    {
        String[] args = {"-c"};
        parser.parse(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidCharacterArgument() throws ParseException
    {
        String[] args = {"-c", "invalid"};
        parser.parse(options, args, false);
    }

    @Test
    public void testParseWithValidCharacterArgument() throws ParseException
    {
        String[] args = {"-c", "a"};
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals("a", cmd.getOptionValue("c"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredOption() throws ParseException
    {
        String[] args = {"--"};
        parser.parse(options, args, false);

        // since there are required options, this test should fail
        Assert.fail("Missing required options: [a, group1, group2]");
    }

    @Test
    public void testParseWithMultipleRequiredOptions() throws ParseException
    {
        String[] args = {"-a", "-b"};
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals("-a", cmd.getOptionValue("a"));
        Assert.assertEquals("-b", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithMultipleRequiredOptionsAndGroup() throws ParseException
    {
        String[] args = {"-a", "-b", "--group1"};
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals("-a", cmd.getOptionValue("a"));
        Assert.assertEquals("-b", cmd.getOptionValue("b"));
        Assert.assertTrue(options.isSetGroup("group1"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredGroup() throws ParseException
    {
        String[] args = {"-a", "-b"};
        parser.parse(options, args, false);

        // since there is a required group, this test should fail
        Assert.fail("Missing required option group: group1");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException
    {
        String[] args = {"-x", "--"};
        parser.parse(options, args, true);

        // since there is no option 'x', this test should fail
        Assert.fail("Unrecognized option: x");
    }
}