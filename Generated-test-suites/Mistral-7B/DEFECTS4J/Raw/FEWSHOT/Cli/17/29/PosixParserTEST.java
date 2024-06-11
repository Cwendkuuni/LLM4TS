package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten_Empty() {
        Options options = new Options();
        String[] arguments = new String[0];
        PosixParser parser = new PosixParser();

        String[] expected = {};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        Options options = new Options();
        String[] arguments = {"-"};
        PosixParser parser = new PosixParser();

        String[] expected = {"-"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleOption() {
        Options options = new Options();
        options.addOption("a", false, "a");
        String[] arguments = {"-a"};
        PosixParser parser = new PosixParser();

        String[] expected = {"-a"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionWithArg() {
        Options options = new Options();
        options.addOption("a", true, "a");
        String[] arguments = {"-a", "arg"};
        PosixParser parser = new PosixParser();

        String[] expected = {"-a", "arg"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionWithLongArg() {
        Options options = new Options();
        options.addOption("a", true, "a");
        String[] arguments = {"-a", "long argument"};
        PosixParser parser = new PosixParser();

        String[] expected = {"-a", "long argument"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOption() {
        Options options = new Options();
        options.addOption("longOption", false, null);
        String[] arguments = {"--longOption"};
        PosixParser parser = new PosixParser();

        String[] expected = {"--longOption"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithArg() {
        Options options = new Options();
        options.addOption("longOption", true, null);
        String[] arguments = {"--longOption", "arg"};
        PosixParser parser = new PosixParser();

        String[] expected = {"--longOption", "arg"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithTwoArgs() {
        Options options = new Options();
        options.addOption("longOption", true, null);
        String[] arguments = {"--longOption", "arg1", "arg2"};
        PosixParser parser = new PosixParser();

        String[] expected = {"--longOption", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionWithArgAndLongOption() {
        Options options = new Options();
        options.addOption("a", true, "a");
        options.addOption("longOption", false, null);
        String[] arguments = {"-a", "arg", "--longOption"};
        PosixParser parser = new PosixParser();

        String[] expected = {"-a", "arg", "--longOption"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"-a", "arg", "--longOption", "nonOption"};
        PosixParser parser = new PosixParser();

        String[] expected = {"-a", "arg"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOptionWithLongOption() {
        Options options = new Options();
        options.addOption("a", true, "a");
        options.addOption("longOption", false, null);
        String[] arguments = {"-a", "arg", "--longOption", "nonOption"};
        PosixParser parser = new PosixParser();

        String[] expected = {"-a", "arg"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithArgAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("longOption", true, null);
        String[] arguments = {"--longOption", "arg", "nonOption"};
        PosixParser parser = new PosixParser();

        String[] expected = {"--longOption", "arg"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_BurstToken() {
        Options options = new Options();
        options.addOption("a", false, "a");
        String[] arguments = {"-abcd"};
        PosixParser parser = new PosixParser();

        String[] expected = {"-a", "-b", "-c", "-d"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_BurstTokenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "a");
        String[] arguments = {"-abcd"};
        PosixParser parser = new PosixParser();

        String[] expected = {"-a"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_BurstTokenWithLongOption() {
        Options options = new Options();
        options.addOption("a", false, "a");
        options.addOption("longOption", false, null);
        String[] arguments = {"-a--longOption", "abcd"};
        PosixParser parser = new PosixParser();

        String[] expected = {"-a", "--longOption", "abcd"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_BurstTokenWithLongOptionAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "a");
        options.addOption("longOption", false, null);
        String[] arguments = {"-a--longOption", "abcd"};
        PosixParser parser = new PosixParser();

        String[] expected = {"-a"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

}