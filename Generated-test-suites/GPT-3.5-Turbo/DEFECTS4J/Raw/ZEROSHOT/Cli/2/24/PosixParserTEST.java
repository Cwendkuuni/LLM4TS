import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {
    
    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        
        assertFalse(parser.eatTheRest);
        assertEquals(0, parser.tokens.size());
        assertNull(parser.currentOption);
    }
    
    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = new String[] {"--option1", "value1", "-option2", "value2"};
        boolean stopAtNonOption = false;
        
        String[] flattened = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(4, flattened.length);
        assertEquals("--option1", flattened[0]);
        assertEquals("value1", flattened[1]);
        assertEquals("-option2", flattened[2]);
        assertEquals("value2", flattened[3]);
    }
    
    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        ArrayList<String> tokens = new ArrayList<>();
        Iterator<String> iter = tokens.iterator();
        
        parser.gobble(iter);
        
        assertEquals(0, parser.tokens.size());
        
        tokens.add("token1");
        tokens.add("token2");
        tokens.add("token3");
        
        iter = tokens.iterator();
        
        parser.gobble(iter);
        
        assertEquals(3, parser.tokens.size());
        assertEquals("token1", parser.tokens.get(0));
        assertEquals("token2", parser.tokens.get(1));
        assertEquals("token3", parser.tokens.get(2));
    }
    
    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        Option option = new Option("o", "option", true, "Option");
        option.setArgName("value");
        parser.currentOption = option;
        
        parser.process("value");
        
        assertEquals(1, parser.tokens.size());
        assertEquals("value", parser.tokens.get(0));
        assertNull(parser.currentOption);
        
        parser.currentOption = new Option("a", "arg", true, "Argument");
        parser.process("value");
        
        assertEquals(2, parser.tokens.size());
        assertEquals("value", parser.tokens.get(1));
    }
    
    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        parser.processSingleHyphen("-");
        
        assertEquals(1, parser.tokens.size());
        assertEquals("-", parser.tokens.get(0));
    }
    
    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option option = new Option("a", "arg", true, "Argument");
        options.addOption(option);
        
        parser.options = options;
        parser.processOptionToken("a", false);
        
        assertEquals(1, parser.tokens.size());
        assertEquals("a", parser.tokens.get(0));
        assertEquals(option, parser.currentOption);
        
        option = new Option("o", "option", false, "Option");
        options.addOption(option);
        
        parser.processOptionToken("o", true);
        
        assertTrue(parser.eatTheRest);
    }
    
    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("o", "option1", true, "Option 1");
        options.addOption("a", "option2", false, "Option 2");
        
        parser.options = options;
        
        parser.burstToken("-oa", false);
        
        assertEquals(2, parser.tokens.size());
        assertEquals("-o", parser.tokens.get(0));
        assertEquals("-a", parser.tokens.get(1));
        
        parser.init();
        parser.burstToken("-abcd", true);
        
        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        
        parser.init();
        parser.burstToken("-abcd", false);
        
        assertEquals(3, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
        assertEquals("-d", parser.tokens.get(2));
    }
}