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
    public void testFlattenSimple() {
        Options options = new Options();
        String[] arguments = new String[] {"-a", "--optionB", "-c", "valueC", "--test"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList("-a", "--optionB", "-c", "valueC", "--test");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithLongOptions() {
        Options options = new Options();
        String[] arguments = new String[] {"--longOptionWithValue=valueA", "--longOptionWithValue2=valueB"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList("--longOptionWithValue=valueA", "--longOptionWithValue2=valueB");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithSingleHyphen() {
        Options options = new Options();
        options.addOption("h", "help", false, "Display this help message");
        String[] arguments = new String[] {"-h"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList("-h");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithDoubleHyphen() {
        Options options = new Options();
        options.addOption("h", "help", false, "Display this help message");
        String[] arguments = new String[] {"--", "--help"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList("--help");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithValue() {
        Options options = new Options();
        options.addOption("v", "verbose", true, "Print additional information");
        String[] arguments = new String[] {"-v", "value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList("-v", "value");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithLongValue() {
        Options options = new Options();
        options.addOption("l", "list", true, "Print a list of items");
        String[] arguments = new String[] {"--list", "item1", "item2", "item3"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList("--list=item1", "--list=item2", "--list=item3");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithMultipleValues() {
        Options options = new Options();
        options.addOption("m", "multivalue", true, "Print multiple values");
        String[] arguments = new String[] {"--multivalue", "value1", "value2", "value3"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList("--multivalue=value1", "--multivalue=value2", "--multivalue=value3");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithMultipleOptions() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", false, "Option B");
        String[] arguments = new String[] {"-a", "-b"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList("-a", "-b");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithMixedOptionsAndArguments() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", false, "Option B");
        String[] arguments = new String[] {"-a", "argA", "-b", "argB"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList("-a", "argA", "-b", "argB");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithLongOptionValue() {
        Options options = new Options();
        options.addOption("l", "list", true, "Print a list of items");
        String[] arguments = new String[] {"--list", "item-with-long-name"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList("--list=item-with-long-name");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithLongOptionValueAndArguments() {
        Options options = new Options();
        options.addOption("l", "list", true, "Print a list of items");
        String[] arguments = new String[] {"--list", "item1", "item2", "item3"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList("--list=item1", "--list=item2", "--list=item3");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", false, "Option B");
        String[] arguments = new String[] {"-a", "argA", "-c", "argC"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        List<String> expected = Arrays.asList("-a", "argA", "-c", "argC");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        String[] tokens = {"--optionWithValue", "--longOptionWithValue=valueA", "--longOptionWithValue2=valueB", "optionWithMultipleValues", "optionWithValue=valueC", "optionWithLongValue=valueD"};

        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken("--optionWithValueWithValueAwithValueB", false);

        List<String> expected = Arrays.asList("-optionWithValue", "--optionWithValue=valueA", "--optionWithValue=valueB");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        Options options = new Options();
        String[] tokens = {"--optionWithValue", "--optionWithValue=valueA", "--optionB", "--optionWithValue2", "--optionWithValue=valueC", "--optionWithLongValue=valueD"};

        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken("--optionWithValueWithValueAwithOptionBwithValueCwithOptionWithLongValue", true);

        List<String> expected = Arrays.asList("-optionWithValueWithValueA", "--optionB", "--optionWithLongValue=valueD");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        String result = parser.processSingleHyphen("-");

        assertEquals("-", result);
    }
}