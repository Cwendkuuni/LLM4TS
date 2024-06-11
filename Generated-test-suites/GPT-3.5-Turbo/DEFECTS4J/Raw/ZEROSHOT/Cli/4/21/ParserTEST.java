import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        Options options = new Options();
        options.addOption(new Option("a", "first"));
        options.addOption(new Option("b", "second"));
        
        String[] arguments = {"-a", "-b"};
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        
        try {
            CommandLine cmd = parser.parse(options, arguments);
            
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
        } catch (ParseException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testParseWithProperties() {
        Options options = new Options();
        options.addOption(new Option("a", "first"));
        options.addOption(new Option("b", "second"));
        
        String[] arguments = {"-a"};
        
        Properties properties = new Properties();
        properties.setProperty("b", "true");
        
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        
        try {
            CommandLine cmd = parser.parse(options, arguments, properties);
            
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
        } catch (ParseException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testParseWithStopAtNonOption() {
        Options options = new Options();
        options.addOption(new Option("a", "first"));
        options.addOption(new Option("b", "second"));
        
        String[] arguments = {"-a", "-b", "arg1", "arg2"};
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        
        try {
            CommandLine cmd = parser.parse(options, arguments, true);
            
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertEquals(2, cmd.getArgList().size());
        } catch (ParseException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testParseWithRequiredOptions() {
        Options options = new Options();
        options.addRequiredOption(new Option("a", "first"));
        options.addRequiredOption(new Option("b", "second"));
        
        String[] arguments = {};
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        
        try {
            CommandLine cmd = parser.parse(options, arguments);
            
            fail("Exception not thrown");
        } catch (MissingOptionException e) {
            assertTrue(e.getMessage().contains("a"));
            assertTrue(e.getMessage().contains("b"));
        } catch (ParseException e) {
            fail("Wrong exception thrown");
        }
    }

}