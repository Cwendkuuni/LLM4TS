import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParserTest
{
    private CommandLineParser parser;
    private Options options;

    @Before
    public void setUp()
    {
        options = new Options();
        options.addRequiredOption("i", "input", true, "input file");
        options.addOption("o", "output", true, "output file");
        options.addOption("h", "help", false, "print help");
        parser = new DefaultParser();
    }

    @Test
    public void testParseCorrectUsage() throws ParseException
    {
        String[] args = new String[]{"-i", "input.txt", "-o", "output.txt"};
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertNull(cmd.getArgs());
        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
        Assert.assertFalse(cmd.hasOption("help"));
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException
    {
        String[] args = new String[]{"-o", "output.txt"};
        parser.parse(options, args, true);
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() throws ParseException
    {
        String[] args = new String[]{"-x", "output.txt"};
        parser.parse(options, args, true);
    }

    @Test
    public void testHelpOption() throws ParseException
    {
        String[] args = new String[]{"-h"};
        CommandLine cmd = parser.parse(options, args, true);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("ParserTest", options);
    }

    @Test
    public void testExtraArguments() throws ParseException
    {
        String[] args = new String[]{"-i", "input.txt", "-o", "output.txt", "extra1", "extra2"};
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertArrayEquals(new String[]{"extra1", "extra2"}, cmd.getArgs());
    }

    @Test
    public void testMissingValueOption() throws ParseException
    {
        String[] args = new String[]{"-i"};
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertNull(cmd.getOptionValue("input"));
    }

    @Test
    public void testValueOptionWithValue() throws ParseException
    {
        String[] args = new String[]{"-i", "input.txt", "-o", "output.txt", "--", "value"};
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertEquals("value", cmd.getOptionValue("value", "defaultValue"));
    }

    @Test
    public void testValueOptionWithArrayValue() throws ParseException
    {
        options.addOption("a", "array", true, "array value", true);

        String[] args = new String[]{"-a", "value1", "-a", "value2", "-a", "value3"};
        CommandLine cmd = parser.parse(options, args, true);

        String[] array = cmd.getOptionValues("array");
        Assert.assertArrayEquals(new String[]{"value1", "value2", "value3"}, array);
    }

    @Test
    public void testValueOptionWithMultipleValues() throws ParseException
    {
        options.addOption("a", "array", true, "array value");

        String[] args = new String[]{"-a", "value1", "-a", "value2", "-a", "value3"};
        CommandLine cmd = parser.parse(options, args, true);

        String[] array = cmd.getOptionValues("array");
        Assert.assertArrayEquals(new String[]{"value1", "value2", "value3"}, array);
    }

    @Test
    public void testGroupOption() throws ParseException
    {
        OptionGroup group = new OptionGroup("g", "group");
        group.setDescription("This is a group");

        options.addOptionGroup(group);

        group.addOption("i", "input", true, "input file for the group");
        group.addRequiredOption("o", "output", true, "output file for the group");

        String[] args = new String[]{"-g", "--input", "input.txt", "--output", "output.txt"};
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertNull(cmd.getArgs());
        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));

        Assert.assertEquals("This is a group", cmd.getOptionValue("g"));
    }

    @Test(expected = ParseException.class)
    public void testMissingGroupOption() throws ParseException
    {
        String[] args = new String[]{"--output", "output.txt"};
        parser.parse(options, args, true);
    }

    @Test(expected = ParseException.class)
    public void testMissingGroupOptionValue() throws ParseException
    {
        String[] args = new String[]{"--g", "--input", "input.txt"};
        parser.parse(options, args, true);
    }
}