import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertTrue(parser.tokens.isEmpty());
        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);
    }
    
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        
        String[] arguments = {"-a", "-b", "value1", "--", "-c", "value2"};
        
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, true);
        
        assertEquals(6, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("-b", flattened[1]);
        assertEquals("value1", flattened[2]);
        assertEquals("--", flattened[3]);
        assertEquals("-c", flattened[4]);
        assertEquals("value2", flattened[5]);
    }
    
    @Test
    public void testProcess() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        
        String[] arguments = {"value1", "value2"};
        
        PosixParser parser = new PosixParser();
        parser.init();
        parser.options = options;
        
        parser.process(arguments[0]);
        assertEquals(3, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("value1", parser.tokens.get(1));
        assertEquals("value2", parser.tokens.get(2));
        
        parser.init();
        parser.currentOption = options.getOption("a");
        parser.process(arguments[0]);
        assertEquals(2, parser.tokens.size());
        assertEquals("value1", parser.tokens.get(0));
        assertEquals("value2", parser.tokens.get(1));
        
        parser.init();
        parser.currentOption = options.getOption("b");
        parser.process(arguments[0]);
        assertEquals(1, parser.tokens.size());
        assertEquals("value1", parser.tokens.get(0));
    }
    
    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        parser.init();
        parser.processSingleHyphen("-");
        assertEquals(1, parser.tokens.size());
        assertEquals("-", parser.tokens.get(0));
    }
    
    @Test
    public void testProcessOptionTokenWithOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        
        PosixParser parser = new PosixParser();
        parser.init();
        parser.options = options;
        
        parser.processOptionToken("-a", true);
        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals(options.getOption("a"), parser.currentOption);
    }
    
    @Test
    public void testProcessOptionTokenWithoutOption() {
        Options options = new Options();
        
        PosixParser parser = new PosixParser();
        parser.init();
        parser.options = options;
        
        parser.processOptionToken("-b", true);
        assertTrue(parser.eatTheRest);
        
        parser.init();
        parser.processOptionToken("-b", false);
        assertEquals(1, parser.tokens.size());
        assertEquals("-b", parser.tokens.get(0));
    }
    
    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        
        PosixParser parser = new PosixParser();
        parser.init();
        parser.options = options;
        
        parser.burstToken("-ab", true);
        assertEquals(3, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
        assertEquals(options.getOption("b"), parser.currentOption);
        
        parser.init();
        parser.burstToken("-c", true);
        assertEquals(4, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("-c", parser.tokens.get(1));
        assertEquals("--", parser.tokens.get(2));
        assertEquals("-c", parser.tokens.get(3));
    }

}