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
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");

        String[] arguments = {"-a", "valueA"};
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, false);

        assertEquals(2, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("valueA", flattened[1]);
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        parser.tokens.add("token1");
        parser.tokens.add("token2");

        Iterator<String> iter = Arrays.asList("token3", "token4").iterator();
        parser.gobble(iter);

        assertEquals(4, parser.tokens.size());
        assertEquals("token1", parser.tokens.get(0));
        assertEquals("token2", parser.tokens.get(1));
        assertEquals("token3", parser.tokens.get(2));
        assertEquals("token4", parser.tokens.get(3));
    }

    @Test
    public void testProcess() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");

        PosixParser parser = new PosixParser();
        parser.options = options;

        parser.process("-a");

        assertEquals(2, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("-a", parser.tokens.get(1));
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");

        PosixParser parser = new PosixParser();
        parser.options = options;

        parser.burstToken("-ab", false);

        assertEquals(3, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
        assertEquals("--", parser.tokens.get(2));
    }
}