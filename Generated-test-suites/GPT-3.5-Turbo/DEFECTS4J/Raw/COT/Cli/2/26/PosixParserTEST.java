import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertFalse(parser.eatTheRest);
        assertTrue(parser.tokens.isEmpty());
        assertNull(parser.currentOption);
    }

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(Option.builder("a").argName("arg").build());

        String[] arguments = {"-a", "value"};
        String[] expected = {"-a", "value"};

        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(Option.builder("a").build());

        parser.options = options;

        String token = "-a";
        parser.processOptionToken(token, false);
        assertNotNull(parser.currentOption);
        assertEquals("-a", parser.tokens.get(0));

        parser.currentOption = null;
        token = "-b";
        parser.processOptionToken(token, false);
        assertNull(parser.currentOption);
        assertEquals("-b", parser.tokens.get(1));
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(Option.builder("a").build());

        parser.options = options;

        String token = "-abc";
        parser.burstToken(token, false);
        assertNull(parser.currentOption);
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
        assertEquals("-c", parser.tokens.get(2));

        parser.tokens.clear();
        token = "-ab";
        parser.burstToken(token, false);
        assertNull(parser.currentOption);
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));

        parser.tokens.clear();
        token = "-a";
        parser.burstToken(token, false);
        assertNull(parser.currentOption);
        assertEquals("-a", parser.tokens.get(0));
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();

        parser.currentOption = Option.builder("a").argName("arg").build();

        String value = "value";
        parser.process(value);
        assertNull(parser.currentOption);
        assertEquals(value, parser.tokens.get(0));

        parser.tokens.clear();
        parser.currentOption = Option.builder("a").argName("arg").hasArgs().build();

        value = "value";
        parser.process(value);
        assertNull(parser.currentOption);
        assertEquals(value, parser.tokens.get(0));

        parser.tokens.clear();
        parser.currentOption = null;

        value = "value";
        parser.process(value);
        assertTrue(parser.eatTheRest);
        assertEquals("--", parser.tokens.get(0));
        assertEquals(value, parser.tokens.get(1));
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();

        String hyphen = "-";
        parser.processSingleHyphen(hyphen);
        assertEquals(hyphen, parser.tokens.get(0));
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();

        String[] remainingTokens = {"token1", "token2"};

        Iterator<String> iter = Arrays.asList(remainingTokens).iterator();

        parser.eatTheRest = true;
        parser.gobble(iter);
        assertEquals(2, parser.tokens.size());
        assertTrue(parser.tokens.containsAll(Arrays.asList(remainingTokens)));

        parser.eatTheRest = false;
        parser.tokens.clear();
        parser.gobble(iter);
        assertTrue(parser.tokens.isEmpty());
    }

}