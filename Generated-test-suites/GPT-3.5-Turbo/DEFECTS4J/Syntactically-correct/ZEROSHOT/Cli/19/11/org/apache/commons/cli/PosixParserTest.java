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
        // TODO: Add test cases for the flatten method
        // Make sure to test all possible scenarios and edge cases
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        parser.init();

        parser.eatTheRest = true;
        parser.tokens.add("token1");
        parser.tokens.add("token2");
        parser.tokens.add("token3");

        String[] remainingTokens = {"remaining1", "remaining2"};

        parser.gobble(remainingTokens.iterator());

        assertEquals(5, parser.tokens.size());
        assertEquals("token1", parser.tokens.get(0));
        assertEquals("token2", parser.tokens.get(1));
        assertEquals("token3", parser.tokens.get(2));
        assertEquals("remaining1", parser.tokens.get(3));
        assertEquals("remaining2", parser.tokens.get(4));
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.init();

        parser.currentOption = new Option("test", true, "Test Option");

        parser.process("value1");
        assertNotNull(parser.currentOption);
        assertEquals(1, parser.tokens.size());
        assertEquals("value1", parser.tokens.get(0));

        parser.currentOption = new Option("test", false, "Test Option");
        parser.process("value2");
        assertNull(parser.currentOption);
        assertEquals(2, parser.tokens.size());
        assertEquals("value1", parser.tokens.get(0));
        assertEquals("value2", parser.tokens.get(1));

        parser.currentOption = null;
        parser.process("value3");
        assertTrue(parser.eatTheRest);
        assertEquals(4, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("value3", parser.tokens.get(1));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        parser.init();

        Options options = new Options();
        options.addOption(new Option("a", false, "Option A"));
        options.addOption(new Option("b", true, "Option B"));

        parser.options = options;

        parser.processOptionToken("-a", false);
        assertNotNull(parser.currentOption);
        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));

        parser.processOptionToken("-c", false);
        assertNull(parser.currentOption);
        assertEquals(2, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-c", parser.tokens.get(1));

        parser.processOptionToken("-d", true);
        assertNull(parser.currentOption);
        assertEquals(3, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-c", parser.tokens.get(1));
        assertEquals("-d", parser.tokens.get(2));
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        parser.init();

        parser.currentOption = new Option("a", false, "Option A");

        parser.burstToken("-abc", false);
        assertNull(parser.currentOption);
        assertEquals(1, parser.tokens.size());
        assertEquals("-abc", parser.tokens.get(0));

        parser.currentOption = new Option("b", true, "Option B");

        parser.burstToken("-bcd", false);
        assertNull(parser.currentOption);
        assertEquals(2, parser.tokens.size());
        assertEquals("-bcd", parser.tokens.get(1));
    }
}