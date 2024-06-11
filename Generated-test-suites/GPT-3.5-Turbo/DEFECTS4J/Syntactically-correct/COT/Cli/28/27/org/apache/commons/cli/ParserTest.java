
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse_OptionsStringArray() {
        // Create test data
        Options options = new Options();
        String[] arguments = new String[]{"-a", "value1", "-b", "value2"};
        
        // Perform the test
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };
        
        CommandLine result = parser.parse(options, arguments);
        
        // Check the result
        assertTrue(result.hasOption("a"));
        assertEquals("value1", result.getOptionValue("a"));
        
        assertTrue(result.hasOption("b"));
        assertEquals("value2", result.getOptionValue("b"));
    }

    @Test
    public void testParse_OptionsStringArrayProperties() {
        // Create test data
        Options options = new Options();
        String[] arguments = new String[]{"-a", "value1", "-b", "value2"};
        Properties properties = new Properties();
        properties.setProperty("c", "value3");
        
        // Perform the test
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };
        
        CommandLine result = parser.parse(options, arguments, properties);
        
        // Check the result
        assertTrue(result.hasOption("a"));
        assertEquals("value1", result.getOptionValue("a"));
        
        assertTrue(result.hasOption("b"));
        assertEquals("value2", result.getOptionValue("b"));
        
        assertTrue(result.hasOption("c"));
        assertEquals("value3", result.getOptionValue("c"));
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions() throws ParseException {
        // Create test data
        Options options = new Options();
        options.addOption(Option.builder("a").required(true).build());
        
        // Perform the test
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };
        
        parser.setOptions(options);
        parser.checkRequiredOptions();
    }

    @Test
    public void testProcessProperties() {
        // Create test data
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        Properties properties = new Properties();
        properties.setProperty("a", "value1");
        properties.setProperty("b", "value2");
        
        // Perform the test
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };
        
        parser.setOptions(options);
        parser.processProperties(properties);
        
        // Check the result
        assertTrue(parser.getCommandLine().hasOption("a"));
        assertEquals("value1", parser.getCommandLine().getOptionValue("a"));
        
        assertFalse(parser.getCommandLine().hasOption("b"));
    }

    @Test
    public void testProcessOption() throws ParseException {
        // Create test data
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        
        ListIterator iterator = Arrays.asList("-a").listIterator();
        
        // Perform the test
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };
        
        parser.setOptions(options);
        parser.processOption("-a", iterator);
        
        // Check the result
        assertTrue(parser.getCommandLine().hasOption("a"));
    }
}
