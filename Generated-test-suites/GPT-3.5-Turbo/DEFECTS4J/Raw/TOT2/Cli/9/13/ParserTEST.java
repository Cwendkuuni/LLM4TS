import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.*;

public class ParserTest {

    @Test
    public void testParseWithOptionsAndArguments() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b with argument");

        Parser parser = new DefaultParser();
        String[] args = {"-a", "-b", "value"};
        
        try {
            CommandLine cmd = parser.parse(options, args);
        
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertEquals("value", cmd.getOptionValue("b"));
        } catch (ParseException e) {
            fail("Exception thrown when parsing options and arguments");
        }
    }

    @Test
    public void testParseWithProperties() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        
        Parser parser = new GnuParser();
        String[] args = {};
        
        Properties properties = new Properties();
        properties.setProperty("a", "true");
        
        try {
            CommandLine cmd = parser.parse(options, args, properties);
            
            assertTrue(cmd.hasOption("a"));
        } catch (ParseException e) {
            fail("Exception thrown when parsing with properties");
        }
    }

    @Test
    public void testMissingOptionException() {
        Options options = new Options();
        options.addOption("a", true, "option a");
        options.addOption("b", true, "option b");
        
        Parser parser = new PosixParser();
        String[] args = {"-a", "value"};
        
        try {
            parser.parse(options, args);
            fail("MissingOptionException not thrown");
        } catch (MissingOptionException e) {
            // Expected exception
        } catch (ParseException e) {
            fail("Incorrect exception thrown");
        }
    }

    @Test
    public void testUnrecognizedOptionException() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        
        Parser parser = new PosixParser();
        String[] args = {"-b"};
        
        try {
            parser.parse(options, args);
            fail("UnrecognizedOptionException not thrown");
        } catch (UnrecognizedOptionException e) {
            // Expected exception
        } catch (ParseException e) {
            fail("Incorrect exception thrown");
        }
    }

}