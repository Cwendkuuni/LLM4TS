import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {
    
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        
        String[] arguments = {"-a", "-b", "value"};
        
        String[] result = PosixParser.flatten(options, arguments, false);
        
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
    }
    
    @Test
    public void testFlatten_stopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        
        String[] arguments = {"-a", "-b", "value", "nonOption"};
        
        String[] result = PosixParser.flatten(options, arguments, true);
        
        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
        assertEquals("nonOption", result[3]);
    }
    
    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        List<String> tokens = new ArrayList<>();
        
        parser.processSingleHyphen(tokens, "-");
        
        assertEquals(1, tokens.size());
        assertEquals("-", tokens.get(0));
    }
    
    @Test
    public void testProcessSingleHyphen_emptyArgument() {
        PosixParser parser = new PosixParser();
        List<String> tokens = new ArrayList<>();
        
        parser.processSingleHyphen(tokens, "");
        
        assertEquals(0, tokens.size());
    }
    
    @Test
    public void testProcessOptionToken_hasOption() {
        PosixParser parser = new PosixParser();
        List<String> tokens = new ArrayList<>();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        
        parser.processOptionToken(tokens, options, "a");
        
        assertEquals(1, tokens.size());
        assertEquals("a", tokens.get(0));
    }
    
    @Test
    public void testProcessOptionToken_stopAtNonOption() {
        PosixParser parser = new PosixParser();
        List<String> tokens = new ArrayList<>();
        Options options = new Options();
        
        parser.processOptionToken(tokens, options, "a");
        
        assertEquals(0, tokens.size());
    }
    
    @Test
    public void testProcessOptionToken_noOption_stopAtNonOption() {
        PosixParser parser = new PosixParser();
        List<String> tokens = new ArrayList<>();
        Options options = new Options();
        
        parser.processOptionToken(tokens, options, "a");
        
        assertEquals(0, tokens.size());
    }
    
    @Test
    public void testProcessOptionToken_noOption_noStopAtNonOption() {
        PosixParser parser = new PosixParser();
        List<String> tokens = new ArrayList<>();
        Options options = new Options();
        
        parser.processOptionToken(tokens, options, "a");
        
        assertEquals(1, tokens.size());
        assertEquals("a", tokens.get(0));
    }
    
    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        List<String> tokens = new ArrayList<>();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        
        parser.burstToken(tokens, options, "-ab");
        
        assertEquals(2, tokens.size());
        assertEquals("-a", tokens.get(0));
        assertEquals("-b", tokens.get(1));
    }
    
    @Test
    public void testBurstToken_stopAtNonOption() {
        PosixParser parser = new PosixParser();
        List<String> tokens = new ArrayList<>();
        Options options = new Options();
        
        parser.burstToken(tokens, options, "-ab");
        
        assertEquals(1, tokens.size());
        assertEquals("-ab", tokens.get(0));
    }
    
    @Test
    public void testBurstToken_noOption_stopAtNonOption() {
        PosixParser parser = new PosixParser();
        List<String> tokens = new ArrayList<>();
        Options options = new Options();
        
        parser.burstToken(tokens, options, "-ab");
        
        assertEquals(1, tokens.size());
        assertEquals("-ab", tokens.get(0));
    }
    
    @Test
    public void testBurstToken_noOption_noStopAtNonOption() {
        PosixParser parser = new PosixParser();
        List<String> tokens = new ArrayList<>();
        Options options = new Options();
        
        parser.burstToken(tokens, options, "-ab");
        
        assertEquals(2, tokens.size());
        assertEquals("-a", tokens.get(0));
        assertEquals("-b", tokens.get(1));
    }
}