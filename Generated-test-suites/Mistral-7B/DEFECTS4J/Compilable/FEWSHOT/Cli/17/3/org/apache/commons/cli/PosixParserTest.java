package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        Options options = new Options();
        String[] args = new String[]{"--foo", "-b", "-c", "d", "--", "e", "f"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        String[] expected = new String[]{"--foo", "-b", "-c", "d", "--", "e", "f"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptions() {
        Options options = new Options();
        options.addOption("longOption", true, "l");
        String[] args = new String[]{"--longOption", "--another-long-option=value", "arg1", "arg2", "--", "e", "f"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        String[] expected = new String[]{"--longOption", "--another-long-option=value", "arg1", "arg2", "--", "e", "f"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithShortOptions() {
        Options options = new Options();
        options.addOption("o", true, "o");
        String[] args = new String[]{"--", "-o", "value", "arg1", "arg2", "--", "e", "f"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        String[] expected = new String[]{"--", "--o=value", "arg1", "arg2", "--", "e", "f"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithMixedShortAndLongOptions() {
        Options options = new Options();
        options.addOption("o", true, "o");
        options.addOption("l", "longOption", true, "l");
        String[] args = new String[]{"--longOption", "--o", "value", "arg1", "arg2", "--", "e", "f"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        String[] expected = new String[]{"--longOption", "--o=value", "arg1", "arg2", "--", "e", "f"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithShortOptionRequiringArgument() {
        Options options = new Options();
        options.addOption("o", "option", true, "o");
        String[] args = new String[]{"--", "-o", "arg1", "arg2", "--", "e", "f"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        String[] expected = new String[]{"--", "--o=arg1", "--o=arg2", "--", "e", "f"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithShortOptionRequiringMultipleArguments() {
        Options options = new Options();
        options.addOption("o", "option", true, "o");
        String[] args = new String[]{"--", "-o", "arg1", "arg2", "arg3", "--", "e", "f"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        String[] expected = new String[]{"--", "--o=arg1", "--o=arg2", "--o=arg3", "--", "e", "f"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithShortOptionWithArgumentAndValue() {
        Options options = new Options();
        options.addOption("o", "option", true, "o");
        String[] args = new String[]{"--", "-o=value", "--", "e", "f"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        String[] expected = new String[]{"--", "--o=value", "--", "e", "f"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("o", true, "o");
        String[] args = new String[]{"--", "-o", "value", "arg1", "arg2", "--nonOption", "--", "e", "f"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        String[] expected = new String[]{"--", "--o=value", "arg1", "arg2", "--nonOption"};
        assertArrayEquals(expected, result);
    }
}