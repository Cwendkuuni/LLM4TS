package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenEmptyArgs() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {};
        String[] expected = {};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenSingleArg() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"arg1"};
        String[] expected = {"arg1"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenSingleOption() {
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        PosixParser parser = new PosixParser();
        String[] args = {"-o", "value"};
        String[] expected = {"option", "value"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenOptionWithArgument() {
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        PosixParser parser = new PosixParser();
        String[] args = {"-o", "value1", "arg1"};
        String[] expected = {"option", "value1", "arg1"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenDoubleOption() {
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        PosixParser parser = new PosixParser();
        String[] args = {"--option", "value"};
        String[] expected = {"option", "value"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenDoubleOptionWithArgument() {
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        PosixParser parser = new PosixParser();
        String[] args = {"--option", "value1", "arg1"};
        String[] expected = {"option", "value1", "arg1"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenDoubleDash() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"--", "arg1"};
        String[] expected = {"--", "arg1"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenDoubleDashWithArg() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"--", "arg1", "arg2"};
        String[] expected = {"--", "arg1", "arg2"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenBurstedArg() {
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        PosixParser parser = new PosixParser();
        String[] args = {"-o", "optionArg1", "arg1", "arg2"};
        String[] expected = {"option", "optionArg1", "arg1", "arg2"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenBurstedOption() {
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        PosixParser parser = new PosixParser();
        String[] args = {"--option", "optionArg1", "arg1", "arg2"};
        String[] expected = {"option", "optionArg1", "arg1", "arg2"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenBurstedOptionWithArg() {
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        PosixParser parser = new PosixParser();
        String[] args = {"--option", "optionArg1", "arg1", "arg2", "arg3"};
        String[] expected = {"option", "optionArg1", "arg1", "arg2", "arg3"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        PosixParser parser = new PosixParser();
        String[] args = {"-o", "optionArg1", "arg1", "nonOptionArg"};
        String[] expected = {"option", "optionArg1", "arg1", "nonOptionArg"};
        assertArrayEquals(expected, parser.flatten(options, args, true));
    }

    @Test
    public void testBurstedOptionWithArgs() {
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        PosixParser parser = new PosixParser();

        String[] args = {"--option", "optionArg1", "arg1", "arg2", "arg3"};
        List actual = Arrays.asList(parser.flatten(options, args, false));

        List expected = Arrays.asList("option", "optionArg1", "arg1", "arg2", "arg3");
        assertEquals(expected, actual);
    }

    @Test
    public void testFlattenWithNull() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = null;
        assertNull(parser.flatten(options, args, false));
    }
}