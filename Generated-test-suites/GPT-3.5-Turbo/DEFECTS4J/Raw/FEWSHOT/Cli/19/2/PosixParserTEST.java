package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlattenWithNoArguments() {
        String[] args = {};
        String[] result = parser.flatten(options, args, false);
        assertEquals(0, result.length);
    }

    @Test
    public void testFlattenWithSingleOption() {
        options.addOption(new Option("f", false, "Foo"));

        String[] args = {"-f"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("-f", result[0]);
    }

    @Test
    public void testFlattenWithMultipleOptions() {
        options.addOption(new Option("f", false, "Foo"));
        options.addOption(new Option("b", false, "Bar"));
        options.addOption(new Option("q", false, "Qux"));

        String[] args = {"-f", "-b", "-q"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(3, result.length);
        assertEquals("-f", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-q", result[2]);
    }

    @Test
    public void testFlattenWithOptionAndArgument() {
        options.addOption(new Option("f", true, "Foo"));

        String[] args = {"-f", "value"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("-f", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlattenWithOptionAndMultipleArguments() {
        options.addOption(new Option("f", true, "Foo"));

        String[] args = {"-f", "value1", "value2"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(3, result.length);
        assertEquals("-f", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("value2", result[2]);
    }

    @Test
    public void testFlattenWithOptionAndArgumentAfterNonOption() {
        options.addOption(new Option("f", true, "Foo"));

        String[] args = {"-f", "value", "nonOption"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("-f", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlattenWithOptionAndArgumentAfterNonOptionStopAtNonOption() {
        options.addOption(new Option("f", true, "Foo"));

        String[] args = {"-f", "value", "nonOption"};
        String[] result = parser.flatten(options, args, true);
        assertEquals(3, result.length);
        assertEquals("-f", result[0]);
        assertEquals("value", result[1]);
        assertEquals("nonOption", result[2]);
    }

    @Test
    public void testFlattenWithOptionAndMultipleArgumentsAfterNonOption() {
        options.addOption(new Option("f", true, "Foo"));

        String[] args = {"-f", "value1", "value2", "nonOption"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(3, result.length);
        assertEquals("-f", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("value2", result[2]);
    }

    @Test
    public void testFlattenWithOptionAndMultipleArgumentsAfterNonOptionStopAtNonOption() {
        options.addOption(new Option("f", true, "Foo"));

        String[] args = {"-f", "value1", "value2", "nonOption"};
        String[] result = parser.flatten(options, args, true);
        assertEquals(4, result.length);
        assertEquals("-f", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("value2", result[2]);
        assertEquals("nonOption", result[3]);
    }

    @Test
    public void testFlattenWithNonOption() {
        String[] args = {"nonOption"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("nonOption", result[0]);
    }

    @Test
    public void testFlattenWithNonOptionStopAtNonOption() {
        String[] args = {"nonOption"};
        String[] result = parser.flatten(options, args, true);
        assertEquals(1, result.length);
        assertEquals("nonOption", result[0]);
    }

}