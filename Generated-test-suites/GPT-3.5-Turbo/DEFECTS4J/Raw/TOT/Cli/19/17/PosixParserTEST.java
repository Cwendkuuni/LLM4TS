import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {
    
    private PosixParser parser;
    private Options options;
    private String[] args;
    
    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
        
        // Add options for testing
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B with argument");
        options.addOption("c", "custom", false, "Custom option");
        
        args = new String[] { "-a", "-b", "argument", "-c", "--", "extra" };
    }
    
    @Test
    public void testFlatten() {
        String[] flattenedArgs = parser.flatten(options, args, true);
        
        assertNotNull(flattenedArgs);
        assertEquals(7, flattenedArgs.length);
        
        assertEquals("-a", flattenedArgs[0]);
        assertEquals("-b", flattenedArgs[1]);
        assertEquals("argument", flattenedArgs[2]);
        assertEquals("-c", flattenedArgs[3]);
        assertEquals("--", flattenedArgs[4]);
        assertEquals("extra", flattenedArgs[5]);
    }
    
    @Test
    public void testProcessOptionToken() {
        parser.processOptionToken("-a", false);
        assertNotNull(parser.currentOption);
        assertEquals("Option A", parser.currentOption.getDescription());
        
        parser.processOptionToken("-d", true);
        assertNull(parser.currentOption);
        
        parser.processOptionToken("-b", true);
        assertNotNull(parser.currentOption);
        assertEquals("Option B with argument", parser.currentOption.getDescription());
    }
    
    @Test
    public void testBurstToken() {
        parser.burstToken("-bargument", false);
        assertNotNull(parser.currentOption);
        assertEquals("Option B with argument", parser.currentOption.getDescription());
        
        parser.burstToken("-d", false);
        assertNull(parser.currentOption);
        
        parser.burstToken("-b", true);
        assertNull(parser.currentOption);
    }
    
    @Test
    public void testProcess() {
        parser.process("value");
        
        assertNull(parser.currentOption);
        assertTrue(parser.eatTheRest);
        assertEquals(3, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("value", parser.tokens.get(1));
    }
    
    @Test
    public void testGobble() {
        parser.eatTheRest = false;
        
        parser.gobble(Arrays.asList("token1", "token2").iterator());
        assertEquals(0, parser.tokens.size());
        
        parser.eatTheRest = true;
        
        parser.gobble(Arrays.asList("token1", "token2").iterator());
        assertEquals("token1", parser.tokens.get(0));
        assertEquals("token2", parser.tokens.get(1));
    }
    
}