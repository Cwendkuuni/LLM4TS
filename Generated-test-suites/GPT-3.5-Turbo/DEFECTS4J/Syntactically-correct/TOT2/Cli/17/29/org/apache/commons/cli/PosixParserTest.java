import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertNotNull(parser.options);
        assertFalse(parser.eatTheRest);
        assertEquals(0, parser.tokens.size());
        assertNull(parser.currentOption);
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", "alpha", false, "Option alpha");
        options.addOption("b", "beta", true, "Option beta");

        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--", "arg1", "-b", "value"};
        String[] tokens = parser.flatten(options, args, false);
        assertEquals(5, tokens.length);
        assertEquals("-a", tokens[0]);
        assertEquals("--", tokens[1]);
        assertEquals("arg1", tokens[2]);
        assertEquals("-b", tokens[3]);
        assertEquals("value", tokens[4]);
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.process("value");
        assertEquals(2, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("value", parser.tokens.get(1));
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
        Options options = new Options();
        options.addOption("a", "alpha", false, "Option alpha");

        PosixParser parser = new PosixParser();
        parser.processOptionToken("-a", false);
        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));

        parser.processOptionToken("-b", false);
        assertEquals(1, parser.tokens.size());

        parser.processOptionToken("-b", true);
        assertTrue(parser.eatTheRest);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", "alpha", false, "Option alpha");

        PosixParser parser = new PosixParser();
        parser.burstToken("-ab", false);
        assertEquals(1, parser.tokens.size());
        assertEquals("-ab", parser.tokens.get(0));

        parser.burstToken("-ab", true);
        assertEquals(1, parser.tokens.size());

        parser.burstToken("-abc", false);
        assertEquals(1, parser.tokens.size());
    }

}