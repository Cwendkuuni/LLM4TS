import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();

        Option option1 = new Option("a", false, "option a");
        Option option2 = new Option("b", false, "option b");
        Option option3 = new Option("c", true, "option c");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        String[] arguments = new String[]{"-a", "-b", "-c", "value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
        assertEquals("value", result[3]);
        assertEquals("--", result[4]);
    }

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();

        assertNull(parser.currentOption);
        assertFalse(parser.eatTheRest);
        assertTrue(parser.tokens.isEmpty());
    }

    @Test
    public void testProcessOptionToken_Success() {
        Options options = new Options();
        Option option = new Option("a", false, "option a");
        options.addOption(option);

        PosixParser parser = new PosixParser();
        parser.options = options;
        parser.processOptionToken("-a", false);

        assertEquals(option, parser.currentOption);
        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
    }

    @Test
    public void testProcessOptionToken_StopAtNonOption() {
        Options options = new Options();
        Option option = new Option("a", false, "option a");
        options.addOption(option);

        PosixParser parser = new PosixParser();
        parser.options = options;
        parser.processOptionToken("-a", true);

        assertNull(parser.currentOption);
        assertTrue(parser.eatTheRest);
        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.currentOption = new Option("c", true, "option c");
        parser.process("value");

        assertNull(parser.currentOption);
        assertFalse(parser.eatTheRest);
        assertEquals(1, parser.tokens.size());
        assertEquals("value", parser.tokens.get(0));
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        parser.eatTheRest = true;

        String[] arguments = new String[]{"value1", "value2"};

        parser.gobble(Arrays.asList(arguments).iterator());

        assertEquals(2, parser.tokens.size());
        assertEquals("value1", parser.tokens.get(0));
        assertEquals("value2", parser.tokens.get(1));
    }

    @Test
    public void testBurstToken_Success() {
        Options options = new Options();
        Option option1 = new Option("a", false, "option a");
        Option option2 = new Option("b", false, "option b");
        options.addOption(option1);
        options.addOption(option2);

        PosixParser parser = new PosixParser();
        parser.options = options;
        parser.burstToken("-ab", false);

        assertEquals(2, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
    }

    @Test
    public void testBurstToken_StopAtNonOption() {
        Options options = new Options();
        Option option1 = new Option("a", false, "option a");
        Option option2 = new Option("b", false, "option b");
        options.addOption(option1);
        options.addOption(option2);

        PosixParser parser = new PosixParser();
        parser.options = options;
        parser.burstToken("-ab", true);

        assertNull(parser.currentOption);
        assertTrue(parser.eatTheRest);
        assertEquals(1, parser.tokens.size());
        assertEquals("-ab", parser.tokens.get(0));
    }

    @Test
    public void testBurstToken_UnknownOption_StopAtNonOption() {
        Options options = new Options();
        Option option = new Option("a", false, "option a");
        options.addOption(option);

        PosixParser parser = new PosixParser();
        parser.options = options;
        parser.burstToken("-ab", true);

        assertNull(parser.currentOption);
        assertTrue(parser.eatTheRest);
        assertEquals(1, parser.tokens.size());
        assertEquals("-ab", parser.tokens.get(0));
    }
}