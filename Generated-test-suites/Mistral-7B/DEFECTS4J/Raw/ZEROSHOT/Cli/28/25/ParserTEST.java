import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest
{
    @Test
    public void testParse() throws ParseException
    {
        String[] args = {"-a", "1", "--foo", "bar", "--", "arg1", "arg2"};
        Options options = new Options();
        Option optionA = new Option("a", true, "a help message for option a");
        Option optionFoo = new Option("foo", true, "a help message for option foo");

        options.addOption(optionA);
        options.addOption(optionFoo);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(args);

        // test options
        assertEquals(true, cmd.hasOption("a"));
        assertEquals(1, cmd.getOptionValues("a").length);
        assertEquals("1", cmd.getOptionValues("a")[0]);

        assertEquals(true, cmd.hasOption("foo"));
        assertEquals(1, cmd.getOptionValues("foo").length);
        assertEquals("bar", cmd.getOptionValues("foo")[0]);

        // test arguments
        List<String> argList = cmd.getArgs();
        assertEquals(2, argList.size());
        assertEquals("arg1", argList.get(0));
        assertEquals("arg2", argList.get(1));

        // test required options
        assertFalse(cmd.hasOption("not-an-option"));

        // test properties
        Properties properties = new Properties();
        properties.setProperty("myOption", "someValue");
        options.addOption("myOption", true, "help message for myOption");
        cmd = parser.parse(args, properties);
        assertEquals("someValue", cmd.getOptionValues("myOption")[0]);
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws ParseException
    {
        String[] args = {"--not-an-option", "arg1", "arg2"};
        Options options = new Options();
        Option optionA = new Option("a", true, "help message for option a");
        options.addOption(optionA);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(args);
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException, UnrecognizedOptionException
    {
        String[] args = {"-a"};
        Options options = new Options();
        Option optionA = new Option("a", true, "help message for option a");
        Option optionB = new Option("b", true, "help message for option b");
        optionA.setRequired(true);
        optionB.setRequired(false);

        options.addOption(optionA);
        options.addOption(optionB);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(args);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException
    {
        String[] args = {"-x", "arg1", "arg2"};
        Options options = new Options();
        Option optionA = new Option("a", true, "help message for option a");
        Option optionB = new Option("b", true, "help message for option b");

        options.addOption(optionA);
        options.addOption(optionB);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(args);

        // assert that the unrecognized option is ignored
        assertNull(parser.getCommandLine().getArgList().get(0));
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOptions() throws ParseException
    {
        String[] args = {"-a"};
        Options options = new Options();
        Option optionA = new Option("a", true, "help message for option a");
        Option optionB = new Option("b", true, "help message for option b");
        optionA.setRequired(true);
        optionB.setRequired(true);

        options.addOption(optionA);
        options.addOption(optionB);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(args);
    }

    @Test
    public void testStopAtNonOption() throws ParseException
    {
        String[] args = {"--", "arg1", "arg2"};
        Options options = new Options();
        Option optionA = new Option("a", true, "help message for option a");

        options.addOption(optionA);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(args, null, true);

        // test that all remaining arguments are added to the args list
        List<String> argList = cmd.getArgs();
        assertEquals(2, argList.size());
        assertEquals("arg1", argList.get(0));
        assertEquals("arg2", argList.get(1));
    }

    @Test
    public void testParseProperties() throws ParseException
    {
        String[] args = {"-DmyProperty=value"};
        Options options = new Options();
        Option option = new Option("D", true, "help message for property D");

        options.addOption(option);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(args);

        assertEquals(1, cmd.getOptionValues("D").length);
        assertEquals("value", cmd.getOptionValues("D")[0]);
    }
}