package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        String[] args = {"--foo", "-o", "out.txt", "-abcc", "--bar=value"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option fooOption = new Option("foo", true, false, "Print foo message");
        Option barOption = new Option("bar", true, true, "Print bar message with value");
        options.addOption(fooOption);
        options.addOption(barOption);
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--foo", "-o", "out.txt", "-a", "-b", "-c", "--bar=value"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] args = {"--foo", "-o", "out.txt", "-abcc", "--bar=value", "nonOption"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option fooOption = new Option("foo", true, false, "Print foo message");
        Option barOption = new Option("bar", true, true, "Print bar message with value");
        options.addOption(fooOption);
        options.addOption(barOption);
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"--foo", "-o", "out.txt", "-abcc", "--bar=value", "nonOption"}, result);
    }

    @Test
    public void testFlattenWithLongOption() {
        String[] args = {"--long-option-with-arg=value"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option longOption = new Option("long-option-with-arg", true, true, "Print long option message with arg value");
        options.addOption(longOption);
        String[] result = parser.flatten(options, args, false);
        assertEquals("--long-option-with-arg=value", result[0]);
    }

    @Test
    public void testFlattenWithNoOptions() {
        String[] args = {"arg1", "arg2"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"arg1", "arg2"}, result);
    }

    @Test
    public void testFlattenWithEmptyArgs() {
        String[] args = {};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testFlattenWithUnexpectedToken() {
        String[] args = {"--foo", "-o", "out.txt", "nonOption"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option fooOption = new Option("foo", true, false, "Print foo message");
        options.addOption(fooOption);
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--foo", "-o", "out.txt"}, result);
        assertNull(parser.currentOption);
    }
}