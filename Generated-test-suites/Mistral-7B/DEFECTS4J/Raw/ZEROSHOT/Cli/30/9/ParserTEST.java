import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ParserTest
{
    @Test
    public void testParse() throws ParseException
    {
        // Given a Options object with some defined Options and required options
        Options options = new Options();
        options.addOption("o", "option", true, "An option with an argument");
        options.addOption("a", "another", true, "Another option with an argument");
        options.addRequiredOption("r", "required", true, "A required option");

        String[] arguments = new String[]{"--option", "value1", "--another", "value2", "--required"};

        // When parsing the arguments based on the given Options
        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments, null, false);

        // Then the CommandLine object should contain the parsed options and arguments
        assertEquals(2, commandLine.getOptions().size());
        assertEquals("value1", commandLine.getOptionValue("option"));
        assertEquals("value2", commandLine.getOptionValue("another"));
        assertEquals("--required", commandLine.getOptionValue("required"));
        assertEquals(0, commandLine.getArgs().length);
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException
    {
        // Given a Options object with a required option
        Options options = new Options();
        options.addRequiredOption("r", "required", true, "A required option");

        String[] arguments = new String[]{"--another", "value2"};

        // When parsing the arguments based on the given Options
        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments, null, false);

        // Then a MissingOptionException should be thrown
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException
    {
        // Given a Options object
        Options options = new Options();

        String[] arguments = new String[]{"--unrecognized", "value"};

        // When parsing the arguments based on the given Options
        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments, null, false);

        // Then an UnrecognizedOptionException should be thrown
    }
}