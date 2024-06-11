import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseWithOptionsAndArguments() {
        // Test parsing options and arguments
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b with argument");
        
        String[] args = {"-a", "-b", "value"};
        
        Parser parser = new DefaultParser();
        
        try {
            CommandLine cmd = parser.parse(options, args);
            
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertEquals("value", cmd.getOptionValue("b"));
        } catch (ParseException e) {
            fail("Exception not expected: " + e.getMessage());
        }
    }

    @Test
    public void testParseRequiredOptions() {
        // Test parsing required options
        Options options = new Options();
        Option optionA = new Option("a", "Option A");
        optionA.setRequired(true);
        options.addOption(optionA);
        
        String[] args = {};
        
        Parser parser = new DefaultParser();
        
        try {
            parser.parse(options, args);
            fail("MissingOptionException expected");
        } catch (MissingOptionException e) {
            // Expected exception
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testParseInvalidOption() {
        // Test parsing invalid option
        Options options = new Options();
        String[] args = {"-c"};
        
        Parser parser = new DefaultParser();
        
        try {
            parser.parse(options, args);
            fail("UnrecognizedOptionException expected");
        } catch (UnrecognizedOptionException e) {
            // Expected exception
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}