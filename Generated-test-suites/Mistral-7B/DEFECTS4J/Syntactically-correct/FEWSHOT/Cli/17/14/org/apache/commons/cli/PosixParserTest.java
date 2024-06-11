package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", true, "A option");
        options.addOption("b", 1, "B option");
        options.addOption("c", 3, "C option");
        options.addOption("d", null, "D option", true, "d");
        options.addOption("e", null, "E option", true, "e");
        options.addOption("f", null, "F option", true, "f");
        parser = new PosixParser();
    }

    @Test
    public void testFlattenEmpty() {
        String[] args = {};
        String[] expected = {};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenSingleArgument() {
        String[] args = {"arg"};
        String[] expected = {"arg"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenSingleOption() {
        String[] args = {"a"};
        String[] expected = {"a"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenDoubleOption() {
        String[] args = {"--d", "value"};
        String[] expected = {"d", "value"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenDoubleOptionWithArgument() {
        String[] args = {"--d=value", "arg"};
        String[] expected = {"d", "value", "arg"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenMultipleOptions() {
        String[] args = {"a", "b", "1", "c", "3"};
        String[] expected = {"a", "b", "c"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenMultipleOptionsWithArguments() {
        String[] args = {"a", "--d=value", "c", "--e=value2", "f"};
        String[] expected = {"a", "d", "value", "c", "e", "value2", "f"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenOptionWithLongArgument() {
        String[] args = {"--longOption=longValue"};
        String[] expected = {"longOption", "longValue"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenOptionWithLongArgumentAndAnotherOption() {
        String[] args = {"--longOption=longValue", "a"};
        String[] expected = {"longOption", "longValue", "a"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] args = {"a", "--", "nonOption"};
        String[] expected = {"a", "nonOption"};
        assertArrayEquals(expected, parser.flatten(options, args, true));
    }

    @Test
    public void testFlattenStopAtNonOptionWithLongOption() {
        String[] args = {"a", "--longOption=longValue", "--"};
        String[] expected = {"a", "longOption", "longValue"};
        assertArrayEquals(expected, parser.flatten(options, args, true));
    }
}