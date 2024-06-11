import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", false, "option c");

        String[] args = new String[]{"-a", "-b", "value", "arg1", "--", "arg2"};

        String[] result = parser.flatten(options, args, true);

        assertArrayEquals(new String[]{"-a", "-b", "value", "--", "arg2"}, result);
    }

    @Test
    public void testInit() {
        parser.init();

        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);
        assertEquals(0, parser.tokens.size());
    }

    @Test
    public void testProcess() {
        Option option = new Option("a", false, "option a");
        option.setArgs(2);
        parser.currentOption = option;

        parser.process("value");

        assertNull(parser.currentOption);
        assertEquals(1, parser.tokens.size());
        assertEquals("value", parser.tokens.get(0));
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("a", false, "option a");

        parser.options = options;

        parser.processOptionToken("-a", true);

        assertNotNull(parser.currentOption);
        assertEquals(option, parser.currentOption);
        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");

        parser.options = options;

        parser.burstToken("-ab", true);

        assertNull(parser.currentOption);
        assertEquals(2, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
    }
}