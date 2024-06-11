package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        String[] args = new String[]{"--foo", "-h", "--bar=value", "-a", "-b=value", "--baz=value1,value2", "-c", "--", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("foo", false, null, "A boolean option");
        options.addOption("h", false, null, "Print this help message");
        options.addOption("bar", true, null, "An integer option");
        options.addOption("baz", true, null, "An array option");
        options.addOption("c", false, null, "A boolean option with argument");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(15, result.length);
        assertArrayEquals(new String[]{"--foo", "-h", "--bar", "value", "-a", "-b", "value", "--baz", "value1", "value2", "--", "arg1", "arg2"}, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] args = new String[]{"--foo", "-h", "--bar=value", "-a", "-b=value", "--baz=value1,value2", "-c", "--", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("foo", false, null, "A boolean option");
        options.addOption("h", false, null, "Print this help message");
        options.addOption("bar", true, null, "An integer option");
        options.addOption("baz", true, null, "An array option");
        options.addOption("c", false, "arg", "A boolean option with argument");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals(11, result.length);
        assertArrayEquals(new String[]{"--foo", "-h", "--bar=value", "-a", "-b=value", "--baz=value1,value2", "--", "arg1", "arg2"}, Arrays.copyOf(result, 11));
        assertEquals(Arrays.asList("arg1", "arg2").toString(), Arrays.toString(Arrays.copyOfRange(result, 11, result.length)));
    }

    @Test
    public void testFlattenEmptyArgs() {
        String[] args = new String[0];
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(0, result.length);
    }

    @Test
    public void testFlattenNullArgs() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, null, false);

        assertNull(result);
    }

    @Test
    public void testFlattenEmptyOptions() {
        String[] args = new String[]{"--foo", "-h", "--bar=value", "-a", "-b=value", "--baz=value1,value2", "-c", "--", "arg1", "arg2"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(15, result.length);
        assertArrayEquals(new String[]{"--foo", "-h", "--bar=value", "-a", "-b=value", "--baz", "value1", "value2", "--", "arg1", "arg2"}, result);
    }

    @Test
    public void testFlattenStopAtNonOptionEmptyOptions() {
        String[] args = new String[]{"--foo", "-h", "--bar=value", "-a", "-b=value", "--baz=value1,value2", "-c", "--", "arg1", "arg2"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals(10, result.length);
        assertArrayEquals(new String[]{"--foo", "-h", "--bar=value", "-a", "-b=value", "--baz", "--", "arg1", "arg2"}, Arrays.copyOf(result, 10));
        assertArrayEquals(new String[0], Arrays.copyOfRange(result, 10, result.length));
    }
}