import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertNull(parser.currentOption);
        assertFalse(parser.eatTheRest);
        assertEquals(0, parser.tokens.size());
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        PosixParser parser = new PosixParser();
        String[] args = {"-a", "-b", "value"};
        String[] result = parser.flatten(options, args, true);
        
        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
        assertEquals("--", result[3]);
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
        options.addOption("a", false, "Option a");

        PosixParser parser = new PosixParser();
        parser.processOptionToken("a", false);
        assertNotNull(parser.currentOption);
        assertEquals("a", parser.currentOption.getOpt());
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        PosixParser parser = new PosixParser();
        parser.burstToken("-abc", false);
        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
    }

}