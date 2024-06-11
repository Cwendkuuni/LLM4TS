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
    public void testFlattenEmpty() {
        Options options = new Options();
        String[] arguments = new String[0];
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(0, result.length);
    }

    @Test
    public void testFlattenSingleToken() {
        Options options = new Options();
        String[] arguments = new String[]{"foo"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(1, result.length);
        assertEquals("foo", result[0]);
    }

    @Test
    public void testFlattenSingleOption() {
        Options options = new Options();
        options.addOption("f", false, null, "A flag");
        String[] arguments = new String[]{"-f"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(1, result.length);
        assertEquals("-f", result[0]);
    }

    @Test
    public void testFlattenSingleOptionWithArg() {
        Options options = new Options();
        options.addOption("f", true, null, "An argument");
        String[] arguments = new String[]{"-f", "arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(2, result.length);
        assertEquals("-f", result[0]);
        assertEquals("arg", result[1]);
    }

    @Test
    public void testFlattenMultipleOptions() {
        Options options = new Options();
        options.addOption("f", false, null, "A flag");
        options.addOption("o", true, null, "An option with an argument");
        String[] arguments = new String[]{"-f", "-o", "arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(3, result.length);
        assertEquals("-f", result[0]);
        assertEquals("-o", result[1]);
        assertEquals("arg", result[2]);
    }

    @Test
    public void testFlattenLongOption() {
        Options options = new Options();
        options.addOption("longOption", false, null, "A long option");
        String[] arguments = new String[]{"--longOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(1, result.length);
        assertEquals("--longOption", result[0]);
    }

    @Test
    public void testFlattenLongOptionWithArg() {
        Options options = new Options();
        options.addOption("longOption", true, null, "A long option with an argument");
        String[] arguments = new String[]{"--longOption", "arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(2, result.length);
        assertEquals("--longOption", result[0]);
        assertEquals("arg", result[1]);
    }

    @Test
    public void testFlattenSingleDash() {
        Options options = new Options();
        String[] arguments = new String[]{"-"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlattenDoubleDash() {
        Options options = new Options();
        String[] arguments = new String[]{"--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(1, result.length);
        assertEquals("--", result[0]);
    }

    @Test
    public void testFlattenEmptyOptionList() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo", "--bar"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(0, result.length);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("f", false, null, "A flag");
        String[] arguments = new String[]{"foo", "-f"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(2, result.length);
        assertEquals("foo", result[0]);
        assertEquals("-f", result[1]);
    }

    @Test
    public void testFlattenBurstTokenSingleOption() {
        Options options = new Options();
        options.addOption("fo", true, null, "An option with a single character id");
        String[] arguments = new String[]{"-fo", "arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(2, result.length);
        assertEquals("-fo", result[0]);
        assertEquals("arg", result[1]);
    }

    @Test
    public void testFlattenBurstTokenMultipleOptions() {
        Options options = new Options();
        options.addOption("f", false, null, "Option f");
        options.addOption("o", true, null, "Option o");
        options.addOption("foo", false, null, "Option foo");
        String[] arguments = new String[]{"-foobar"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(4, result.length);
        assertEquals("-f", result[0]);
        assertEquals("-o", result[1]);
        assertEquals("-oo", result[2]);
        assertEquals("bar", result[3]);
    }

    @Test
    public void testFlattenBurstTokenLongOption() {
        Options options = new Options();
        options.addOption("longOption", false, null, "A long option");
        String[] arguments = new String[]{"--longOptionArg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(2, result.length);
        assertEquals("--longOption", result[0]);
        assertEquals("Arg", result[1]);
    }

    @Test
    public void testFlattenBurstTokenLongOptionWithArg() {
        Options options = new Options();
        options.addOption("longOption", true, null, "A long option with an argument");
        String[] arguments = new String[]{"--longOptionArgValue"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(3, result.length);
        assertEquals("--longOption", result[0]);
        assertEquals("ArgValue", result[1]);
        assertEquals("", result[2]);
    }

    @Test
    public void testFlattenBurstTokenEmptyToken() {
        Options options = new Options();
        String[] arguments = new String[]{"--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(1, result.length);
        assertEquals("--", result[0]);
    }

    @Test
    public void testFlattenBurstTokenEmptyOptionList() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo", "--bar"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(0, result.length);
    }

    @Test
    public void testFlattenBurstTokenStopAtNonOption() {
        Options options = new Options();
        options.addOption("f", false, null, "Option f");
        String[] arguments = new String[]{"foo", "-foobar"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(3, result.length);
        assertEquals("foo", result[0]);
        assertEquals("-foobar", result[1]);
        assertEquals("", result[2]);
    }

    @Test
    public void testFlattenBurstTokenMixed() {
        Options options = new Options();
        options.addOption("f", false, null, "Option f");
        options.addOption("o", true, null, "Option o");
        String[] arguments = new String[]{"foo", "--longOptionArgValue", "-foobar"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(6, result.length);
        assertEquals("foo", result[0]);
        assertEquals("--longOption", result[1]);
        assertEquals("ArgValue", result[2]);
        assertEquals("-f", result[3]);
        assertEquals("-o", result[4]);
        assertEquals("bar", result[5]);
    }

    @Test
    public void testFlattenBurstTokenWithArgAndLongOption() {
        Options options = new Options();
        options.addOption("f", true, null, "Option f with argument");
        options.addOption("lo", true, null, "Option lo with argument");
        options.addOption("longOption", true, null, "A long option with argument");
        String[] arguments = new String[]{"-fo", "--longOptionArgValue"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(5, result.length);
        assertEquals("-f", result[0]);
        assertEquals("argValue", result[1]);
        assertEquals("--longOption", result[2]);
        assertEquals("ArgValue", result[3]);
        assertEquals("", result[4]);
    }
}