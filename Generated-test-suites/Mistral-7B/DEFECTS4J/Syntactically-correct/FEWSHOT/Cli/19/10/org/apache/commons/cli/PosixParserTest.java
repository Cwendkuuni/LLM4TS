package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    private static final String[] EMPTY_ARGUMENTS = {};
    private static final String[] ARGUMENTS1 = {"--foo", "-a", "--bar=baz", "-b"};
    private static final String[] ARGUMENTS2 = {"--foo", "-a", "--bar=baz", "-b", "--qux"};
    private static final String[] ARGUMENTS3 = {"--foo", "-a--qux", "-b", "--bar=baz"};

    @Test
    public void testFlatten_Empty() {
        PosixParser parser = new PosixParser();
        assertArrayEquals(EMPTY_ARGUMENTS, parser.flatten(new Options(), EMPTY_ARGUMENTS, false));
    }

    @Test
    public void testFlatten_NoOptions() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"arg1", "arg2"};
        String[] expected = {"arg1", "arg2"};
        assertArrayEquals(expected, parser.flatten(new Options(), arguments, false));
    }

    @Test
    public void testFlatten_SimpleOptions() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--foo", "-a", "--bar=baz", "-b"};
        String[] expected = {"--foo", "--bar=baz", "-b", "-a"};
        assertArrayEquals(expected, parser.flatten(new Options(), arguments, false));
    }

    @Test
    public void testFlatten_ComplexOptions() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--foo", "-a", "--bar=baz", "-b", "--qux"};
        String[] expected = {"--foo", "--bar=baz", "--qux", "-b", "-a"};
        assertArrayEquals(expected, parser.flatten(new Options(), arguments, false));
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--foo", "-a", "--bar=baz", "-b", "--qux", "arg"};
        String[] expected = {"--foo", "-a", "--bar=baz", "-b", "--qux", "arg"};
        assertArrayEquals(expected, parser.flatten(new Options(), arguments, true));
    }

    @Test
    public void testFlatten_BurstedOptions() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--foo", "-a--qux", "-b", "--bar=baz"};
        String[] expected = {"--foo", "--qux", "-b", "--bar=baz", "-a"};
        assertArrayEquals(expected, parser.flatten(new Options(), arguments, false));
    }

    @Test
    public void testBurstToken_SimpleOption() {
        PosixParser parser = new PosixParser();
        String token = "-a";
        assertArrayEquals(new String[]{"-a"}, parser.burstToken(token, false));
    }

    @Test
    public void testBurstToken_OptionWithArg() {
        PosixParser parser = new PosixParser();
        String token = "--bar=baz";
        List expected = Arrays.asList("-b", "--bar");
        assertArrayEquals(expected.toArray(new String[0]), parser.burstToken(token, false));
    }

    @Test
    public void testBurstToken_UnrecognizedToken() {
        PosixParser parser = new PosixParser();
        String token = "qux";
        assertNull(parser.burstToken(token, false));
    }

    @Test
    public void testBurstToken_StopAtNonOption() {
        PosixParser parser = new PosixParser();
        String token = "--qux";
        String[] expected = {"--qux"};
        assertArrayEquals(expected, parser.burstToken(token, true));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Option option = new Option("foo", "f", false, "FOO");
        String token = "-f";
        parser.processOptionToken(token, false);
        assertSame(option, parser.currentOption);
    }
}