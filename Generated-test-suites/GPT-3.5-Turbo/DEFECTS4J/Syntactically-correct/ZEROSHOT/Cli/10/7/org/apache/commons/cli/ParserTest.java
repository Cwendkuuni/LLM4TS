import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
    }

    @Test
    public void testSetOptions() {
        Options options = new Options();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        parser.setOptions(options);
        assertNotNull(parser.getRequiredOptions());
    }

    @Test
    public void testProcessArgs() {
        Option option = Option.builder("test").hasArg().build();
        ListIterator iter = Arrays.asList("value1", "value2").listIterator();
        try {
            parser.processArgs(option, iter);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
        String[] expectedValues = {"value1", "value2"};
        assertArrayEquals(expectedValues, option.getValues());
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissingArg() throws ParseException {
        Option option = Option.builder("test").hasArg().build();
        ListIterator iter = Arrays.asList().listIterator();
        parser.processArgs(option, iter);
    }

    @Test
    public void testProcessOption() {
        Option option = Option.builder("test").build();
        ListIterator iter = Arrays.asList("-test").listIterator();
        try {
            parser.processOption("-test", iter);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
        assertTrue(parser.getCommandLine().hasOption("test"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognized() throws ParseException {
        ListIterator iter = Arrays.asList("-unknown").listIterator();
        parser.processOption("-unknown", iter);
    }

    @Test
    public void testParse() {
        Options options = new Options();
        String[] arguments = {"-test"};
        try {
            parser.parse(options, arguments);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
        assertTrue(parser.getCommandLine().hasOption("test"));
    }

    @Test
    public void testParseWithProperties() {
        Options options = new Options();
        String[] arguments = {"-test"};
        Properties properties = new Properties();
        properties.setProperty("test", "value");
        try {
            parser.parse(options, arguments, properties);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
        assertTrue(parser.getCommandLine().hasOption("test"));
    }

    @Test
    public void testParseStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"-test", "arg"};
        try {
            parser.parse(options, arguments, true);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
        assertTrue(parser.getCommandLine().hasOption("test"));
        assertEquals("arg", parser.getCommandLine().getArgList().get(0));
    }

    @Test
    public void testParseWithPropertiesStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"-test", "arg"};
        Properties properties = new Properties();
        properties.setProperty("test", "value");
        try {
            parser.parse(options, arguments, properties, true);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
        assertTrue(parser.getCommandLine().hasOption("test"));
        assertEquals("arg", parser.getCommandLine().getArgList().get(0));
    }

    @Test
    public void testCheckRequiredOptions() {
        Options options = new Options();
        Option option = Option.builder("test").required().build();
        options.addOption(option);
        String[] arguments = {};
        try {
            parser.parse(options, arguments);
        } catch (MissingOptionException e) {
            // Expected exception, do nothing
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
        assertTrue(parser.getRequiredOptions().contains(option.getKey()));
    }
}