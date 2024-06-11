import org.apache.commons.cli.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;


public class ParserTest {

    private CommandLineParser parser;
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
    public void testParse() {
        String[] args = {"-a", "value1", "-b", "value2"};

        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        try {
            CommandLine cmd = parser.parse(options, args);

            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertEquals("value1", cmd.getOptionValue("a"));
            assertEquals("value2", cmd.getOptionValue("b"));
        } catch (ParseException e) {
            fail("Should not have thrown ParseException");
        }
    }

    @Test
    public void testParseWithMissingOption() {
        String[] args = {"-a", "value1"};

        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        try {
            parser.parse(options, args);
            fail("Should have thrown MissingOptionException");
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: b", e.getMessage());
        } catch (ParseException e) {
            fail("Should not have thrown ParseException");
        }
    }

    @Test
    public void testProcessArgs() {
        Option opt = new Option("a", true, "Option a");

        ListIterator iterator = Arrays.asList("value1", "value2").listIterator();

        try {
            parser.processArgs(opt, iterator);

            assertTrue(opt.getValues().length == 2);
            assertEquals("value1", opt.getValue(0));
            assertEquals("value2", opt.getValue(1));
        } catch (ParseException e) {
            fail("Should not have thrown ParseException");
        }
    }

    @Test
    public void testProcessArgsWithMissingArg() {
        Option opt = new Option("a", true, "Option a");

        ListIterator iterator = Arrays.asList().listIterator();

        try {
            parser.processArgs(opt, iterator);
            fail("Should have thrown MissingArgumentException");
        } catch (MissingArgumentException e) {
            assertEquals("Missing argument for option:a", e.getMessage());
        } catch (ParseException e) {
            fail("Should not have thrown ParseException");
        }
    }

    @Test
    public void testProcessOption() {
        Option opt = new Option("a", false, "Option a");
        ListIterator iterator = Arrays.asList("value").listIterator();

        try {
            parser.processOption("a", iterator);

            assertTrue(opt.hasArg());
            assertEquals("value", opt.getValue());
        } catch (ParseException e) {
            fail("Should not have thrown ParseException");
        }
    }

    @Test
    public void testProcessOptionWithUnrecognizedOption() {
        ListIterator iterator = Arrays.asList("value").listIterator();

        try {
            parser.processOption("c", iterator);
            fail("Should have thrown UnrecognizedOptionException");
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: c", e.getMessage());
        } catch (ParseException e) {
            fail("Should not have thrown ParseException");
        }
    }

    @Test
    public void testProcessOptionWithRequiredOption() {
        Option opt = new Option("a", true, "Option a");
        opt.setRequired(true);
        ListIterator iterator = Arrays.asList("value").listIterator();

        try {
            parser.processOption("a", iterator);

            assertFalse(parser.getRequiredOptions().contains("a"));
            assertTrue(opt.hasArg());
            assertEquals("value", opt.getValue());
        } catch (ParseException e) {
            fail("Should not have thrown ParseException");
        }
    }
}