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
        String[] arguments = new String[]{"-a", "arg1", "-b", "-c", "--option=value"};
        String[] expected = new String[]{"-a", "arg1", "-b", "-c", "--", "--option", "value"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        List<String> tokens = new ArrayList<>();
        tokens.add("token1");
        tokens.add("token2");
        tokens.add("token3");

        Iterator iter = tokens.iterator();
        parser.gobble(iter);

        assertTrue(iter.hasNext());
        assertEquals("token1", iter.next());
        assertTrue(iter.hasNext());
        assertEquals("token2", iter.next());
        assertTrue(iter.hasNext());
        assertEquals("token3", iter.next());
        assertFalse(iter.hasNext());
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.currentOption = new Option("a", "option A", false);

        parser.process("value");

        assertFalse(parser.eatTheRest);
        assertEquals(1, parser.tokens.size());
        assertEquals("value", parser.tokens.get(0));
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        parser.processSingleHyphen("-");

        assertEquals(1, parser.tokens.size());
        assertEquals("-", parser.tokens.get(0));
    }

    @Test
    public void testProcessOptionTokenWhenOptionExists() {
        PosixParser parser = new PosixParser();
        parser.options = new Options();
        parser.options.addOption("a", "option A", false);
        parser.options.addOption("b", "option B", false);

        parser.processOptionToken("-a", true);

        assertNull(parser.currentOption);
        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
    }

    @Test
    public void testProcessOptionTokenWhenOptionDoesNotExistAndStopAtNonOptionIsTrue() {
        PosixParser parser = new PosixParser();
        parser.options = new Options();
        parser.options.addOption("a", "option A", false);
        parser.options.addOption("b", "option B", false);

        parser.processOptionToken("-c", true);

        assertTrue(parser.eatTheRest);
    }

    @Test
    public void testProcessOptionTokenWhenOptionDoesNotExistAndStopAtNonOptionIsFalse() {
        PosixParser parser = new PosixParser();
        parser.options = new Options();
        parser.options.addOption("a", "option A", false);
        parser.options.addOption("b", "option B", false);

        parser.processOptionToken("-c", false);

        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        parser.options = new Options();
        parser.options.addOption("a", "option A", false);
        parser.options.addOption("b", "option B", false);

        parser.burstToken("-abc", true);

        assertEquals(3, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
        assertEquals("-c", parser.tokens.get(2));
    }
}