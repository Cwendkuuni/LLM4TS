import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertTrue(parser.tokens.isEmpty());
        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "value"};
        String[] flattened = parser.flatten(options, arguments, true);

        assertEquals(2, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("value", flattened[1]);
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.process("value");

        assertTrue(parser.eatTheRest);
        assertFalse(parser.tokens.isEmpty());
        assertEquals(2, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("value", parser.tokens.get(1));
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        parser.processSingleHyphen("-");

        assertFalse(parser.tokens.isEmpty());
        assertEquals(1, parser.tokens.size());
        assertEquals("-", parser.tokens.get(0));
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        PosixParser parser = new PosixParser();
        parser.options = options;

        parser.processOptionToken("-a", false);
        assertNotNull(parser.currentOption);
        assertFalse(parser.tokens.isEmpty());
        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("a", parser.currentOption.getOpt());
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        PosixParser parser = new PosixParser();
        parser.options = options;

        parser.burstToken("-ab", false);

        assertFalse(parser.tokens.isEmpty());
        assertEquals(2, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
    }
}