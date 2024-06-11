import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertFalse(parser.eatTheRest);
        assertEquals(0, parser.tokens.size());
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption(new Option("f", "foo", false, "foo option"));
        options.addOption(new Option("b", "bar", true, "bar option"));

        String[] arguments = {"--foo", "-b", "value", "nonOption"};

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, true);

        assertEquals(4, flattened.length);
        assertEquals("--foo", flattened[0]);
        assertEquals("-b", flattened[1]);
        assertEquals("value", flattened[2]);
        assertEquals("nonOption", flattened[3]);
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        parser.tokens.add("token1");
        parser.tokens.add("token2");
        parser.tokens.add("token3");

        Iterator iter = parser.tokens.iterator();
        parser.gobble(iter);

        assertEquals(3, parser.tokens.size());
        assertTrue(parser.eatTheRest);

        while(iter.hasNext()){
            iter.next();
            iter.remove();
        }

        parser.gobble(iter);

        assertEquals(0, parser.tokens.size());
        assertFalse(parser.eatTheRest);
    }

    @Test
    public void testProcessNonOptionToken() {
        PosixParser parser = new PosixParser();
        parser.processNonOptionToken("value");

        assertEquals(2, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("value", parser.tokens.get(1));
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption(new Option("o", "option", false, "option"));

        PosixParser parser = new PosixParser();
        parser.options = options;

        parser.processOptionToken("-o", true);
        assertEquals(1, parser.tokens.size());
        assertEquals("-o", parser.tokens.get(0));

        parser.processOptionToken("-n", true);
        assertEquals(1, parser.tokens.size());
        assertEquals("-n", parser.tokens.get(0));

        parser.processOptionToken("-n", false);
        assertEquals(2, parser.tokens.size());
        assertEquals("-n", parser.tokens.get(0));
        assertEquals("--", parser.tokens.get(1));
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption(new Option("o", "option", true, "option"));

        PosixParser parser = new PosixParser();
        parser.options = options;

        parser.burstToken("-oValue", true);
        assertEquals(1, parser.tokens.size());
        assertEquals("-oValue", parser.tokens.get(0));

        parser.burstToken("-oValue", false);
        assertEquals(2, parser.tokens.size());
        assertEquals("-o", parser.tokens.get(0));
        assertEquals("Value", parser.tokens.get(1));

        parser.burstToken("-aValue", true);
        assertEquals(3, parser.tokens.size());
        assertEquals("-aValue", parser.tokens.get(2));

        parser.burstToken("-aValue", false);
        assertEquals(3, parser.tokens.size());
        assertEquals("-aValue", parser.tokens.get(2));
    }
}