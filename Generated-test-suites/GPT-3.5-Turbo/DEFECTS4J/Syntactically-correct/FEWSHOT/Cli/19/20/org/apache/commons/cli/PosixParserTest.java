package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setup() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption(new Option("f", "file", true, "input file"));
        options.addOption(new Option("d", "directory", false, "input directory"));

        String[] args = new String[] {"-f", "test.txt", "-d"};

        String[] result = parser.flatten(options, args, false);

        assertEquals("-f", result[0]);
        assertEquals("test.txt", result[1]);
        assertEquals("-d", result[2]);
    }

    @Test
    public void testInit() {
        parser.init();

        assertNull(parser.currentOption);
        assertNull(parser.options);
        assertEquals(0, parser.tokens.size());
        assertFalse(parser.eatTheRest);
    }

    @Test
    public void testProcess() {
        Option option = new Option("f", "file", true, "input file");
        option.setArgs(2);
        parser.currentOption = option;

        parser.process("test.txt");

        assertEquals("test.txt", parser.tokens.get(0));
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption(new Option("f", "file", true, "input file"));

        parser.options = options;

        parser.processOptionToken("-f", false);

        assertEquals("-f", parser.tokens.get(0));
        assertNotNull(parser.currentOption);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption(new Option("f", "file", true, "input file"));

        parser.options = options;

        parser.burstToken("-f", false);

        assertEquals("-f", parser.tokens.get(0));
    }
}