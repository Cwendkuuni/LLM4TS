import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        List tokens = parser.tokens;
        assertEquals(0, tokens.size());
        assertFalse(parser.eatTheRest);
    }

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-a", "arg1", "--option", "value"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertEquals(4, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("arg1", flattened[1]);
        assertEquals("--option", flattened[2]);
        assertEquals("value", flattened[3]);
    }

    @Test
    public void testProcessNonOptionToken() {
        PosixParser parser = new PosixParser();
        parser.processNonOptionToken("value");
        List tokens = parser.tokens;
        assertEquals(2, tokens.size());
        assertEquals("--", tokens.get(0));
        assertEquals("value", tokens.get(1));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "option a");
        parser.options = options;
        parser.processOptionToken("-a", false);
        List tokens = parser.tokens;
        assertEquals(1, tokens.size());
        assertEquals("-a", tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "option a");
        parser.options = options;
        parser.burstToken("-ab", false);
        List tokens = parser.tokens;
        assertEquals(2, tokens.size());
        assertEquals("-a", tokens.get(0));
        assertEquals("b", tokens.get(1));
    }

}