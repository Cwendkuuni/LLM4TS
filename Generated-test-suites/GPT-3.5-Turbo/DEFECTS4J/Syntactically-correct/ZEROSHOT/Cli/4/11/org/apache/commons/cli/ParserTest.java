import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Properties;
import org.apache.commons.cli.*;

public class ParserTest {

    // Test the parse method with only options
    @Test
    public void testParseOptionsOnly() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] args = {"-a", "-b", "value"};
        
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        
        CommandLine cmd = parser.parse(options, args);
        
        assertFalse("Option A should not be present", cmd.hasOption("a"));
        assertTrue("Option B should be present", cmd.hasOption("b"));
        assertEquals("Option B value is incorrect", "value", cmd.getOptionValue("b"));
    }

    // Test the parse method with options and arguments
    @Test
    public void testParseOptionsAndArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] args = {"-a", "-b", "value", "arg1", "arg2"};
        
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        
        CommandLine cmd = parser.parse(options, args);
        
        assertFalse("Option A should not be present", cmd.hasOption("a"));
        assertTrue("Option B should be present", cmd.hasOption("b"));
        assertEquals("Option B value is incorrect", "value", cmd.getOptionValue("b"));
        assertEquals("Incorrect number of arguments", 2, cmd.getArgs().length);
        assertEquals("Argument 1 is incorrect", "arg1", cmd.getArgs()[0]);
        assertEquals("Argument 2 is incorrect", "arg2", cmd.getArgs()[1]);
    }

    // Test the parse method with options and properties
    @Test
    public void testParseOptionsAndProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        Properties properties = new Properties();
        properties.setProperty("a", "true");
        properties.setProperty("b", "value");

        String[] args = {};
        
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        
        CommandLine cmd = parser.parse(options, args, properties);
        
        assertTrue("Option A should be present", cmd.hasOption("a"));
        assertTrue("Option B should be present", cmd.hasOption("b"));
        assertEquals("Option B value is incorrect", "value", cmd.getOptionValue("b"));
    }

    // Test the processArgs method
    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("a", true, "Option A");

        String[] args = {"arg1", "arg2"};
        
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        ListIterator iterator = Arrays.asList(args).listIterator();
        
        assertEquals("Incorrect number of processed arguments", 2, option.getValues().length);
        assertEquals("Processed argument 1 is incorrect", "arg1", option.getValues()[0]);
        assertEquals("Processed argument 2 is incorrect", "arg2", option.getValues()[1]);
    }

    // Test the processOption method
    @Test
    public void testProcessOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option A");

        String arg = "-a";
        
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        ListIterator iterator = Arrays.asList(args).listIterator();
        CommandLine cmd = new CommandLine();
        
        parser.processOption(arg, iterator);
        
        assertTrue("Option A should be present", cmd.hasOption("a"));
    }

}