package org.apache.commons.cli;

import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        options.addOption(new Option("a"));
        options.addOption(new Option("b"));
        options.addOption(new Option("c"));
        options.addOption(new Option("d", true, "option d"));
        
        PosixParser parser = new PosixParser();

        String[] args = new String[]{"-a", "arg1", "-b", "arg2", "--", "-c"};
        String[] expected = new String[]{"-a", "arg1", "-b", "arg2", "--", "-c"};
        assertArrayEquals(expected, parser.flatten(options, args, true));

        args = new String[]{"-a", "arg1", "-b", "-c"};
        expected = new String[]{"-a", "arg1", "-b", "-c"};
        assertArrayEquals(expected, parser.flatten(options, args, true));

        args = new String[]{"-a", "arg1", "-b", "arg2", "-d", "arg3"};
        expected = new String[]{"-a", "arg1", "-b", "arg2", "-d", "arg3"};
        assertArrayEquals(expected, parser.flatten(options, args, true));

        args = new String[]{"-a", "arg1", "-b", "arg2", "-d"};
        expected = new String[]{"-a", "arg1", "-b", "arg2", "-d"};
        assertArrayEquals(expected, parser.flatten(options, args, true));
    }

    @Test
    public void testFlatten_NonStopAtNonOption() {
        Options options = new Options();
        options.addOption(new Option("a"));
        options.addOption(new Option("b"));
        options.addOption(new Option("c"));
        options.addOption(new Option("d", true, "option d"));

        PosixParser parser = new PosixParser();

        String[] args = new String[]{"-a", "arg1", "-b", "arg2", "--", "-c"};
        String[] expected = new String[]{"-a", "arg1", "-b", "arg2", "--", "-c"};
        assertArrayEquals(expected, parser.flatten(options, args, false));

        args = new String[]{"-a", "arg1", "-b", "-c"};
        expected = new String[]{"-a", "arg1", "-b", "-c"};
        assertArrayEquals(expected, parser.flatten(options, args, false));

        args = new String[]{"-a", "arg1", "-b", "arg2", "-d", "arg3"};
        expected = new String[]{"-a", "arg1", "-b", "arg2", "-d", "arg3"};
        assertArrayEquals(expected, parser.flatten(options, args, false));

        args = new String[]{"-a", "arg1", "-b", "arg2", "-d"};
        expected = new String[]{"-a", "arg1", "-b", "arg2", "-d"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlatten_NoOptions() {
        Options options = new Options();
        PosixParser parser = new PosixParser();

        String[] args = new String[]{"-a", "arg1", "-b", "arg2"};
        String[] expected = new String[]{"-a", "arg1", "-b", "arg2"};
        assertArrayEquals(expected, parser.flatten(options, args, true));

        args = new String[]{"-a", "arg1", "-b", "-c"};
        expected = new String[]{"-a", "arg1", "-b", "-c"};
        assertArrayEquals(expected, parser.flatten(options, args, true));

        args = new String[]{"-a", "arg1", "-b", "arg2", "-d", "arg3"};
        expected = new String[]{"-a", "arg1", "-b", "arg2", "-d", "arg3"};
        assertArrayEquals(expected, parser.flatten(options, args, true));

        args = new String[]{"-a", "arg1", "-b", "arg2", "-d"};
        expected = new String[]{"-a", "arg1", "-b", "arg2", "-d"};
        assertArrayEquals(expected, parser.flatten(options, args, true));
    }
}