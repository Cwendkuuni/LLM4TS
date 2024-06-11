import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class ParserTest {
    private Parser parser;

    @Before
    public void setUp() {
        parser = new MockParser();
    }

    @Test
    public void testParseWithNoOptionsAndArguments() {
        Options options = new Options();
        String[] arguments = new String[]{};

        CommandLine result = parser.parse(options, arguments);

        assertNotNull(result);
        assertTrue(result.getOptions().isEmpty());
        assertTrue(result.getArgs().isEmpty());
    }

    @Test
    public void testParseWithSingleOption() {
        Options options = new Options();
        Option option = new Option("v", "verbose", false, "enable verbose mode");
        options.addOption(option);
        String[] arguments = new String[]{"-v"};

        CommandLine result = parser.parse(options, arguments);

        assertTrue(result.hasOption("v"));
        assertEquals("verbose", result.getOptionValue("v"));
    }

    @Test
    public void testParseWithRequiredOption() {
        Options options = new Options();
        Option option = new Option("f", "file", true, "specify input file");
        option.setRequired(true);
        options.addOption(option);
        String[] arguments = new String[]{"-f", "input.txt"};

        CommandLine result = parser.parse(options, arguments);

        assertTrue(result.hasOption("f"));
        assertEquals("input.txt", result.getOptionValue("f"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() {
        Options options = new Options();
        Option option = new Option("o", "output", true, "specify output file");
        option.setRequired(true);
        options.addOption(option);
        String[] arguments = new String[]{};

        parser.parse(options, arguments);
    }

    // Add more test cases to cover edge cases and exception handling scenarios

    private class MockParser extends Parser {
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
            return arguments;
        }
    }
}