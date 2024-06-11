import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        // Test init method
        parser.init();
        assertEquals(parser.tokens.size(), 0);
        assertFalse(parser.eatTheRest);
    }

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Description of option 'a'");
        options.addOption("b", true, "Description of option 'b'");
        
        // Test flatten method
        String[] arguments = {"-a", "--", "-b", "value"};
        String[] flattenedTokens = parser.flatten(options, arguments, true);
        
        assertEquals(flattenedTokens.length, 5);
        assertEquals(flattenedTokens[0], "-a");
        assertEquals(flattenedTokens[1], "--");
        assertEquals(flattenedTokens[2], "-b");
        assertEquals(flattenedTokens[3], "value");
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        Iterator<String> iter = Arrays.asList("test1", "test2", "test3").iterator();
        
        // Test gobble method
        parser.gobble(iter);
        assertEquals(parser.tokens.size(), 0);
        
        parser.eatTheRest = true;
        parser.gobble(iter);
        assertEquals(parser.tokens.size(), 3);
    }

    @Test
    public void testProcessNonOptionToken() {
        PosixParser parser = new PosixParser();
        String value = "testValue";
        
        // Test processNonOptionToken method
        parser.processNonOptionToken(value);
        assertTrue(parser.eatTheRest);
        assertEquals(parser.tokens.size(), 2);
        assertEquals(parser.tokens.get(0), "--");
        assertEquals(parser.tokens.get(1), value);
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        String token = "-a";
        
        // Test processOptionToken method
        parser.processOptionToken(token, true);
        assertTrue(parser.eatTheRest);
        assertEquals(parser.tokens.size(), 1);
        assertEquals(parser.tokens.get(0), token);
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Description of option 'a'");
        String token = "-abcd";
        
        // Test burstToken method
        parser.burstToken(token, true);
        
        assertEquals(parser.tokens.size(), 2);
        assertEquals(parser.tokens.get(0), "-a");
        assertEquals(parser.tokens.get(1), "-bcd");
    }

}