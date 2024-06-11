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

        String[] arguments = {"-a", "valueA", "--optionB=valueB"};
        String[] flattenedArguments = parser.flatten(options, arguments, false);

        assertEquals(4, flattenedArguments.length);
        assertEquals("-a", flattenedArguments[0]);
        assertEquals("valueA", flattenedArguments[1]);
        assertEquals("--optionB", flattenedArguments[2]);
        assertEquals("valueB", flattenedArguments[3]);
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        Iterator iter = Arrays.asList(new String[]{"token1", "token2"}).iterator();
        parser.gobble(iter);
        assertEquals(0, parser.tokens.size());
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        String value = "testValue";
        parser.process(value);
        assertEquals(3, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("testValue", parser.tokens.get(1));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");

        parser.processOptionToken("-a", false);
        assertNotNull(parser.currentOption);
        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));

        parser.processOptionToken("-b", true);
        assertNull(parser.currentOption);
        assertEquals(2, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");

        parser.burstToken("-ab", true);
        assertNotNull(parser.currentOption);
        assertEquals(3, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
        assertEquals("-ab", parser.currentOption);

        parser.init();
        parser.burstToken("-ac", true);
        assertNull(parser.currentOption);
        assertEquals(2, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-c", parser.tokens.get(1));
    }
}