import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertEquals(false, parser.eatTheRest);
        assertEquals(0, parser.tokens.size());
        assertNull(parser.currentOption);
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] arguments = {"-a", "value1", "--b=value2", "-c", "value3"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("--b", result[2]);
        assertEquals("value2", result[3]);
        assertEquals("-c", result[4]);
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        parser.tokens.add("token1");
        parser.tokens.add("token2");
        parser.tokens.add("token3");

        Iterator iter = Arrays.asList(new String[]{"token4", "token5"})
                .iterator();

        parser.gobble(iter);

        assertEquals("token1", parser.tokens.get(0));
        assertEquals("token2", parser.tokens.get(1));
        assertEquals("token3", parser.tokens.get(2));
        assertEquals("token4", parser.tokens.get(3));
        assertEquals("token5", parser.tokens.get(4));
    }

    @Test
    public void testProcess() {
        Options options = new Options();
        options.addOption("a", true, "Option a");

        PosixParser parser = new PosixParser();
        parser.options = options;

        parser.currentOption = options.getOption("a");
        parser.process("value");

        assertEquals(2, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("value", parser.tokens.get(1));
        assertNull(parser.currentOption);

        parser.currentOption = null;
        parser.process("value");

        assertEquals(3, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("value", parser.tokens.get(1));
        assertEquals("value", parser.tokens.get(2));
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
        parser.options = options;

        parser.processOptionToken("-a", false);

        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertNull(parser.currentOption);

        parser.processOptionToken("-b", true);

        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertTrue(parser.eatTheRest);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        PosixParser parser = new PosixParser();
        parser.options = options;

        parser.burstToken("-abc", false);

        assertEquals(2, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-bc", parser.tokens.get(1));

        parser.burstToken("-abc", true);

        assertEquals(1, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
    }

}