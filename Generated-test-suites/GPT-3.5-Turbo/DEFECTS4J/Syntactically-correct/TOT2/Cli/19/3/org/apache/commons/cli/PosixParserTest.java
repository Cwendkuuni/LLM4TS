import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertEquals(0, parser.tokens.size());
        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);
    }

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A description");
        options.addOption("b", "optionB", true, "Option B description");

        String[] arguments = {"--optionA", "-b", "value", "data"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(4, result.length);
        assertEquals("--optionA", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
        assertEquals("data", result[3]);
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        Iterator iter = Arrays.asList("arg1", "arg2", "arg3").iterator();

        parser.eatTheRest = true;
        parser.gobble(iter);

        assertFalse(iter.hasNext());
        assertEquals(3, parser.tokens.size());
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.currentOption = new Option("a", "optionA", false, "Option A description");

        parser.process("value");

        assertEquals(1, parser.tokens.size());
        assertEquals("value", parser.tokens.get(0));
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A description");

        parser.options = options;
        parser.processOptionToken("-a", true);

        assertEquals("a", parser.currentOption.getOpt());
        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A description");

        parser.options = options;
        parser.burstToken("-ab", true);

        assertEquals(2, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
    }
}