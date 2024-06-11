import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        // Test case for parse(Options options, String[] arguments)
        // Test with null options and null arguments
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        try {
            parser.parse(null, null);
            fail("ParseException should be thrown when options and arguments are null");
        } catch (ParseException e) {
            // Expected ParseException
        }

        // Test with empty options and empty arguments
        try {
            parser.parse(new Options(), new String[0]);
            fail("ParseException should be thrown when options and arguments are empty");
        } catch (ParseException e) {
            // Expected ParseException
        }

        // Test case for parse(Options options, String[] arguments, Properties properties)
        // Test with null properties
        try {
            parser.parse(new Options(), new String[0], null);
            fail("ParseException should be thrown when properties is null");
        } catch (ParseException e) {
            // Expected ParseException
        }
    }

    @Test
    public void testProcessProperties() {
        // Test case for processProperties(Properties properties)
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };

        // Test with null properties
        parser.processProperties(null);

        // Test with empty properties
        parser.processProperties(new Properties());

        // Test with properties containing invalid option
        try {
            Properties properties = new Properties();
            properties.setProperty("invalidOption", "value");
            parser.processProperties(properties);
            fail("UnrecognizedOptionException should be thrown when properties contains invalid option");
        } catch (UnrecognizedOptionException e) {
            // Expected UnrecognizedOptionException
        }

    }

    @Test
    public void testProcessArgs() {
        // Test case for processArgs(Option opt, ListIterator iter)
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };

        // Test with option without argument and no optional argument
        Option option = new Option("a", false, "description");
        ListIterator iterator = Arrays.asList("value").listIterator();
        try {
            parser.processArgs(option, iterator);
            fail("MissingArgumentException should be thrown when option does not have argument");
        } catch (MissingArgumentException e) {
            // Expected MissingArgumentException
        }

        // Test with option with optional argument
        option = Option.builder("a")
                .optionalArg(true)
                .build();
        iterator = Arrays.asList("value1", "value2").listIterator();
        parser.processArgs(option, iterator);
        assertEquals("value1", option.getValue());

        // Test with missing optional argument
        iterator = Arrays.asList().listIterator();
        parser.processArgs(option, iterator);
        assertNull(option.getValue());
    }

    @Test
    public void testProcessOption() {
        // Test case for processOption(String arg, ListIterator iter)
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };

        // Test with unrecognized option
        ListIterator iterator = Arrays.asList("value").listIterator();
        try {
            parser.processOption("invalidOption", iterator);
            fail("UnrecognizedOptionException should be thrown when option is unrecognized");
        } catch (UnrecognizedOptionException e) {
            // Expected UnrecognizedOptionException
        }

        // Test with option without argument
        Option option = new Option("a", false, "description");
        iterator = Arrays.asList().listIterator();
        parser.processOption("a", iterator);
        assertTrue(option.hasArg());

        // Test with option with argument
        option = Option.builder("b")
                .hasArg()
                .build();
        iterator = Arrays.asList("value").listIterator();
        parser.processOption("b", iterator);
        assertEquals("value", option.getValue());
    }

}