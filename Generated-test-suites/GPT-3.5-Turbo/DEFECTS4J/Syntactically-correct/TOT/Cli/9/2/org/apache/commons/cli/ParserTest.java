import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setup() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };

        options = new Options();
    }

    @Test
    public void testParse_withOptionsAndArguments_returnCommandLineObject() throws ParseException {
        String[] arguments = { "-a", "value1", "-b", "value2" };

        options.addOption("a", true, "option a");
        options.addOption("b", true, "option b");

        CommandLine result = parser.parse(options, arguments);

        assertNotNull(result);
        assertTrue(result.hasOption("a"));
        assertEquals("value1", result.getOptionValue("a"));
        assertTrue(result.hasOption("b"));
        assertEquals("value2", result.getOptionValue("b"));
    }

    @Test
    public void testParse_withRequiredOptions_returnCommandLineObject() throws ParseException {
        String[] arguments = { "-a" };

        options.addOption("a", true, "option a");
        options.addRequiredOption("a");

        CommandLine result = parser.parse(options, arguments);

        assertNotNull(result);
        assertTrue(result.hasOption("a"));
        assertNull(result.getOptionValue("a"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_withMissingRequiredOptions_throwMissingOptionException() throws ParseException {
        String[] arguments = { "-b" };

        options.addRequiredOption("a");

        parser.parse(options, arguments);
    }

    @Test
    public void testProcessArgs_withOptionAndMultipleValues_addValuesToOption() throws ParseException {
        Option option = new Option("a", true, "option a");

        ListIterator iterator = Arrays.asList("value1", "value2").listIterator();

        parser.processArgs(option, iterator);

        assertTrue(option.hasArgs());
        assertEquals(2, option.getValues().length);
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs_withMissingArgument_throwMissingArgumentException() throws ParseException {
        Option option = new Option("a", true, "option a");

        ListIterator iterator = Arrays.asList().listIterator();

        parser.processArgs(option, iterator);
    }

    @Test
    public void testProcessOption_withValidOption_addOptionToCommandLine() throws ParseException {
        Option option = new Option("a", true, "option a");

        ListIterator iterator = Arrays.asList("value1").listIterator();

        parser.processOption("a", iterator);

        assertTrue(parser.cmd.hasOption("a"));
        assertEquals("value1", parser.cmd.getOptionValue("a"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOption_withInvalidOption_throwUnrecognizedOptionException() throws ParseException {
        ListIterator iterator = Arrays.asList("value1").listIterator();

        parser.processOption("c", iterator);
    }

}