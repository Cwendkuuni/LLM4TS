import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        List tokens = parser.getTokens();
        assertTrue(tokens.isEmpty());
    }
    
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        
        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "--b=value", "c"};
        
        String[] flattened = parser.flatten(options, arguments, true);
        assertEquals(4, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("--b", flattened[1]);
        assertEquals("value", flattened[2]);
        assertEquals("c", flattened[3]);
    }
    
    @Test
    public void testProcessNonOptionToken() {
        PosixParser parser = new PosixParser();
        parser.processNonOptionToken("value");
        List tokens = parser.getTokens();
        assertEquals(2, tokens.size());
        assertEquals("--", tokens.get(0));
        assertEquals("value", tokens.get(1));
    }
    
    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        parser.processOptionToken("-a", true);
        List tokens = parser.getTokens();
        assertEquals(1, tokens.size());
        assertEquals("-a", tokens.get(0));
    }
    
    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        
        PosixParser parser = new PosixParser();
        parser.setOptions(options);
        parser.burstToken("-ab", true);
        List tokens = parser.getTokens();
        assertEquals(2, tokens.size());
        assertEquals("-a", tokens.get(0));
        assertEquals("-b", tokens.get(1));
    }
}