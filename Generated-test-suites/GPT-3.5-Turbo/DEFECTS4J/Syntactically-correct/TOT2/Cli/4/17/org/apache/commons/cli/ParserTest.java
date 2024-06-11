import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseWithNullOptionsAndArguments() {
        Parser parser = new ConcreteParser();
        CommandLine cmd = parser.parse(null, null);
        assertNull(cmd);
    }

    @Test
    public void testParseWithValidOptionsAndArguments() {
        Parser parser = new ConcreteParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");
        String[] arguments = {"-a"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testParseWithInvalidOption() {
        Parser parser = new ConcreteParser();
        Options options = new Options();
        String[] arguments = {"-z"};
        try {
            CommandLine cmd = parser.parse(options, arguments);
            fail("Expected UnrecognizedOptionException");
        } catch (UnrecognizedOptionException e) {
            // Exception expected
        }
    }

    @Test
    public void testParseWithMissingRequiredOption() {
        Parser parser = new ConcreteParser();
        Options options = new Options();
        Option option = new Option("r", true, "Required Option");
        option.setRequired(true);
        options.addOption(option);
        String[] arguments = {};
        try {
            CommandLine cmd = parser.parse(options, arguments);
            fail("Expected MissingOptionException");
        } catch (MissingOptionException e) {
            // Exception expected
        }
    }

    @Test
    public void testParseWithStopAtNonOption() {
        Parser parser = new ConcreteParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");
        String[] arguments = {"-a", "value1", "-b", "-c", "value2"};
        CommandLine cmd = parser.parse(options, arguments, true);
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertEquals("value1", cmd.getOptionValue("a"));
        assertEquals(2, cmd.getArgList().size());
        assertEquals("-b", cmd.getArgList().get(0));
        assertEquals("-c", cmd.getArgList().get(1));
    }

    static class ConcreteParser extends Parser {
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
            return arguments;
        }
    }
}