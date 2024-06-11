import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertTrue(parser.tokens.isEmpty());
        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        Option option = new Option("a", "option a");
        options.addOption(option);

        PosixParser parser = new PosixParser();
        String[] args = {"-a", "value"};
        String[] result = parser.flatten(options, args, false);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testSpecialTokenHandling() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] args = {"--option=value"};
        String[] result = parser.flatten(options, args, false);

        assertEquals(2, result.length);
        assertEquals("--option", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        parser.processSingleHyphen("-");
        List tokens = parser.tokens;
        assertEquals(1, tokens.size());
        assertEquals("-", tokens.get(0));
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        Option option = new Option("o", "option o");
        options.addOption(option);

        PosixParser parser = new PosixParser();
        boolean stopAtNonOption = true;
        parser.processOptionToken("-o", stopAtNonOption);
        Option currentOption = parser.currentOption;

        assertNotNull(currentOption);
        assertEquals(option, currentOption);
        assertEquals(1, parser.tokens.size());
        assertEquals("-o", parser.tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        Option option = new Option("o", "option o");
        options.addOption(option);

        PosixParser parser = new PosixParser();
        boolean stopAtNonOption = true;
        parser.burstToken("-ovalue", stopAtNonOption);
        
        assertNull(parser.currentOption);
        assertEquals(2, parser.tokens.size());
        assertEquals("-o", parser.tokens.get(0));
        assertEquals("value", parser.tokens.get(1));
    }
}