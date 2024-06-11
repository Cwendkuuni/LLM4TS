
import org.apache.commons.cli.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse_OptionsStringArray() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "test option a");
        options.addOption("b", true, "test option b");

        Parser parser = new Parser() {

            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        String[] arguments = {"-a", "-b", "value"};

        CommandLine commandLine = parser.parse(options, arguments);

        assertTrue(commandLine.hasOption("a"));
        assertEquals("value", commandLine.getOptionValue("b"));
    }

    @Test
    public void testParse_OptionsStringArrayProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "test option a");
        options.addOption("b", true, "test option b");

        Parser parser = new Parser() {

            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        String[] arguments = {"-a", "-b", "value"};
        Properties properties = new Properties();
        properties.put("c", "value");

        CommandLine commandLine = parser.parse(options, arguments, properties);

        assertTrue(commandLine.hasOption("a"));
        assertEquals("value", commandLine.getOptionValue("b"));
        assertEquals("value", commandLine.getOptionValue("c"));
    }

    @Test
    public void testParse_OptionsStringArrayBoolean() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "test option a");
        options.addOption("b", true, "test option b");

        Parser parser = new Parser() {

            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        String[] arguments = {"-a", "-b", "value"};

        CommandLine commandLine = parser.parse(options, arguments, true);

        assertTrue(commandLine.hasOption("a"));
        assertEquals("value", commandLine.getOptionValue("b"));
    }

    @Test
    public void testParse_OptionsStringArrayPropertiesBoolean() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "test option a");
        options.addOption("b", true, "test option b");

        Parser parser = new Parser() {

            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        String[] arguments = {"-a", "-b", "value"};
        Properties properties = new Properties();
        properties.put("c", "value");

        CommandLine commandLine = parser.parse(options, arguments, properties, true);

        assertTrue(commandLine.hasOption("a"));
        assertEquals("value", commandLine.getOptionValue("b"));
        assertEquals("value", commandLine.getOptionValue("c"));
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("t", true, "test option");

        Parser parser = new Parser() {

            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        ListIterator iterator = Arrays.asList("-t", "value1", "value2").listIterator();

        parser.processArgs(option, iterator);

        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs_MissingArgument() throws ParseException {
        Option option = new Option("t", true, "test option");

        Parser parser = new Parser() {

            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        ListIterator iterator = Arrays.asList("-t").listIterator();

        parser.processArgs(option, iterator);
    }

    @Test
    public void testProcessOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "test option a");
        options.addOption("b", true, "test option b");

        Parser parser = new Parser() {

            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }

            @Override
            protected void processArgs(Option opt, ListIterator iter) throws ParseException {
                opt.addValueForProcessing((String) iter.next());
            }
        };

        ListIterator iterator = Arrays.asList("-a").listIterator();
        parser.processOption("-a", iterator);

        assertTrue(iterator.hasNext());
        assertEquals("-a", iterator.next());

        iterator = Arrays.asList("-a", "value1").listIterator();
        parser.processOption("-a", iterator);

        assertTrue(iterator.hasNext());
        assertEquals("value1", iterator.next());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOption_UnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "test option a");

        Parser parser = new Parser() {

            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        ListIterator iterator = Arrays.asList("-b").listIterator();
        parser.processOption("-b", iterator);
    }
}
