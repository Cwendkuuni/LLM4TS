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
                return null;
            }
        };
    }

    @Test
    public void testParse() {
        Options options = new Options();
        String[] arguments = new String[]{"-a", "value1", "-b", "value2"};
        try {
            parser.parse(options, arguments);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
        assertNotNull(parser.getCmd());
    }

    @Test
    public void testParseWithOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        String[] arguments = new String[]{"-a", "value1", "-b", "value2"};
        try {
            parser.parse(options, arguments);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
        assertTrue(parser.getCmd().hasOption("a"));
        assertTrue(parser.getCmd().hasOption("b"));
        assertEquals("value1", parser.getCmd().getOptionValue("a"));
        assertEquals("value2", parser.getCmd().getOptionValue("b"));
    }

    @Test
    public void testParseWithRequiredOptions() {
        Options options = new Options();
        Option optionA = new Option("a", true, "Option A");
        Option optionB = new Option("b", true, "Option B");
        optionA.setRequired(true);
        options.addOption(optionA);
        options.addOption(optionB);
        String[] arguments = new String[]{"-a", "value1"};
        try {
            parser.parse(options, arguments);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
        assertTrue(parser.getCmd().hasOption("a"));
        assertFalse(parser.getCmd().hasOption("b"));
        assertEquals("value1", parser.getCmd().getOptionValue("a"));
    }

    @Test
    public void testParseWithInvalidOption() {
        Options options = new Options();
        String[] arguments = new String[]{"-c", "value1"};
        try {
            parser.parse(options, arguments);
            fail("Expected UnrecognizedOptionException");
        } catch (UnrecognizedOptionException e) {
            // Expected exception
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test
    public void testProcessArgs() {
        Option option = new Option("a", true, "Option A");
        ListIterator<String> iterator = Arrays.asList("-a", "value1", "value2").listIterator();
        try {
            parser.processArgs(option, iterator);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
        assertEquals(2, option.getValues().length);
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test
    public void testProcessArgsWithMissingArgument() {
        Option option = new Option("a", true, "Option A");
        ListIterator<String> iterator = Arrays.asList("-a").listIterator();
        try {
            parser.processArgs(option, iterator);
            fail("Expected MissingArgumentException");
        } catch (MissingArgumentException e) {
            // Expected exception
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test
    public void testProcessOption() {
        Options options = new Options();
        Option optionA = new Option("a", true, "Option A");
        Option optionB = new Option("b", true, "Option B");
        options.addOption(optionA);
        options.addOption(optionB);
        ListIterator<String> iterator = Arrays.asList("-a", "value1").listIterator();
        try {
            parser.processOption("-a", iterator);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
        assertTrue(parser.getCmd().hasOption("a"));
        assertFalse(parser.getCmd().hasOption("b"));
        assertEquals("value1", parser.getCmd().getOptionValue("a"));
    }

    @Test
    public void testProcessOptionWithInvalidOption() {
        Options options = new Options();
        ListIterator<String> iterator = Arrays.asList("-c").listIterator();
        try {
            parser.processOption("-c", iterator);
            fail("Expected UnrecognizedOptionException");
        } catch (UnrecognizedOptionException e) {
            // Expected exception
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }
}