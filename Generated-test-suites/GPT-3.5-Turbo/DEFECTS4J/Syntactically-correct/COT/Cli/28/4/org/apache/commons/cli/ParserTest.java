import org.junit.Test;
import org.apache.commons.cli.*;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse_Options_Arguments() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").desc("Option A").build());
        options.addOption(Option.builder("b").desc("Option B").build());

        String[] arguments = {"-a", "-b"};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };

        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
    }

    @Test
    public void testParse_Options_Arguments_Properties() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").desc("Option A").build());

        String[] arguments = {"-a"};

        Properties properties = new Properties();
        properties.setProperty("a", "value");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };

        CommandLine cmd = parser.parse(options, arguments, properties);

        assertTrue(cmd.hasOption("a"));
        assertEquals("value", cmd.getOptionValue("a"));
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions_WithMissingOptions() throws MissingOptionException {
        Options options = new Options();
        options.addRequiredOption(Option.builder("a").desc("Option A").build());

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };

        parser.checkRequiredOptions();
    }

    @Test
    public void testProcessOption_WithValidOption() throws ParseException {
        Options options = new Options();
        Option optionA = Option.builder("a").desc("Option A").build();
        options.addOption(optionA);

        ListIterator iterator = Arrays.asList("-a", "value").listIterator();

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };

        parser.processOption("-a", iterator);

        assertTrue(optionA.hasArg());
        assertEquals("value", optionA.getValue());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOption_WithInvalidOption() throws ParseException {
        Options options = new Options();
        Option optionA = Option.builder("a").desc("Option A").build();
        options.addOption(optionA);

        ListIterator iterator = Arrays.asList("-b").listIterator();

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };

        parser.processOption("-b", iterator);
    }
}