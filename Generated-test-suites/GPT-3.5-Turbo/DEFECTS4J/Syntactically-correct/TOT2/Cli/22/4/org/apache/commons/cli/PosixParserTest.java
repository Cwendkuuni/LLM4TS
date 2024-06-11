import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertTrue(parser.tokens.isEmpty());
        assertFalse(parser.eatTheRest);
    }

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-a", "-b", "--foo", "bar"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(5, result.length);
        assertEquals("-", result[0]);
        assertEquals("-", result[1]);
        assertEquals("--", result[2]);
        assertEquals("--foo", result[3]);
        assertEquals("bar", result[4]);
    }

    @Test
    public void testProcessNonOptionToken() {
        PosixParser parser = new PosixParser();
        parser.processNonOptionToken("value");
        assertTrue(parser.eatTheRest);
        assertTrue(parser.tokens.contains("--"));
        assertTrue(parser.tokens.contains("value"));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "");
        String token = "-a";
        parser.processOptionToken(token, false);
        assertTrue(parser.tokens.contains(token));

        token = "-c";
        parser.processOptionToken(token, true);
        assertTrue(parser.eatTheRest);

        token = "-d";
        parser.processOptionToken(token, false);
        assertTrue(parser.tokens.contains(token));
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "");
        options.addOption("b", true, "");
        String token = "-ab";
        parser.burstToken(token, false);
        assertTrue(parser.tokens.contains("-a"));
        assertTrue(parser.tokens.contains("-b"));
    }
}