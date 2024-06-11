package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testEmpty() {
        PosixParser parser = new PosixParser();
        String[] args = {};
        String[] expected = {};
        assertArrayEquals(expected, parser.flatten(new Options(), args, false));
    }

    @Test
    public void testSingleNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"foo"};
        String[] expected = {"foo"};
        assertArrayEquals(expected, parser.flatten(new Options(), args, false));
    }

    @Test
    public void testSingleOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a"};
        String[] expected = {"-a"};
        assertArrayEquals(expected, parser.flatten(new Options(), args, false));
    }

    @Test
    public void testSingleOptionWithArgument() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "a");
        String[] args = {"-a", "bar"};
        String[] expected = {"-a", "bar"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testLongOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--longOption"};
        String[] expected = {"--longOption"};
        assertArrayEquals(expected, parser.flatten(new Options(), args, false));
    }

    @Test
    public void testLongOptionWithArgument() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOption", true, "lo");
        String[] args = {"--longOption", "bar"};
        String[] expected = {"--longOption", "bar"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testOptionWithArgumentAndNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "a");
        String[] args = {"-a", "bar", "foo"};
        String[] expected = {"-a", "bar", "foo"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testOptionBursted() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "a");
        String[] args = {"-a--foo", "bar"};
        String[] expected = {"-a", "-foo", "bar"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testOptionBurstedWithLongOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOption", true, "lo");
        String[] args = {"--longOption--foo", "bar"};
        String[] expected = {"--longOption", "-foo", "bar"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testOptionBurstedWithLongAndShortOptions() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "a");
        options.addOption("longOption", true, "lo");
        String[] args = {"--longOption--foo", "-a", "bar"};
        String[] expected = {"--longOption", "-a", "bar"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testOptionBurstedWithLongOptionAndArgument() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOption", true, "lo");
        String[] args = {"--longOption--foo=bar", "baz"};
        String[] expected = {"--longOption", "bar", "baz"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "a");
        String[] args = {"-a", "bar", "foo", "baz"};
        String[] expected = {"-a", "bar"};
        assertArrayEquals(expected, parser.flatten(options, args, true));
    }

    @Test
    public void testStopAtNonOptionWithLongOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "a");
        options.addOption("longOption", true, "lo");
        String[] args = {"--longOption--foo", "-a", "bar", "baz"};
        String[] expected = {"--longOption", "-a"};
        assertArrayEquals(expected, parser.flatten(options, args, true));
    }

    @Test
    public void testStopAtNonOptionWithOptionAndArgument() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "a");
        options.addOption("longOption", true, "lo");
        String[] args = {"--longOption--foo=bar", "-a", "baz"};
        String[] expected = {"--longOption", "-a"};
        assertArrayEquals(expected, parser.flatten(options, args, true));
    }

    @Test
    public void testStopAtNonOptionWithLongOptionAndArgument() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "a");
        options.addOption("longOption", true, "lo");
        String[] args = {"--longOption--foo=bar", "--longOption--baz", "baz"};
        String[] expected = {"--longOption--foo", "--longOption--baz"};
        assertArrayEquals(expected, parser.flatten(options, args, true));
    }

    @Test
    public void testStopAtNonOptionWithOptionWithArgument() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "a");
        String[] args = {"-a", "--longOption--foo=bar", "baz"};
        String[] expected = {"-a", "--longOption--foo"};
        assertArrayEquals(expected, parser.flatten(options, args, true));
    }

    @Test
    public void testStopAtNonOptionWithOptionBursted() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "a");
        String[] args = {"-a--foo", "--longOption--bar", "baz"};
        String[] expected = {"-a--foo"};
        assertArrayEquals(expected, parser.flatten(options, args, true));
    }

    @Test
    public void testStopAtNonOptionWithOptionBurstedWithLongOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "a");
        options.addOption("longOption", true, "lo");
        String[] args = {"-a--foo", "--longOption--bar", "baz"};
        String[] expected = {"-a--foo"};
        assertArrayEquals(expected, parser.flatten(options, args, true));
    }

    private void assertArrayEquals(String[] expected, String[] actual) {
        assertArrayEquals(expected, actual, "Unexpected array values");
    }

    private void assertArrayEquals(String[] expected, String[] actual, String message) {
        if (expected.length != actual.length) {
            throw new AssertionError(message + ": wrong number of elements");
        }
        for (int i = 0; i < expected.length; i++) {
            if (!expected[i].equals(actual[i])) {
                throw new AssertionError(
                        message + ": expected [" + Arrays.toString(expected) + "], but was [" + Arrays.toString(actual) + ']');
            }
        }
    }
}