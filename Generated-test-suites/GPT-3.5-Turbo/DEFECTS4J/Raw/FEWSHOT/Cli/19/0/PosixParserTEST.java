package org.apache.commons.cli;

import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption(Option.builder("a").argName("file").hasArg().build());
        options.addOption(Option.builder("b").argName("file").hasArg().build());

        PosixParser parser = new PosixParser();

        String[] arguments = {"-a", "file1", "-b", "file2"};
        String[] expectedTokens = {"-a", "file1", "-b", "file2"};

        assertArrayEquals(expectedTokens, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").argName("file").hasArg().build());
        options.addOption(Option.builder("b").argName("file").hasArg().build());

        PosixParser parser = new PosixParser();

        String[] arguments = {"-a", "file1", "-b", "file2", "nonOption"};

        String[] expectedTokens = {"-a", "file1", "-b", "file2", "nonOption"};

        assertArrayEquals(expectedTokens, parser.flatten(options, arguments, true));
    }

    @Test
    public void testFlatten_Empty() {
        Options options = new Options();

        PosixParser parser = new PosixParser();

        String[] arguments = {};

        String[] expectedTokens = {};

        assertArrayEquals(expectedTokens, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_NullOptions() {
        PosixParser parser = new PosixParser();

        String[] arguments = {"-a", "file1", "-b", "file2"};

        String[] expectedTokens = {"-a", "file1", "-b", "file2"};

        assertArrayEquals(expectedTokens, parser.flatten(null, arguments, false));
    }

    @Test
    public void testFlatten_NullArguments() {
        Options options = new Options();
        options.addOption(Option.builder("a").argName("file").hasArg().build());
        options.addOption(Option.builder("b").argName("file").hasArg().build());

        PosixParser parser = new PosixParser();

        String[] expectedTokens = {};

        assertArrayEquals(expectedTokens, parser.flatten(options, null, false));
    }

    @Test
    public void testFlatten_NullOptionsAndArguments() {
        PosixParser parser = new PosixParser();

        String[] expectedTokens = {};

        assertArrayEquals(expectedTokens, parser.flatten(null, null, false));
    }
}