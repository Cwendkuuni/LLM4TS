import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "-b", "value1", "--option", "value2", "--"};
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("option", true, "Option with value");
        
        String[] result = parser.flatten(options, args, true);
        assertEquals(6, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value1", result[2]);
        assertEquals("--option", result[3]);
        assertEquals("value2", result[4]);
        assertEquals("--", result[5]);
    }
    
    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.process("value");
        assertEquals("--", parser.tokens.get(0));
        assertEquals("value", parser.tokens.get(1));
    }
    
    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        parser.processSingleHyphen("-");
        assertEquals("-", parser.tokens.get(0));
    }
    
    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        
        parser.options = options;
        parser.processOptionToken("-a", true);
        
        assertEquals("-a", parser.tokens.get(0));
        assertTrue(parser.currentOption != null);
        
        parser.processOptionToken("-b", true);
        assertEquals("--", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
        assertTrue(parser.eatTheRest);
    }
    
    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        
        parser.options = options;
        parser.burstToken("-ab", true);
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
        
        parser.burstToken("-c", true);
        assertEquals("--", parser.tokens.get(0));
        assertEquals("-c", parser.tokens.get(1));
        
        parser.burstToken("-d", false);
        assertEquals("-d", parser.tokens.get(0));
    }
}