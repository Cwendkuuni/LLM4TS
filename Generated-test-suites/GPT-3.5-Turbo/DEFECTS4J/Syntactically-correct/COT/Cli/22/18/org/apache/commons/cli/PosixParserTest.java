
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        // Create options
        Options options = new Options();
        options.addOption(new Option("a", false, "Option A"));
        options.addOption(new Option("b", true, "Option B"));
        
        // Create PosixParser object
        PosixParser parser = new PosixParser();
        
        // Test normal case
        String[] args1 = {"--a", "value", "arg1", "arg2"};
        String[] expected1 = {"--a", "value", "arg1", "arg2"};
        try {
            assertArrayEquals(expected1, parser.flatten(options, args1, false));
        } catch (ParseException e) {
            e.printStackTrace();
            fail("ParseException thrown");
        }
        
        // Test stopAtNonOption case
        String[] args2 = {"--a", "value", "arg1", "arg2"};
        String[] expected2 = {"--", "value", "arg1", "arg2"};
        try {
            assertArrayEquals(expected2, parser.flatten(options, args2, true));
        } catch (ParseException e) {
            e.printStackTrace();
            fail("ParseException thrown");
        }
    }
    
    @Test
    public void testProcessNonOptionToken() {
        // Create PosixParser object
        PosixParser parser = new PosixParser();
        
        // Test normal case
        String value = "value";
        parser.processNonOptionToken(value);
        assertTrue(parser.isEatTheRest());
        assertEquals("--", parser.getTokens().get(0));
        assertEquals(value, parser.getTokens().get(1));
    }
    
    @Test
    public void testProcessOptionToken() {
        // Create PosixParser object
        PosixParser parser = new PosixParser();
        
        // Create options
        Options options = new Options();
        options.addOption(new Option("a", false, "Option A"));
        
        // Test normal case
        String token = "-a";
        parser.processOptionToken(token, false);
        assertFalse(parser.isEatTheRest());
        assertEquals(token, parser.getTokens().get(0));
        
        // Test stopAtNonOption case
        String token2 = "-b";
        parser.processOptionToken(token2, true);
        assertTrue(parser.isEatTheRest());
        assertEquals(token2, parser.getTokens().get(1));
    }
    
    @Test
    public void testBurstToken() {
        // Create PosixParser object
        PosixParser parser = new PosixParser();
        
        // Create options
        Options options = new Options();
        options.addOption(new Option("a", true, "Option A"));
        
        // Test Option exists case
        String token = "-aValue";
        parser.burstToken(token, false);
        assertFalse(parser.isEatTheRest());
        assertEquals("-a", parser.getTokens().get(0));
        assertEquals("Value", parser.getTokens().get(1));
        
        // Test non-Option case with stopAtNonOption
        String token2 = "-b";
        parser.burstToken(token2, true);
        assertTrue(parser.isEatTheRest());
        assertEquals("--", parser.getTokens().get(2));
        assertEquals("b", parser.getTokens().get(3));
        
        // Test non-Option case without stopAtNonOption
        String token3 = "-b";
        parser.burstToken(token3, false);
        assertFalse(parser.isEatTheRest());
        assertEquals("-b", parser.getTokens().get(4));
    }
}
