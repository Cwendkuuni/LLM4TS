import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.*;

public class ParserTest {
    
    @Test
    public void testParseOptionsStringArray() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] arguments = {"-a", "arg1", "-b", "arg2"};
        
        try {
            CommandLine cmd = parser.parse(options, arguments);
            assertEquals("arg1", cmd.getOptionValue("a"));
            assertEquals("arg2", cmd.getOptionValue("b"));
        } catch (ParseException e) {
            fail("ParseException thrown");
        }
    }
    
    @Test
    public void testParseOptionsStringArrayProperties() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] arguments = {"-a", "arg1"};
        Properties properties = new Properties();
        properties.setProperty("b", "arg2");
        
        try {
            CommandLine cmd = parser.parse(options, arguments, properties);
            assertEquals("arg1", cmd.getOptionValue("a"));
            assertEquals("arg2", cmd.getOptionValue("b"));
        } catch (ParseException e) {
            fail("ParseException thrown");
        }
    }
    
    // More test cases for other methods
    
}