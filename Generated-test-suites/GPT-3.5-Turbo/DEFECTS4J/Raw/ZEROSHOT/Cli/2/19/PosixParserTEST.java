import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

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
        String[] arguments = {"-a", "arg1", "arg2"};
        boolean stopAtNonOption = false;
        
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("arg2", result[2]);
    }
    
    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        Iterator iter = Arrays.asList("arg1", "arg2").iterator();
        parser.tokens.add("-a");
        parser.gobble(iter);
        
        assertEquals(3, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("arg1", parser.tokens.get(1));
        assertEquals("arg2", parser.tokens.get(2));
    }
    
    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.currentOption = new Option("a", "test option");
        parser.process("value");
        
        assertEquals(1, parser.tokens.size());
        assertEquals("value", parser.tokens.get(0));
        assertNull(parser.currentOption);
        
        parser.init();
        parser.currentOption = new Option("a", "test option", true);
        parser.process("value");
        
        assertEquals(1, parser.tokens.size());
        assertEquals("value", parser.tokens.get(0));
        assertNull(parser.currentOption);
        
        parser.init();
        parser.currentOption = new Option("a", "test option", false, true);
        parser.process("value");
        
        assertEquals(1, parser.tokens.size());
        assertEquals("value", parser.tokens.get(0));
        assertNull(parser.currentOption);
        
        parser.init();
        parser.currentOption = null;
        parser.process("value");
        
        assertEquals(3, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("value", parser.tokens.get(1));
        assertNull(parser.currentOption);
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
        parser.options = new Options();
        parser.options.addOption(new Option("a", "test option"));
        parser.options.addOption(new Option("b", "test option"));
        
        parser.processOptionToken("-a", false);
        
        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertNotNull(parser.currentOption);
        
        parser.init();
        parser.processOptionToken("-c", true);
        
        assertEquals(0, parser.tokens.size());
        assertNull(parser.currentOption);
        assertTrue(parser.eatTheRest);
        
    }
    
    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        parser.options = new Options();
        parser.options.addOption(new Option("a", "test option"));
        parser.options.addOption(new Option("b", "test option"));
        parser.options.addOption(new Option("c", "test option", true));
        parser.options.addOption(new Option("d", "test option", false, true));
        
        parser.burstToken("-abcd", false);
        
        assertEquals(4, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
        assertEquals("-c", parser.tokens.get(2));
        assertEquals("-d", parser.tokens.get(3));
        
        parser.init();
        parser.burstToken("-cvalue", false);
        
        assertEquals(2, parser.tokens.size());
        assertEquals("-c", parser.tokens.get(0));
        assertEquals("value", parser.tokens.get(1));
        
        parser.init();
        parser.burstToken("-efg", true);
        
        assertEquals(1, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        
        parser.init();
        parser.burstToken("-efg", false);
        
        assertEquals(3, parser.tokens.size());
        assertEquals("-e", parser.tokens.get(0));
        assertEquals("-f", parser.tokens.get(1));
        assertEquals("-g", parser.tokens.get(2));
    }
}