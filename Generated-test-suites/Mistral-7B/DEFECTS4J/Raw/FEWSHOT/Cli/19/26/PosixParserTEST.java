package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", true, "option-a");
        options.addOption("b", 1, "option-b");
        options.addOption("c", 2, "option-c");
        options.addOption("d", 3, "option-d");
        options.addOption("e", 4, "option-e");

        String[] args = {"--a", "-c", "1", "-d", "3", "-e", "4", "foo", "bar", "--"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(11, result.length);
        assertEquals("a", result[0]);
        assertEquals("c", result[1]);
        assertEquals("1", result[2]);
        assertEquals("d", result[3]);
        assertEquals("3", result[4]);
        assertEquals("e", result[5]);
        assertEquals("4", result[6]);
        assertEquals("foo", result[7]);
        assertEquals("bar", result[8]);
        assertEquals("--", result[9]);
        assertEquals(null, result[10]);
    }

    @Test
    public void testFlattenWithLongOptions() {
        Options options = new Options();
        options.addOption("long-option-a", true, "long-option-a-help");
        options.addOption("long-option-b", 1, "long-option-b-help");

        String[] args = {"--long-option-a", "--long-option-b=1", "--long-option-a", "value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(5, result.length);
        assertEquals("long-option-a", result[0]);
        assertEquals("long-option-b", result[1]);
        assertEquals("1", result[2]);
        assertEquals("long-option-a", result[3]);
        assertEquals("value", result[4]);
    }

    @Test
    public void testFlattenWithShortAndLongOptions() {
        Options options = new Options();
        options.addOption("a", true, "option-a");
        options.addOption("b", 1, "option-b");
        options.addOption("c", 2, "option-c");
        options.addOption("d", true, "option-d");

        String[] args = {"--a", "-b", "1", "--d", "foo", "-c", "2", "--", "bar"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(8, result.length);
        assertEquals("a", result[0]);
        assertEquals("d", result[1]);
        assertEquals(null, result[2]);
        assertEquals("b", result[3]);
        assertEquals("1", result[4]);
        assertEquals("c", result[5]);
        assertEquals("2", result[6]);
        assertEquals("--", result[7]);
        assertEquals("bar", result[8]);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "option-a");
        options.addOption("b", 1, "option-b");
        options.addOption("c", 2, "option-c");

        String[] args = {"--a", "-b", "1", "--", "foo", "bar"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals(4, result.length);
        assertEquals("a", result[0]);
        assertEquals("b", result[1]);
        assertEquals("1", result[2]);
        assertEquals("foo", result[3]);
    }

    @Test
    public void testFlattenWithInvalidToken() {
        Options options = new Options();
        options.addOption("a", true, "option-a");
        options.addOption("b", 1, "option-b");

        String[] args = {"--a", "-b", "1", "--foo", "--"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(5, result.length);
        assertEquals("a", result[0]);
        assertEquals("b", result[1]);
        assertEquals("1", result[2]);
        assertEquals("foo", result[3]);
        assertEquals("--", result[4]);
    }

    @Test
    public void testFlattenWithInvalidTokenAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "option-a");
        options.addOption("b", 1, "option-b");

        String[] args = {"--a", "-b", "1", "--foo", "--"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals(3, result.length);
        assertEquals("a", result[0]);
        assertEquals("b", result[1]);
        assertEquals("1", result[2]);
    }
}