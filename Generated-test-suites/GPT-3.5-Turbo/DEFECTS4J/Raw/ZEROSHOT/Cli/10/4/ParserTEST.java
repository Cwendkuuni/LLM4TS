import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        Options options = new Options();
        options.addOption("v", false, "Prints version information");
        options.addOption("h", false, "Prints help information");
        
        String[] arguments = {"-v", "-h"};
        
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        
        try {
            CommandLine cmd = parser.parse(options, arguments);
            assertTrue(cmd.hasOption("v"));
            assertTrue(cmd.hasOption("h"));
        } catch (ParseException e) {
            fail("Error parsing arguments: " + e.getMessage());
        }
    }

    @Test
    public void testParseWithOptionsAndProperties() {
        Options options = new Options();
        options.addOption("v", false, "Prints version information");
        
        Properties properties = new Properties();
        properties.setProperty("v", "true");
        
        String[] arguments = {};
        
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        
        try {
            CommandLine cmd = parser.parse(options, arguments, properties);
            assertTrue(cmd.hasOption("v"));
        } catch (ParseException e) {
            fail("Error parsing arguments: " + e.getMessage());
        }
    }

    @Test
    public void testParseWithOptionsAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("v", false, "Prints version information");
        options.addOption("h", false, "Prints help information");
        
        String[] arguments = {"-v", "abc", "-h"};
        
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        
        try {
            CommandLine cmd = parser.parse(options, arguments, true);
            assertTrue(cmd.hasOption("v"));
            assertFalse(cmd.hasOption("h"));
            assertEquals(2, cmd.getArgs().length);
        } catch (ParseException e) {
            fail("Error parsing arguments: " + e.getMessage());
        }
    }

    @Test
    public void testParseWithOptionsAndPropertiesAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("v", false, "Prints version information");
        
        Properties properties = new Properties();
        properties.setProperty("v", "true");
        
        String[] arguments = {"-v", "abc"};
        
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        
        try {
            CommandLine cmd = parser.parse(options, arguments, properties, true);
            assertTrue(cmd.hasOption("v"));
            assertEquals(1, cmd.getArgs().length);
        } catch (ParseException e) {
            fail("Error parsing arguments: " + e.getMessage());
        }
    }

    @Test
    public void testProcessArgs() {
        Option option = new Option("n", true, "Sets the name");
        ListIterator iterator = Arrays.asList("John", "Doe").listIterator();
        
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        
        try {
            parser.processArgs(option, iterator);
            String[] values = option.getValues();
            assertNotNull(values);
            assertEquals(2, values.length);
            assertEquals("John", values[0]);
            assertEquals("Doe", values[1]);
        } catch (ParseException e) {
            fail("Error processing arguments: " + e.getMessage());
        }
    }

    @Test
    public void testProcessOption() {
        Options options = new Options();
        options.addOption("n", true, "Sets the name");
        
        ListIterator iterator = Arrays.asList("-n", "John").listIterator();
        
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        
        try {
            CommandLine cmd = new CommandLine();
            parser.setOptions(options);
            parser.processOption("-n", iterator);
            Option option = cmd.getOption("n");
            assertNotNull(option);
            assertEquals("John", option.getValue());
        } catch (ParseException e) {
            fail("Error processing option: " + e.getMessage());
        }
    }
}