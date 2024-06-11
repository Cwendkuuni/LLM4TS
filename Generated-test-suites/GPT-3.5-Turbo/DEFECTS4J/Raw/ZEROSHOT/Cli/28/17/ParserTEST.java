import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        
        String[] args = {"-a", "-b", "value"};

        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };

        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value", cmd.getOptionValue("b"));
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("a", true, "option a");

        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };

        ListIterator iterator = Arrays.asList("value1", "value2").listIterator();

        parser.processArgs(option, iterator);

        String[] values = option.getValues();
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissingArgument() throws ParseException {
        Option option = new Option("a", true, "option a");

        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };

        ListIterator iterator = Collections.emptyList().listIterator();

        parser.processArgs(option, iterator);
    }

    @Test
    public void testProcessOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "option a");

        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };

        ListIterator iterator = Collections.singletonList("-a").listIterator();

        CommandLine cmd = new CommandLine();
        parser.processOption("-a", iterator);

        assertTrue(cmd.hasOption("a"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "option a");

        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };

        ListIterator iterator = Collections.singletonList("-b").listIterator();

        parser.processOption("-b", iterator);
    }
}