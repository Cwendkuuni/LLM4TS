import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        // Test that eatTheRest is set to false
        assertFalse(parser.eatTheRest);
        // Test that tokens list is cleared
        assertEquals(0, parser.tokens.size());
    }

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", "apple", false, "apple option");
        options.addOption("b", "banana", true, "banana option");

        String[] arguments = {"--apple", "aValue", "bValue", "--banana=123"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(5, result.length);
        assertEquals("--apple", result[0]);
        assertEquals("aValue", result[1]);
        assertEquals("bValue", result[2]);
        assertEquals("--banana", result[3]);
        assertEquals("123", result[4]);
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", "apple", false, "apple option");
        String token = "-apple";
        boolean stopAtNonOption = false;

        parser.burstToken(token, stopAtNonOption);

        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
    }

    @Test
    public void testProcessNonOptionToken() {
        PosixParser parser = new PosixParser();
        parser.processNonOptionToken("value");

        assertTrue(parser.eatTheRest);
        assertEquals(2, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("value", parser.tokens.get(1));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", "apple", false, "apple option");
        String token = "-apple";
        boolean stopAtNonOption = false;

        parser.processOptionToken(token, stopAtNonOption);

        assertEquals(1, parser.tokens.size());
        assertEquals("-apple", parser.tokens.get(0));
    }
}