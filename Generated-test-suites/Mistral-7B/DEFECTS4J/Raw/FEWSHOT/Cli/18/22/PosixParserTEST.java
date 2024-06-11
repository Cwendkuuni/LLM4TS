package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenNoOptions() {
        String[] args = new String[]{"myProgram", "arg1", "arg2"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"myProgram", "arg1", "arg2"}, result);
    }

    @Test
    public void testFlattenOptionsWithNoArgs() {
        String[] args = new String[]{"--option1", "--option2"};
        Options options = new Options();
        options.addOption("option1", false, "o1");
        options.addOption("option2", false, "o2");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--option1", "--option2"}, result);
    }

    @Test
    public void testFlattenOptionsWithArgs() {
        String[] args = new String[]{"--option1", "value1", "--option2", "value2"};
        Options options = new Options();
        Option option1 = new Option("o1", "option1", false, "o");
        option1.setArgName("value");
        Option option2 = new Option("o2", "option2", false, "p");
        option2.setArgName("value");
        options.addOption(option1);
        options.addOption(option2);
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--option1", "value1", "--option2", "value2"}, result);
    }

    @Test
    public void testFlattenOptionsWithLongOptions() {
        String[] args = new String[]{"--long-option1", "value1", "--long-option2=value2"};
        Options options = new Options();
        Option option1 = new Option("lo1", "long option 1", false);
        Option option2 = new Option("lo2", "long option 2", true, "v");
        option2.setLongOpt("longOption2");
        options.addOption(option1);
        options.addOption(option2);
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--long-option1", "value1", "--long-option2", "value2"}, result);
    }

    @Test
    public void testFlattenOptionsWithMixed() {
        String[] args = new String[]{"--option1", "value1", "-o", "arg1", "--option2=value2", "-p", "arg2"};
        Options options = new Options();
        Option option1 = new Option("o1", "option1", false);
        option1.setArgName("value");
        Option option2 = new Option("o2", "option2", true, "v");
        option2.setArgName("value");
        options.addOption(option1);
        options.addOption(option2);
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--option1", "value1", "--option2", "value2", "-o", "arg1"}, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] args = new String[]{"--option1", "value1", "-invalidOption", "arg1", "--option2", "value2"};
        Options options = new Options();
        Option option1 = new Option("o1", "option1", false);
        option1.setArgName("value");
        Option option2 = new Option("o2", "option2", false);
        options.addOption(option1);
        options.addOption(option2);
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"--option1", "value1", "-invalidOption", "arg1"}, result);
    }

    @Test
    public void testBurstTokenSingleOption() {
        String token = "-o";
        PosixParser parser = new PosixParser();
        List<String> result = parser.burstToken(token, false);
        assertArrayEquals(new String[]{"-o"}, result.toArray(new String[0]));
    }

    @Test
    public void testBurstTokenLongOption() {
        String token = "--long-option";
        PosixParser parser = new PosixParser();
        List<String> result = parser.burstToken(token, false);
        assertArrayEquals(new String[]{"--long-option"}, result.toArray(new String[0]));
    }

    @Test
    public void testBurstTokenOptionWithArg() {
        String token = "--option";
        PosixParser parser = new PosixParser();
        List<String> result = parser.burstToken(token, true);
        assertArrayEquals(new String[]{"--option"}, result.toArray(new String[0]));
    }

    @Test
    public void testBurstTokenInvalidOption() {
        String token = "-invalid";
        PosixParser parser = new PosixParser();
        List<String> result = parser.burstToken(token, false);
        assertArrayEquals(new String[]{"--", token}, result.toArray(new String[0]));
    }

    @Test
    public void testBurstTokenInvalidOptionWithArg() {
        String token = "--invalid=arg";
        PosixParser parser = new PosixParser();
        List<String> result = parser.burstToken(token, true);
        assertArrayEquals(new String[]{"--invalid", "arg"}, result.toArray(new String[0]));
    }

    private void assertArrayEquals(String[] expected, String[] actual) {
        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError("Expected: " + Arrays.toString(expected) + "\nBut was: " + Arrays.toString(actual));
        }
    }
}