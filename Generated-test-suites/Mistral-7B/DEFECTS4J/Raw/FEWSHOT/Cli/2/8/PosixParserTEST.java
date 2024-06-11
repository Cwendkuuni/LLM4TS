package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenEmpty() {
        Options options = new Options();
        String[] arguments = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testFlattenSingleOption() {
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        String[] arguments = {"-o", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"o", "value"}, result);
    }

    @Test
    public void testFlattenSingleDash() {
        Options options = new Options();
        String[] arguments = {"-"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-"}, result);
    }

    @Test
    public void testFlattenDoubleDash() {
        Options options = new Options();
        String[] arguments = {"--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--"}, result);
    }

    @Test
    public void testFlattenOptionWithValue() {
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        String[] arguments = {"-o", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"o", "value"}, result);
    }

    @Test
    public void testFlattenOptionWithMultipleValues() {
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        String[] arguments = {"-o", "value1", "-o", "value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"o", "value1", "o", "value2"}, result);
    }

    @Test
    public void testFlattenMultipleOptions() {
        Options options = new Options();
        options.addOption("o1", "option1", true, "Option1 description");
        options.addOption("o2", "option2", true, "Option2 description");
        String[] arguments = {"-o1", "value1", "-o2", "value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"o1", "value1", "o2", "value2"}, result);
    }

    @Test
    public void testFlattenSingleHyphen() {
        Options options = new Options();
        String[] arguments = {"-"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-"}, result);
    }

    @Test
    public void testFlattenDoubleHyphen() {
        Options options = new Options();
        String[] arguments = {"--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--"}, result);
    }

    @Test
    public void testFlattenDoubleDashWithValue() {
        Options options = new Options();
        options.addOption("d", "double-dash", true, "Double-dash option");
        String[] arguments = {"--d", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"d", "value"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        String[] arguments = {"-o", "value", "non-option"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"o", "value"}, result);
    }

    @Test
    public void testFlattenWithLongOptions() {
        Options options = new Options();
        options.addOption("long-option", "long-option-description", false, "Long option");
        String[] arguments = {"--long-option"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"long-option"}, result);
    }

    @Test
    public void testFlattenWithLongOptionValue() {
        Options options = new Options();
        options.addOption("long-option", true, "Long option with value");
        String[] arguments = {"--long-option", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"long-option", "value"}, result);
    }

    @Test
    public void testFlattenWithLongOptionValueAndOtherOptions() {
        Options options = new Options();
        options.addOption("long-option", true, "Long option with value");
        options.addOption("o", "option", true, "Option description");
        String[] arguments = {"--long-option", "value", "-o", "option_value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"long-option", "value", "o", "option_value"}, result);
    }
}