import org.junit.*;
import static org.junit.Assert.*;

public class ParserTest {
    
    @Test
    public void testFlatten() {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };
        
        Options options = new Options();
        String[] arguments = {"-a", "arg1", "-b", "arg2"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        String[] expected = {"-a", "arg1", "-b", "arg2"};
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testParse() throws ParseException {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };
        
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        
        String[] arguments = {"-a", "-b", "arg1"};
        
        CommandLine result = parser.parse(options, arguments);
        
        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
        assertEquals("arg1", result.getOptionValue("b"));
    }
    
    
    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions() throws ParseException {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };
        
        Options options = new Options();
        options.addOption("a", true, "option a");
        options.addOption("b", true, "option b");
        
        String[] arguments = {"-a", "arg1"};
        
        parser.parse(options, arguments);
    }
    
}