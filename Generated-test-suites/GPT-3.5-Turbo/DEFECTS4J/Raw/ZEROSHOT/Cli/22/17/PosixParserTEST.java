import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    
    private PosixParser parser;
    
    @Before
    public void setUp() {
        parser = new PosixParser();
        parser.init();
        parser.tokens.clear();
    }
    
    @Test
    public void testFlattenWithStopAtNonOptionTrue() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", false, "option c");
        
        String[] arguments = {"-a", "-b", "value", "arg1", "arg2"};
        
        String[] expected = {"-a", "-b", "value", "--", "arg1", "arg2"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithStopAtNonOptionFalse() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", false, "option c");
        
        String[] arguments = {"-a", "-b", "value", "arg1", "arg2"};
        
        String[] expected = {"-a", "-b", "value", "arg1", "arg2"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        
        parser.options = options;
        
        String token = "-a";
        
        parser.processOptionToken(token, true);
        
        assertTrue(parser.eatTheRest);
        assertTrue(parser.tokens.contains(token));
    }
    
    @Test
    public void testProcessNonOptionToken() {
        String value = "value";
        
        parser.processNonOptionToken(value);
        
        assertTrue(parser.eatTheRest);
        assertTrue(parser.tokens.contains("--"));
        assertTrue(parser.tokens.contains(value));
    }
    
    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        
        parser.options = options;
        
        String token = "-abc";
        
        parser.burstToken(token, true);
        
        assertTrue(parser.tokens.contains("-a"));
        assertTrue(parser.tokens.contains("-b"));
        assertTrue(parser.tokens.contains("-c"));
    }
}