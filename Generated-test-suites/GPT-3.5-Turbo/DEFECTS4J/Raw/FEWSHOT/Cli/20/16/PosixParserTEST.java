package org.apache.commons.cli;

import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten_StopAtNonOptionFalse() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").desc("desc").build());

        String[] arguments = {"-a", "arg1", "-b", "arg2", "nonOption"};
        String[] expected = {"-a", "arg1", "-b", "arg2", "nonOption"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOptionTrue() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").desc("desc").build());

        String[] arguments = {"-a", "arg1", "-b", "arg2", "nonOption"};
        String[] expected = {"-a", "arg1", "-b", "arg2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_NoOptions() {
        Options options = new Options();

        String[] arguments = {"arg1", "arg2"};
        String[] expected = {"arg1", "arg2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").desc("desc").build());

        String[] arguments = {"--b=value"};
        String[] expected = {"--b", "value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithoutValue() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").desc("desc").build());

        String[] arguments = {"--a"};
        String[] expected = {"--a"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").desc("desc").build());

        String[] arguments = {"-"};
        String[] expected = {"-"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_InvalidOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").desc("desc").build());

        String[] arguments = {"-x"};
        String[] expected = {"-x"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }
}