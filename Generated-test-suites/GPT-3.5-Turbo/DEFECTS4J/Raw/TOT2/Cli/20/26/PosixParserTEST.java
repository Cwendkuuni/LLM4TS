import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertNull(parser.currentOption);
        assertEquals(0, parser.tokens.size());
        assertFalse(parser.eatTheRest);
    }

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        String[] arguments = {"-a", "valueA", "--optionB", "valueB"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("valueA", result[1]);
        assertEquals("--", result[2]);
        assertEquals("--optionB", result[3]);
        assertEquals("valueB", result[4]);
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        List<String> remainingTokens = new ArrayList<>();
        remainingTokens.add("token1");
        remainingTokens.add("token2");
        Iterator iter = remainingTokens.iterator();
        parser.gobble(iter);
        assertEquals(2, parser.tokens.size());
        assertEquals("token1", parser.tokens.get(0));
        assertEquals("token2", parser.tokens.get(1));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("t", "test", false, "Test Option");
        parser.options = options;
        boolean stopAtNonOption = false;
        parser.processOptionToken("-t", stopAtNonOption);
        assertNotNull(parser.currentOption);
        assertEquals("-t", parser.tokens.get(0));
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.currentOption = new Option("t", "test", false, "Test Option");
        parser.currentOption.setArgs(1);
        parser.process("value");
        assertEquals(1, parser.tokens.size());
        assertEquals("value", parser.tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        parser.options = options;
        boolean stopAtNonOption = false;
        parser.burstToken("-ab", stopAtNonOption);
        assertEquals(2, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
    }
}