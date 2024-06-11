import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {
    
    private PosixParser parser;
    
    @Before
    public void setUp() {
        parser = new PosixParser();
    }
    
    @Test
    public void testFlatten_WithValidOption_ReturnsFlattenedArguments() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        
        String[] arguments = {"-a", "-b", "value"};
        
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        
        assertNotNull(flattenedArguments);
        assertEquals("-a", flattenedArguments[0]);
        assertEquals("-b", flattenedArguments[1]);
        assertEquals("value", flattenedArguments[2]);
    }
    
    @Test
    public void testFlatten_WithInvalidOption_ReturnsFlattenedArguments() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        
        String[] arguments = {"-a", "-b"};
        
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        
        assertNotNull(flattenedArguments);
        assertEquals("-a", flattenedArguments[0]);
        assertEquals("-b", flattenedArguments[1]);
    }
    
    @Test
    public void testFlatten_WithStopAtNonOption_ReturnsFlattenedArgumentsUntilNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        
        String[] arguments = {"-a", "-b", "value", "nonOption"};
        
        String[] flattenedArguments = parser.flatten(options, arguments, true);
        
        assertNotNull(flattenedArguments);
        assertEquals("-a", flattenedArguments[0]);
        assertEquals("-b", flattenedArguments[1]);
        assertEquals("value", flattenedArguments[2]);
        assertEquals("nonOption", flattenedArguments[3]);
    }
    
    // Test other methods in PosixParser class
    
    @Test
    public void testProcess_WithOptionAndHasArg_AddsValueToProcessedTokens() {
        String value = "value";
        parser.process(value);
        
        assertEquals(1, parser.getTokens().size());
        assertEquals(value, parser.getTokens().get(0));
        assertNull(parser.getCurrentOption());
    }
    
    @Test
    public void testProcess_WithOptionAndHasArgs_AddsValueToProcessedTokens() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        
        String value = "value";
        parser.setCurrentOption(options.getOption("a"));
        parser.process(value);
        
        assertEquals(1, parser.getTokens().size());
        assertEquals(value, parser.getTokens().get(0));
        assertNull(parser.getCurrentOption());
    }
    
    @Test
    public void testProcess_WithoutOption_AddsSpecialTokenAndValueToProcessedTokens() {
        parser.setStopAtNonOption(true);
        
        String value = "value";
        parser.process(value);
        
        assertEquals(2, parser.getTokens().size());
        assertEquals("--", parser.getTokens().get(0));
        assertEquals(value, parser.getTokens().get(1));
    }
    
    @Test
    public void testProcessSingleHyphen_WithHyphen_AddsHyphenToProcessedTokens() {
        String hyphen = "-";
        parser.processSingleHyphen(hyphen);
        
        assertEquals(1, parser.getTokens().size());
        assertEquals(hyphen, parser.getTokens().get(0));
    }
    
    @Test
    public void testProcessOptionToken_WithValidOption_AddsTokenToProcessedTokens() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        
        String token = "-a";
        boolean stopAtNonOption = false;
        parser.processOptionToken(token, stopAtNonOption);
        
        assertEquals(1, parser.getTokens().size());
        assertEquals(token, parser.getTokens().get(0));
        assertEquals(options.getOption("a"), parser.getCurrentOption());
    }
    
    @Test
    public void testProcessOptionToken_WithInvalidOptionAndStopAtNonOption_SetsEatTheRest() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        
        String token = "-b";
        boolean stopAtNonOption = true;
        parser.processOptionToken(token, stopAtNonOption);
        
        assertEquals(0, parser.getTokens().size());
        assertTrue(parser.isEatTheRest());
    }
    
    @Test
    public void testBurstToken_WithValidToken_BurstsTokenAndAddsToProcessedTokens() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        
        String token = "-abvalue";
        boolean stopAtNonOption = false;
        parser.burstToken(token, stopAtNonOption);
        
        assertEquals(3, parser.getTokens().size());
        assertEquals("-a", parser.getTokens().get(0));
        assertEquals("-b", parser.getTokens().get(1));
        assertEquals("value", parser.getTokens().get(2));
        assertNull(parser.getCurrentOption());
    }
    
    @Test
    public void testBurstToken_WithInvalidTokenAndStopAtNonOption_AddsTokenAsSingleHyphen() {
        String token = "-cdef";
        boolean stopAtNonOption = true;
        parser.burstToken(token, stopAtNonOption);
        
        assertEquals(4, parser.getTokens().size());
        assertEquals("-", parser.getTokens().get(0));
        assertEquals("-c", parser.getTokens().get(1));
        assertEquals("-d", parser.getTokens().get(2));
        assertEquals("-e", parser.getTokens().get(3));
        assertNull(parser.getCurrentOption());
    }
    
    @Test
    public void testGobble_WithEatTheRest_AddsRemainingTokensToProcessedTokens() {
        Iterator<String> iter = Arrays.asList("-a", "-b", "value").iterator();
        parser.setEatTheRest(true);
        
        parser.gobble(iter);
        
        assertEquals(3, parser.getTokens().size());
        assertEquals("-a", parser.getTokens().get(0));
        assertEquals("-b", parser.getTokens().get(1));
        assertEquals("value", parser.getTokens().get(2));
    }
}