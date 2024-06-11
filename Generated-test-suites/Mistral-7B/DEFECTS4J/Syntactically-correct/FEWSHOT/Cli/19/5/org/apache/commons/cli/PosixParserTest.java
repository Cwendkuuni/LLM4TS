package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten_EmptyArgList() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] arguments = {};

        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = new ArrayList<>();

        assertEquals(0, result.length);
        assertEquals(expected.size(), parser.tokens.size());
        assertEquals(result, parser.tokens.toArray(new String[0]));
    }

    @Test
    public void testFlatten_SingleArgument() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] arguments = {"arg"};

        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("arg");

        assertEquals(1, result.length);
        assertEquals(expected.size(), parser.tokens.size());
        assertEquals(Arrays.asList("arg"), parser.tokens);
    }

    @Test
    public void testFlatten_EmptyOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] arguments = {"--foo"};

        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--foo");

        assertEquals(1, result.length);
        assertEquals(expected.size(), parser.tokens.size());
        assertEquals(Arrays.asList("--foo"), parser.tokens);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] arguments = {"-"};

        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-");

        assertEquals(1, result.length);
        assertEquals(expected.size(), parser.tokens.size());
        assertEquals(Arrays.asList("-"), parser.tokens);
    }

    @Test
    public void testFlatten_SingleOption() {
        Options options = new Options();
        options.addOption("foo", true, "f");
        PosixParser parser = new PosixParser();
        String[] arguments = {"-f"};

        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-f", "foo");

        assertEquals(1, result.length);
        assertEquals(expected.size(), parser.tokens.size());
        assertEquals(Arrays.asList("-f", "foo"), parser.tokens);
    }

    @Test
    public void testFlatten_OptionWithArg() {
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", 1, "b");
        PosixParser parser = new PosixParser();
        String[] arguments = {"-f", "arg1", "-b", "arg2"};

        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-f", "arg1", "-b", "arg2", "foo", "arg1", "bar", "arg2");

        assertEquals(result.length, expected.size());
        for (int i = 0; i < result.length; i++) {
            assertEquals(expected.get(i), result[i]);
        }
    }

    @Test
    public void testFlatten_LongOption() {
        Options options = new Options();
        options.addOption("long-option", true, "l");
        PosixParser parser = new PosixParser();
        String[] arguments = {"--long-option"};

        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--long-option", "long-option");

        assertEquals(1, result.length);
        assertEquals(expected.size(), parser.tokens.size());
        assertEquals(Arrays.asList("--long-option", "long-option"), parser.tokens);
    }

    @Test
    public void testFlatten_LongOptionWithArg() {
        Options options = new Options();
        options.addOption("long-option", true, "l");
        options.addOption("another-option", 1, "a");
        PosixParser parser = new PosixParser();
        String[] arguments = {"--long-option=arg1", "--another-option=arg2"};

        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--long-option=arg1", "long-option", "--another-option=arg2", "another-option", "arg2");

        assertEquals(result.length, expected.size());
        for (int i = 0; i < result.length; i++) {
            assertEquals(expected.get(i), result[i]);
        }
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        options.addOption("foo", true, "f");
        PosixParser parser = new PosixParser();
        String[] arguments = {"-f", "arg1", "-bar"};

        String[] result = parser.flatten(options, arguments, true);
        List<String> expected = Arrays.asList("-f", "arg1", "--", "bar");

        assertEquals(result.length, expected.size());
        for (int i = 0; i < result.length; i++) {
            assertEquals(expected.get(i), result[i]);
        }
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();

        parser.process("foo");
        assertTrue(parser.tokens.isEmpty());
        parser.init();
        parser.process("--foo");
        List<String> expected = Arrays.asList("--foo");
        assertEquals(expected.size(), parser.tokens.size());
        assertEquals(Arrays.asList("--foo"), parser.tokens);

        parser.init();
        parser.process("-f");
        expected = Arrays.asList("-f", "foo");
        assertEquals(expected.size(), parser.tokens.size());
        assertEquals(Arrays.asList("-f", "foo"), parser.tokens);

        parser.init();
        parser.process("--");
        assertTrue(parser.tokens.isEmpty());
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();

        Options options = new Options();
        options.addOption("foo", true, "f");
        parser.init();
        parser.processOptionToken("-f", false);
        List<String> expected = Arrays.asList("-f", "foo");
        assertEquals(expected.size(), parser.tokens.size());
        assertEquals(Arrays.asList("-f", "foo"), parser.tokens);

        parser.init();
        parser.processOptionToken("-f", true);
        expected = Arrays.asList("--", "-f");
        assertEquals(expected.size(), parser.tokens.size());
        assertEquals(Arrays.asList("--", "-f"), parser.tokens);
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();

        Options options = new Options();
        options.addOption("foo", true, "f");
        parser.init();
        parser.burstToken("-ffoo", false);
        List<String> expected = Arrays.asList("-f", "-f", "foo");
        assertEquals(expected.size(), parser.tokens.size());
        assertEquals(Arrays.asList("-f", "-f", "foo"), parser.tokens);

        parser.init();
        parser.burstToken("-ffoo", true);
        expected = Arrays.asList("--", "-f", "-f", "foo");
        assertEquals(expected.size(), parser.tokens.size());
        assertEquals(Arrays.asList("--", "-f", "-f", "foo"), parser.tokens);
    }
}