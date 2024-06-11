import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertEquals(false, parser.eatTheRest);
        assertEquals(0, parser.tokens.size());
    }

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"--option1", "value1", "--option2"};
        String[] expected = {"--option1", "value1", "--option2"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        Iterator iter = Arrays.asList("arg1", "arg2", "arg3").iterator();
        parser.eatTheRest = true;
        parser.tokens.add("--");

        parser.gobble(iter);

        assertEquals(4, parser.tokens.size());
        assertEquals("arg1", parser.tokens.get(1));
        assertEquals("arg2", parser.tokens.get(2));
        assertEquals("arg3", parser.tokens.get(3));
    }

    @Test
    public void testProcessNonOptionToken() {
        PosixParser parser = new PosixParser();
        parser.processNonOptionToken("value");
        
        assertEquals(true, parser.eatTheRest);
        assertEquals("--", parser.tokens.get(0));
        assertEquals("value", parser.tokens.get(1));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option option = new Option("option", false, "");
        options.addOption(option);

        parser.options = options;
        parser.processOptionToken("-option", false);

        assertEquals("-option", parser.tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option option = new Option("option", false, "");
        options.addOption(option);

        parser.options = options;
        parser.burstToken("-option", false);

        assertEquals("-option", parser.tokens.get(0));
    }
}