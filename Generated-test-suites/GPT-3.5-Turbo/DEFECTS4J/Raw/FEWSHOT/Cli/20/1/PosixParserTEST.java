package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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
        options.addOption(new Option("f", "foo", false, "foo option"));
        options.addOption(new Option("b", "bar", true, "bar option"));

        PosixParser parser = new PosixParser();
        String[] arguments = {"--foo", "-b", "value"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals("foo", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        IterLinkList iter = new IterLinkList();
        iter.add("arg1");
        iter.add("arg2");
        iter.add("arg3");

        parser.eatTheRest = true;
        parser.gobble(iter);

        assertEquals("arg1", parser.tokens.get(0));
        assertEquals("arg2", parser.tokens.get(1));
        assertEquals("arg3", parser.tokens.get(2));
    }

    @Test
    public void testProcess() {
        Options options = new Options();
        options.addOption(new Option("b", "bar", true, "bar option"));

        PosixParser parser = new PosixParser();
        parser.currentOption = options.getOption("bar");
        parser.process("value");

        assertEquals("value", parser.tokens.get(0));
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption(new Option("b", "bar", true, "bar option"));

        PosixParser parser = new PosixParser();
        parser.options = options;
        parser.processOptionToken("-b", true);

        assertEquals("-b", parser.tokens.get(0));
        assertEquals(options.getOption("bar"), parser.currentOption);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        Option option = new Option("b", "bar", true, "bar option");
        option.setArgs(2);
        options.addOption(option);

        PosixParser parser = new PosixParser();
        parser.options = options;
        parser.burstToken("-barvalue", true);

        assertEquals("-b", parser.tokens.get(0));
        assertEquals("arvalue", parser.tokens.get(1));
        assertEquals(option, parser.currentOption);
    }
}