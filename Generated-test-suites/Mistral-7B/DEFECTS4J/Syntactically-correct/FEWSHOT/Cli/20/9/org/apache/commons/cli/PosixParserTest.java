package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 2, "Option C");
        options.addOption("d", false, "Option D");
        options.addOption("e", "abc", "Option E");
        options.addOption("f", "def", "Option F");
        options.addOption("g", "hij", "Option G");

        String[] args = {"--a", "--b", "-c", "-d", "-e", "abc", "def", "hij", "--g"};
        String[] expected = {"--a", "-c", "-d", "-e", "abc", "def", "--g"};
        String[] result = new PosixParser().flatten(options, args, false);
        assertArrayEquals(expected, result);

        // test when stopAtNonOption is set to true
        String[] args2 = {"--a", "--b", "-c", "nonOption", "-e", "abc", "def", "hij", "--g"};
        String[] expected2 = {"--a", "--b", "nonOption", "-e", "abc", "def", "--g"};
        String[] result2 = new PosixParser().flatten(options, args2, true);
        assertArrayEquals(expected2, result2);
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.init();
        String[] args = {"--a", "--b", "-c", "-d"};

        // process long options
        parser.process(args[0]);
        assertEquals(Arrays.asList("--a"), parser.getTokens());

        parser.init();
        parser.process(args[0]);
        parser.process("value");
        assertEquals(Arrays.asList("--a", "value"), parser.getTokens());

        parser.init();
        parser.process(args[1]);
        assertEquals(Arrays.asList("--b"), parser.getTokens());

        // process single hyphen
        parser.init();
        parser.process("-");
        assertEquals(Arrays.asList("-"), parser.getTokens());

        // process option with argument
        parser.init();
        parser.process("-c");
        parser.process("arg1");
        assertEquals(Arrays.asList("-c", "arg1"), parser.getTokens());

        // process non-option when stopAtNonOption is false
        parser.init();
        parser.process("nonOption");
        assertEquals(Arrays.asList("--", "nonOption"), parser.getTokens());

        // process non-option when stopAtNonOption is true
        parser.init();
        parser.process("nonOption");
        assertTrue(parser.isEatTheRest());
        assertEquals(Arrays.asList("nonOption"), parser.getTokens());
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");

        parser.init();
        parser.processOptionToken("a", false);
        assertSame(options.getOption("a"), parser.getCurrentOption());
        assertEquals(Arrays.asList("-a"), parser.getTokens());

        parser.init();
        parser.processOptionToken("a", true);
        assertNull(parser.getCurrentOption());
        assertEquals(Arrays.asList("-a"), parser.getTokens());

        parser.init();
        parser.processOptionToken("nonOption", false);
        assertTrue(parser.isEatTheRest());
        assertEquals(Arrays.asList("--", "nonOption"), parser.getTokens());
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        parser.init();
        parser.burstToken("--a--b", false);
        assertEquals(Arrays.asList("--a", "-b"), parser.getTokens());

        parser.init();
        parser.burstToken("--a--b1", false);
        assertEquals(Arrays.asList("--a", "-b1"), parser.getTokens());

        parser.init();
        parser.burstToken("--a--b1--c", false);
        assertEquals(Arrays.asList("--a", "-b1", "-c"), parser.getTokens());

        parser.init();
        parser.burstToken("--a--b1--c--", true);
        assertTrue(parser.isEatTheRest());
        assertEquals(Arrays.asList("--a", "-b1", "--"), parser.getTokens());

        parser.init();
        parser.burstToken("-a-b", false);
        assertEquals(Arrays.asList("-a-b"), parser.getTokens());

        parser.init();
        parser.burstToken("-a--b", false);
        assertEquals(Arrays.asList("-a", "--b"), parser.getTokens());

        parser.init();
        parser.burstToken("-a--b1", false);
        assertEquals(Arrays.asList("-a", "-b1"), parser.getTokens());

        parser.init();
        parser.burstToken("-a--b1--c", false);
        assertEquals(Arrays.asList("-a", "-b1", "-c"), parser.getTokens());
    }

    private List<String> getTokens(PosixParser parser) {
        return parser.tokens;
    }

    private void assertArrayEquals(Object expected, Object actual) {
        if (!(expected instanceof String[] && actual instanceof String[])) {
            throw new AssertionError("Expected and actual have incompatible types.");
        }
        String[] e = (String[]) expected;
        String[] a = (String[]) actual;
        if (e.length != a.length) {
            throw new AssertionError("Arrays have different lengths.");
        }
        for (int i = 0; i < e.length; i++) {
            if (!e[i].equals(a[i])) {
                throw new AssertionError("Expected [" + Arrays.toString(e) + "] but was [" + Arrays.toString(a) + "].");
            }
        }
    }
}