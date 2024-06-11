package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenEmptyArgs() {
        Options options = new Options();
        String[] args = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testFlattenSimpleArgs() {
        Options options = new Options();
        String[] args = {"--foo", "-a", "-b", "-c", "arg1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--foo", "-a", "-b", "-c", "arg1"}, result);
    }

    @Test
    public void testFlattenWithLongOptions() {
        Options options = new Options();
        options.addOptional("long--option", true, "longOptionArg");
        String[] args = {"--long--option=value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--long--option", "value"}, result);
    }

    @Test
    public void testFlattenWithMixedOptions() {
        Options options = new Options();
        options.addOption("a", true, "aOption");
        options.addOption("b", true, "bOption");
        options.addOption("c", false, "cOption");
        options.addOptional("d--long--option", true, "dLongOption");
        String[] args = {"-a", "--d--long--option=value", "-b", "-c", "arg1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-a", "--d--long--option", "-b", "-c", "arg1"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "aOption");
        options.addOption("b", true, "bOption");
        options.addOption("c", false, "cOption");
        options.addOptional("d--long--option", true, "dLongOption");
        String[] args = {"-a", "--d--long--option=value", "nonOption", "-b", "-c", "arg1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"-a", "nonOption", "-b", "-c", "arg1"}, result);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        String[] args = {"--foo--bar--baz"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--foo", "--bar", "--baz"}, result);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        String[] args = {"--", "-", "-foo"};
        String[] result = parser.flatten(null, args, false);
        assertArrayEquals(new String[]{"--", "-", "-foo"}, result);
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("foo", true, "fooOption");
        PosixParser parser = new PosixParser();
        String[] args = {"-foo", "--", "arg1"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-foo", "arg1"}, result);
        assertSame(options.findOption("foo"), parser.currentOption);
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        String[] args = {"--", "arg1", "-foo", "arg2"};
        String[] result = parser.flatten(null, args, false);
        assertArrayEquals(new String[]{"--", "arg1", "-foo", "arg2"}, result);
    }
}