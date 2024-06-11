import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;
    private Options options;
    private String[] arguments;

    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        options = new Options();
        arguments = new String[0];
    }

    @Test
    public void testParse() throws ParseException {
        CommandLine result = parser.parse(options, arguments);
        assertNotNull(result);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Properties properties = new Properties();
        CommandLine result = parser.parse(options, arguments, properties);
        assertNotNull(result);
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        CommandLine result = parser.parse(options, arguments, true);
        assertNotNull(result);
    }

    @Test
    public void testParseWithStopAtNonOptionAndProperties() throws ParseException {
        Properties properties = new Properties();
        CommandLine result = parser.parse(options, arguments, properties, true);
        assertNotNull(result);
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("opt", true, "option");
        ListIterator iterator = Arrays.asList("arg1", "arg2").listIterator();
        parser.processArgs(option, iterator);
        String[] result = option.getValues();
        String[] expected = {"arg1", "arg2"};
        assertArrayEquals(expected, result);
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissingArgument() throws ParseException {
        Option option = new Option("opt", true, "option");
        ListIterator iterator = Arrays.asList().listIterator();
        parser.processArgs(option, iterator);
    }

    @Test
    public void testProcessOption() throws ParseException {
        Option option = new Option("opt", false, "option");
        ListIterator iterator = Arrays.asList("arg").listIterator();
        parser.processOption("opt", iterator);
        CommandLine result = parser.parse(options, arguments);
        assertTrue(result.hasOption("opt"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognizedOption() throws ParseException {
        ListIterator iterator = Arrays.asList().listIterator();
        parser.processOption("opt", iterator);
    }
}