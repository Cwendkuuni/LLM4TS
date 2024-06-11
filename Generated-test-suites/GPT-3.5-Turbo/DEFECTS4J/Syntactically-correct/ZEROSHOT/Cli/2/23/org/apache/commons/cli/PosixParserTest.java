import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        PosixParser parser = new PosixParser();
        String[] arguments = parser.flatten(options, new String[]{"-a", "-b", "value"}, true);

        assertEquals(4, arguments.length);
        assertEquals("-a", arguments[0]);
        assertEquals("-b", arguments[1]);
        assertEquals("value", arguments[2]);
        assertEquals("--", arguments[3]);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        ArrayList tokens = new ArrayList();
        parser.processSingleHyphen("-", tokens);

        assertEquals(1, tokens.size());
        assertEquals("-", tokens.get(0));
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        PosixParser parser = new PosixParser(options);
        ArrayList tokens = new ArrayList();
        parser.processOptionToken("a", tokens);

        assertEquals(1, tokens.size());
        assertEquals("a", tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");
        PosixParser parser = new PosixParser(options);
        ArrayList tokens = new ArrayList();
        parser.burstToken("-ab", tokens);

        assertEquals(2, tokens.size());
        assertEquals("-a", tokens.get(0));
        assertEquals("-b", tokens.get(1));
    }

}