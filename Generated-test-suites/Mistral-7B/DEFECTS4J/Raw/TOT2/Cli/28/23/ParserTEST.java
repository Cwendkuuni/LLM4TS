import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest
{
    @Test
    public void testParserWithEmptyArguments() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();

        String[] arguments = new String[0];

        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParserWithValidArguments() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        String[] arguments = {"-a", "--b=2"};

        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals("Option A is set to true", true, cmd.hasOption("a"));
        Assert.assertEquals("Option B is set to 2", 2, cmd.getOptionValue("b").length());
        Assert.assertEquals("Command line arguments are empty", new String[0], cmd.getArgs().toArray(new String[0]));
    }

    @Test(expected = ParseException.class)
    public void testParserWithInvalidArgument() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] arguments = {"--invalid"};

        parser.parse(options, arguments, true);
    }

    @Test(expected = ParseException.class)
    public void testParserWithMissingRequiredOption() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addRequiredOption("a", true, "Option A");

        String[] arguments = new String[0];

        try
        {
            parser.parse(options, arguments);
        }
        catch (MissingOptionException e)
        {
            Assert.assertEquals("Missing required option: a", e.getMessage());
            throw e;
        }
    }

    @Test(expected = ParseException.class)
    public void testParserWithUnmatchedOption() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        String[] arguments = {"--a", "hello", "--b=2", "--c"};

        try
        {
            parser.parse(options, arguments, true);
        }
        catch (ParseException e)
        {
            Assert.assertEquals("Unrecognized option: c", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testParserWithProperties() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        Properties properties = new Properties();
        properties.put("a", "valueA");
        properties.put("b", "valueB");

        String[] arguments = {};

        CommandLine cmd = parser.parse(options, arguments, properties, true);

        Assert.assertEquals("Option A is set to true and has the value 'valueA'", true, cmd.hasOption("a"));
        Assert.assertEquals("Option B is set to 1 and has no value", 1, cmd.getOptionValue("b").length());
        Assert.assertEquals("Command line arguments are empty", new String[0], cmd.getArgs().toArray(new String[0]));
    }

    @Test
    public void testParserWithMixedArgumentsAndOptions() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        String[] arguments = {"--a", "arg1", "-b=2", "arg2"};

        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals("Option A is set to true", true, cmd.hasOption("a"));
        Assert.assertEquals("Option B is set to 2", 2, cmd.getOptionValue("b").length());
        Assert.assertEquals("Command line arguments contain arg1 and arg2", Arrays.asList("arg1", "arg2"), Arrays.asList(cmd.getArgs()));
    }

    @Test
    public void testParserWithGroupOptions() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();

        OptionGroup group = new OptionGroup("Group", "Options in this group");
        group.setRequired(true);

        options.addOptionGroup(group);
        group.addOption(new Option("a", true, "Option A in Group"));
        group.addOption(new Option("b", true, "Option B in Group"));

        String[] arguments = {"--group", "--a", "--b"};

        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertTrue(group.isSelected());
    }
}