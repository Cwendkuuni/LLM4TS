package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten() {
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);

        String[] arguments = {"--option=value", "-ab", "--", "arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(5, result.length);
        assertEquals("--option", result[0]);
        assertEquals("value", result[1]);
        assertEquals("-a", result[2]);
        assertEquals("-b", result[3]);
        assertEquals("--", result[4]);
    }

    @Test
    public void testProcess() {
        parser.init();

        parser.process("value");

        assertEquals(3, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("value", parser.tokens.get(1));
    }

    @Test
    public void testProcessOptionToken_ValidOption() {
        Option option = new Option("a", "option a");
        Options options = new Options();
        options.addOption(option);

        parser.processOptionToken("-a", false);

        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals(option, parser.currentOption);
    }

    @Test
    public void testProcessOptionToken_InvalidOption_StopAtNonOption() {
        parser.processOptionToken("-x", true);

        assertEquals(0, parser.tokens.size());
        assertEquals(true, parser.eatTheRest);
    }

    @Test
    public void testBurstToken_ValidOption() {
        Option option = new Option("a", "option a");
        Options options = new Options();
        options.addOption(option);

        parser.burstToken("-abc", false);

        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals(option, parser.currentOption);
    }

    @Test
    public void testBurstToken_InvalidOption_StopAtNonOption() {
        parser.burstToken("-abc", true);

        assertEquals(3, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("bc", parser.tokens.get(1));
    }

    @Test
    public void testBurstToken_InvalidOption() {
        parser.burstToken("-abc", false);

        assertEquals(1, parser.tokens.size());
        assertEquals("-abc", parser.tokens.get(0));
    }
}