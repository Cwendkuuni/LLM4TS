import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest
{
    @Test
    public void testParseNormalCase() throws ParseException
    {
        String[] args = {"-a", "1", "--optionB", "2", "--optionC=3"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option optionA = new Option("a", true, false, "an option");
        Option optionB = new Option("b", true, false, "another option");
        Option optionC = new Option("c", true, false, "yet another option");
        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, args, null, false);

        assertEquals(3, cmd.getArgs().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
        assertEquals("3", cmd.getOptionValue("c"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingRequiredOption() throws ParseException
    {
        String[] args = {"--optionA"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option optionA = new Option("a", true, true, "a required option");
        options.addOption(optionA);
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, args, null, false);

        assertFalse(cmd.hasOption("a"));
        fail();
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidOption() throws ParseException
    {
        String[] args = {"--invalidOption"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        parser.setOptions(options);

        try
        {
            parser.parse(options, args, null, false);
        }
        catch (UnrecognizedOptionException e)
        {
            assertEquals("Unrecognized option: invalidOption", e.getMessage());
            throw e;
        }
    }

    @Test(expected = ParseException.class)
    public void testParseMissingValueOption() throws ParseException
    {
        String[] args = {"-a", "--optionB"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option optionA = new Option("a", true, false, "an option");
        Option optionB = new Option("b", true, true, "an option with a value");
        options.addOption(optionA);
        options.addOption(optionB);
        parser.setOptions(options);

        try
        {
            parser.parse(options, args, null, false);
        }
        catch (MissingArgumentException e)
        {
            assertEquals("Missing required argument for option: b", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        String[] args = {"--foo=bar", "--optionA"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option optionA = new Option("a", true, false, "an option");
        Option optionFoo = new Option("foo", true, false, "another option");
        options.addOption(optionA);
        options.addOption(optionFoo);
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, args, new Properties(), false);

        assertEquals(1, cmd.getArgs().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("foo"));
        assertEquals("bar", cmd.getOptionValue("foo"));
    }
}