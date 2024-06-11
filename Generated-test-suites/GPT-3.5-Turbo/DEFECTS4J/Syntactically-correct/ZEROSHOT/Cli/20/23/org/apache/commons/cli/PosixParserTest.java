import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption(new Option("a", false, "Option a"));
        options.addOption(new Option("b", true, "Option b with argument"));
        options.addOption(new Option("c", true, "Option c with argument"));
        
        String[] arguments = {"-a", "--b", "argument", "-c", "argument"};
        
        String[] flattened = parser.flatten(options, arguments, false);
        
        assertEquals(5, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("--b", flattened[1]);
        assertEquals("argument", flattened[2]);
        assertEquals("-c", flattened[3]);
        assertEquals("argument", flattened[4]);
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption(new Option("a", false, "Option a"));
        
        String token = "-a";
        
        parser.processOptionToken(token, false);
        
        assertEquals(options.getOption("a"), parser.currentOption);
        assertEquals("-a", parser.tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption(new Option("a", false, "Option a"));
        options.addOption(new Option("b", true, "Option b with argument"));
        options.addOption(new Option("c", true, "Option c with argument"));
        
        String token = "-abc";
        
        parser.burstToken(token, false);
        
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
        assertEquals("-c", parser.tokens.get(2));
    }

    @Test
    public void testProcess() {
        Options options = new Options();
        options.addOption(new Option("a", false, "Option a"));
        options.addOption(new Option("b", true, "Option b with argument"));
        options.addOption(new Option("c", true, "Option c with argument"));
        
        String value = "value";
        
        parser.currentOption = options.getOption("b");
        
        parser.process(value);
        
        assertEquals(null, parser.currentOption);
        assertEquals("value", parser.tokens.get(0));
    }
    
}