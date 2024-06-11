package org.apache.commons.cli.test;

import static org.apache.commons.cli.ParseException.PARSE_FAILURE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testEmpty() {
        String[] args = {};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[0], result);
    }

    @Test
    public void testSingleOption() {
        String[] args = {"--option"};
        Options options = new Options();
        options.addOption("option", true, "o");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--option"}, result);
    }

    @Test
    public void testSingleOptionWithValue() {
        String[] args = {"--option", "value"};
        Options options = new Options();
        options.addOption("option", true, "o");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--option", "value"}, result);
    }

    @Test
    public void testTwoOptions() {
        String[] args = {"--option1", "--option2"};
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--option1", "--option2"}, result);
    }

    @Test
    public void testTwoOptionsWithValues() {
        String[] args = {"--option1", "value1", "--option2", "value2"};
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--option1", "value1", "--option2", "value2"}, result);
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() {
        String[] args = {"--invalid"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertNull(result);
    }

    @Test(expected = ParseException.class)
    public void testInvalidOptionWithValue() {
        String[] args = {"--invalid", "value"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertNull(result);
    }

    @Test
    public void testStopAtNonOption() {
        String[] args = {"--option", "--stop", "value"};
        Options options = new Options();
        options.addOption("option", true, "o");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertEquals(new String[]{"--option"}, result);
    }

    @Test
    public void testSingleDash() {
        String[] args = {"-"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"-"}, result);
    }

    @Test
    public void testDoubleDash() {
        String[] args = {"--"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--"}, result);
    }

    @Test
    public void testBurstedOption() {
        String[] args = {"-o", "option", "value"};
        Options options = new Options();
        options.addOption("option", true, "o");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"-o", "option", "value"}, result);
    }

    @Test
    public void testBurstedOptionWithValue() {
        String[] args = {"-o", "option", "value1", "value2"};
        Options options = new Options();
        options.addOption("option", true, "o");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"-o", "option", "value1", "value2"}, result);
    }

    @Test
    public void testBurstedOptionWithArg() {
        String[] args = {"-o", "option", "value", "arg"};
        Options options = new Options();
        options.addOption("option", true, "o");
        options.addOption("option", true, "o");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"-o", "option", "value", "arg"}, result);
    }

    @Test
    public void testBurstedOptionWithArgs() {
        String[] args = {"-o", "option", "value1", "value2", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("option", true, "o");
        options.addOption("option", true, "o");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"-o", "option", "value1", "value2", "arg1", "arg2"}, result);
    }

    @Test
    public void testBurstedInvalidOption() {
        String[] args = {"-o", "invalid"};
        Options options = new Options();
        options.addOption("option", true, "o");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[0], result);
    }

    @Test
    public void testBurstedInvalidOptionWithValue() {
        String[] args = {"-o", "invalid", "value"};
        Options options = new Options();
        options.addOption("option", true, "o");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[0], result);
    }

    @Test
    public void testBurstedInvalidOptionWithArg() {
        String[] args = {"-o", "invalid", "value", "arg"};
        Options options = new Options();
        options.addOption("option", true, "o");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[0], result);
    }

    @Test
    public void testBurstedInvalidOptionWithArgs() {
        String[] args = {"-o", "invalid", "value1", "value2", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("option", true, "o");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[0], result);
    }

    @Test
    public void testBurstedStopAtNonOption() {
        String[] args = {"-o", "--stop", "value"};
        Options options = new Options();
        options.addOption("option", true, "o");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertEquals(new String[]{"-o"}, result);
    }

    @Test
    public void testBurstedEmpty() {
        String[] args = {};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[0], result);
    }

    @Test
    public void testBurstedDoubleDash() {
        String[] args = {"--"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--"}, result);
    }
}