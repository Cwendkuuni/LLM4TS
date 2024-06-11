import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new ParserImpl();
        options = new Options();

        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");

        options.addOption(option1);
        options.addOption(option2);
    }

    @Test
    public void testParse_withEmptyArguments_shouldReturnEmptyCommandLine() throws ParseException {
        String[] arguments = {};
        CommandLine commandLine = parser.parse(options, arguments);

        assertTrue(commandLine.getOptions().isEmpty());
        assertTrue(commandLine.getArgs().isEmpty());
    }

    @Test
    public void testParse_withNoOptionArguments_shouldReturnCommandLineWithArgs() throws ParseException {
        String[] arguments = {"arg1", "arg2"};
        CommandLine commandLine = parser.parse(options, arguments);

        assertTrue(commandLine.getOptions().isEmpty());
        assertFalse(commandLine.getArgs().isEmpty());
        assertEquals("arg1", commandLine.getArgs().get(0));
        assertEquals("arg2", commandLine.getArgs().get(1));
    }

    @Test
    public void testParse_withOptionArguments_shouldReturnCommandLineWithOption() throws ParseException {
        String[] arguments = {"-a"};
        CommandLine commandLine = parser.parse(options, arguments);

        assertFalse(commandLine.getOptions().isEmpty());
        assertTrue(commandLine.hasOption("a"));
        assertNotNull(commandLine.getOptionValue("a"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_withInvalidOption_shouldThrowUnrecognizedOptionException() throws ParseException {
        String[] arguments = {"-c"};
        parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse_withMissingArgumentForRequiredOption_shouldThrowMissingArgumentException() throws ParseException {
        String[] arguments = {"-b"};
        parser.parse(options, arguments);
    }

    @Test
    public void testParse_withArgumentsIncludingDoubleDash_shouldAddRemainingArgsToCommandLine() throws ParseException {
        String[] arguments = {"-a", "--", "arg1", "arg2"};
        CommandLine commandLine = parser.parse(options, arguments);

        assertFalse(commandLine.getOptions().isEmpty());
        assertTrue(commandLine.hasOption("a"));
        assertFalse(commandLine.getArgs().isEmpty());
        assertEquals("arg1", commandLine.getArgs().get(0));
        assertEquals("arg2", commandLine.getArgs().get(1));
    }

    // Additional test cases can be added for each method of the Parser class

}