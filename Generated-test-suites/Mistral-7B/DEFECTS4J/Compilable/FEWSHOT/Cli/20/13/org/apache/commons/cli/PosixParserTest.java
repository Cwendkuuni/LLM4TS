package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testEmpty() {
        Options options = new Options();
        String[] args = new String[0];
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testSingleOption() {
        Options options = new Options();
        options.addOption("option", true, "o");
        String[] args = {"-o"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"option"}, result);
    }

    @Test
    public void testSingleLongOption() {
        Options options = new Options();
        options.addOption("option", true, "o");
        String[] args = {"--option"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"option"}, result);
    }

    @Test
    public void testSingleShortOptionWithValue() {
        Options options = new Options();
        options.addOption("option", true, "o");
        String[] args = {"-o", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"option", "value"}, result);
    }

    @Test
    public void testSingleLongOptionWithValue() {
        Options options = new Options();
        options.addOption("option", true, "o");
        String[] args = {"--option=value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"option", "value"}, result);
    }

    @Test
    public void testMultipleOptions() {
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");
        String[] args = {"-o1", "value1", "-o2", "value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"option1", "value1", "option2", "value2"}, result);
    }

    @Test
    public void testLongOptionWithValueAndTrailingHyphen() {
        Options options = new Options();
        options.addOption("option", true, "o");
        String[] args = {"--option=value-"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"option", "value"}, result);
    }

    @Test
    public void testLongOptionWithValueAndLeadingHyphen() {
        Options options = new Options();
        options.addOption("option", true, "o");
        String[] args = {"--option=-value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"option", "value"}, result);
    }

    @Test
    public void testLongOptionWithValueAndNoHyphen() {
        Options options = new Options();
        options.addOption("option", true, "o");
        String[] args = {"--option=value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"option", "value"}, result);
    }

    @Test
    public void testStopAtNonOption() {
        Options options = new Options();
        options.addOption("option", true, "o");
        String[] args = {"-o", "value", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"option", "value", "arg1", "arg2"}, result);
    }

    @Test
    public void testBurstTokenSingleOption() {
        Options options = new Options();
        options.addOption("option", true, "o");
        String[] args = {"-ofoo"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"option", "foo"}, result);
    }

    @Test
    public void testBurstTokenMultipleOptions() {
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");
        String[] args = {"--option1o1foobar1", "--option2o2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"option1", "foobar1", "option2"}, result);
    }

    @Test
    public void testBurstTokenLongOptionWithValue() {
        Options options = new Options();
        options.addOption("option", true, "o");
        String[] args = {"--option=foo--bar"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"option", "foo--bar"}, result);
    }

    @Test
    public void testBurstTokenLongOptionWithValueAndTrailingHyphen() {
        Options options = new Options();
        options.addOption("option", true, "o");
        String[] args = {"--option=foo--bar-"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"option", "foo--bar"}, result);
    }

    @Test
    public void testBurstTokenLongOptionWithValueAndLeadingHyphen() {
        Options options = new Options();
        options.addOption("option", true, "o");
        String[] args = {"--option=-foo--bar"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"option", "foo--bar"}, result);
    }

    @Test
    public void testBurstTokenLongOptionWithValueAndNoHyphen() {
        Options options = new Options();
        options.addOption("option", true, "o");
        String[] args = {"--option=foo--bar"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"option", "foo--bar"}, result);
    }

    @Test
    public void testBurstTokenLongOptionWithArgument() {
        Options options = new Options();
        options.addOption("option", true, "o");
        options.addOption("arg", true, "a");
        String[] args = {"--option=foo--bar--arg=arg1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"option", "foo--bar", "arg", "arg1"}, result);
    }

    @Test
    public void testBurstTokenLongOptionWithArguments() {
        Options options = new Options();
        options.addOption("option", true, "o");
        options.addOption("arg", true, "a");
        String[] args = {"--option=foo--bar--arg=arg1--arg2--arg3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"option", "foo--bar", "arg", "arg1--arg2--arg3"}, result);
    }
}