import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParserTest
{
    @Test
    public void testParseWithSimpleOptions() throws Exception
    {
        String[] args = {"-a", "1", "-b", "2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));

        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));

        Assert.assertFalse(cmd.hasOption("c"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws Exception
    {
        String[] args = {"-a"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertFalse(cmd.hasOption("b"));
        Assert.assertNull(cmd.getOptionValue("b"));

        // this should throw an exception
        parser.parse(options, args, false);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingOption() throws Exception
    {
        String[] args = {"-c", "3"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertFalse(cmd.hasOption("c"));
        Assert.assertNull(cmd.getOptionValue("c"));

        // this should throw an exception
        parser.parse(options, args, false);
    }

    @Test
    public void testParseWithOptionGroups() throws Exception
    {
        String[] args = {"--group1", "value1", "--group2", "value2"};
        Options options = new Options();
        OptionGroup group1 = new OptionGroup("group1");
        group1.addOption(new Option("g1", true, "Group 1 option"));
        group1.setRequired(false);

        OptionGroup group2 = new OptionGroup("group2");
        group2.addOption(new Option("g2", true, "Group 2 option"));
        group2.setRequired(false);

        options.addOptionGroup(group1);
        options.addOptionGroup(group2);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertTrue(cmd.hasOption("g1"));
        Assert.assertEquals("value1", cmd.getOptionValue("g1"));

        Assert.assertTrue(cmd.hasOption("g2"));
        Assert.assertEquals("value2", cmd.getOptionValue("g2"));

        Assert.assertFalse(cmd.hasOption("unknown"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws Exception
    {
        String[] args = {"--unknown"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        // this should throw an exception
        parser.parse(options, args, false);
    }

    @Test
    public void testParseWithProperties() throws Exception
    {
        String[] args = {"--property1=value1", "--property2=value2"};
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        Options options = new Options();
        options.addOption("property1", false, "Property 1");
        options.addOption("property2", false, "Property 2");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties, false);

        Assert.assertTrue(cmd.hasOption("property1"));
        Assert.assertEquals("value1", cmd.getOptionValue("property1"));

        Assert.assertTrue(cmd.hasOption("property2"));
        Assert.assertEquals("value2", cmd.getOptionValue("property2"));

        Assert.assertFalse(cmd.hasOption("unknown"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws Exception
    {
        String[] args = {"--", "-x", "value1"};
        Options options = new Options();
        options.addOption("x", true, "Option X");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertFalse(cmd.hasOption("x"));
        Assert.assertNull(cmd.getOptionValue("x"));

        Assert.assertEquals(Arrays.asList("--", "-x", "value1"), Arrays.asList(cmd.getArgs()));
    }
}