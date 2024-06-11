package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testEmpty() {
        Options options = new Options();
        String[] args = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertNull(result);
    }

    @Test
    public void testSingleOption() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("opt").withValueOptional(false).create("opt"));
        String[] args = {"--opt"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--opt"}, result);
    }

    @Test
    public void testSingleOptionWithArg() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("opt").withValueOptional(true).create("opt"));
        String[] args = {"--opt", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--opt", "value"}, result);
    }

    @Test
    public void testMultipleOptions() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("opt1").withValueOptional(false).create("opt1"));
        options.addOption(OptionBuilder.withLongOpt("opt2").withValueOptional(false).create("opt2"));
        String[] args = {"--opt1", "--opt2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--opt1", "--opt2"}, result);
    }

    @Test
    public void testOptionWithArg() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("opt").withValueOptional(true).create("opt"));
        String[] args = {"--opt", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--opt", "value"}, result);
    }

    @Test
    public void testTwoOptionsWithArg() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("opt1").withValueOptional(true).create("opt1"));
        options.addOption(OptionBuilder.withLongOpt("opt2").withValueOptional(true).create("opt2"));
        String[] args = {"--opt1", "value1", "--opt2", "value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--opt1", "value1", "--opt2", "value2"}, result);
    }

    @Test
    public void testLongOptionWithArgAndSpecialToken() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("opt").withValueOptional(true).create("opt"));
        String[] args = {"--long-option", "value", "--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--long-option", "value"}, result);
    }

    @Test
    public void testLongOptionWithArgAndOtherOption() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("opt1").withValueOptional(true).create("opt1"));
        options.addOption(OptionBuilder.withLongOpt("opt2").withValueOptional(false).create("opt2"));
        String[] args = {"--opt1", "value1", "--opt2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--opt1", "value1", "--opt2"}, result);
    }

    @Test
    public void testEmptyValueOption() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("opt").withValueOptional(true).create("opt"));
        String[] args = {"--opt"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--opt"}, result);
    }

    @Test
    public void testStopAtNonOption() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("opt").withValueOptional(false).create("opt"));
        String[] args = {"--opt", "value", "nonOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertEquals(new String[]{"--opt", "value"}, result);
    }

    @Test
    public void testStopAtNonOptionWithEmptyValueOption() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("opt").withValueOptional(true).create("opt"));
        String[] args = {"--opt", "value", "nonOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertEquals(new String[]{"--opt", "value", "nonOption"}, result);
    }

    @Test
    public void testStopAtNonOptionWithEmptyArgs() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("opt").withValueOptional(false).create("opt"));
        String[] args = {"--opt"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertEquals(new String[]{"--opt"}, result);
    }

    @Test
    public void testLongOptionWithArgAndSpecialTokenAndOtherOption() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("opt1").withValueOptional(true).create("opt1"));
        options.addOption(OptionBuilder.withLongOpt("opt2").withValueOptional(false).create("opt2"));
        String[] args = {"--opt1", "value1", "--opt2", "--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--opt1", "value1"}, result);
    }

    @Test
    public void testLongOptionWithArgAndMultipleOtherOptions() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("opt1").withValueOptional(true).create("opt1"));
        options.addOption(OptionBuilder.withLongOpt("opt2").withValueOptional(false).create("opt2"));
        String[] args = {"--opt1", "value1", "--opt2", "--opt3", "--opt4"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--opt1", "value1", "--opt2", "--opt3", "--opt4"}, result);
    }

    @Test
    public void testBurstTokenSingleOption() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("opt").withValueOptional(false).create("opt"));
        String[] args = {"--op-t"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--opt"}, result);
    }

    @Test
    public void testBurstTokenWithOptionAndArg() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("opt").withValueOptional(true).create("opt"));
        String[] args = {"--op-t-arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--opt", "--arg"}, result);
    }

    @Test
    public void testBurstTokenWithOptionAndArgAndOtherOption() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("opt1").withValueOptional(true).create("opt1"));
        options.addOption(OptionBuilder.withLongOpt("opt2").withValueOptional(false).create("opt2"));
        String[] args = {"--op-t-arg1", "--opt2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--opt1", "--arg", "--opt2"}, result);
    }

    @Test
    public void testBurstTokenWithOptionAndSpecialToken() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("opt").withValueOptional(false).create("opt"));
        String[] args = {"--op-t--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--opt"}, result);
    }

    @Test
    public void testBurstTokenWithMultipleOptions() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("opt1").withValueOptional(false).create("opt1"));
        options.addOption(OptionBuilder.withLongOpt("opt2").withValueOptional(false).create("opt2"));
        String[] args = {"--op-t1-t2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--opt1", "--opt2"}, result);
    }

    @Test
    public void testBurstTokenWithOptionAndArgAndSpecialToken() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("opt").withValueOptional(true).create("opt"));
        String[] args = {"--op-t-arg--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--opt", "--arg"}, result);
    }

    @Test
    public void testBurstTokenWithOptionAndArgAndMultipleOtherOptions() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("opt1").withValueOptional(true).create("opt1"));
        options.addOption(OptionBuilder.withLongOpt("opt2").withValueOptional(false).create("opt2"));
        String[] args = {"--op-t-arg1", "--opt2", "--opt3", "--opt4"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--opt1", "--arg", "--opt2", "--opt3", "--opt4"}, result);
    }

    @Test
    public void testBurstTokenWithInvalidOption() {
        Options options = new Options();
        String[] args = {"--invalid"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertTrue(Arrays.asList(result).contains("--"));
    }
}