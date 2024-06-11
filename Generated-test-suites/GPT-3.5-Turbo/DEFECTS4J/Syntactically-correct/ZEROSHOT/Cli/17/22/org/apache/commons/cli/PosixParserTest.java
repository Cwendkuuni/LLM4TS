import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        
        PosixParser parser = new PosixParser();
        
        String[] args = {"-a", "-b", "value", "-c", "value1", "value2"};
        String[] result = parser.flatten(options, args, false);
        
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
        assertEquals("-c", result[3]);
        assertEquals("value1", result[4]);
        assertEquals("value2", result[5]);
    }
    
    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        
        parser.processSingleHyphen("-");
        String[] result = (String[]) parser.getTokens().toArray(new String[parser.getTokens().size()]);
        
        assertEquals("-", result[0]);
    }
    
    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        
        PosixParser parser = new PosixParser();
        parser.setOptions(options);
        
        parser.processOptionToken("a", false);
        String[] result = (String[]) parser.getTokens().toArray(new String[parser.getTokens().size()]);
        
        assertEquals("a", result[0]);
        
        parser.processOptionToken("b", true);
        result = (String[]) parser.getTokens().toArray(new String[parser.getTokens().size()]);
        
        assertEquals("a", result[0]);
    }
    
    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        
        PosixParser parser = new PosixParser();
        parser.setOptions(options);
        
        parser.burstToken("-ab", false);
        String[] result = (String[]) parser.getTokens().toArray(new String[parser.getTokens().size()]);
        
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        
        parser.burstToken("-bc", true);
        result = (String[]) parser.getTokens().toArray(new String[parser.getTokens().size()]);
        
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
    }
}