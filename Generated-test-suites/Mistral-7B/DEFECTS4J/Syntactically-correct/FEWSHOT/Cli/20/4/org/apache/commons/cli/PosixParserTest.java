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
    public void testFlattenWithEmptyArgs() {
        PosixParser parser = new PosixParser();
        String[] args = {};
        Options options = new Options();
        String[] result = parser.flatten(options, args, false);
        assertNull(result);
    }

    @Test
    public void testFlattenWithBasicOptions() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--optionB", "--optionC=value"};
        Options options = new Options();
        Option optionA = new Option("a", "a", false, "a help message");
        Option optionB = new Option("b", "b", true, "b help message");
        Option optionC = new Option("c", "c", true, "c help message");
        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);
        String[] result = parser.flatten(options, args, false);
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("--optionB", result[1]);
        assertEquals("--optionC=value", result[2]);
    }

    @Test
    public void testFlattenWithLongOptionarg() {
        PosixParser parser = new PosixParser();
        String[] args = {"--optionB=longArg"};
        Options options = new Options();
        Option optionB = new Option("b", "b", true, "b help message");
        options.addOption(optionB);
        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("--optionB=longArg", result[0]);
    }

    @Test
    public void testFlattenWithMultipleLongOptions() {
        PosixParser parser = new PosixParser();
        String[] args = {"--optionB", "--optionC=value"};
        Options options = new Options();
        Option optionB = new Option("b", "b", true, "b help message");
        Option optionC = new Option("c", "c", true, "c help message");
        options.addOption(optionB);
        options.addOption(optionC);
        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("--optionB", result[0]);
        assertEquals("--optionC=value", result[1]);
    }

    @Test
    public void testFlattenWithLongOptionWithArg() {
        PosixParser parser = new PosixParser();
        String[] args = {"--optionB=longArgVal"};
        Options options = new Options();
        Option optionB = new Option("b", "b", true, "b help message");
        options.addOption(optionB);
        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("--optionB=longArgVal", result[0]);
    }

    @Test
    public void testFlattenWithMixedShortAndLongOptions() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--optionB", "-c", "value", "--optionD"};
        Options options = new Options();
        Option optionA = new Option("a", "a", false, "a help message");
        Option optionB = new Option("b", "b", true, "b help message");
        Option optionC = new Option("c", "c", true, "c help message");
        Option optionD = new Option("d", "d", false, "d help message");
        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);
        options.addOption(optionD);
        String[] result = parser.flatten(options, args, false);
        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("--optionB", result[1]);
        assertEquals("-c", result[2]);
        assertEquals("value", result[3]);
        assertEquals("--optionD", result[4]);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--optionB", "-c", "value", "--optionD", "randomArg"};
        Options options = new Options();
        Option optionA = new Option("a", "a", false, "a help message");
        Option optionB = new Option("b", "b", true, "b help message");
        Option optionC = new Option("c", "c", true, "c help message");
        Option optionD = new Option("d", "d", false, "d help message");
        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);
        String[] result = parser.flatten(options, args, true);
        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("--optionB", result[1]);
        assertEquals("-c", result[2]);
        assertEquals("value", result[3]);
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        String token = "--optionA--optionB=value";
        String[] result = parser.burstToken(token, false);
        assertEquals(3, result.length);
        assertEquals("--optionA", result[0]);
        assertEquals("--optionB", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String token = "--optionA--optionB=value--randomArg";
        String[] result = parser.burstToken(token, true);
        assertEquals(4, result.length);
        assertEquals("--optionA", result[0]);
        assertEquals("--optionB=value", result[1]);
        assertNull(result[2]);
        assertEquals("--randomArg", result[3]);
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        String token = "--optionA";
        boolean stopAtNonOption = false;
        parser.processOptionToken(token, stopAtNonOption);
        assertEquals(1, parser.tokens.size());
        assertEquals("--optionA", parser.tokens.get(0));

        stopAtNonOption = true;
        parser.processOptionToken(token, stopAtNonOption);
        assertEquals(1, parser.tokens.size());
        assertEquals("--optionA", parser.tokens.get(0));
        assertEquals(1, parser.tokens.size());
    }

    @Test
    public void testProcessOptionTokenWithStopAtNonOptionAndValue() {
        PosixParser parser = new PosixParser();
        String token = "--optionA=value";
        boolean stopAtNonOption = true;
        parser.processOptionToken(token, stopAtNonOption);
        assertEquals(2, parser.tokens.size());
        assertEquals("--optionA", parser.tokens.get(0));
        assertEquals("value", parser.tokens.get(1));
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        String value = "value";
        parser.process(value);
        assertEquals(2, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals(value, parser.tokens.get(1));

        parser.init();
        parser.currentOption = new Option("a", "a", false, "a help message");
        parser.tokens.add("-a");
        parser.tokens.add("--optionB");
        parser.process(value);
        assertEquals(4, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("--optionB", parser.tokens.get(1));
        assertEquals("value", parser.tokens.get(2));
        assertEquals("--", parser.tokens.get(3));
    }

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertFalse(parser.eatTheRest);
        assertTrue(parser.tokens.isEmpty());
        assertNull(parser.currentOption);
    }
}