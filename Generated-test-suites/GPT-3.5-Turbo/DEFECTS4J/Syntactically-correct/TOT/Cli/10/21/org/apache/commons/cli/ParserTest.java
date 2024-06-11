import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private Parser parser;
    private Options options;
    private Option option1;
    private Option option2;
    private Option option3;

    @Before
    public void setUp() {
        parser = new ParserImpl();
        options = new Options();
        option1 = new Option("a", "option1", false, "Option 1");
        option2 = new Option("b", "option2", true, "Option 2");
        option3 = new Option("c", "option3", false, "Option 3");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
    }

    @Test
    public void testParse_NoArguments() throws ParseException {
        String[] arguments = {};

        CommandLine result = parser.parse(options, arguments);

        assertFalse(result.hasOption("a"));
        assertFalse(result.hasOption("b"));
        assertFalse(result.hasOption("c"));
    }

    @Test
    public void testParse_Option1() throws ParseException {
        String[] arguments = {"-a"};

        CommandLine result = parser.parse(options, arguments);

        assertTrue(result.hasOption("a"));
        assertFalse(result.hasOption("b"));
        assertFalse(result.hasOption("c"));
    }

    @Test
    public void testParse_Option2WithValue() throws ParseException {
        String[] arguments = {"-b", "value"};

        CommandLine result = parser.parse(options, arguments);

        assertFalse(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
        assertEquals("value", result.getOptionValue("b"));
        assertFalse(result.hasOption("c"));
    }

    @Test
    public void testParse_Option2WithoutValue() throws ParseException {
        String[] arguments = {"-b"};

        try {
            parser.parse(options, arguments);
            fail("Expected MissingArgumentException");
        } catch (MissingArgumentException e) {
            assertEquals("Missing argument for option:b", e.getMessage());
        }
    }

    @Test
    public void testParse_Option3() throws ParseException {
        String[] arguments = {"-c"};

        CommandLine result = parser.parse(options, arguments);

        assertFalse(result.hasOption("a"));
        assertFalse(result.hasOption("b"));
        assertTrue(result.hasOption("c"));
    }

    @Test
    public void testParse_UnknownOption() throws ParseException {
        String[] arguments = {"-d"};

        try {
            parser.parse(options, arguments);
            fail("Expected UnrecognizedOptionException");
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: d", e.getMessage());
        }
    }

    @Test
    public void testParse_StopAtNonOption() throws ParseException {
        String[] arguments = {"-a", "value", "nonOption"};

        CommandLine result = parser.parse(options, arguments, true);

        assertTrue(result.hasOption("a"));
        assertEquals("value", result.getOptionValue("a"));
        assertFalse(result.hasOption("b"));
        assertFalse(result.hasOption("c"));
        assertTrue(result.getArgList().contains("nonOption"));
    }

    @Test
    public void testParse_Properties() throws ParseException {
        Properties properties = new Properties();
        properties.setProperty("a", "value1");
        properties.setProperty("b", "value2");
        properties.setProperty("c", "true");

        String[] arguments = {};

        CommandLine result = parser.parse(options, arguments, properties);

        assertTrue(result.hasOption("a"));
        assertEquals("value1", result.getOptionValue("a"));
        assertTrue(result.hasOption("b"));
        assertEquals("value2", result.getOptionValue("b"));
        assertTrue(result.hasOption("c"));
    }

    @Test
    public void testParse_MissingRequiredOption() throws ParseException {
        options.addRequiredOption(option1);
        options.addRequiredOption(option2);

        String[] arguments = {};

        try {
            parser.parse(options, arguments);
            fail("Expected MissingOptionException");
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: a, b", e.getMessage());
        }
    }
}