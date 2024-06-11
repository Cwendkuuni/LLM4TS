import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertEquals(parser.getTokens().size(), 0);
        assertFalse(parser.isEatTheRest());
    }

    @Test
    public void testFlatten_Options() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", "alpha", false, "desc");
        options.addOption("b", "beta", true, "desc");
        options.addOption("c", "charlie", false, "desc");

        String[] arguments = {"--alpha", "-b", "value"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(result.length, 2);
        assertEquals(result[0], "--alpha");
        assertEquals(result[1], "-b value");
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", "alpha", false, "desc");
        options.addOption("b", "beta", true, "desc");
        options.addOption("c", "charlie", false, "desc");

        String[] arguments = {"--alpha", "-b", "value", "nonOption"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(result.length, 3);
        assertEquals(result[0], "--alpha");
        assertEquals(result[1], "-b value");
        assertEquals(result[2], "nonOption");
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", "alpha", false, "desc");
        options.addOption("b", "beta", true, "desc");
        options.addOption("c", "charlie", false, "desc");

        parser.processOptionToken("-a", false);
        assertEquals(parser.getTokens().get(0), "-a");

        parser.processOptionToken("-d", false);
        assertEquals(parser.getTokens().get(1), "-d");
    }

    @Test
    public void testProcessNonOptionToken() {
        PosixParser parser = new PosixParser();

        parser.processNonOptionToken("value");
        assertEquals(parser.getTokens().get(0), "--");
        assertEquals(parser.getTokens().get(1), "value");
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", "alpha", false, "desc");
        options.addOption("b", "beta", true, "desc");
        options.addOption("c", "charlie", false, "desc");

        parser.burstToken("-abc", false);
        assertEquals(parser.getTokens().get(0), "-a");
        assertEquals(parser.getTokens().get(1), "-b");
        assertEquals(parser.getTokens().get(2), "-c");

        parser.burstToken("-abdef", true);
        assertEquals(parser.getTokens().get(3), "--");
        assertEquals(parser.getTokens().get(4), "def");
    }
}