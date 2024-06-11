import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.*;

public class ParserTest {

    @Test
    public void testParseOptionsStringArray() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        
        String[] arguments = {"-a"};
        
        Parser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, arguments);
        
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
    }

    @Test
    public void testParseOptionsStringArrayProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        
        String[] arguments = {"-a"};
        
        Properties properties = new Properties();
        properties.setProperty("b", "value");
        
        Parser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, arguments, properties);
        
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseOptionsStringArrayStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        
        String[] arguments = {"-a", "arg1", "-b", "arg2"};
        
        Parser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, arguments, true);
        
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertArrayEquals(new String[]{"arg1", "-b", "arg2"}, cmd.getArgs());
    }

    @Test
    public void testParseOptionsStringArrayPropertiesStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        
        String[] arguments = {"-a", "arg1", "-b", "arg2"};
        
        Properties properties = new Properties();
        properties.setProperty("b", "value");
        
        Parser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, arguments, properties, true);
        
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value", cmd.getOptionValue("b"));
        assertArrayEquals(new String[]{"arg1", "-b", "arg2"}, cmd.getArgs());
    }
    
}