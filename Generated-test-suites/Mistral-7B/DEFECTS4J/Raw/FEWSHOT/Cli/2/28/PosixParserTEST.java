package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("o", "option", false, "Option description");
        options.addOption("a", "another", true, "Another description");

        String[] arguments = {"--option", "-a", "value", "arg1", "--", "-o", "anotherValue"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = new ArrayList<>();
        expected.add("--option");
        expected.add("o");
        expected.add("--");
        expected.add("-a");
        expected.add("a");
        expected.add("value");
        expected.add("-o");
        expected.add("anotherValue");

        assertEquals(expected.toString(), Arrays.toString(result));
    }

    @Test
    public void testStopAtNonOption() {
        Options options = new Options();
        options.addOption("o", "option", false, "Option description");
        options.addOption("a", "another", true, "Another description");

        String[] arguments = {"--option", "-a", "value", "arg1", "--arg", "value2", "-o"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        List<String> expected = new ArrayList<>();
        expected.add("--option");
        expected.add("o");
        expected.add("--arg");
        expected.add("arg1");
        expected.add("--");
        expected.add("value2");
        expected.add("-o");

        assertEquals(expected.toString(), Arrays.toString(result));
    }

    @Test
    public void testEmptyArguments() {
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(null, new String[]{}, true);
        assertEquals("[]", Arrays.toString(result));
    }

    @Test
    public void testEmptyOptions() {
        String[] arguments = {"arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(null, arguments, false);
        assertEquals("[]", Arrays.toString(result));
    }

    @Test
    public void testSingleHyphen() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-"};
        String[] result = parser.flatten(null, arguments, false);
        assertEquals("[-]", Arrays.toString(result));
    }

    @Test
    public void testSingleOption() {
        Options options = new Options();
        options.addOption("o", "option", false, "Option description");

        String[] arguments = {"-o"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = new ArrayList<>();
        expected.add("o");

        assertEquals(expected.toString(), Arrays.toString(result));
    }

    @Test
    public void testTwoCharacterOption() {
        Options options = new Options();
        options.addOption("oo", "twoCharOption", false, "Two character option description");

        String[] arguments = {"-oo"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = new ArrayList<>();
        expected.add("oo");

        assertEquals(expected.toString(), Arrays.toString(result));
    }

    @Test
    public void testOptionWithValue() {
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");

        String[] arguments = {"-o", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = new ArrayList<>();
        expected.add("o");
        expected.add("value");

        assertEquals(expected.toString(), Arrays.toString(result));
    }

    @Test
    public void testOptionWithMultipleValues() {
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");

        String[] arguments = {"-o", "value1", "-o", "value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = new ArrayList<>();
        expected.add("o");
        expected.add("value1");
        expected.add("value2");

        assertEquals(expected.toString(), Arrays.toString(result));
    }

    @Test
    public void testMultipleOptions() {
        Options options = new Options();
        options.addOption("o1", "option1", false, "Option1 description");
        options.addOption("o2", "option2", false, "Option2 description");

        String[] arguments = {"-o1", "-o2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = new ArrayList<>();
        expected.add("o1");
        expected.add("o2");

        assertEquals(expected.toString(), Arrays.toString(result));
    }

    @Test
    public void testOptionWithLongArgument() {
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");

        String[] arguments = {"-o", "a very very long argument"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = new ArrayList<>();
        expected.add("o");
        expected.add("a very very long argument");

        assertEquals(expected.toString(), Arrays.toString(result));
    }

    @Test
    public void testOptionWithShortArgument() {
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");

        String[] arguments = {"-o", "a"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = new ArrayList<>();
        expected.add("o");
        expected.add("a");

        assertEquals(expected.toString(), Arrays.toString(result));
    }

    @Test
    public void testOptionWithLongName() {
        Options options = new Options();
        options.addOption("longOption", "long option description", false, "longOption description");

        String[] arguments = {"--longOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = new ArrayList<>();
        expected.add("longOption");

        assertEquals(expected.toString(), Arrays.toString(result));
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        options.addOption("a", "another", true, "Another description");

        String[] arguments = {"--option-a", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = new ArrayList<>();
        expected.add("--option");
        expected.add("o");
        expected.add("a");
        expected.add("value");

        assertEquals(expected.toString(), Arrays.toString(result));
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        options.addOption("a", "another", true, "Another description");

        String[] arguments = {"--option-another", "value1", "value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        List<String> expected = new ArrayList<>();
        expected.add("--option");
        expected.add("another");
        expected.add("value1");
        expected.add("value2");

        assertEquals(expected.toString(), Arrays.toString(result));
    }

    @Test
    public void testBurstTokenWithTwoCharacterOption() {
        Options options = new Options();
        options.addOption("oo", "twoCharOption", true, "Two character option description");

        String[] arguments = {"--oo-value1", "value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = new ArrayList<>();
        expected.add("oo");
        expected.add("--");
        expected.add("value1");
        expected.add("value2");

        assertEquals(expected.toString(), Arrays.toString(result));
    }

    @Test
    public void testBurstTokenWithOptionValue() {
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");

        String[] arguments = {"--option=value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = new ArrayList<>();
        expected.add("o");
        expected.add("value");

        assertEquals(expected.toString(), Arrays.toString(result));
    }

    @Test
    public void testBurstTokenWithMultipleValues() {
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");

        String[] arguments = {"--option=value1", "--option=value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = new ArrayList<>();
        expected.add("o");
        expected.add("value1");
        expected.add("value2");

        assertEquals(expected.toString(), Arrays.toString(result));
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-"};
        String[] result = parser.flatten(null, arguments, false);

        List<String> expected = new ArrayList<>();
        expected.add("-");

        assertEquals(expected.toString(), Arrays.toString(result));
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("o", "option", false, "Option description");

        String[] arguments = {"-o"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = new ArrayList<>();
        expected.add("o");

        assertEquals(expected.toString(), Arrays.toString(result));
    }

    @Test
    public void testProcessOptionTokenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("o", "option", false, "Option description");
        options.addOption("a", "another", true, "Another description");

        String[] arguments = {"-o", "--another"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        List<String> expected = new ArrayList<>();
        expected.add("o");
        expected.add("--another");

        assertEquals(expected.toString(), Arrays.toString(result));
    }

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();

        assertFalse(parser.eatTheRest);
        assertTrue(parser.tokens.isEmpty());
        assertNull(parser.currentOption);
    }
}