import org.junit.*;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Options options = new Options();
        Parser parser = new ParserImpl();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Parser parser = new ParserImpl();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Parser parser = new ParserImpl();
        parser.setOptions(options);
        assertEquals(options.getRequiredOptions(), parser.getRequiredOptions());
    }

    @Test
    public void testParse() {
        Options options = new Options();
        Parser parser = new ParserImpl();
        String[] arguments = new String[]{"-a", "value1", "--option", "value2"};
        try {
            CommandLine result = parser.parse(options, arguments);
            assertNotNull(result);
            assertTrue(result.hasOption("a"));
            assertTrue(result.hasOption("option"));
            assertEquals("value1", result.getOptionValue("a"));
            assertEquals("value2", result.getOptionValue("option"));
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test
    public void testParseWithProperties() {
        Options options = new Options();
        Parser parser = new ParserImpl();
        String[] arguments = new String[]{"-a", "value1", "--option", "value2"};
        Properties properties = new Properties();
        properties.setProperty("b", "value3");
        properties.setProperty("c", "value4");
        try {
            CommandLine result = parser.parse(options, arguments, properties);
            assertNotNull(result);
            assertTrue(result.hasOption("a"));
            assertTrue(result.hasOption("option"));
            assertTrue(result.hasOption("b"));
            assertTrue(result.hasOption("c"));
            assertEquals("value1", result.getOptionValue("a"));
            assertEquals("value2", result.getOptionValue("option"));
            assertEquals("value3", result.getOptionValue("b"));
            assertEquals("value4", result.getOptionValue("c"));
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test
    public void testParseStopAtNonOption() {
        Options options = new Options();
        Parser parser = new ParserImpl();
        String[] arguments = new String[]{"-a", "value1", "arg1", "-b"};
        try {
            CommandLine result = parser.parse(options, arguments, true);
            assertNotNull(result);
            assertTrue(result.hasOption("a"));
            assertFalse(result.hasOption("b"));
            assertEquals("value1", result.getOptionValue("a"));
            assertEquals("arg1", result.getArgList().get(0));
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test
    public void testParseException() {
        Options options = new Options();
        Parser parser = new ParserImpl();
        String[] arguments = new String[]{"--option", "value2"};
        try {
            parser.parse(options, arguments);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Missing required option: a", e.getMessage());
        }
    }

    private class ParserImpl extends Parser {
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
            return arguments;
        }
    }
}