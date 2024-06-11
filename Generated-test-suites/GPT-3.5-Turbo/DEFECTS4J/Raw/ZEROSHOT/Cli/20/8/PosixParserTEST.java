import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setup() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] arguments = {"-a", "-b", "value"};

        String[] flattened = parser.flatten(options, arguments, true);

        assertNotNull(flattened);
        assertEquals(3, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("-b", flattened[1]);
        assertEquals("value", flattened[2]);
    }

    @Test
    public void testInit() {
        parser.init();
        assertEquals(false, parser.eatTheRest);
        assertNotNull(parser.tokens);
        assertEquals(0, parser.tokens.size());
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        parser.options = options;
        parser.processOptionToken("-a", true);

        assertNotNull(parser.currentOption);
        assertEquals("-a", parser.tokens.get(0));
    }

    @Test
    public void testProcessOptionToken_NoOption() {
        Options options = new Options();

        parser.options = options;
        parser.processOptionToken("-a", true);

        assertNull(parser.currentOption);
        assertEquals("-a", parser.tokens.get(0));
    }

    @Test
    public void testProcessOptionToken_StopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        parser.options = options;
        parser.processOptionToken("-b", true);

        assertNull(parser.currentOption);
        assertEquals(true, parser.eatTheRest);
        assertEquals("-b", parser.tokens.get(0));
    }

    @Test
    public void testProcess() {
        Options options = new Options();
        Option option = new Option("a", false, "Option a");
        option.setArgs(2);
        options.addOption(option);

        parser.options = options;
        parser.currentOption = option;
        parser.process("value1");

        assertNull(parser.currentOption);
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("value1", parser.tokens.get(1));
    }

    @Test
    public void testProcess_NoCurrentOption() {
        parser.process("value");

        assertEquals(true, parser.eatTheRest);
        assertEquals("--", parser.tokens.get(0));
        assertEquals("value", parser.tokens.get(1));
    }

    @Test
    public void testGobble() {
        Iterator iterator = Arrays.asList("-a", "value1", "value2").iterator();
        parser.eatTheRest = true;

        parser.gobble(iterator);

        assertEquals(3, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("value1", parser.tokens.get(1));
        assertEquals("value2", parser.tokens.get(2));
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        parser.options = options;
        parser.burstToken("-ab", true);

        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
    }

    @Test
    public void testBurstToken_StopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        parser.options = options;
        parser.burstToken("-ab", true);

        assertNull(parser.currentOption);
        assertEquals(true, parser.eatTheRest);
        assertEquals("-ab", parser.tokens.get(0));
    }

    @Test
    public void testBurstToken_NoOption() {
        Options options = new Options();

        parser.options = options;
        parser.burstToken("-ab", true);

        assertNull(parser.currentOption);
        assertEquals("-ab", parser.tokens.get(0));
    }
}