package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenEmpty() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {};
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[0], result);
    }

    @Test
    public void testFlattenWithOptions() {
        Options options = new Options();
        options.addOption("a", true, "Short option a");
        options.addOption("b", 1, "Option b with an argument");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--b=foo"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"-a", "--b=foo"}, result);
    }

    @Test
    public void testFlattenWithLongOption() {
        Options options = new Options();
        options.addOption("long-option", false, "Long option with no argument");
        PosixParser parser = new PosixParser();
        String[] args = {"--long-option"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--long-option"}, result);
    }

    @Test
    public void testFlattenWithLongOptionAndArgument() {
        Options options = new Options();
        options.addOption("long-option", true, "Long option with an argument");
        PosixParser parser = new PosixParser();
        String[] args = {"--long-option=foo"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--long-option=foo"}, result);
    }

    @Test
    public void testFlattenWithShortAndLongOptions() {
        Options options = new Options();
        options.addOption("a", true, "Short option a");
        options.addOption("b", 1, "Option b with an argument");
        options.addOption("long-option", false, "Long option with no argument");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--b=foo", "--long-option"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"-a", "--b=foo", "--long-option"}, result);
    }

    @Test
    public void testFlattenWithShortOptionArg() {
        Options options = new Options();
        options.addOption("a", 1, "Short option a with an argument");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "arg"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"-a", "arg"}, result);
    }

    @Test
    public void testFlattenWithShortAndLongOptionsAndArgs() {
        Options options = new Options();
        options.addOption("a", true, "Short option a");
        options.addOption("b", 1, "Option b with an argument");
        options.addOption("long-option", false, "Long option with no argument");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--b=foo", "-", "--long-option=bar", "arg"};
        String[] result = parser.flatten(options, args, false);
        List<String> expected = new ArrayList<>();
        expected.add("-a");
        expected.add("--b=foo");
        expected.add("-");
        expected.add("--long-option=bar");
        expected.add("arg");
        assertEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithShortOptionArgAndLongOptionArg() {
        Options options = new Options();
        options.addOption("a", 1, "Short option a with an argument");
        options.addOption("long-option", 1, "Long option with an argument");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "arg1", "--long-option=arg2"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"-a", "arg1", "--long-option=arg2"}, result);
    }

    @Test
    public void testFlattenWithLongOptionArgAndShortOptionArg() {
        Options options = new Options();
        options.addOption("a", 1, "Short option a with an argument");
        options.addOption("long-option", 1, "Long option with an argument");
        PosixParser parser = new PosixParser();
        String[] args = {"--long-option=arg1", "-a", "arg2"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--long-option=arg1", "-a", "arg2"}, result);
    }

    @Test
    public void testFlattenWithSingleHyphen() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"-"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"-"}, result);
    }

    @Test
    public void testFlattenWithDoubleHyphen() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"--"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--"}, result);
    }

    @Test
    public void testFlattenWithTwoDoubleHyphens() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"-- --"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--", "--"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Short option a");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "non-option"};
        String[] result = parser.flatten(options, args, true);
        assertEquals(new String[]{"-a", "non-option"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndLongOption() {
        Options options = new Options();
        options.addOption("a", true, "Short option a");
        options.addOption("long-option", false, "Long option with no argument");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--long-option", "non-option"};
        String[] result = parser.flatten(options, args, true);
        assertEquals(new String[]{"-a", "--long-option", "non-option"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndLongOptionWithArg() {
        Options options = new Options();
        options.addOption("a", true, "Short option a");
        options.addOption("long-option", 1, "Long option with an argument");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--long-option=arg", "non-option"};
        String[] result = parser.flatten(options, args, true);
        List<String> expected = new ArrayList<>();
        expected.add("-a");
        expected.add("--long-option=arg");
        expected.add("non-option");
        assertEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        parser.processOptionToken("-a", false);
        assertNull(parser.currentOption);

        options.addOption("a", true, "Short option a");
        parser = new PosixParser();
        parser.processOptionToken("-a", false);
        assertSame(options.getOption("a"), parser.currentOption);

        options.clear();
        parser = new PosixParser();
        parser.processOptionToken("--long-option", false);
        assertNull(parser.currentOption);

        options.addOption("long-option", false, "Long option with no argument");
        parser = new PosixParser();
        parser.processOptionToken("--long-option", false);
        assertSame(options.getOption("long-option"), parser.currentOption);
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.process("--foo=bar");
        assertTrue(parser.eatTheRest);
        assertEquals("--foo=bar", parser.tokens.get(parser.tokens.size() - 1));

        parser = new PosixParser();
        parser.process("-a");
        assertFalse(parser.eatTheRest);
        assertEquals("-a", parser.tokens.get(parser.tokens.size() - 1));

        parser = new PosixParser();
        parser.process("--");
        assertTrue(parser.eatTheRest);
        assertEquals("--", parser.tokens.get(parser.tokens.size() - 1));
    }

    @Test
    public void testBurstTokenEmpty() {
        PosixParser parser = new PosixParser();
        parser.burstToken("", false);
        assertTrue(parser.eatTheRest);
        assertEquals("", parser.tokens.get(parser.tokens.size() - 1));
    }

    @Test
    public void testBurstTokenSingleOption() {
        PosixParser parser = new PosixParser();
        parser.burstToken("-a", false);
        assertEquals("-a", parser.tokens.get(parser.tokens.size() - 1));
    }

    @Test
    public void testBurstTokenLongOption() {
        PosixParser parser = new PosixParser();
        parser.burstToken("--long-option", false);
        assertEquals("--long-option", parser.tokens.get(parser.tokens.size() - 1));
    }

    @Test
    public void testBurstTokenOptionWithArg() {
        Options options = new Options();
        options.addOption("a", 1, "Short option a with an argument");
        PosixParser parser = new PosixParser();
        parser.burstToken("-a", false);
        assertEquals("-a", parser.tokens.get(parser.tokens.size() - 1));
        assertEquals("arg", parser.tokens.get(parser.tokens.size() - 2));
    }

    @Test
    public void testBurstTokenLongOptionWithArg() {
        Options options = new Options();
        options.addOption("long-option", 1, "Long option with an argument");
        PosixParser parser = new PosixParser();
        parser.burstToken("--long-option=arg", false);
        assertEquals("--long-option=arg", parser.tokens.get(parser.tokens.size() - 1));
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Short option a");
        PosixParser parser = new PosixParser();
        parser.burstToken("-a", true);
        assertEquals("-a", parser.tokens.get(parser.tokens.size() - 1));

        parser.tokens.clear();
        parser.eatTheRest = false;
        parser.currentOption = null;

        parser.burstToken("--non-option", true);
        assertTrue(parser.eatTheRest);
        assertEquals("--non-option", parser.tokens.get(parser.tokens.size() - 1));

        parser.tokens.clear();
        parser.eatTheRest = false;
        parser.currentOption = null;

        parser.burstToken("--long-option=arg", true);
        assertTrue(parser.eatTheRest);
        assertEquals("--long-option=arg", parser.tokens.get(parser.tokens.size() - 1));
    }
}