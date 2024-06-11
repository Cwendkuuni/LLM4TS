package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenEmpty() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {};

        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenSingle() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = { "--foo" };

        String[] result = parser.flatten(options, args, false);

        assertEquals(1, result.length);
        assertEquals("--foo", result[0]);
    }

    @Test
    public void testFlattenSingleValueOption() {
        Options options = new Options();
        options.addOption("f", "foo", true, "f");
        PosixParser parser = new PosixParser();
        String[] args = { "-f" };

        String[] result = parser.flatten(options, args, false);

        assertEquals(1, result.length);
        assertEquals("-f", result[0]);
    }

    @Test
    public void testFlattenMultiValueOption() {
        Options options = new Options();
        options.addOption("f", "foo", false, "f");
        PosixParser parser = new PosixParser();
        String[] args = { "--foo", "bar", "--foo", "baz" };

        String[] result = parser.flatten(options, args, false);

        assertEquals(5, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("bar", result[1]);
        assertEquals("--foo", result[2]);
        assertEquals("baz", result[3]);
        assertEquals("", result[4]);
    }

    @Test
    public void testFlattenWithArguments() {
        Options options = new Options();
        options.addOption("f", "foo", false, "f");
        PosixParser parser = new PosixParser();
        String[] args = { "--foo", "bar", "baz" };

        String[] result = parser.flatten(options, args, false);

        assertEquals(4, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("bar", result[1]);
        assertEquals("baz", result[2]);
        assertEquals("", result[3]);
    }

    @Test
    public void testFlattenWithArgumentsAndSingleValueOption() {
        Options options = new Options();
        options.addOption("f", "foo", true, "f");
        PosixParser parser = new PosixParser();
        String[] args = { "--foo", "bar", "-f", "baz" };

        String[] result = parser.flatten(options, args, false);

        assertEquals(5, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("bar", result[1]);
        assertEquals("-f", result[2]);
        assertEquals("baz", result[3]);
        assertEquals("", result[4]);
    }

    @Test
    public void testFlattenWithArgumentsAndMultiValueOption() {
        Options options = new Options();
        options.addOption("f", "foo", false, "f");
        PosixParser parser = new PosixParser();
        String[] args = { "--foo", "bar,baz", "-f", "qux" };

        String[] result = parser.flatten(options, args, false);

        assertEquals(6, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("bar,baz", result[1]);
        assertEquals("-f", result[2]);
        assertEquals("qux", result[3]);
        assertEquals("", result[4]);
        assertEquals("", result[5]);
    }

    @Test
    public void testFlattenWithLongOptions() {
        Options options = new Options();
        options.addOption("foo", false, "f", "foo option");
        PosixParser parser = new PosixParser();
        String[] args = { "--foo-option" };

        String[] result = parser.flatten(options, args, false);

        assertEquals(1, result.length);
        assertEquals("--foo-option", result[0]);
    }

    @Test
    public void testFlattenWithLongOptionsAndValues() {
        Options options = new Options();
        options.addOption("foo", true, "f", "foo option");
        PosixParser parser = new PosixParser();
        String[] args = { "--foo-option", "bar" };

        String[] result = parser.flatten(options, args, false);

        assertEquals(2, result.length);
        assertEquals("--foo-option", result[0]);
        assertEquals("bar", result[1]);
    }

    @Test
    public void testFlattenWithLongOptionsAndMultipleValues() {
        Options options = new Options();
        options.addOption("foo", false, "f", "foo option");
        PosixParser parser = new PosixParser();
        String[] args = { "--foo-option", "bar,baz" };

        String[] result = parser.flatten(options, args, false);

        assertEquals(2, result.length);
        assertEquals("--foo-option", result[0]);
        assertEquals("bar,baz", result[1]);
    }

    @Test
    public void testFlattenWithLongOptionsAndArguments() {
        Options options = new Options();
        options.addOption("foo", false, "f", "foo option");
        PosixParser parser = new PosixParser();
        String[] args = { "--foo-option", "bar", "baz" };

        String[] result = parser.flatten(options, args, false);

        assertEquals(4, result.length);
        assertEquals("--foo-option", result[0]);
        assertEquals("bar", result[1]);
        assertEquals("baz", result[2]);
        assertEquals("", result[3]);
    }

    @Test
    public void testFlattenWithLongOptionsAndArgumentsAndValues() {
        Options options = new Options();
        options.addOption("foo", true, "f", "foo option");
        PosixParser parser = new PosixParser();
        String[] args = { "--foo-option", "bar", "qux" };

        String[] result = parser.flatten(options, args, false);

        assertEquals(4, result.length);
        assertEquals("--foo-option", result[0]);
        assertEquals("bar", result[1]);
        assertEquals("qux", result[2]);
        assertEquals("", result[3]);
    }

    @Test
    public void testFlattenWithInterspersedOptions() {
        Options options = new Options();
        options.addOption("f", "foo", false, "f");
        PosixParser parser = new PosixParser();
        String[] args = { "-f", "bar", "--foo", "baz", "-f", "qux" };

        String[] result = parser.flatten(options, args, false);

        assertEquals(7, result.length);
        assertEquals("-f", result[0]);
        assertEquals("bar", result[1]);
        assertEquals("--foo", result[2]);
        assertEquals("", result[3]);
        assertEquals("baz", result[4]);
        assertEquals("-f", result[5]);
        assertEquals("qux", result[6]);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("f", "foo", false, "f");
        PosixParser parser = new PosixParser();
        String[] args = { "-f", "bar", "--not-an-option", "baz", "-f", "qux" };

        String[] result = parser.flatten(options, args, true);

        assertEquals(5, result.length);
        assertEquals("-f", result[0]);
        assertEquals("bar", result[1]);
        assertEquals("--not-an-option", result[2]);
        assertEquals("baz", result[3]);
        assertEquals("-f", result[4]);
    }

    @Test
    public void testFlattenWithEmpty() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {};

        String[] result = parser.flatten(options, args, false);

        assertEquals(0, result.length);
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndEmpty() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = { "--not-an-option" };

        String[] result = parser.flatten(options, args, true);

        assertEquals(0, result.length);
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndArguments() {
        Options options = new Options();
        options.addOption("f", "foo", false, "f");
        PosixParser parser = new PosixParser();
        String[] args = { "-f", "bar", "--not-an-option", "baz" };

        String[] result = parser.flatten(options, args, true);

        assertEquals(3, result.length);
        assertEquals("-f", result[0]);
        assertEquals("bar", result[1]);
        assertEquals("--not-an-option", result[2]);
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndLongOptions() {
        Options options = new Options();
        options.addOption("foo", false, "f", "foo option");
        PosixParser parser = new PosixParser();
        String[] args = { "--not-an-option", "--foo-option", "bar" };

        String[] result = parser.flatten(options, args, true);

        assertEquals(3, result.length);
        assertEquals("--not-an-option", result[0]);
        assertEquals("--foo-option", result[1]);
        assertEquals("bar", result[2]);
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndLongOptionsWithValues() {
        Options options = new Options();
        options.addOption("foo", true, "f", "foo option");
        PosixParser parser = new PosixParser();
        String[] args = { "--not-an-option", "--foo-option=bar" };

        String[] result = parser.flatten(options, args, true);

        assertEquals(3, result.length);
        assertEquals("--not-an-option", result[0]);
        assertEquals("--foo-option=bar", result[1]);
        assertEquals("", result[2]);
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndLongOptionsWithArguments() {
        Options options = new Options();
        options.addOption("foo", false, "f", "foo option");
        PosixParser parser = new PosixParser();
        String[] args = { "--not-an-option", "--foo-option", "bar", "baz" };

        String[] result = parser.flatten(options, args, true);

        assertEquals(5, result.length);
        assertEquals("--not-an-option", result[0]);
        assertEquals("--foo-option", result[1]);
        assertEquals("bar", result[2]);
        assertEquals("baz", result[3]);
        assertEquals("", result[4]);
    }
}