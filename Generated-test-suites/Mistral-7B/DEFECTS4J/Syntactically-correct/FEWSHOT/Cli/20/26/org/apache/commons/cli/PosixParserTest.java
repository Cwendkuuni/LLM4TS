package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testPosixParserFlatten() {
        Options options = new Options();
        options.addOption("a", true, "Short option a");
        options.addOption("b", 1, "Option b with an argument");
        options.addOption("c", 2, "Option c with two arguments");
        options.addOption("d", null, "Option d with no argument");
        options.addOption("e", 0, "Option e with no argument");

        String[] arguments = new String[]{"-a", "--b=foo", "-c", "arg1", "arg2", "--d", "arg3", "arg4", "--e", "arg5"};
        String[] expected = new String[]{"-a", "--b", "foo", "-c", "arg1", "arg2", "--d", "arg3", "arg4", "--e", "arg5"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testPosixParserFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Short option a");
        options.addOption("b", 1, "Option b with an argument");
        options.addOption("c", 2, "Option c with two arguments");
        options.addOption("d", null, "Option d with no argument");
        options.addOption("e", 0, "Option e with no argument");

        String[] arguments = new String[]{"-a", "--b=foo", "-c", "arg1", "arg2", "--d", "arg3", "arg4", "--e", "arg5", "nonOption"};
        String[] expected = new String[]{"-a", "--b", "foo", "-c", "arg1", "arg2", "--d", "arg3", "arg4", "--e", "arg5", "nonOption"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testPosixParserFlattenNoOptions() {
        Options options = new Options();

        String[] arguments = new String[]{"nonOption1", "nonOption2", "nonOption3"};
        String[] expected = new String[]{"nonOption1", "nonOption2", "nonOption3"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testPosixParserFlattenNoOptionsStopAtNonOption() {
        Options options = new Options();

        String[] arguments = new String[]{"nonOption1", "nonOption2", "nonOption3"};
        String[] expected = new String[]{"nonOption1", "nonOption2", "nonOption3"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testPosixParserBurstTokenOption() {
        Options options = new Options();
        options.addOption("o", null, "Option o");
        options.addOption("p", 1, "Option p with an argument");

        String token = "-op";
        String[] expected = new String[]{"-o", "--p"};

        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);

        assertEquals(expected[0], parser.tokens.get(0));
        assertEquals(expected[1], parser.tokens.get(1));
        assertEquals(null, parser.currentOption);
        assertEquals(2, parser.tokens.size());
    }

    @Test
    public void testPosixParserBurstTokenNoOption() {
        Options options = new Options();

        String token = "-qr";
        String[] expected = new String[]{"--", "q", "r"};

        PosixParser parser = new PosixParser();
        parser.burstToken(token, true);

        assertEquals(expected[0], parser.tokens.get(parser.tokens.size() - 2));
        assertEquals(expected[1], parser.tokens.get(parser.tokens.size() - 1));
        assertNull(parser.currentOption);
        assertEquals(3, parser.tokens.size());
    }

    @Test
    public void testPosixParserBurstTokenEmptyToken() {
        Options options = new Options();

        String token = "--";

        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);

        assertEquals(token, parser.tokens.get(parser.tokens.size() - 1));
        assertEquals(null, parser.currentOption);
        assertEquals(1, parser.tokens.size());
    }
}