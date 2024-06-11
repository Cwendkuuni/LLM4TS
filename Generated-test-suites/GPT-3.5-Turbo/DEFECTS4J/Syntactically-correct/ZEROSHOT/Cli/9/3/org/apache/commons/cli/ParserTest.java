import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
    }

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{};

        CommandLine result = parser.parse(options, arguments);

        assertNotNull(result);
    }

    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        String[] arguments = new String[]{"-a", "-b", "value"};

        CommandLine result = parser.parse(options, arguments);

        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
        assertEquals("value", result.getOptionValue("b"));
        assertFalse(result.hasOption("c"));
    }

    @Test
    public void testParseWithRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addRequiredOption("b", "required option B");
        String[] arguments = new String[]{"-a"};

        try {
            parser.parse(options, arguments);
            fail("MissingOptionException should be thrown");
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: b", e.getMessage());
        }
    }

    @Test
    public void testProcessProperties() {
        Properties properties = new Properties();
        properties.setProperty("a", "value");

        parser.processProperties(properties);

        assertTrue(parser.cmd.hasOption("a"));
    }

    @Test
    public void testCheckRequiredOptions() {
        Options options = new Options();
        options.addRequiredOption("a", "required option A");

        parser.setOptions(options);

        try {
            parser.checkRequiredOptions();
            fail("MissingOptionException should be thrown");
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: a", e.getMessage());
        }
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("a", true, "Option A");
        ListIterator iterator = new ArrayList<String>().listIterator();
        iterator.add("value");

        parser.processArgs(option, iterator);

        assertTrue(option.hasArg());
        assertEquals("value", option.getValue());
    }

    @Test
    public void testProcessOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option A");

        parser.setOptions(options);

        String arg = "a";
        ListIterator iterator = new ArrayList<String>().listIterator();

        parser.processOption(arg, iterator);

        assertTrue(parser.cmd.hasOption("a"));
    }

}