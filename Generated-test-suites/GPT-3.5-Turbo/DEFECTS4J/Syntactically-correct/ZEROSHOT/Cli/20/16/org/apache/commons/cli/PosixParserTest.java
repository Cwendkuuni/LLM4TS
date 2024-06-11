import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;

    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").hasArg().build());

        String[] arguments = new String[]{"-a", "-b", "-c", "value"};

        String[] expected = new String[]{"-a", "-b", "-c", "value"};

        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testInit() {
        parser.init();

        assertNull(parser.currentOption);
        assertFalse(parser.eatTheRest);
        assertTrue(parser.tokens.isEmpty());
        assertNull(parser.options);
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());

        parser.options = options;

        parser.processOptionToken("-a", false);

        assertNotNull(parser.currentOption);
        assertEquals("-a", parser.tokens.get(0));
    }

    @Test
    public void testProcess() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());

        parser.options = options;
        parser.currentOption = options.getOption("a");

        parser.process("value");

        assertNull(parser.currentOption);
        assertEquals("--", parser.tokens.get(0));
        assertEquals("value", parser.tokens.get(1));
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());

        parser.options = options;

        parser.burstToken("-abc", false);

        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
        assertEquals("-c", parser.tokens.get(2));
    }

    @Test
    public void testGobble() {
        Iterator iter = Arrays.asList("-a", "-b", "-c").iterator();

        parser.eatTheRest = true;

        parser.gobble(iter);

        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
        assertEquals("-c", parser.tokens.get(2));
    }
}