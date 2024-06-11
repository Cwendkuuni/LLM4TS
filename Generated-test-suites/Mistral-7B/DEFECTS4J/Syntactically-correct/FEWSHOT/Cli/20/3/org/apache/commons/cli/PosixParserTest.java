package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenSingleOption() {
        Options options = new Options();
        options.addOption("a", true, "A short option");

        String[] args = { "-a" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List expected = Arrays.asList("-a");
        assertEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenSingleLongOption() {
        Options options = new Options();
        options.addOption("longOption", true, "A long option");

        String[] args = { "--longOption" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List expected = Arrays.asList("--longOption");
        assertEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenSingleOptionWithValue() {
        Options options = new Options();
        options.addOption("a", true, "A short option");

        String[] args = { "-a", "value" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List expected = Arrays.asList("-a", "value");
        assertEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenSingleLongOptionWithValue() {
        Options options = new Options();
        options.addOption("longOption", true, "A long option");

        String[] args = { "--longOption", "value" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List expected = Arrays.asList("--longOption", "value");
        assertEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenMultipleOptions() {
        Options options = new Options();
        options.addOption("a", true, "A short option");
        options.addOption("b", true, "Another short option");
        options.addOption("c", false, "A boolean option");

        String[] args = { "-a", "-b", "--c" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List expected = Arrays.asList("-a", "-b", "--c");
        assertEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenMultipleLongOptions() {
        Options options = new Options();
        options.addOption("longOptionA", true, "A long option A");
        options.addOption("longOptionB", true, "A long option B");

        String[] args = { "--longOptionA", "--longOptionB" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List expected = Arrays.asList("--longOptionA", "--longOptionB");
        assertEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenOptionWithValueAndArguments() {
        Options options = new Options();
        options.addOption("option", true, "An option with an argument");
        options.addRequiredOption("arg", true, "An argument");

        String[] args = { "-option", "arg1", "arg2" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List expected = Arrays.asList("-option", "arg1", "arg2");
        assertEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenOptionWithValueAndArguments_Long() {
        Options options = new Options();
        options.addOption("option", true, "An option with an argument");
        options.addRequiredOption("arg", true, "An argument");

        String[] args = { "--option", "arg1", "arg2" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List expected = Arrays.asList("--option", "arg1", "arg2");
        assertEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "A short option");

        String[] args = { "arg1", "-a", "arg3" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        List expected = Arrays.asList("arg1", "-a", "arg3");
        assertEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testBurstTokenSingleOption() {
        PosixParser parser = new PosixParser();
        List tokens = parser.burstToken("-o", false);

        List expected = Arrays.asList("-o");
        assertEquals(expected, tokens);
    }

    @Test
    public void testBurstTokenLongOption() {
        PosixParser parser = new PosixParser();
        List tokens = parser.burstToken("--longOption", false);

        List expected = Arrays.asList("--longOption");
        assertEquals(expected, tokens);
    }

    @Test
    public void testBurstTokenSingleOptionWithArgument() {
        PosixParser parser = new PosixParser();
        List tokens = parser.burstToken("-o=value", false);

        List expected = Arrays.asList("-o", "value");
        assertEquals(expected, tokens);
    }

    @Test
    public void testBurstTokenLongOptionWithArgument() {
        PosixParser parser = new PosixParser();
        List tokens = parser.burstToken("--longOption=value", false);

        List expected = Arrays.asList("--longOption", "value");
        assertEquals(expected, tokens);
    }

    @Test
    public void testBurstTokenInvalidOption() {
        PosixParser parser = new PosixParser();
        List tokens = parser.burstToken("invalid", false);

        assertTrue(tokens.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBurstTokenInvalidOptionWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        List tokens = parser.burstToken("invalid", true);
    }

    @Test
    public void testBurstTokenMultipleOptions() {
        PosixParser parser = new PosixParser();
        List tokens = parser.burstToken("-o1 -o2", false);

        List expected = Arrays.asList("-o1", "-o2");
        assertEquals(expected, tokens);
    }

    @Test
    public void testBurstTokenMultipleOptionsWithArguments() {
        PosixParser parser = new PosixParser();
        List tokens = parser.burstToken("-o1 value1 -o2 value2", false);

        List expected = Arrays.asList("-o1", "value1", "-o2", "value2");
        assertEquals(expected, tokens);
    }

    @Test
    public void testBurstTokenInvalidOptionWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        List tokens = parser.burstToken("invalid", true);

        assertFalse(tokens.isEmpty());
        assertTrue(tokens.contains("--"));
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        List tokens = parser.burstToken("-a invalid", true);

        List expected = Arrays.asList("-a", "--");
        assertEquals(expected, tokens);
    }
}