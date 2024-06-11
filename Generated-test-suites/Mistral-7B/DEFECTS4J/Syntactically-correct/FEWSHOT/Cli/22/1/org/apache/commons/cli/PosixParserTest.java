package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenEmptyArgs() {
        Options options = new Options();
        String[] args = new String[0];
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertNull(result);
    }

    @Test
    public void testFlattenOptionsOnly() {
        Options options = new Options();
        options.addOption("o", "option", false, "A test option");
        options.addOption("h", "help", true, "Print help");
        String[] args = new String[]{"--option", "--help"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--option", "--help"}, result);
    }

    @Test
    public void testFlattenSingleArg() {
        Options options = new Options();
        String[] args = new String[]{"arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"arg"}, result);
    }

    @Test
    public void testFlattenSingleOption() {
        Options options = new Options();
        options.addOption("o", "option", false, "A test option");
        String[] args = new String[]{"-o"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"-o"}, result);
    }

    @Test
    public void testFlattenShortOptionWithArg() {
        Options options = new Options();
        options.addOption("o", "option", true, "An option with an argument");
        String[] args = new String[]{"-o", "arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"-o", "arg"}, result);
    }

    @Test
    public void testFlattenLongOptionWithArg() {
        Options options = new Options();
        options.addOption("l", "longoption", true, "A long option with an argument");
        String[] args = new String[]{"--longoption", "arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--longoption", "arg"}, result);
    }

    @Test
    public void testFlattenDoubleShortOptions() {
        Options options = new Options();
        options.addOption("oo", "doubleOption", false, "A double short option");
        String[] args = new String[]{"-oo"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"-oo"}, result);
    }

    @Test
    public void testFlattenDoubleShortOptionWithArg() {
        Options options = new Options();
        options.addOption("oo", "doubleOption", true, "A double short option with an argument");
        String[] args = new String[]{"-oo", "arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"-oo", "arg"}, result);
    }

    @Test
    public void testFlattenLongOptionWithShortOption() {
        Options options = new Options();
        options.addOption("lo", "longOption", false, "A long option with a short option");
        String[] args = new String[]{"--lo", "-s"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--lo", "-s"}, result);
    }

    @Test
    public void testFlattenLongOptionWithShortOptionAndArg() {
        Options options = new Options();
        options.addOption("lo", "longOption", true, "A long option with a short option and argument");
        String[] args = new String[]{"--lo", "-s", "arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--lo", "-s", "arg"}, result);
    }

    @Test
    public void testFlattenLongOptionWithMultipleargs() {
        Options options = new Options();
        options.addOption("lo", "longOption", true, "A long option with multiple arguments");
        String[] args = new String[]{"--lo", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--lo", "arg1", "arg2"}, result);
    }

    @Test
    public void testFlattenLongOptionWithMultipleargsAndShortOption() {
        Options options = new Options();
        options.addOption("lo", "longOption", true, "A long option with multiple arguments");
        options.addOption("s", "shortOption", false, "A short option");
        String[] args = new String[]{"--lo", "arg1", "arg2", "-s"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--lo", "arg1", "arg2", "-s"}, result);
    }

    @Test
    public void testFlattenLongOptionWithMultipleargsAndShortOptionWithArg() {
        Options options = new Options();
        options.addOption("lo", "longOption", true, "A long option with multiple arguments");
        options.addOption("s", "shortOption", true, "A short option with an argument");
        String[] args = new String[]{"--lo", "arg1", "arg2", "--s", "arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--lo", "arg1", "arg2", "--s", "arg"}, result);
    }

    @Test
    public void testFlattenLongOptionWithMultipleargsAndShortOptionWithMultipleargs() {
        Options options = new Options();
        options.addOption("lo", "longOption", true, "A long option with multiple arguments");
        options.addOption("so", "shortOption", true, "A short option with multiple arguments");
        String[] args = new String[]{"--lo", "arg1", "arg2", "--so", "arg3", "arg4"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--lo", "arg1", "arg2", "--so", "arg3", "arg4"}, result);
    }

    @Test
    public void testFlattenLongOptionWithMultipleargsAndShortOptionWithMultipleargsAndLongOption() {
        Options options = new Options();
        options.addOption("lo", "longOption", true, "A long option with multiple arguments");
        options.addOption("so", "shortOption", true, "A short option with multiple arguments");
        options.addOption("loo", "longOption2", false, "Another long option");
        String[] args = new String[]{"--lo", "arg1", "arg2", "--so", "arg3", "arg4", "--loo"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--lo", "arg1", "arg2", "--so", "arg3", "arg4", "--loo"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("o", "option", false, "A test option");
        String[] args = new String[]{"--option", "arg", "nonOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertEquals(new String[]{"--option", "arg", "nonOption"}, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithEmptyOption() {
        Options options = new Options();
        String[] args = new String[0];
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenWithNullOptions() {
        String[] args = new String[]{"--option", "arg"};
        PosixParser parser = new PosixParser();
        try {
            parser.flatten(null, args, false);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFlattenWithNullArgs() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        try {
            parser.flatten(options, null, false);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    private void assertArrayEquals(String[] expected, String[] actual) {
        assertArrayEquals(expected, actual, false);
    }

    private void assertArrayEquals(String[] expected, String[] actual, boolean ignoreOrder) {
        List<String> expectedList = Arrays.asList(expected);
        List<String> actualList = Arrays.asList(actual);
        if (ignoreOrder) {
            assertTrue(expectedList.containsAll(actualList));
            assertTrue(actualList.containsAll(expectedList));
        } else {
            assertEquals(expectedList, actualList);
        }
    }
}