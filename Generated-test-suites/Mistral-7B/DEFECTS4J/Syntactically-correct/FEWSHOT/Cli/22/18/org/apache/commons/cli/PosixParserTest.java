package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenEmpty() {
        PosixParser parser = new PosixParser();
        String[] args = {};
        String[] expected = {};
        assertArrayEquals(expected, parser.flatten(new Options(), args, false));
    }

    @Test
    public void testFlattenNoOptions() {
        PosixParser parser = new PosixParser();
        String[] args = { "arg1", "arg2" };
        String[] expected = { "arg1", "arg2" };
        assertArrayEquals(expected, parser.flatten(new Options(), args, false));
    }

    @Test
    public void testFlattenSingleOption() {
        PosixParser parser = new PosixParser();
        String[] args = { "--option" };
        String[] expected = { "option" };
        assertArrayEquals(expected, parser.flatten(new Options(), args, false));
    }

    @Test
    public void testFlattenSingleValueOption() {
        PosixParser parser = new PosixParser();
        String[] args = { "--option=value" };
        String[] expected = { "option", "value" };
        assertArrayEquals(expected, parser.flatten(new Options(), args, false));
    }

    @Test
    public void testFlattenMultipleOptions() {
        PosixParser parser = new PosixParser();
        String[] args = { "--option1", "--option2", "--option3=value" };
        String[] expected = { "option1", "option2", "option3", "value" };
        assertArrayEquals(expected, parser.flatten(new Options(), args, false));
    }

    @Test
    public void testFlattenStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = { "--option1", "--option2", "--", "arg1", "arg2" };
        List expected = new ArrayList();
        expected.add("option1");
        expected.add("option2");
        expected.add("--");
        expected.add("arg1");
        expected.add("arg2");
        List actual = new ArrayList();
        for (Object obj : parser.flatten(new Options(), args, true)) {
            actual.add(obj.toString());
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testFlattenWithLongOptionValue() {
        PosixParser parser = new PosixParser();
        String[] args = { "--option=long option value" };
        String[] expected = { "option", "long option value" };
        assertArrayEquals(expected, parser.flatten(new Options(), args, false));
    }

    @Test
    public void testFlattenWithShortOptionValue() {
        PosixParser parser = new PosixParser();
        String[] args = { "-o", "value" };
        String[] expected = { "o", "value" };
        assertArrayEquals(expected, parser.flatten(new Options(), args, false));
    }

    @Test
    public void testFlattenWithShortOptionWithArg() {
        PosixParser parser = new PosixParser();
        String[] args = { "-o", "value1", "value2" };
        String[] expected = { "o", "value1", "value2" };
        assertArrayEquals(expected, parser.flatten(new Options(), args, false));
    }

    @Test
    public void testFlattenWithMixedOptions() {
        PosixParser parser = new PosixParser();
        String[] args = { "--option1", "-o", "value", "--option2", "arg" };
        String[] expected = { "option1", "o", "value", "option2", "arg" };
        assertArrayEquals(expected, parser.flatten(new Options(), args, false));
    }

    @Test
    public void testFlattenWithShortOptionTwice() {
        PosixParser parser = new PosixParser();
        String[] args = { "-o", "-o" };
        String[] expected = { "o", "o" };
        assertArrayEquals(expected, parser.flatten(new Options(), args, false));
    }

    @Test
    public void testFlattenWithShortOptionAndLongOption() {
        PosixParser parser = new PosixParser();
        String[] args = { "-o", "--option", "value" };
        String[] expected = { "o", "option", "value" };
        assertArrayEquals(expected, parser.flatten(new Options(), args, false));
    }

    @Test
    public void testFlattenWithShortOptionWithArgAndLongOption() {
        PosixParser parser = new PosixParser();
        String[] args = { "-o", "arg", "--option", "value" };
        String[] expected = { "o", "arg", "option", "value" };
        assertArrayEquals(expected, parser.flatten(new Options(), args, false));
    }

    @Test
    public void testFlattenWithLongOptionWithArgTwice() {
        PosixParser parser = new PosixParser();
        String[] args = { "--option=value1", "--option=value2" };
        String[] expected = { "option", "value1", "option", "value2" };
        assertArrayEquals(expected, parser.flatten(new Options(), args, false));
    }

    @Test
    public void testFlattenWithLongOptionWithArgAndValueTwice() {
        PosixParser parser = new PosixParser();
        String[] args = { "--option=value1", "--option=value2", "--option=value3" };
        String[] expected = { "option", "value1", "option", "value2", "option", "value3" };
        assertArrayEquals(expected, parser.flatten(new Options(), args, false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithInvalidLongOption() {
        PosixParser parser = new PosixParser();
        String[] args = { "--invalidOption" };
        parser.flatten(new Options(), args, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithInvalidShortOption() {
        PosixParser parser = new PosixParser();
        String[] args = { "-z" };
        parser.flatten(new Options(), args, false);
    }
}