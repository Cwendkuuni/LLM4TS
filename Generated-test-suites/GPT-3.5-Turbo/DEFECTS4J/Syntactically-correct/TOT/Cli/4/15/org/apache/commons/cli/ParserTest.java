import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse_noOptions() throws ParseException {
        Options options = new Options();
        Parser parser = new ParserImpl();
        String[] arguments = {};

        CommandLine result = parser.parse(options, arguments);

        assertEquals(0, result.getOptions().length);
        assertEquals(0, result.getArgs().length);
    }

    @Test
    public void testParse_singleOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "option a");
        Parser parser = new ParserImpl();
        String[] arguments = {"-a"};

        CommandLine result = parser.parse(options, arguments);

        assertTrue(result.hasOption("a"));
    }

    @Test
    public void testParse_singleOptionWithArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "option a with argument");
        Parser parser = new ParserImpl();
        String[] arguments = {"-a", "argument"};

        CommandLine result = parser.parse(options, arguments);

        assertTrue(result.hasOption("a"));
        assertEquals("argument", result.getOptionValue("a"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_unrecognizedOption() throws ParseException {
        Options options = new Options();
        Parser parser = new ParserImpl();
        String[] arguments = {"-b"};

        parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse_missingArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "option a with argument");
        Parser parser = new ParserImpl();
        String[] arguments = {"-a"};

        parser.parse(options, arguments);
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_missingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("a", false, "required option a");
        Parser parser = new ParserImpl();
        String[] arguments = {};

        parser.parse(options, arguments);
    }

    private class ParserImpl extends Parser {
        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
            return arguments;
        }
    }
}