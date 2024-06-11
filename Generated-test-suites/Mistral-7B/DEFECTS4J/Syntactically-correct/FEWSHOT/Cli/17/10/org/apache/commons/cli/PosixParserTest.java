package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testPosixParser_flatten_emptyArgs() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = new String[0];

        parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        assertEquals(expected.toArray(new String[expected.size()]), parser.getTokens().toArray(new String[parser.getTokens().size()]));
    }

    @Test
    public void testPosixParser_flatten_noOptions() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "arg2"};

        parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>(Arrays.asList("arg1", "arg2"));
        assertEquals(expected.toArray(new String[expected.size()]), parser.getTokens().toArray(new String[parser.getTokens().size()]));
    }

    @Test
    public void testPosixParser_flatten_singleOption() {
        Options options = new Options();
        options.addOption("o", "option", false, "An option");
        PosixParser parser = new PosixParser();
        String[] args = {"-o"};

        parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>(Arrays.asList("-o", "option"));
        assertEquals(expected.toArray(new String[expected.size()]), parser.getTokens().toArray(new String[parser.getTokens().size()]));
    }

    @Test
    public void testPosixParser_flatten_optionWithValue() {
        Options options = new Options();
        options.addOption("o", "option", true, "An option with value");
        PosixParser parser = new PosixParser();
        String[] args = {"-o", "value"};

        parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>(Arrays.asList("-o", "value"));
        assertEquals(expected.toArray(new String[expected.size()]), parser.getTokens().toArray(new String[parser.getTokens().size()]));
    }

    @Test
    public void testPosixParser_flatten_optionWithLongId() {
        Options options = new Options();
        options.addOption("longIdOption", "An option with long id", false, "A long id option");
        PosixParser parser = new PosixParser();
        String[] args = {"--longIdOption"};

        parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>(Arrays.asList("--longIdOption"));
        assertEquals(expected.toArray(new String[expected.size()]), parser.getTokens().toArray(new String[parser.getTokens().size()]));
    }

    @Test
    public void testPosixParser_flatten_optionWithLongIdAndValue() {
        Options options = new Options();
        options.addOption("longIdOption", "An option with long id", true, "A long id option with value");
        PosixParser parser = new PosixParser();
        String[] args = {"--longIdOption=value"};

        parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>(Arrays.asList("--longIdOption=value"));
        assertEquals(expected.toArray(new String[expected.size()]), parser.getTokens().toArray(new String[parser.getTokens().size()]));
    }

    @Test
    public void testPosixParser_flatten_multipleOptions() {
        Options options = new Options();
        options.addOption("o", "option1", false, "Option 1");
        options.addOption("o", "option2", false, "Option 2");
        PosixParser parser = new PosixParser();
        String[] args = {"-o", "value1", "-o", "value2"};

        parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>(Arrays.asList("-o", "option1", "value1", "-o", "option2", "value2"));
        assertEquals(expected.toArray(new String[expected.size()]), parser.getTokens().toArray(new String[parser.getTokens().size()]));
    }

    @Test
    public void testPosixParser_flatten_stopAtNonOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "-o", "value", "arg3"};

        parser.flatten(options, args, true);

        List<String> expected = new ArrayList<>(Arrays.asList("arg1", "-o", "value"));
        assertEquals(expected.toArray(new String[expected.size()]), parser.getTokens().toArray(new String[parser.getTokens().size()]));
    }

    @Test
    public void testPosixParser_burstToken() {
        Options options = new Options();
        PosixParser parser = new PosixParser();

        parser.burstToken("-foobar", false);

        List<String> expected = new ArrayList<>(Arrays.asList("-f", "-o", "o", "b", "a", "r"));
        List<String> tokens = new ArrayList<>(parser.getTokens());
        Collections.sort(expected);
        Collections.sort(tokens);
        assertTrue(expected.equals(tokens));
    }

    @Test
    public void testPosixParser_burstToken_stopAtNonOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();

        parser.burstToken("-foobar", true);

        List<String> expected = new ArrayList<>(Arrays.asList("--", "foobar"));
        List<String> tokens = new ArrayList<>(parser.getTokens());
        assertEquals(expected.toArray(new String[expected.size()]), tokens.subList(0, tokens.size()).toArray(new String[tokens.size()]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPosixParser_burstToken_invalidOptionId() {
        Options options = new Options();
        PosixParser parser = new PosixParser();

        parser.burstToken("-x", false);
    }

    @Test
    public void testPosixParser_getTokens() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "-o", "value1", "-o", "value2"};

        parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>(Arrays.asList("arg1", "-o", "option1", "value1", "-o", "option1", "value2"));
        List<String> tokens = new ArrayList<>(parser.getTokens());
        Collections.sort(expected);
        Collections.sort(tokens);
        assertTrue(expected.equals(tokens));
    }

    @Test
    public void testPosixParser_currentOption() {
        Options options = new Options();
        options.addOption("o", "option1", false, "Option 1");
        PosixParser parser = new PosixParser();
        String[] args = {"-o"};

        parser.flatten(options, args, false);

        Option currentOption = parser.getCurrentOption();

        assertEquals("option1", currentOption.getLongOpt());
        assertNull(currentOption.getValue());
    }

    @Test
    public void testPosixParser_currentOption_withValue() {
        Options options = new Options();
        options.addOption("o", "option1", true, "Option 1 with value");
        PosixParser parser = new PosixParser();
        String[] args = {"-o", "value"};

        parser.flatten(options, args, false);

        Option currentOption = parser.getCurrentOption();

        assertEquals("option1", currentOption.getLongOpt());
        assertEquals("value", currentOption.getValue());
    }

    @Test
    public void testPosixParser_hasCurrentOption() {
        Options options = new Options();
        options.addOption("o", "option1", false, "Option 1");
        PosixParser parser = new PosixParser();
        String[] args = {"-o"};

        parser.flatten(options, args, false);

        assertTrue(parser.hasCurrentOption());
    }

    @Test
    public void testPosixParser_hasCurrentOption_noOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "arg2"};

        parser.flatten(options, args, false);

        assertFalse(parser.hasCurrentOption());
    }

    @Test
    public void testPosixParser_getOptions() {
        Options options = new Options();
        options.addOption("o", "option1", false, "Option 1");
        PosixParser parser = new PosixParser();
        String[] args = {"-o"};

        parser.flatten(options, args, false);

        assertEquals(options, parser.getOptions());
    }
}