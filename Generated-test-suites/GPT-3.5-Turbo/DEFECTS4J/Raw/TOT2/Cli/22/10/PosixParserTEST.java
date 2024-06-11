import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        // Check if eatTheRest is false
        assertFalse(parser.eatTheRest);
        // Check if tokens list is empty
        assertTrue(parser.tokens.isEmpty());
    }

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Test option a");
        options.addOption("b", true, "Test option b");
        
        String[] arguments = {"--foo", "-a", "value", "-b", "123", "extra"};
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(5, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("-a", result[1]);
        assertEquals("value", result[2]);
        assertEquals("-b", result[3]);
        assertEquals("123", result[4]);
    }

    @Test
    public void testProcessNonOptionToken() {
        PosixParser parser = new PosixParser();
        parser.processNonOptionToken("test");
        // Check if eatTheRest is true
        assertTrue(parser.eatTheRest);
        // Check if tokens list has the correct values
        assertEquals(2, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("test", parser.tokens.get(1));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("x", false, "Test option x");
        
        parser.processOptionToken("-x", true);
        // Check if eatTheRest is false
        assertFalse(parser.eatTheRest);
        // Check if tokens list has the correct value
        assertEquals(1, parser.tokens.size());
        assertEquals("-x", parser.tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("z", true, "Test option z");
        
        String token = "-zvalue";
        parser.burstToken(token, true);
        // Check if tokens list has the correct values
        assertEquals(2, parser.tokens.size());
        assertEquals("-z", parser.tokens.get(0));
        assertEquals("value", parser.tokens.get(1));
    }
}