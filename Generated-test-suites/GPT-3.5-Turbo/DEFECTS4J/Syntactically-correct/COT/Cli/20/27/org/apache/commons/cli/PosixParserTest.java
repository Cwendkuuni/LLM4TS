import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {
    
    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        
        assertFalse(parser.eatTheRest);
        assertNotNull(parser.tokens);
        assertEquals(0, parser.tokens.size());
        assertNull(parser.currentOption);
    }
    
    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        
        String[] arguments = new String[]{"-a", "arg1", "-b", "arg2", "arg3"};
        String[] flattened = parser.flatten(options, arguments, true);
        
        assertEquals(5, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("arg1", flattened[1]);
        assertEquals("-b", flattened[2]);
        assertEquals("arg2", flattened[3]);
        assertEquals("arg3", flattened[4]);
    }
    
    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        parser.tokens.add("arg1");
        parser.tokens.add("arg2");
        
        Iterator iter = Arrays.asList(new String[]{"arg3", "arg4"}).iterator();
        parser.gobble(iter);
        
        assertEquals(4, parser.tokens.size());
        assertEquals("arg1", parser.tokens.get(0));
        assertEquals("arg2", parser.tokens.get(1));
        assertEquals("arg3", parser.tokens.get(2));
        assertEquals("arg4", parser.tokens.get(3));
    }
    
    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.currentOption = new Option("a", "optionA", false, "Option A");
        
        parser.process("arg1");
        
        assertEquals(0, parser.tokens.size());
        assertNull(parser.currentOption);
        
        parser.currentOption = new Option("b", "optionB", true, "Option B");
        
        parser.process("arg2");
        
        assertEquals(1, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertNull(parser.currentOption);
        
        parser.eatTheRest = false;
        parser.process("arg3");
        
        assertEquals(2, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("arg3", parser.tokens.get(1));
    }
    
    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        
        boolean stopAtNonOption = true;
        
        parser.processOptionToken("-a", stopAtNonOption);
        
        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertNotNull(parser.currentOption);
        
        parser.tokens.clear();
        parser.currentOption = null;
        
        parser.processOptionToken("-b", stopAtNonOption);
        
        assertEquals(0, parser.tokens.size());
        assertNull(parser.currentOption);
        
        stopAtNonOption = false;
        
        parser.processOptionToken("-b", stopAtNonOption);
        
        assertEquals(1, parser.tokens.size());
        assertEquals("-b", parser.tokens.get(0));
        assertNull(parser.currentOption);
    }
    
    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        
        boolean stopAtNonOption = true;
        
        parser.burstToken("-abc", stopAtNonOption);
        
        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertNull(parser.currentOption);
        
        parser.tokens.clear();
        parser.currentOption = null;
        
        parser.burstToken("-ab", stopAtNonOption);
        
        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertNotNull(parser.currentOption);
        
        parser.tokens.clear();
        parser.currentOption = null;
        
        parser.burstToken("-b", stopAtNonOption);
        
        assertEquals(1, parser.tokens.size());
        assertEquals("-b", parser.tokens.get(0));
        assertNull(parser.currentOption);
        
        parser.tokens.clear();
        parser.currentOption = null;
        stopAtNonOption = false;
        
        parser.burstToken("-abc", stopAtNonOption);
        
        assertEquals(3, parser.tokens.size());
        assertEquals("-", parser.tokens.get(0));
        assertEquals("a", parser.tokens.get(1));
        assertEquals("bc", parser.tokens.get(2));
        assertNull(parser.currentOption);
    }
}