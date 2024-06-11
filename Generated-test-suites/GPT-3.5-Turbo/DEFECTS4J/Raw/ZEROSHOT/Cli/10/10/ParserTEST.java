import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testProcessOption() throws Exception {
        // Test case 1: Testing processOption() with a valid option
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
        Options options = new Options();
        Option option = new Option("a", "option_a");
        options.addOption(option);
        parser.setOptions(options);

        ListIterator<String> iterator = Arrays.asList("-a").listIterator();
        parser.processOption("-a", iterator);

        assertTrue(parser.cmd.hasOption("a"));

        // Test case 2: Testing processOption() with an invalid option
        try {
            iterator = Arrays.asList("-b").listIterator();
            parser.processOption("-b", iterator);
            fail("Should throw UnrecognizedOptionException when an invalid option is provided.");
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: -b", e.getMessage());
        }
    }

    @Test
    public void testProcessArgs() throws Exception {
        // Test case 1: Testing processArgs() with a valid option and argument
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
        Options options = new Options();
        Option option = Option.builder("p")
                .hasArg()
                .argName("path")
                .desc("The path argument")
                .build();
        options.addOption(option);
        parser.setOptions(options);

        ListIterator<String> iterator = Arrays.asList("C:/path/to/file").listIterator();
        parser.processArgs(option, iterator);

        assertEquals("C:/path/to/file", parser.cmd.getParsedOptionValue("p"));

        // Test case 2: Testing processArgs() with a missing argument for a required option
        try {
            iterator = Arrays.asList().listIterator();
            parser.processArgs(option, iterator);
            fail("Should throw MissingArgumentException when a required argument is missing.");
        } catch (MissingArgumentException e) {
            assertEquals("Missing argument for option:p", e.getMessage());
        }
    }

    @Test
    public void testProcessProperties() throws Exception {
        // Test case 1: Testing processProperties() with valid properties
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
        Options options = new Options();
        Option option = Option.builder("x")
                .hasArg()
                .argName("value")
                .desc("The value argument")
                .build();
        options.addOption(option);
        parser.setOptions(options);

        Properties properties = new Properties();
        properties.setProperty("x", "123");
        parser.processProperties(properties);

        assertEquals("123", parser.cmd.getParsedOptionValue("x"));

        // Test case 2: Testing processProperties() with missing option in properties
        properties.clear();
        properties.setProperty("y", "456");
        parser.processProperties(properties);

        assertNull(parser.cmd.getParsedOptionValue("y"));
    }

    @Test
    public void testCheckRequiredOptions() throws Exception {
        // Test case 1: Testing checkRequiredOptions() with missing required options
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
        Options options = new Options();
        Option option = Option.builder("a")
                .required()
                .desc("The required option")
                .build();
        options.addOption(option);
        parser.setOptions(options);

        try {
            parser.checkRequiredOptions();
            fail("Should throw MissingOptionException when required options are missing.");
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: a", e.getMessage());
        }

        // Test case 2: Testing checkRequiredOptions() with all required options present
        options = new Options();
        option = Option.builder("a")
                .required()
                .desc("The required option")
                .build();
        Option option2 = Option.builder("b")
                .required()
                .desc("Another required option")
                .build();
        options.addOption(option);
        options.addOption(option2);
        parser.setOptions(options);

        // Add the required options so they are not considered missing
        parser.cmd.addOption(option);
        parser.cmd.addOption(option2);

        try {
            parser.checkRequiredOptions();
        } catch (MissingOptionException e) {
            fail("Should not throw MissingOptionException when all required options are present.");
        }
    }
}