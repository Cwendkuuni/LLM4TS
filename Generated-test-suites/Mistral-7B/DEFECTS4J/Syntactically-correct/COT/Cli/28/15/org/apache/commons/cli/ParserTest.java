import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParserTest
{
    private Parser parser;
    private Options options;

    @Before
    public void setUp()
    {
        options = new Options();
        parser = new DefaultParser();
        parser.setOptions(options);
    }

    @Test
    public void testParse_SimpleUsage() throws ParseException
    {
        String[] arguments = {"--option1", "value1", "--option2", "value2"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertEquals("value1", cmd.getOptionValue("option1"));
        Assert.assertEquals("value2", cmd.getOptionValue("option2"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_MissingRequiredOption() throws ParseException
    {
        String[] arguments = {"--option1", "--option3"};
        options.addRequiredOption("option2");

        parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse_MissingRequiredArgument() throws ParseException
    {
        Option option = new Option("option", "opt", true, "default");
        option.setRequired(true);
        options.addOption(option);

        String[] arguments = {"--option"};

        parser.parse(options, arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_UnrecognizedOption() throws ParseException
    {
        String[] arguments = {"--unknownOption"};

        parser.parse(options, arguments);
    }

    @Test
    public void testParse_Properties() throws ParseException
    {
        Properties properties = new Properties();
        properties.setProperty("option1", "value1");
        properties.setProperty("option2", "value2");

        String[] arguments = {"--option1", "--option2"};

        CommandLine cmd = parser.parse(options, arguments, properties, false);

        Assert.assertEquals("value1", cmd.getOptionValue("option1"));
        Assert.assertEquals("value2", cmd.getOptionValue("option2"));
    }

    @Test
    public void testParse_StopAtNonOption() throws ParseException
    {
        Option option = new Option("option", "opt", true, "default");
        option.setRequired(false);

        String[] arguments = {"--option1", "value1", "--option2", "value2", "nonOption"};
        options.addOption(option);

        CommandLine cmd = parser.parse(options, arguments, null, true);

        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertEquals("value1", cmd.getOptionValue("option1"));
        Assert.assertEquals("value2", cmd.getOptionValue("option2"));
        Assert.assertEquals("nonOption", cmd.getArgString(0));
    }
}