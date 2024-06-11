package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenEmptyArgs() {
        Options options = new Options();
        String[] args = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenSingleArg() {
        Options options = new Options();
        String[] args = {"arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"arg"}, result);
    }

    @Test
    public void testFlattenDoubleDash() {
        Options options = new Options();
        String[] args = {"--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--"}, result);
    }

    @Test
    public void testFlattenSingleHyphen() {
        Options options = new Options();
        String[] args = {"-"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-"}, result);
    }

    @Test
    public void testFlattenOptionNoArg() {
        Options options = new Options().addOption(new Option("o", false, "Option o with no argument"));
        String[] args = {"-o"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-o"}, result);
    }

    @Test
    public void testFlattenOptionSingleArg() {
        Options options = new Options().addOption(new Option("a", true, "Option a with single argument"));
        String[] args = {"-a", "arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-a", "arg"}, result);
    }

    @Test
    public void testFlattenOptionMultipleArgs() {
        Options options = new Options().addOption(new Option("a", true, "Option a with multiple arguments"));
        String[] args = {"-a", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-a", "arg1", "arg2"}, result);
    }

    @Test
    public void testFlattenOptionWithValue() {
        Options options = new Options().addOption(new Option("o", true, "Option o with value"));
        String[] args = {"-o", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-o", "value"}, result);
    }

    @Test
    public void testFlattenOptionWithValueAndArg() {
        Options options = new Options().addOption(new Option("a", true, "Option a with value and argument"));
        String[] args = {"-a", "value", "arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-a", "value"}, result);
    }

    @Test
    public void testFlattenOptionWithValueAndMultipleArgs() {
        Options options = new Options().addOption(new Option("a", true, "Option a with value and multiple arguments"));
        String[] args = {"-a", "value", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-a", "value", "arg1", "arg2"}, result);
    }

    @Test
    public void testFlattenBurstToken() {
        Options options = new Options().addOption(new Option("a", true, "Option a with single argument"));
        String[] args = {"-a--foo", "bar"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-a", "--foo", "bar"}, result);
    }

    @Test
    public void testFlattenBurstTokenWithStopAtNonOption() {
        Options options = new Options().addOption(new Option("a", true, "Option a with single argument"));
        String[] args = {"-a--foo", "bar", "baz"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"-a--foo", "bar", "baz"}, result);
    }

    @Test
    public void testFlattenBurstTokenWithStopAtNonOptionAndNoArg() {
        Options options = new Options().addOption(new Option("a", true, "Option a with single argument"));
        String[] args = {"-a--foo"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"--foo"}, result);
    }

    @Test
    public void testFlattenBurstTokenWithStopAtNonOptionAndArg() {
        Options options = new Options().addOption(new Option("a", true, "Option a with single argument"));
        String[] args = {"-a--foo", "arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"--foo", "arg"}, result);
    }

    @Test
    public void testFlattenBurstTokenWithStopAtNonOptionAndMultipleArgs() {
        Options options = new Options().addOption(new Option("a", true, "Option a with multiple arguments"));
        String[] args = {"-a--foo", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"--foo", "arg1", "arg2"}, result);
    }
}