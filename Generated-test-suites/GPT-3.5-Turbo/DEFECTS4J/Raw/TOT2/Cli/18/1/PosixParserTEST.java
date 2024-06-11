import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertTrue(parser.getTokens().isEmpty());
        assertFalse(parser.isEatTheRest());
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("h", "help", false, "print help");
        options.addOption("o", "output", true, "output file path");

        PosixParser parser = new PosixParser();
        String[] arguments = {"-h", "-o", "output.txt"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-h", result[0]);
        assertEquals("-o", result[1]);
        assertEquals("output.txt", result[2]);
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        Iterator<String> iterator = Arrays.asList("1", "2", "3").iterator();
        parser.gobble(iterator);
        assertTrue(parser.getTokens().isEmpty());
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.process("value");
        assertTrue(parser.isEatTheRest());
        assertEquals(2, parser.getTokens().size());
        assertEquals("--", parser.getTokens().get(0));
        assertEquals("value", parser.getTokens().get(1));
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        parser.processSingleHyphen("-");
        assertEquals(1, parser.getTokens().size());
        assertEquals("-", parser.getTokens().get(0));
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("o", "output", true, "output file path");

        PosixParser parser = new PosixParser();

        parser.processOptionToken("-o", false);
        assertEquals("-o", parser.getTokens().get(0));
        assertNotNull(parser.getCurrentOption());

        parser.getTokens().clear();
        parser.setCurrentOption(null);

        parser.processOptionToken("-x", false);
        assertTrue(parser.isEatTheRest());
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("o", "output", true, "output file path");
        options.addOption("v", false, "verbose");

        PosixParser parser = new PosixParser();

        parser.burstToken("-ov", false);
        assertEquals(2, parser.getTokens().size());
        assertEquals("-o", parser.getTokens().get(0));
        assertEquals("-v", parser.getTokens().get(1));

        parser.getTokens().clear();

        parser.burstToken("-xyz", true);
        assertEquals(3, parser.getTokens().size());
        assertEquals("--", parser.getTokens().get(0));
        assertEquals("xyz", parser.getTokens().get(1));
        assertEquals("-x", parser.getTokens().get(2));
    }
}