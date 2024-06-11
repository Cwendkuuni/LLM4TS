package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private Options options;
    private PosixParser parser;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 2, "Option C", true);
        options.addOption("d", 3, "Option D", true);
        options.addOption("e", 4, "Option E");
        options.addOption("f", 5, "Option F", true);
        parser = new PosixParser();
    }

    @Test
    public void testFlattenNoOptions() {
        String[] args = new String[0];
        String[] expected = new String[0];
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenWithOptions() {
        String[] args = {"--a", "--b", "-c", "1", "-d", "1", "2", "-e", "3", "-f", "4"};
        String[] expected = {"--a", "--b", "-c", "--d", "1", "--d", "2", "-e", "3", "--f", "4"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] args = {"--a", "--b", "-c", "1", "-d", "1", "2", "-e", "3", "-f", "4", "--", "x", "y", "z"};
        String[] expected = {"--a", "--b", "-c", "1", "-d", "1", "2", "-e", "3", "--", "x", "y", "z"};
        assertArrayEquals(expected, parser.flatten(options, args, true));
    }

    @Test
    public void testFlattenWithLongOptions() {
        String[] args = {"--long-option-a", "--long-option-b=value", "--long-option-c=value1 value2"};
        String[] expected = {"--long-option-a", "--long-option-b", "value", "--long-option-c", "value1", "value2"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenLongOptionWithValueAndToken() {
        String[] args = {"--long-option-a", "--long-option-b=value", "--long-option-b=another", "--"};
        String[] expected = {"--long-option-a", "--long-option-b", "value", "--long-option-b", "another"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenWithMixedOptionsAndArguments() {
        String[] args = {"--a", "-b", "c", "--d=1", "-e=2", "f", "--g"};
        String[] expected = {"--a", "--d", "1", "-b", "c", "--g", "-e", "2", "f"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        assertNull(parser.getCurrentOption());
        assertTrue(parser.getTokens().isEmpty());
        assertFalse(parser.isEatTheRest());
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        parser.processSingleHyphen("-");
        String[] expected = {"-"};
        assertArrayEquals(expected, parser.getTokens().toArray(new String[parser.getTokens().size()]));
    }

    @Test
    public void testProcessDoubleHyphen() {
        PosixParser parser = new PosixParser();
        parser.processDoubleHyphen("--");
        String[] expected = {"--"};
        assertArrayEquals(expected, parser.getTokens().toArray(new String[parser.getTokens().size()]));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        parser.setOptions(options);
        String[] args = {"-a", "-b", "-c", "--d=1", "-e=2", "f", "--g"};
        parser.init();

        parser.parseNext(args, 0);
        assertEquals("a", parser.getCurrentOption().getKey());
        assertTrue(parser.getCurrentOption().getValue() == null);

        parser.parseNext(args, 1);
        assertEquals("b", parser.getCurrentOption().getKey());
        assertTrue(parser.getCurrentOption().getValue() == null);

        parser.parseNext(args, 2);
        assertEquals("c", parser.getCurrentOption().getKey());
        assertTrue(parser.getCurrentOption().getValue() == null);

        parser.parseNext(args, 3);
        assertEquals("d", parser.getCurrentOption().getKey());
        assertEquals("1", parser.getCurrentOption().getValue());

        parser.parseNext(args, 4);
        assertEquals("e", parser.getCurrentOption().getKey());
        assertEquals("2", parser.getCurrentOption().getValue());

        parser.parseNext(args, 5);
        assertEquals("f", parser.getCurrentOption().getKey());
        assertTrue(parser.getCurrentOption().getValue() == null);

        parser.parseNext(args, 6);
        assertEquals("g", parser.getCurrentOption().getKey());
        assertTrue(parser.getCurrentOption().getValue() == null);

        String[] tokens = parser.getTokens().toArray(new String[parser.getTokens().size()]);
        assertArrayEquals(new String[]{"--a", "--d", "1", "--e", "2", "f", "--g"}, tokens);
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        String[] args = {"--ab", "-c", "-de", "1", "-ef=value", "f", "--ghij"};
        parser.init();

        parser.parseNext(args, 0);
        parser.burstToken("ab", false);
        String[] tokens = parser.getTokens().toArray(new String[parser.getTokens().size()]);
        assertArrayEquals(new String[]{"--a", "--b"}, tokens);

        parser.parseNext(args, 1);
        parser.processSingleHyphen("-");
        tokens = parser.getTokens().toArray(new String[parser.getTokens().size()]);
        assertArrayEquals(new String[]{"--a", "--b", "-"}, tokens);

        parser.parseNext(args, 2);
        parser.parseNext(args, 3);
        parser.processOptionToken("-c", false);
        tokens = parser.getTokens().toArray(new String[parser.getTokens().size()]);
        assertArrayEquals(new String[]{"--a", "--b", "-", "-c"}, tokens);

        parser.parseNext(args, 4);
        parser.processOptionToken("de", false);
        parser.parseNext(args, 5);
        parser.processSingleHyphen("-");
        tokens = parser.getTokens().toArray(new String[parser.getTokens().size()]);
        assertArrayEquals(new String[]{"--a", "--b", "-", "-c", "--d", "1"}, tokens);

        parser.parseNext(args, 6);
        parser.burstToken("ef", false);
        tokens = parser.getTokens().toArray(new String[parser.getTokens().size()]);
        assertArrayEquals(new String[]{"--a", "--b", "-", "-c", "--d", "1", "--e", "value"}, tokens);

        parser.parseNext(args, 7);
        parser.processSingleHyphen("-");
        tokens = parser.getTokens().toArray(new String[parser.getTokens().size()]);
        assertArrayEquals(new String[]{"--a", "--b", "-", "-c", "--d", "1", "--e", "value", "-"}, tokens);

        parser.parseNext(args, 8);
        parser.processDoubleHyphen("--");
        tokens = parser.getTokens().toArray(new String[parser.getTokens().size()]);
        assertArrayEquals(new String[]{"--a", "--b", "-", "-c", "--d", "1", "--e", "value", "-", "--ghij"}, tokens);
    }
}