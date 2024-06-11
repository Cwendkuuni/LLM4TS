package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("o", "option", true, "An option with a value");
        options.addOption("a", "another", false, "An option without a value");
        options.addOption("l", "long", true, "A long option");
        options.addOption("g", "group", 0, "A no-args option");

        String[] args = {"--option", "-o", "value", "--another", "-a", "--long=longValue", "--group", "-g"};
        String[] expected = {"--option", "-o", "value", "--another", "-a", "--long", "longValue", "--group", "-g"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);

        String[] args2 = {"--option=value", "--another", "--long=longValue", "--group", "-g"};
        String[] expected2 = {"--option=value", "--another", "--long", "longValue", "--group", "-g"};

        String[] result2 = parser.flatten(options, args2, false);
        assertArrayEquals(expected2, result2);

        String[] args3 = {"--option", "--another", "--long=longValue", "--group", "-g", "--"};
        String[] expected3 = {"--option", "--another", "--long", "longValue", "--group", "-g", "--"};

        String[] result3 = parser.flatten(options, args3, false);
        assertArrayEquals(expected3, result3);

        String[] args4 = {"--option", "--another", "--long=longValue", "--group", "-g", "--", "arg1", "arg2"};
        String[] expected4 = {"--option", "--another", "--long", "longValue", "--group", "-g", "--", "arg1", "arg2"};

        String[] result4 = parser.flatten(options, args4, false);
        assertArrayEquals(expected4, result4);

        String[] args5 = {"--option", "--another", "--long=longValue", "--group", "-g", "--", "-o", "value2"};
        String[] expected5 = {"--option", "--another", "--long", "longValue", "--group", "-g", "--", "-o", "value2"};

        String[] result5 = parser.flatten(options, args5, false);
        assertArrayEquals(expected5, result5);

        String[] args6 = {"--option", "--another", "--long=longValue", "--group", "-g", "--", "-a"};
        String[] expected6 = {"--option", "--another", "--long", "longValue", "--group", "-g", "--", "-a"};

        String[] result6 = parser.flatten(options, args6, false);
        assertArrayEquals(expected6, result6);

        String[] args7 = {"--option", "--another", "--long=longValue", "--group", "-g", "--", "-a", "arg"};
        String[] expected7 = {"--option", "--another", "--long", "longValue", "--group", "-g", "--", "-a", "arg"};

        String[] result7 = parser.flatten(options, args7, false);
        assertArrayEquals(expected7, result7);

        String[] args8 = {"--option", "--another", "--long=longValue", "--group", "-g", "--", "-o", "value2", "--another"};
        String[] expected8 = {"--option", "--another", "--long", "longValue", "--group", "-g", "--", "-o", "value2", "--another"};

        String[] result8 = parser.flatten(options, args8, false);
        assertArrayEquals(expected8, result8);

        String[] args9 = {"--option", "--another", "--long=longValue", "--group", "-g", "--", "-o", "value2", "--another=arg"};
        String[] expected9 = {"--option", "--another", "--long", "longValue", "--group", "-g", "--", "-o", "value2", "--another=arg"};

        String[] result9 = parser.flatten(options, args9, false);
        assertArrayEquals(expected9, result9);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("o", "option", true, "An option with a value");
        options.addOption("a", "another", false, "An option without a value");
        options.addOption("l", "long", true, "A long option");
        options.addOption("g", "group", 0, "A no-args option");

        String[] args = {"--option", "-o", "value"};
        String[] expected = {"--option", "-o", "value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(expected, result);

        String[] args2 = {"--option", "-o", "value", "--another"};
        String[] expected2 = {"--option", "-o", "value", "--another"};

        String[] result2 = parser.flatten(options, args2, true);
        assertArrayEquals(expected2, result2);

        String[] args3 = {"--option", "-o", "value", "--another", "--group"};
        String[] expected3 = {"--option", "-o", "value", "--another", "--group"};

        String[] result3 = parser.flatten(options, args3, true);
        assertArrayEquals(expected3, result3);
    }

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        assertFalse(parser.eatTheRest);
        assertEquals(0, parser.tokens.size());
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("o", "option", true, "An option with a value");

        PosixParser parser = new PosixParser();

        parser.processOptionToken("-o", false);
        assertSame(options.getOption("o"), parser.currentOption);
        assertTrue(parser.tokens.isEmpty());

        parser.init();

        parser.processOptionToken("--foo", false);
        assertEquals("--foo", parser.tokens.get(0));
        assertTrue(parser.tokens.size() == 1);

        parser.processOptionToken("-i", false);
        assertEquals("-i", parser.tokens.get(1));
        assertTrue(parser.tokens.size() == 2);

        parser.processOptionToken("--", false);
        assertEquals("--", parser.tokens.get(2));
        assertTrue(parser.tokens.size() == 3);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("o", "option", true, "An option with a value");

        PosixParser parser = new PosixParser();

        String[] tokens = parser.burstToken("-o--longValue", false)[0];
        assertEquals(2, tokens.length);
        assertEquals("-o", tokens[0]);
        assertEquals("--longValue", tokens[1]);

        tokens = parser.burstToken("-o--longValue--another", false)[0];
        assertEquals(3, tokens.length);
        assertEquals("-o", tokens[0]);
        assertEquals("--longValue", tokens[1]);
        assertEquals("--another", tokens[2]);

        tokens = parser.burstToken("-o--longValue--another=arg", false)[0];
        assertEquals(3, tokens.length);
        assertEquals("-o", tokens[0]);
        assertEquals("--longValue", tokens[1]);
        assertEquals("--another=arg", tokens[2]);

        tokens = parser.burstToken("--longValue--another=arg", false)[0];
        assertEquals(2, tokens.length);
        assertEquals("--longValue", tokens[0]);
        assertEquals("--another=arg", tokens[1]);

        tokens = parser.burstToken("--longValue--another=arg--", false)[0];
        assertEquals(3, tokens.length);
        assertEquals("--longValue", tokens[0]);
        assertEquals("--another=arg", tokens[1]);
        assertEquals("--", tokens[2]);
    }

    @Test
    public void testProcess() {
        Options options = new Options();
        options.addOption("o", "option", true, "An option with a value");

        PosixParser parser = new PosixParser();

        parser.process("--option");
        assertEquals("--option", parser.tokens.get(0));
        assertTrue(parser.tokens.size() == 1);

        parser.init();

        parser.process("--option");
        assertEquals("--option", parser.tokens.get(0));
        assertTrue(parser.tokens.size() == 1);

        parser.process("-o");
        assertEquals("-o", parser.tokens.get(0));
        assertTrue(parser.tokens.size() == 1);

        parser.process("--");
        assertEquals("--", parser.tokens.get(0));
        assertTrue(parser.tokens.size() == 1);

        parser.init();

        parser.process("--option");
        parser.process("-o");
        parser.process("--");

        assertEquals(Arrays.asList("--option", "-o", "--"), parser.tokens);
        assertTrue(parser.tokens.size() == 3);

        parser.init();

        parser.process("--option");
        parser.process("-o");
        parser.process("--longValue");
        parser.process("--");

        assertEquals(Arrays.asList("--option", "-o", "--longValue", "--"), parser.tokens);
        assertTrue(parser.tokens.size() == 4);

        parser.init();

        parser.process("--option");
        parser.process("-o");
        parser.process("--longValue");
        parser.process("--another");
        parser.process("--");

        assertEquals(Arrays.asList("--option", "-o", "--longValue", "--another", "--"), parser.tokens);
        assertTrue(parser.tokens.size() == 5);
    }

    private void assertArrayEquals(Object expected, Object actual) {
        if (!expected.getClass().isArray()) {
            assertEquals(expected, actual);
            return;
        }

        assertArrayEquals((Object[]) expected, (Object[]) actual);
    }

    private void assertArrayEquals(Object[] expected, Object[] actual) {
        if (expected.length != actual.length) {
            fail("Expected and actual arrays have a different length.");
        }

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}