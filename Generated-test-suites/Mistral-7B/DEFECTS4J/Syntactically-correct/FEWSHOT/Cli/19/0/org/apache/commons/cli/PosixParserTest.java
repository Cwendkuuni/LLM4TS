package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        Options options = new Options();
        String[] args = {"--foo", "-a", "-b", "-c=value", "--", "arg1", "arg2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("--foo");
        expected.add("-a");
        expected.add("-c");
        expected.add("value");
        expected.add("--");
        expected.add("arg1");
        expected.add("arg2");

        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        String[] args = {"--foo", "-a", "-b", "-c=value", "--", "arg1", "arg2", "nonOption"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        List<String> expected = new ArrayList<>();
        expected.add("--foo");
        expected.add("-a");
        expected.add("-b");
        expected.add("nonOption");

        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        String[] args = {"--foo-bar", "--baz=qux"};

        PosixParser parser = new PosixParser();

        // test bursting for valid options
        List<String> tokens = new ArrayList<>();
        parser.burstToken("--foo-bar", false, tokens);
        assertEquals(2, tokens.size());
        assertEquals("-foo", tokens.get(0));
        assertEquals("-bar", tokens.get(1));

        tokens.clear();
        parser.burstToken("--baz=qux", false, tokens);
        assertEquals(3, tokens.size());
        assertEquals("-baz", tokens.get(0));
        assertEquals("qux", tokens.get(1));

        // test bursting for non-options
        tokens.clear();
        parser.burstToken("--foo-nonOption", true, tokens);
        assertEquals(1, tokens.size());
        assertEquals("--foo-nonOption", tokens.get(0));
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B with argument");

        PosixParser parser = new PosixParser();

        // test valid option
        List<String> tokens = new ArrayList<>();
        parser.processOptionToken("-a", false, tokens);
        assertEquals(1, tokens.size());
        assertEquals("-a", tokens.get(0));

        tokens.clear();
        parser.processOptionToken("--b", false, tokens);
        assertEquals(1, tokens.size());
        assertEquals("--b", tokens.get(0));

        // test valid option with argument
        tokens.clear();
        parser.processOptionToken("-b=arg", false, tokens);
        assertEquals(2, tokens.size());
        assertEquals("-b", tokens.get(0));
        assertEquals("arg", tokens.get(1));

        // test invalid option
        tokens.clear();
        parser.processOptionToken("invalid", true, tokens);
        assertTrue(tokens.isEmpty());
    }

    @Test
    public void testProcess() {
        Options options = new Options();
        String[] args = {"--foo", "-a", "-b", "-c=value", "--", "arg1", "arg2", "nonOption"};

        PosixParser parser = new PosixParser();

        List<String> tokens = new ArrayList<>();
        parser.init();

        for (String arg : args) {
            parser.process(arg);
            tokens.addAll(parser.tokens);
            parser.init();
        }

        List<String> expected = new ArrayList<>();
        expected.add("--foo");
        expected.add("-a");
        expected.add("-c");
        expected.add("value");
        expected.add("--");
        expected.add("arg1");
        expected.add("arg2");

        assertArrayEquals(expected.toArray(new String[expected.size()]), tokens.toArray(new String[tokens.size()]));
    }

    @Test
    public void testFlattenWithOptionWithArgs() {
        Options options = new Options();
        options.addOption("a", true, "Option A with arguments");

        String[] args = {"--a", "arg1", "arg2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("--a");
        expected.add("arg1");
        expected.add("arg2");

        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithLongOptions() {
        Options options = new Options();
        options.addOption("longOptionWithArg", true, "Long option with argument");

        String[] args = {"--longOptionWithArg", "arg1", "arg2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("--longOptionWithArg");
        expected.add("arg1");
        expected.add("arg2");

        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithLongOptionWithoutArg() {
        Options options = new Options();
        options.addOption("longOptionWithoutArg", false, "Long option without argument");

        String[] args = {"--longOptionWithoutArg"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("--longOptionWithoutArg");

        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithMixedOptionsAndArguments() {
        Options options = new Options();
        options.addOption("a", true, "Option A with arguments");
        options.addOption("b", false, "Option B without arguments");

        String[] args = {"--a", "arg1", "--b", "arg3"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("--a");
        expected.add("arg1");
        expected.add("--b");

        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithLongOptionsAndArguments() {
        Options options = new Options();
        options.addOption("longOptionWithArg", true, "Long option with argument");

        String[] args = {"--longOptionWithArg=arg1", "arg2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("--longOptionWithArg=arg1");
        expected.add("arg2");

        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithLongOptionsAndMixedArguments() {
        Options options = new Options();
        options.addOption("longOptionWithArg", true, "Long option with argument");

        String[] args = {"--longOptionWithArg=arg1", "arg2", "arg3"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("--longOptionWithArg=arg1");
        expected.add("arg2");
        expected.add("arg3");

        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndLongOptions() {
        Options options = new Options();
        options.addOption("longOptionWithArg", true, "Long option with argument");

        String[] args = {"--longOptionWithArg=arg1", "arg2", "nonOption"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        List<String> expected = new ArrayList<>();
        expected.add("--longOptionWithArg=arg1");
        expected.add("nonOption");

        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndLongOptionsWithoutArguments() {
        Options options = new Options();
        options.addOption("longOptionWithoutArg", false, "Long option without argument");

        String[] args = {"--longOptionWithoutArg", "arg1", "nonOption"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        List<String> expected = new ArrayList<>();
        expected.add("--longOptionWithoutArg");
        expected.add("nonOption");

        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndLongOptionsWithArguments() {
        Options options = new Options();
        options.addOption("longOptionWithArg", true, "Long option with argument");

        String[] args = {"--longOptionWithArg=arg1", "arg2", "arg3", "nonOption"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        List<String> expected = new ArrayList<>();
        expected.add("--longOptionWithArg=arg1");
        expected.add("nonOption");

        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndShortOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A with arguments");

        String[] args = {"-a", "arg1", "-b", "nonOption"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        List<String> expected = new ArrayList<>();
        expected.add("-a");
        expected.add("arg1");
        expected.add("nonOption");

        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }
}