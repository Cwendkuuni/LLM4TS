import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "a simple boolean option");
        options.addOption("b", true, "an option with a value");
        options.addOption("c", "option-c", false, "a named option");

        String[] args = new String[]{"-a", "-b", "value", "--option-c"};

        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        CommandLine commandLine = parser.parse(options, args);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertTrue(commandLine.hasOption("c"));
        assertEquals("value", commandLine.getOptionValue("b"));
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("o", true, "an option with a value");

        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        ListIterator iterator = Arrays.asList("-o", "value1", "value2").listIterator();

        parser.processArgs(option, iterator);

        assertTrue(option.hasArg());
        assertEquals("value1", option.getValue());
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs_missingValue() throws ParseException {
        Option option = new Option("o", true, "an option with a value");

        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        ListIterator iterator = Arrays.asList("-o").listIterator();

        parser.processArgs(option, iterator);
    }

    @Test
    public void testProcessOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "a simple boolean option");

        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        ListIterator iterator = Arrays.asList("-a").listIterator();

        parser.processOption("-a", iterator);

        CommandLine commandLine = parser.getCmd();
        assertTrue(commandLine.hasOption("a"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOption_unrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "a simple boolean option");

        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        ListIterator iterator = Arrays.asList("-b").listIterator();

        parser.processOption("-b", iterator);
    }

    @Test
    public void testProcessOption_requiredOption() throws ParseException {
        Options options = new Options();
        Option option = Option.builder("a").required().build();
        options.addOption(option);

        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        ListIterator iterator = Arrays.asList("-a").listIterator();

        parser.processOption("-a", iterator);

        CommandLine commandLine = parser.getCmd();
        assertTrue(commandLine.hasOption("a"));
        assertFalse(commandLine.getRequiredOptions().contains(option));
    }

    @Test
    public void testProcessOption_namedOption() throws ParseException {
        Options options = new Options();
        Option option = Option.builder("a").longOpt("option-a").build();
        options.addOption(option);

        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        ListIterator iterator = Arrays.asList("--option-a").listIterator();

        parser.processOption("--option-a", iterator);

        CommandLine commandLine = parser.getCmd();
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("option-a"));
    }

    @Test
    public void testProcessProperties() {
        Properties properties = new Properties();
        properties.setProperty("a", "value1");
        properties.setProperty("b", "value2");

        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        parser.processProperties(properties);

        CommandLine commandLine = parser.getCmd();
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertEquals("value1", commandLine.getOptionValue("a"));
        assertEquals("value2", commandLine.getOptionValue("b"));
    }

    @Test
    public void testCheckRequiredOptions() throws ParseException {
        Options options = new Options();
        Option option1 = Option.builder("a").required().build();
        options.addOption(option1);
        Option option2 = Option.builder("b").required().build();
        options.addOption(option2);

        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        CommandLine commandLine = parser.getCmd();
        commandLine.getRequiredOptions().add(option1); // simulate not processing required option

        try {
            parser.checkRequiredOptions();
            fail("MissingOptionException should be thrown");
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: a, b", e.getMessage());
        }

        commandLine.addOption(option1); // now option1 is present
        parser.checkRequiredOptions();

        try {
            commandLine.addOption(option2); // add option2, but it is not required anymore
            parser.checkRequiredOptions();
        } catch (MissingOptionException e) {
            fail("MissingOptionException should not be thrown");
        }
    }
}