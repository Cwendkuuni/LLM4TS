import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ParserTest {
    @Test
    public void testParseWithOptions() {
        Options options = new Options();
        options.addOption("a");
        options.addOption("b");
        
        String[] arguments = {"-a", "-b"};
        CommandLine cmd = Parser.parse(options, arguments);
        
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
    }
    
    @Test
    public void testParseWithArguments() {
        Options options = new Options();
        options.addOption("a");
        
        String[] arguments = {"arg1", "arg2", "arg3"};
        CommandLine cmd = Parser.parse(options, arguments);
        
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals("arg2", cmd.getArgs()[1]);
        assertEquals("arg3", cmd.getArgs()[2]);
    }
    
    @Test
    public void testParseWithProperties() {
        Options options = new Options();
        options.addOption("a");
        options.addOption("b");
        
        String[] arguments = {"-a"};
        
        Properties properties = new Properties();
        properties.setProperty("b", "value");
        
        CommandLine cmd = Parser.parse(options, arguments, properties);
        
        assertTrue(cmd.hasOption("a"));
        assertEquals("value", cmd.getOptionValue("b"));
    }
    
    @Test(expected = ParseException.class)
    public void testParseWithInvalidOption() {
        Options options = new Options();
        options.addOption("a");
        
        String[] arguments = {"-b"};
        
        Parser.parse(options, arguments);
    }
    
    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() {
        Options options = new Options();
        Option option = new Option("a", true);
        options.addOption(option);
        
        String[] arguments = {"-a"};
        
        Parser.parse(options, arguments);
    }
    
    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() {
        Options options = new Options();
        Option option = new Option("a", true);
        option.setRequired(true);
        options.addOption(option);
        
        String[] arguments = {};
        
        Parser.parse(options, arguments);
    }
}