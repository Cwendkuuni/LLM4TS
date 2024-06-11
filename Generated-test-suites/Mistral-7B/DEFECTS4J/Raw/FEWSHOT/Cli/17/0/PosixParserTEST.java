package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo", "-h", "-a", "-b", "--bar=baz", "-c=qux"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = new ArrayList<>();
        expected.add("--foo");
        expected.add("--");
        expected.add("h");
        expected.add("a");
        expected.add("b");
        expected.add("--bar");
        expected.add("baz");
        expected.add("--");
        expected.add("c");
        expected.add("qux");

        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo", "-h", "-a", "-b", "--bar=baz", "-c=qux", "quux"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        List<String> expected = new ArrayList<>();
        expected.add("--foo");
        expected.add("--bar");
        expected.add("baz");
        expected.add("quux");

        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenEmptyArguments() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, new String[0], false);
        assertEquals(new String[0], result);
    }

    @Test
    public void testFlattenNullArguments() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, null, false);
        assertEquals(new String[0], result);
    }

    @Test
    public void testFlattenWithInvalidOption() {
        Options options = new Options();
        options.addOption("z", false, "z", "Z");
        String[] arguments = new String[]{"--foo", "-h", "-z", "-a", "-b", "--bar=baz", "-c=qux"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = new ArrayList<>();
        expected.add("--foo");
        expected.add("--");
        expected.add("h");
        expected.add("a");
        expected.add("b");
        expected.add("--bar");
        expected.add("baz");
        expected.add("--");
        expected.add("c");
        expected.add("qux");
        expected.add("--");
        expected.add("z");

        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithOptionArg() {
        Options options = new Options();
        options.addOption("a", true, "a");
        String[] arguments = new String[]{"--foo", "-a", "arg1", "-a", "arg2", "--bar=baz"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = new ArrayList<>();
        expected.add("--foo");
        expected.add("--");
        expected.add("a");
        expected.add("true");
        expected.add("arg1");
        expected.add("a");
        expected.add("true");
        expected.add("arg2");
        expected.add("--bar");
        expected.add("baz");

        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithMultipleArgs() {
        Options options = new Options();
        options.addOption("a", true, "a");
        String[] arguments = new String[]{"--foo", "-a", "arg1", "--arg=arg1,arg2,arg3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = new ArrayList<>();
        expected.add("--foo");
        expected.add("--");
        expected.add("a");
        expected.add("true");
        expected.add("arg1");
        expected.add("--arg");
        expected.add("arg1");
        expected.add("arg2");
        expected.add("arg3");

        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithLongOptions() {
        Options options = new Options();
        options.addOption("long-option", false, "l", "lo");
        String[] arguments = new String[]{"--long-option", "--foo", "-h", "-a", "--long-option=long-value", "--bar=baz"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = new ArrayList<>();
        expected.add("--long-option");
        expected.add("--");
        expected.add("long-option");
        expected.add("false");
        expected.add("--foo");
        expected.add("--");
        expected.add("h");
        expected.add("a");
        expected.add("--long-option");
        expected.add("long-value");
        expected.add("--bar");
        expected.add("baz");

        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        assertEquals("-", parser.processSingleHyphen("-"));
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("a", true, "a");
        PosixParser parser = new PosixParser();
        Option option = options.getOption("a");
        assertEquals("a", parser.processOptionToken("-a", false)[0]);
        assertSame(option, parser.getCurrentOption());
    }

    @Test
    public void testProcessOptionTokenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "a");
        PosixParser parser = new PosixParser();
        String[] result = parser.processOptionToken("-a", true);
        assertEquals(2, result.length);
        assertEquals("a", result[0]);
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", true, "a");
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken("-a--arg", false);

        List<String> expected = new ArrayList<>();
        expected.add("-a");
        expected.add("true");
        expected.add("arg");

        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "a");
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken("-a--arg", true);

        List<String> expected = new ArrayList<>();
        expected.add("--");
        expected.add("arg");

        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }
}