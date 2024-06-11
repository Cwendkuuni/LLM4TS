import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import static org.junit.Assert.*;
import org.junit.Test;

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
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"--option1", "value1", "-a", "-b", "arg1"};
        String[] flattenedArgs = parser.flatten(options, arguments, false);

        assertEquals(6, flattenedArgs.length);
        assertEquals("--option1", flattenedArgs[0]);
        assertEquals("value1", flattenedArgs[1]);
        assertEquals("-", flattenedArgs[2]);
        assertEquals("-a", flattenedArgs[3]);
        assertEquals("-b", flattenedArgs[4]);
        assertEquals("arg1", flattenedArgs[5]);
    }

    @Test
    public void testProcessSpecialToken() {
        PosixParser parser = new PosixParser();
        String token = "--special=value";
        String value = "special=value";

        parser.process(token);
        assertEquals(3, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("special", parser.tokens.get(1));
        assertEquals("value", parser.tokens.get(2));
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        String hyphen = "-";

        parser.processSingleHyphen(hyphen);
        assertEquals(1, parser.tokens.size());
        assertEquals("-", parser.tokens.get(0));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        String token = "-a";

        parser.processOptionToken(token, false);
        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertNotNull(parser.currentOption);
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        String token = "-abvalue";

        parser.burstToken(token, false);
        assertEquals(3, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
        assertEquals("value", parser.tokens.get(2));
    }
}