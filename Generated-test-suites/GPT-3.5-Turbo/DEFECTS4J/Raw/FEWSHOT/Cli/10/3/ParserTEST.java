import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

public class ParserTest {
    private Parser parser;
    
    @Before
    public void setUp() {
        parser = new ParserImpl();
    }
    
    @After
    public void tearDown() {
        parser = null;
    }
    
    @Test
    public void testParse_noOptions() throws ParseException {
        Options options = new Options();
        String[] args = {};
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.getArgList().isEmpty());
    }
    
    @Test
    public void testParse_withOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");
        
        String[] args = {"-a", "-b", "value"};
        CommandLine cmd = parser.parse(options, args);
        
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value", cmd.getOptionValue("b"));
        assertFalse(cmd.hasOption("c"));
    }
    
    @Test(expected = MissingOptionException.class)
    public void testParse_missingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").required().build());
        options.addOption(Option.builder("b").required().build());
        
        String[] args = {"-a"};
        CommandLine cmd = parser.parse(options, args);
    }
    
    @Test
    public void testParse_withProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");
        
        Properties properties = new Properties();
        properties.setProperty("b", "value");
        
        String[] args = {"-a"};
        CommandLine cmd = parser.parse(options, args, properties);
        
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value", cmd.getOptionValue("b"));
        assertFalse(cmd.hasOption("c"));
    }
    
    @Test
    public void testParse_withStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        
        String[] args = {"-a", "nonOption", "-b", "value"};
        CommandLine cmd = parser.parse(options, args, true);
        
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertEquals("nonOption", cmd.getArgs()[0]);
        assertEquals("-b", cmd.getArgs()[1]);
        assertEquals("value", cmd.getArgs()[2]);
    }
    
    class ParserImpl extends Parser {
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
            // Implementation for testing purposes only
            return arguments;
        }
    }
}