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
    public void testFlattenEmpty() {
        Options options = new Options();
        String[] arguments = new String[0];
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenSingleOption() {
        Options options = new Options();
        options.addOption("a", false, "a option");
        String[] arguments = {"-a"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-a"}, result);
    }

    @Test
    public void testFlattenSingleValueOption() {
        Options options = new Options();
        options.addOption("a", true, "a option");
        String[] arguments = {"-a", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-a", "value"}, result);
    }

    @Test
    public void testFlattenMultipleOptions() {
        Options options = new Options();
        options.addOption("a", false, "a option");
        options.addOption("b", false, "b option");
        String[] arguments = {"-a", "-b"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-a", "-b"}, result);
    }

    @Test
    public void testFlattenMultipleValueOption() {
        Options options = new Options();
        options.addOption("a", true, "a option");
        options.addOption("b", true, "b option");
        String[] arguments = {"-a", "value1", "-a", "value2", "-b", "value3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-a", "value1", "-a", "value2", "-b", "value3"}, result);
    }

    @Test
    public void testFlattenLongOption() {
        Options options = new Options();
        options.addOption("longOption", false, "long option");
        String[] arguments = {"--longOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--longOption"}, result);
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        Options options = new Options();
        options.addOption("longOption", true, "long option");
        String[] arguments = {"--longOption", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--longOption", "value"}, result);
    }

    @Test
    public void testFlattenLongOptionValueWithEquals() {
        Options options = new Options();
        options.addOption("longOption", true, "long option with equals");
        String[] arguments = {"--longOption=value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--longOption", "value"}, result);
    }

    @Test
    public void testFlattenLongOptionWithMultipleValues() {
        Options options = new Options();
        options.addOption("longOption", true, "long option with multiple values");
        String[] arguments = {"--longOption", "value1", "--longOption", "value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--longOption", "value1"}, result);
        assertArrayEquals(Arrays.copyOfRange(result, 1, result.length), new String[]{"--longOption", "value2"});
    }

    @Test
    public void testFlattenIgnoreNonOption() {
        Options options = new Options();
        options.addOption("a", false, "a option");
        String[] arguments = {"-a", "foo", "bar"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"-a", "foo", "bar"}, result);
    }

    @Test
    public void testFlattenBurstedOption() {
        Options options = new Options();
        options.addOption("longOption", false, "long option");
        String[] arguments = {"--long-option"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--long-option"}, result);
    }

    @Test
    public void testFlattenBurstedOptionWithValue() {
        Options options = new Options();
        options.addOption("longOption", true, "long option");
        String[] arguments = {"--long-option", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--long-option", "value"}, result);
    }

    @Test
    public void testFlattenBurstedOptionMultipleValues() {
        Options options = new Options();
        options.addOption("longOption", true, "long option");
        String[] arguments = {"--long-option", "value1", "--long-option", "value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--long-option", "value1"}, result);
        assertArrayEquals(Arrays.copyOfRange(result, 1, result.length), new String[]{"--long-option", "value2"});
    }

    @Test
    public void testFlattenBurstedOptionWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "a option");
        options.addOption("longOption", true, "long option");
        String[] arguments = {"--long-option", "value", "foo", "bar"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"--long-option", "value", "foo", "bar"}, result);
    }
}