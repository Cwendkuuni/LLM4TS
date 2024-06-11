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
    public void testFlatten() {
        Options options = new Options();
        String[] args = {"--foo", "--bar=value", "-a", "-b", "-c=value", "-d", "--", "arg1", "arg2"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        List expected = Arrays.asList("--foo", "--bar=value", "-a", "-b", "-c=value", "-d", "--", "arg1", "arg2");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);

        result = parser.flatten(options, args, true);
        expected = Arrays.asList("--foo", "--bar=value", "-a", "-b", "-c=value", "-d", "arg1", "arg2");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        String[] args = {"--foo", "--bar=value", "-a", "-b", "-c=value", "-d", "--", "arg1", "arg2", "--nonOption"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, true);
        List expected = Arrays.asList("--foo", "--bar=value", "-a", "-b", "-c=value", "-d", "--nonOption", "arg1", "arg2");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testHandleSpecialTokens() {
        Options options = new Options();
        String[] args = {"--foo", "--bar=value", "--", "arg1", "arg2"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        List expected = Arrays.asList("--foo", "--bar=value", "--", "arg1", "arg2");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);

        result = parser.flatten(options, args, true);
        expected = Arrays.asList("--foo", "--bar=value", "--", "arg1", "arg2");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testHandleSingleHyphen() {
        Options options = new Options();
        String[] args = {"-", "--foo"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        List expected = Arrays.asList("-", "--foo");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);

        result = parser.flatten(options, args, true);
        expected = Arrays.asList("-", "--foo");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testHandleSingleCharacterOption() {
        Options options = new Options();
        Option option = new Option("a", true, true, "Print help");
        options.addOption(option);
        String[] args = {"-a"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        List expected = Arrays.asList("-a");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);

        result = parser.flatten(options, args, true);
        expected = Arrays.asList("-a");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testHandleMultiCharacterOption() {
        Options options = new Options();
        Option option = new Option("ab", true, true, "Print help");
        options.addOption(option);
        String[] args = {"-ab"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        List expected = Arrays.asList("-ab");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);

        result = parser.flatten(options, args, true);
        expected = Arrays.asList("-ab");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testHandleOptionWithArgument() {
        Options options = new Options();
        Option option = new Option("a", true, true, "Print help").requiredArg();
        options.addOption(option);
        String[] args = {"-a", "value"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        List expected = Arrays.asList("-a", "value");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);

        result = parser.flatten(options, args, true);
        expected = Arrays.asList("-a", "value");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testHandleOptionWithMultipleArguments() {
        Options options = new Options();
        Option option = new Option("a", true, true, "Print help").requiredArgs(1, Integer.MAX_VALUE);
        options.addOption(option);
        String[] args = {"-a", "value1", "value2"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        List expected = Arrays.asList("-a", "value1", "value2");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);

        result = parser.flatten(options, args, true);
        expected = Arrays.asList("-a", "value1", "value2");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testHandleOptionWithNoArgument() {
        Options options = new Options();
        Option option = new Option("a", true, false, "Print help");
        options.addOption(option);
        String[] args = {"-a"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        List expected = Arrays.asList("-a");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);

        result = parser.flatten(options, args, true);
        expected = Arrays.asList("-a");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testHandleOptionWithLongArgument() {
        Options options = new Options();
        Option option = new Option("a", true, true, "Print help");
        options.addOption(option);
        String[] args = {"-a", "long argument"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        List expected = Arrays.asList("-a", "long argument");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);

        result = parser.flatten(options, args, true);
        expected = Arrays.asList("-a", "long argument");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testHandleOptionWithNoArgumentAndSingleCharacterArg() {
        Options options = new Options();
        Option option = new Option("a", true, false, "Print help");
        Option argOption = new Option("a", true, false, "Print arg");
        options.addOption(option);
        options.addOption(argOption);
        String[] args = {"-a", "-a"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        List expected = Arrays.asList("-a", "-a");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);

        result = parser.flatten(options, args, true);
        expected = Arrays.asList("-a", "-a");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testHandleOptionWithNoArgumentAndMultipleCharacterArg() {
        Options options = new Options();
        Option option = new Option("a", true, false, "Print help");
        Option argOption = new Option("ab", true, false, "Print arg");
        options.addOption(option);
        options.addOption(argOption);
        String[] args = {"-a", "--ab"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        List expected = Arrays.asList("-a", "--ab");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);

        result = parser.flatten(options, args, true);
        expected = Arrays.asList("-a", "--ab");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testHandleOptionBursting() {
        Options options = new Options();
        Option option = new Option("ab", true, true, "Print help");
        options.addOption(option);
        String[] args = {"-abcd"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        List expected = Arrays.asList("-ab", "-c", "-d");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);

        result = parser.flatten(options, args, true);
        expected = Arrays.asList("-ab", "-c", "-d");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testHandleOptionBurstingWithStopAtNonOption() {
        Options options = new Options();
        Option option = new Option("ab", true, true, "Print help");
        options.addOption(option);
        String[] args = {"-ab", "nonOption"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, true);
        List expected = Arrays.asList("-ab", "nonOption");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testHandleOptionWithArgumentAndBursting() {
        Options options = new Options();
        Option option = new Option("a", true, true, "Print help").requiredArg();
        Option argOption = new Option("b", true, true, "Print arg").requiredArg();
        options.addOption(option);
        options.addOption(argOption);
        String[] args = {"-a", "-bcd"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        List expected = Arrays.asList("-a", "-bcd");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);

        result = parser.flatten(options, args, true);
        expected = Arrays.asList("-a", "-bcd");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testHandleEmptyArguments() {
        Options options = new Options();
        String[] args = {};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        List expected = Arrays.asList();
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);

        result = parser.flatten(options, args, true);
        expected = Arrays.asList();
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testHandleNullArguments() {
        Options options = new Options();
        String[] args = null;
        PosixParser parser = new PosixParser();

        try {
            parser.flatten(options, args, false);
            assertFalse(true);
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            parser.flatten(options, args, true);
            assertFalse(true);
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testHandleEmptyOptionList() {
        Options options = new Options();
        String[] args = {"--foo", "--bar"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        List expected = Arrays.asList("--foo", "--bar");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);

        result = parser.flatten(options, args, true);
        expected = Arrays.asList("--foo", "--bar");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testHandleEmptyOptionListWithStopAtNonOption() {
        Options options = new Options();
        String[] args = {"--foo", "--bar"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, true);
        List expected = Arrays.asList("--foo", "--bar");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }
}