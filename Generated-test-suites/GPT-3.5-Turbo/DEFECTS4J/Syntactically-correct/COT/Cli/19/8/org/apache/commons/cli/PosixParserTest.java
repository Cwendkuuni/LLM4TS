
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        
        // Assertions
        assertFalse(parser.eatTheRest);
        assertTrue(parser.tokens.isEmpty());
        assertNull(parser.currentOption);
    }
    
    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", "option-c", true, "Option c");

        String[] arguments = {"-a", "--option-c=abc", "test"};
        String[] expected = {"-a", "--option-c", "abc", "test"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        // Assertions
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.currentOption = new Option("a", false, "Option a");
        
        parser.process("value");
        
        // Assertions
        assertNull(parser.currentOption);
        assertTrue(parser.tokens.contains("value"));
    }
    
    @Test
    public void testProcessOptionTokenExists() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        parser.options = options;
        
        boolean stopAtNonOption = true;
        
        parser.processOptionToken("-a", stopAtNonOption);
        
        // Assertions
        assertNotNull(parser.currentOption);
        assertTrue(parser.tokens.contains("-a"));
    }
    
    @Test
    public void testProcessOptionTokenNotExists() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        parser.options = options;
        
        boolean stopAtNonOption = true;
        
        parser.processOptionToken("-b", stopAtNonOption);
        
        // Assertions
        assertNull(parser.currentOption);
        assertTrue(parser.tokens.contains("-b"));
    }
    
    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        List<String> tokens = Arrays.asList("value1", "value2", "value3");
        Iterator iter = tokens.iterator();
        
        parser.eatTheRest = true;
        parser.gobble(iter);
        
        // Assertions
        assertTrue(parser.tokens.containsAll(tokens));
    }
    
    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        parser.options = options;
        
        boolean stopAtNonOption = true;
        
        String token = "-babc";
        
        parser.burstToken(token, stopAtNonOption);
        
        // Assertions
        assertTrue(parser.tokens.contains("-b"));
        assertTrue(parser.tokens.contains("abc"));
    }
}
