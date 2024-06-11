package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

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

        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenSingleOption() {
        Options options = new Options();
        options.addOption("o", "option", false, "a test option");
        String[] arguments = new String[]{"-o"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("option", result[0]);
    }

    @Test
    public void testFlattenSingleOptionWithArg() {
        Options options = new Options();
        options.addOption("o", null, true, "an option with argument");
        String[] arguments = new String[]{"-o", "arg"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("arg", result[1]);
    }

    @Test
    public void testFlattenLongOption() {
        Options options = new Options();
        options.addOption("longOption", false, null, "a long option");
        String[] arguments = new String[]{"--longOption"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("longOption", result[0]);
    }

    @Test
    public void testFlattenLongOptionWithArg() {
        Options options = new Options();
        options.addOption("longOption", true, null, "a long option with argument");
        String[] arguments = new String[]{"--longOption", "arg"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("arg", result[1]);
    }

    @Test
    public void testFlattenDoubleHyphen() {
        Options options = new Options();
        String[] arguments = new String[]{"--"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(0, result.length);
    }

    @Test
    public void testFlattenDoubleHyphenWithArgument() {
        Options options = new Options();
        String[] arguments = new String[]{"--arg"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("arg", result[0]);
    }

    @Test
    public void testFlattenMultipleOptions() {
        Options options = new Options();
        options.addOption("o", "option1", false, "option 1");
        options.addOption("o", "option2", false, "option 2");
        String[] arguments = new String[]{"-o", "arg1", "-o", "arg2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"option1", "arg1", "option2", "arg2"}, result);
    }

    @Test
    public void testFlattenMultipleOptionsWithArguments() {
        Options options = new Options();
        options.addOption("o", null, true, "option with argument");
        String[] arguments = new String[]{"-o", "arg1", "-o", "arg2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"arg1", "arg2"}, result);
    }

    @Test
    public void testFlattenMixedOptionsAndArguments() {
        Options options = new Options();
        options.addOption("o", null, true, "option with argument");
        options.addOption("l", "longOption", false, "long option");
        String[] arguments = new String[]{"-l", "--arg", "-o", "arg"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"longOption", "--arg", "option with argument", "arg"}, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("o", null, true, "option with argument");
        String[] arguments = new String[]{"-o", "arg1", "nonOption"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"option with argument", "arg1"}, result);
    }

    @Test
    public void testFlattenStopAtNonOptionWithArgument() {
        Options options = new Options();
        options.addOption("o", null, true, "option with argument");
        String[] arguments = new String[]{"nonOption", "--arg"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"--arg"}, result);
    }

    @Test
    public void testFlattenStopAtNonOptionMixed() {
        Options options = new Options();
        options.addOption("o", null, true, "option with argument");
        String[] arguments = new String[]{"-o", "arg1", "nonOption", "--arg"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"option with argument", "arg1"}, result);
    }

    @Test
    public void testFlattenLongOptionWithArguments() {
        Options options = new Options();
        options.addOption("longOption", true, null, "long option with arguments");
        String[] arguments = new String[]{"--longOption", "arg1", "--longOption", "arg2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"longOption", "arg1", "longOption", "arg2"}, result);
    }

    @Test
    public void testBurstTokenOption() {
        Options options = new Options();
        options.addOption("o", "option", false, "option");
        String[] arguments = new String[]{"-oo"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("option", result[0]);
        assertEquals("option", result[1]);
    }

    @Test
    public void testBurstTokenOptionWithArg() {
        Options options = new Options();
        options.addOption("o", null, true, "option with argument");
        String[] arguments = new String[]{"-oo", "arg"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("option with argument", result[0]);
        assertEquals("arg", result[1]);
    }

    @Test
    public void testBurstTokenLongOption() {
        Options options = new Options();
        options.addOption("longOption", false, null, "long option");
        String[] arguments = new String[]{"--longOption--"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("longOption", result[0]);
        assertEquals("longOption", result[1]);
    }

    @Test
    public void testBurstTokenLongOptionWithArg() {
        Options options = new Options();
        options.addOption("longOption", true, null, "long option with argument");
        String[] arguments = new String[]{"--longOption--arg"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("longOption", result[0]);
        assertEquals("arg", result[1]);
    }

    @Test
    public void testBurstTokenStopAtNonOptionOption() {
        Options options = new Options();
        options.addOption("o", "option", false, "option");
        String[] arguments = new String[]{"-oo", "arg", "nonOption"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals("option", result[0]);
        assertEquals("arg", result[1]);
    }

    @Test
    public void testBurstTokenStopAtNonOptionOptionWithArg() {
        Options options = new Options();
        options.addOption("o", null, true, "option with argument");
        String[] arguments = new String[]{"-oo", "arg", "nonOption"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"option with argument", "arg"}, result);
    }

    @Test
    public void testBurstTokenStopAtNonOptionLongOption() {
        Options options = new Options();
        options.addOption("longOption", false, null, "long option");
        String[] arguments = new String[]{"--longOption--", "nonOption"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testBurstTokenStopAtNonOptionLongOptionWithArg() {
        Options options = new Options();
        options.addOption("longOption", true, null, "long option with argument");
        String[] arguments = new String[]{"--longOption--arg", "nonOption"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"--longOption--arg"}, result);
    }

    @Test
    public void testBurstTokenSingleCharacterOption() {
        Options options = new Options();
        options.addOption("o", "option", false, "option");
        String[] arguments = new String[]{"-o"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("option", result[0]);
    }

    @Test
    public void testBurstTokenSingleCharacterOptionWithArg() {
        Options options = new Options();
        options.addOption("o", null, true, "option with argument");
        String[] arguments = new String[]{"-o", "arg"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("option with argument", result[0]);
        assertEquals("arg", result[1]);
    }

    @Test
    public void testBurstTokenSingleCharacterOptionStopAtNonOption() {
        Options options = new Options();
        options.addOption("o", "option", false, "option");
        String[] arguments = new String[]{"-o", "arg", "nonOption"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals("option", result[0]);
        assertEquals("arg", result[1]);
    }

    @Test
    public void testBurstTokenSingleCharacterOptionStopAtNonOptionWithArg() {
        Options options = new Options();
        options.addOption("o", null, true, "option with argument");
        String[] arguments = new String[]{"-o", "arg", "nonOption"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"option with argument", "arg"}, result);
    }

    @Test
    public void testBurstTokenEmptyToken() {
        Options options = new Options();
        String[] arguments = new String[]{"--"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testBurstTokenEmptyTokenStopAtNonOption() {
        Options options = new Options();
        String[] arguments = new String[]{"--", "nonOption"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testBurstTokenEmptyTokenWithArg() {
        Options options = new Options();
        String[] arguments = new String[]{"--arg", "--"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--arg"}, result);
    }

    @Test
    public void testBurstTokenEmptyTokenWithArgStopAtNonOption() {
        Options options = new Options();
        String[] arguments = new String[]{"--arg", "--", "nonOption"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"--arg"}, result);
    }

    @Test
    public void testBurstTokenMultiCharacterToken() {
        Options options = new Options();
        options.addOption("oo", "option", false, "option");
        String[] arguments = new String[]{"-oo"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("option", result[0]);
        assertEquals("option", result[1]);
    }

    @Test
    public void testBurstTokenMultiCharacterTokenWithArg() {
        Options options = new Options();
        options.addOption("oo", null, true, "option with argument");
        String[] arguments = new String[]{"-oo", "arg"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("option with argument", result[0]);
        assertEquals("arg", result[1]);
    }

    @Test
    public void testBurstTokenMultiCharacterTokenStopAtNonOption() {
        Options options = new Options();
        options.addOption("oo", "option", false, "option");
        String[] arguments = new String[]{"-oo", "arg", "nonOption"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals("option", result[0]);
        assertEquals("arg", result[1]);
    }

    @Test
    public void testBurstTokenMultiCharacterTokenStopAtNonOptionWithArg() {
        Options options = new Options();
        options.addOption("oo", null, true, "option with argument");
        String[] arguments = new String[]{"-oo", "arg", "nonOption"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"option with argument", "arg"}, result);
    }

    @Test
    public void testBurstTokenMultiCharacterTokenWithArguments() {
        Options options = new Options();
        options.addOption("oo", null, true, "option with argument");
        String[] arguments = new String[]{"-oo", "arg1", "-oo", "arg2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"option with argument", "arg1", "option with argument", "arg2"}, result);
    }
}