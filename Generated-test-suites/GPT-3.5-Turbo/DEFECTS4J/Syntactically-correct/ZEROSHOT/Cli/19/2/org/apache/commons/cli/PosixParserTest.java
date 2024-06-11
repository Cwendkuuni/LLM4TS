import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertFalse(parser.eatTheRest);
        assertEquals(0, parser.tokens.size());
        assertNull(parser.currentOption);
    }

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-h", "-a", "value"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(3, result.length);
        assertEquals("-h", result[0]);
        assertEquals("-a", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        Iterator iter = Arrays.asList("argument1", "argument2").iterator();
        parser.tokens = new ArrayList();
        parser.eatTheRest = true;
        parser.gobble(iter);
        assertEquals(2, parser.tokens.size());
        assertEquals("argument1", parser.tokens.get(0));
        assertEquals("argument2", parser.tokens.get(1));
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.currentOption = new Option("a", "test option");
        parser.process("value");
        assertEquals(1, parser.tokens.size());
        assertEquals("value", parser.tokens.get(0));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        parser.options = new Options();
        parser.options.addOption(new Option("a", "test option"));
        parser.processOptionToken("-a", false);
        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        parser.options = new Options();
        parser.options.addOption(new Option("a", "test option"));
        parser.options.addOption(new Option("b", "test option"));
        parser.options.addOption(new Option("c", "test option"));
        parser.tokens = new ArrayList();
        parser.burstToken("-abc", false);
        assertEquals(3, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
        assertEquals("-c", parser.tokens.get(2));
    }
}