import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Options;

public class PosixParserTest {
    
    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-a", "-b", "value"};
        String[] result = parser.flatten(options, arguments, true);
        String[] expectedResult = {"-a", "-b", "value"};
        assertArrayEquals(expectedResult, result);
    }
    
    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertEquals(parser.getTokens().size(), 0);
        assertFalse(parser.isEatTheRest());
        assertNull(parser.getCurrentOption());
    }
    
    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "value"};
        parser.process("-a");
        assertEquals(parser.getTokens().size(), 1);
        parser.process("value");
        assertEquals(parser.getTokens().size(), 2);
    }
    
    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        parser.processSingleHyphen("-");
        assertEquals(parser.getTokens().get(0), "-");
    }
    
    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "");
        parser.setOptions(options);
        
        parser.processOptionToken("a", true);
        assertNotNull(parser.getCurrentOption());
        assertEquals(parser.getTokens().get(0), "a");
        
        parser.processOptionToken("b", true);
        assertNull(parser.getCurrentOption());
        assertEquals(parser.getTokens().size(), 1);
    }
    
    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "");
        options.addOption("b", true, "");
        parser.setOptions(options);
        
        parser.burstToken("-ab", true);
        assertNotNull(parser.getCurrentOption());
        assertEquals(parser.getTokens().get(0), "-a");
        assertEquals(parser.getTokens().get(1), "-b");
    }
}