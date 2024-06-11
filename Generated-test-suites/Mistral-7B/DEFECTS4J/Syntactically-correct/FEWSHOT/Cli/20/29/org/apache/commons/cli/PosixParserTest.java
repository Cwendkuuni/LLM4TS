package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
    public void testFlattenSimpleOptions() {
        Options options = new Options();
        options.addOption("a", true, "A short option");
        options.addOption("b", 1, "A long option with one argument");
        options.addOption("c", 2, "A long option with two arguments");
        String[] args = {"-a", "--b", "1", "2", "value"};
        PosixParser parser = new PosixParser();
        String[] expected = {"-a", "--b", "1", "2", "value"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOptionsWithValue() {
        Options options = new Options();
        options.addOption("foo", true, "Foo");
        options.addOption("bar", 1, "Bar");
        String[] args = {"--foo", "--bar=value"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--foo", "--bar", "value"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOptionsWithMultipleValues() {
        Options options = new Options();
        options.addOption("foo", 3, "Foo");
        String[] args = {"--foo", "value1", "value2", "value3"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--foo", "value1", "value2", "value3"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "A short option");
        String[] args = {"--foo", "-a"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--foo", "--", "-a"};
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenBurstingSingleOption() {
        Options options = new Options();
        options.addOption("foo", 1, "Foo");
        String[] args = {"-foo-bar"};
        PosixParser parser = new PosixParser();
        String[] expected = {"-foo", "--bar"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenBurstingLongOptionSingleValue() {
        Options options = new Options();
        options.addOption("foo", 1, "Foo");
        String[] args = {"--foo-bar"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--foo", "--bar"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenBurstingLongOptionMultipleValues() {
        Options options = new Options();
        options.addOption("foo", 3, "Foo");
        String[] args = {"--foo-bar1-bar2-bar3"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--foo", "bar1", "bar2", "bar3"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenBurstingStopAtNonOption() {
        Options options = new Options();
        options.addOption("foo", 1, "Foo");
        String[] args = {"--foo-bar", "-a"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--foo-bar", "--", "-a"};
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenBurstingLongOptionWithArgument() {
        Options options = new Options();
        options.addOption("foo", 1, "Foo");
        String[] args = {"--foo-bar=abc"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--foo", "--bar", "abc"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenBurstingLongOptionWithMultipleArguments() {
        Options options = new Options();
        options.addOption("foo", 3, "Foo");
        String[] args = {"--foo-bar1=abc", "--foo-bar2=def"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--foo", "--bar1", "abc", "--bar2", "def"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }
}