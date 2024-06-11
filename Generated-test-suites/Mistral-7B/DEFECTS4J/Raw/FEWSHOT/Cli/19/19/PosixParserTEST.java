package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", 4, "Option D");
        options.addOption("e", 5, "Option E");
        options.addOption("f", 6, "Option F");
        options.addOption("g", true, "Option G");
        options.addOption("h", false, "Option H");
        options.addOption("i", null, "Option I");
        options.addOption("j", 7, "Option J");

        String[] args = {"--a", "--b=1", "--c", "--d=4", "--e=5", "--f=6", "--g", "--h", "--i=test", "-i", "--", "test", "--j", "--", "test2"};

        String[] expected = {"--a", "--b", "--c", "--d=4", "--e=5", "--f=6", "--g", "--h", "--i=test", "-i", "--", "test", "--j", "--", "test2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", 4, "Option D");
        options.addOption("e", 5, "Option E");
        options.addOption("f", 6, "Option F");
        options.addOption("g", true, "Option G");
        options.addOption("h", false, "Option H");
        options.addOption("i", null, "Option I");
        options.addOption("j", 7, "Option J");

        String[] args = {"--a", "--b=1", "--c", "--d=4", "--e=5", "--f=6", "--g", "--h", "--i=test", "-i", "test", "--j", "--", "test2"};

        String[] expected = {"-i", "test", "--", "test2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithEmptyArgs() {
        Options options = new Options();

        String[] expected = {};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, new String[0], false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("ab", 1, "Option AB");
        options.addOption("acd", 3, "Option AC-D");
        options.addOption("adef", 4, "Option A-DE-F");
        options.addOption("afg", true, "Option AF-G");
        options.addOption("aghi", false, "Option AG-HI");
        options.addOption("aj", 7, "Option A-J");

        String[] args = {"--ab", "--acd", "--adef=test", "--afg", "--aghi", "--aj=test2"};

        PosixParser parser = new PosixParser();
        List<String> tokens = new ArrayList<>();
        parser.flatten(options, args, false);

        parser.burstToken("--ab", tokens);
        assertEquals("--ab", tokens.get(0));
        assertEquals("-a", tokens.get(1));
        assertTrue(tokens.get(2).startsWith("-"));
        assertEquals("b", tokens.get(2).substring(1));

        parser.burstToken("--acd", tokens);
        assertEquals("-a", tokens.get(0));
        assertEquals("-c", tokens.get(1));
        assertEquals("-d", tokens.get(2));

        parser.burstToken("--adef=test", tokens);
        assertEquals("-a", tokens.get(0));
        assertEquals("-d", tokens.get(1));
        assertEquals("--ef=test", tokens.get(2));

        parser.burstToken("--afg", tokens);
        assertEquals("-a", tokens.get(0));
        assertEquals("-f", tokens.get(1));
        assertEquals("-g", tokens.get(2));

        parser.burstToken("--aghi", tokens);
        assertEquals("-a", tokens.get(0));
        assertEquals("-g", tokens.get(1));
        assertFalse(tokens.get(2).startsWith("-"));
        assertEquals("h", tokens.get(2));

        parser.burstToken("--aj=test2", tokens);
        assertEquals("-a", tokens.get(0));
        assertEquals("-j", tokens.get(1));
        assertEquals("--", tokens.get(2));
        assertEquals("test2", tokens.get(3));
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("ab", 1, "Option AB");
        options.addOption("acd", 3, "Option AC-D");
        options.addOption("adef", 4, "Option A-DE-F");
        options.addOption("afg", true, "Option AF-G");
        options.addOption("aghi", false, "Option AG-HI");
        options.addOption("aj", 7, "Option A-J");

        PosixParser parser = new PosixParser();

        parser.processOptionToken("--ab", parser);
        assertSame(options.getOption("a"), parser.currentOption);

        parser.init();
        parser.processOptionToken("--acd", parser);
        assertSame(options.getOption("c"), parser.currentOption);

        parser.init();
        parser.processOptionToken("--adef=test", parser);
        assertSame(options.getOption("d"), parser.currentOption);

        parser.init();
        parser.processOptionToken("--afg", parser);
        assertSame(options.getOption("f"), parser.currentOption);

        parser.init();
        parser.processOptionToken("--aghi", parser);
        assertSame(options.getOption("g"), parser.currentOption);

        parser.init();
        parser.processOptionToken("--aj=test2", parser);
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcess() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("ab", 1, "Option AB");
        options.addOption("acd", 3, "Option AC-D");
        options.addOption("adef", 4, "Option A-DE-F");
        options.addOption("afg", true, "Option AF-G");
        options.addOption("aghi", false, "Option AG-HI");
        options.addOption("aj", 7, "Option A-J");

        PosixParser parser = new PosixParser();

        parser.process("test");
        assertEquals(Arrays.asList("--", "test"), parser.tokens);

        parser.init();
        parser.process("-a");
        assertEquals(Arrays.asList("-a"), parser.tokens);

        parser.init();
        parser.process("-a --b");
        assertEquals(Arrays.asList("-a", "--b"), parser.tokens);

        parser.init();
        parser.process("-a --b=test");
        assertEquals(Arrays.asList("-a", "--b=test"), parser.tokens);

        parser.init();
        parser.process("-a -b");
        assertEquals(Arrays.asList("-a", "-b"), parser.tokens);

        parser.init();
        parser.process("-a -b=test");
        assertEquals(Arrays.asList("-a", "-b=test"), parser.tokens);

        parser.init();
        parser.process("--a");
        assertEquals(Arrays.asList("--a"), parser.tokens);

        parser.init();
        parser.process("--a --b");
        assertEquals(Arrays.asList("--a", "--b"), parser.tokens);

        parser.init();
        parser.process("--a --b=test");
        assertEquals(Arrays.asList("--a", "--b=test"), parser.tokens);

        parser.init();
        parser.process("--a -b");
        assertEquals(Arrays.asList(Arrays.asList("--a", "--"), "-b"), parser.tokens);

        parser.init();
        parser.process("--a -b=test");
        assertEquals(Arrays.asList(Arrays.asList("--a", "--"), "-b=test"), parser.tokens);

        parser.init();
        parser.process("--ab");
        assertEquals(Arrays.asList("--ab"), parser.tokens);

        parser.init();
        parser.process("--acd");
        assertEquals(Arrays.asList("--acd"), parser.tokens);

        parser.init();
        parser.process("--adef=test");
        assertEquals(Arrays.asList("--adef=test"), parser.tokens);

        parser.init();
        parser.process("--afg");
        assertEquals(Arrays.asList("--afg"), parser.tokens);

        parser.init();
        parser.process("--aghi");
        assertEquals(Arrays.asList("--aghi"), parser.tokens);

        parser.init();
        parser.process("--aj=test2");
        assertEquals(Arrays.asList("--aj=test2"), parser.tokens);
    }
}