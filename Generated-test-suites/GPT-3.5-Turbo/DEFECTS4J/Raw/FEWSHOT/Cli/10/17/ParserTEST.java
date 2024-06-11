import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse_optionsAndArguments() {
        // options and arguments provided
        Options options = new Options();
        options.addOption("a", false, "enable option a");
        options.addOption("b", true, "set option b value");
        options.addOption("c", false, "enable option c");

        String[] arguments = new String[]{"-a", "-b", "value", "argument"};

        Parser parser = new ParserImpl();
        CommandLine result = parser.parse(options, arguments);

        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("c"));
        assertTrue(result.hasOption("b"));
        assertEquals("value", result.getOptionValue("b"));
        assertEquals("argument", result.getArgList().get(0));
    }

    @Test
    public void testParse_invalidOption() {
        // invalid option provided
        Options options = new Options();
        options.addOption("a", false, "enable option a");
        options.addOption("b", false, "enable option b");

        String[] arguments = new String[]{"-a", "-c"};

        Parser parser = new ParserImpl();
        try {
            parser.parse(options, arguments);
            fail("Should throw UnrecognizedOptionException");
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: -c", e.getMessage());
        }
    }

    @Test
    public void testParse_missingRequiredOption() {
        // missing required option
        Options options = new Options();
        options.addRequiredOption("r", null, false, "required option r");
        options.addOption("o", false, "optional option o");

        String[] arguments = new String[]{"-o"};

        Parser parser = new ParserImpl();
        try {
            parser.parse(options, arguments);
            fail("Should throw MissingOptionException");
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: r", e.getMessage());
        }
    }

    @Test
    public void testParse_properties() {
        // options specified through properties
        Options options = new Options();
        options.addOption("a", false, "enable option a");
        options.addOption("b", true, "set option b value");

        String[] arguments = new String[]{"argument"};

        Properties properties = new Properties();
        properties.setProperty("a", "true");
        properties.setProperty("b", "value");

        Parser parser = new ParserImpl();
        CommandLine result = parser.parse(options, arguments, properties);

        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
        assertEquals("value", result.getOptionValue("b"));
        assertEquals("argument", result.getArgList().get(0));
    }

    @Test
    public void testParse_stopAtNonOption() {
        // stop parsing at non-option
        Options options = new Options();
        options.addOption("a", false, "enable option a");
        options.addOption("b", true, "set option b value");

        String[] arguments = new String[]{"-a", "argument", "-b", "value"};

        Parser parser = new ParserImpl();
        CommandLine result = parser.parse(options, arguments, true);

        assertTrue(result.hasOption("a"));
        assertFalse(result.hasOption("b"));
        assertEquals("argument", result.getArgList().get(0));
        assertEquals("-b", result.getArgList().get(1));
        assertEquals("value", result.getArgList().get(2));
    }

    private static class ParserImpl extends Parser {
        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
            return arguments;
        }
    }
}