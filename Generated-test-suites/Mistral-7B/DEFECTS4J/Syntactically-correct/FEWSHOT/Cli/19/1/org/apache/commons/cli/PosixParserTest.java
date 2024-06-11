package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenEmptyArgs() {
        Options options = new Options();
        String[] args = {};
        PosixParser parser = new PosixParser();

        String[] expected = {};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");

        String[] args = {"-a"};
        PosixParser parser = new PosixParser();

        String[] expected = {"Option A"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleValueOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"-a", "value"};
        PosixParser parser = new PosixParser();

        String[] expected = {"Option A", "value"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptions() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] args = {"-a", "-b", "value"};
        PosixParser parser = new PosixParser();

        String[] expected = {"Option A", "Option B", "value"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleValuesForOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"-a", "value1", "-a", "value2"};
        PosixParser parser = new PosixParser();

        String[] expected = {"Option A", "value1", "Option A", "value2"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOption() {
        Options options = new Options();
        options.addOption("long-option", true, "Long option");

        String[] args = {"--long-option", "value"};
        PosixParser parser = new PosixParser();

        String[] expected = {"Long option", "value"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        Options options = new Options();
        options.addOption("long-option", true, "Long option");

        String[] args = {"--long-option=value"};
        PosixParser parser = new PosixParser();

        String[] expected = {"Long option", "value"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOptionWithMultipleValues() {
        Options options = new Options();
        options.addOption("long-option", true, "Long option");

        String[] args = {"--long-option=value1", "--long-option=value2"};
        PosixParser parser = new PosixParser();

        String[] expected = {"Long option", "value1", "Long option", "value2"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenDashOptionWithValue() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"-a=value"};
        PosixParser parser = new PosixParser();

        String[] expected = {"Option A", "value"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenDashOptionWithMultipleValues() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"-a=value1", "-a=value2"};
        PosixParser parser = new PosixParser();

        String[] expected = {"Option A", "value1", "Option A", "value2"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenTwoDashOption() {
        Options options = new Options();

        String[] args = {"--"};
        PosixParser parser = new PosixParser();

        String[] expected = {};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenTwoDashOptionWithValues() {
        Options options = new Options();

        String[] args = {"--", "value1", "value2"};
        PosixParser parser = new PosixParser();

        String[] expected = {"value1", "value2"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenNonOptionValue() {
        Options options = new Options();

        String[] args = {"value"};
        PosixParser parser = new PosixParser();

        String[] expected = {"value"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenNonOptionValueWithTwoDashes() {
        Options options = new Options();

        String[] args = {"--value"};
        PosixParser parser = new PosixParser();

        String[] expected = {"--value"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"-a", "value", "non-option"};
        PosixParser parser = new PosixParser();

        String[] expected = {"Option A", "value"};
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOptionLongOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("long-option", true, "Long Option");

        String[] args = {"-a", "value1", "--long-option=value2", "non-option"};
        PosixParser parser = new PosixParser();

        String[] expected = {"Option A", "value1"};
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOptionLongOptionWithValues() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("long-option", true, "Long Option");

        String[] args = {"-a", "value1", "--long-option=value2", "value3"};
        PosixParser parser = new PosixParser();

        String[] expected = {"Option A", "value1"};
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenBurstTokenSingleCharacterOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"--a"};
        PosixParser parser = new PosixParser();

        String[] expected = {"Option A"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenBurstTokenTwoCharacterOption() {
        Options options = new Options();
        options.addOption("ab", true, "Option AB");

        String[] args = {"--ab"};
        PosixParser parser = new PosixParser();

        String[] expected = {"Option AB"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenBurstTokenThreeCharacterOption() {
        Options options = new Options();
        options.addOption("abc", true, "Option ABC");

        String[] args = {"--abc"};
        PosixParser parser = new PosixParser();

        String[] expected = {"Option ABC"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenBurstTokenSingleCharacterOptionWithValue() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"--a=value"};
        PosixParser parser = new PosixParser();

        String[] expected = {"Option A", "value"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenBurstTokenTwoCharacterOptionWithValue() {
        Options options = new Options();
        options.addOption("ab", true, "Option AB");

        String[] args = {"--ab=value"};
        PosixParser parser = new PosixParser();

        String[] expected = {"Option AB", "value"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenBurstTokenThreeCharacterOptionWithValue() {
        Options options = new Options();
        options.addOption("abc", true, "Option ABC");

        String[] args = {"--abc=value"};
        PosixParser parser = new PosixParser();

        String[] expected = {"Option ABC", "value"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenBurstTokenLongOption() {
        Options options = new Options();
        options.addOption("long-option", true, "Option Long Option");

        String[] args = {"--long-option"};
        PosixParser parser = new PosixParser();

        String[] expected = {"Option Long Option"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenBurstTokenLongOptionWithValue() {
        Options options = new Options();
        options.addOption("long-option", true, "Option Long Option");

        String[] args = {"--long-option=value"};
        PosixParser parser = new PosixParser();

        String[] expected = {"Option Long Option", "value"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenBurstTokenLongOptionWithMultipleValues() {
        Options options = new Options();
        options.addOption("long-option", true, "Option Long Option");

        String[] args = {"--long-option=value1", "--long-option=value2"};
        PosixParser parser = new PosixParser();

        String[] expected = {"Option Long Option", "value1", "Option Long Option", "value2"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenBurstTokenNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"--value"};
        PosixParser parser = new PosixParser();

        String[] expected = {"--value"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenBurstTokenNonOptionStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"--value", "non-option"};
        PosixParser parser = new PosixParser();

        String[] expected = {"--value"};
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenBurstTokenLongOptionStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("long-option", true, "Option Long Option");

        String[] args = {"--long-option=value1", "--value", "value2"};
        PosixParser parser = new PosixParser();

        String[] expected = {"--long-option=value1"};
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenBurstTokenLongOptionWithValuesStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("long-option", true, "Option Long Option");

        String[] args = {"--long-option=value1", "--value", "value2", "value3"};
        PosixParser parser = new PosixParser();

        String[] expected = {"--long-option=value1"};
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(expected, result);
    }
}