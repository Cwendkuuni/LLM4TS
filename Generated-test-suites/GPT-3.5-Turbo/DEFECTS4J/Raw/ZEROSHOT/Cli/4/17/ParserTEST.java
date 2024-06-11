import org.junit.*;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        // Create options
        Options options = new Options();
        
        // Create command line arguments
        String[] arguments = {"-a", "--file", "test.txt"};
        
        // Create properties
        Properties properties = new Properties();
        properties.setProperty("file", "test.txt");
        
        // Create Parser object
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        
        try {
            // Test parse method
            CommandLine cmd = parser.parse(options, arguments);
            
            // Test command line options
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("file"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
        
        try {
            // Test parse method with properties
            CommandLine cmd = parser.parse(options, arguments, properties);
            
            // Test command line options
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("file"));
            
            // Test properties
            assertEquals("test.txt", cmd.getOptionValue("file"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
        
        try {
            // Test parse method with stopAtNonOption
            CommandLine cmd = parser.parse(options, arguments, true);
            
            // Test command line options
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("file"));
            
            // Test arguments
            assertEquals(3, cmd.getArgs().length);
            assertEquals("-a", cmd.getArgs()[0]);
            assertEquals("--file", cmd.getArgs()[1]);
            assertEquals("test.txt", cmd.getArgs()[2]);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
        
        try {
            // Test parse method with properties and stopAtNonOption
            CommandLine cmd = parser.parse(options, arguments, properties, true);
            
            // Test command line options
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("file"));
            
            // Test properties
            assertEquals("test.txt", cmd.getOptionValue("file"));
            
            // Test arguments
            assertEquals(3, cmd.getArgs().length);
            assertEquals("-a", cmd.getArgs()[0]);
            assertEquals("--file", cmd.getArgs()[1]);
            assertEquals("test.txt", cmd.getArgs()[2]);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
        
        try {
            // Test parse method with null arguments
            CommandLine cmd = parser.parse(options, null);
            
            // Test command line options
            assertFalse(cmd.hasOption("a"));
            assertFalse(cmd.hasOption("file"));
            
            // Test arguments
            assertEquals(0, cmd.getArgs().length);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
        
        try {
            // Test parse method with empty arguments
            CommandLine cmd = parser.parse(options, new String[0]);
            
            // Test command line options
            assertFalse(cmd.hasOption("a"));
            assertFalse(cmd.hasOption("file"));
            
            // Test arguments
            assertEquals(0, cmd.getArgs().length);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testProcessArgs() {
        // Create option
        Option option = new Option("f", "file", true, "input file");
        
        // Create iterator
        ListIterator iterator = Arrays.asList(new String[] {"test.txt"}).listIterator();
        
        // Create Parser object
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        
        try {
            // Test processArgs method
            parser.processArgs(option, iterator);
            
            // Test option value
            assertEquals("test.txt", option.getValue());
        } catch (ParseException e) {
            fail(e.getMessage());
        }
        
        try {
            // Test processArgs method with missing argument
            iterator = Arrays.asList(new String[0]).listIterator();
            parser.processArgs(option, iterator);
            fail("Should have thrown MissingArgumentException");
        } catch (MissingArgumentException e) {
            // Test exception message
            assertEquals("Missing argument for option:file", e.getMessage());
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testProcessOption() {
        // Create options
        Options options = new Options();
        options.addOption("a", "add", false, "add item");
        options.addOption("r", "remove", false, "remove item");
        
        // Create iterator
        ListIterator iterator = Arrays.asList(new String[] {"-a", "--remove"}).listIterator();
        
        // Create Parser object
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        
        try {
            // Test processOption method
            parser.processOption("-a", iterator);
            parser.processOption("--remove", iterator);
            
            // Test command line options
            assertTrue(options.hasOption("a"));
            assertTrue(options.hasOption("remove"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
        
        try {
            // Test processOption method with unrecognized option
            parser.processOption("-invalid", iterator);
            fail("Should have thrown UnrecognizedOptionException");
        } catch (UnrecognizedOptionException e) {
            // Test exception message
            assertEquals("Unrecognized option: -invalid", e.getMessage());
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }
}