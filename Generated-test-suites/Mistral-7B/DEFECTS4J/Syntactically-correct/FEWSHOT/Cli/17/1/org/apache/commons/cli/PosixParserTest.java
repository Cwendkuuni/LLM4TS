package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenEmpty() {
        Options options = new Options();
        String[] arguments = new String[0];
        PosixParser parser = new PosixParser();
        String[] expected = new String[0];
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSimple() {
        Options options = new Options();
        String[] arguments = {"foo"};
        PosixParser parser = new PosixParser();
        String[] expected = {"foo"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithOptions() {
        Options options = new Options();
        options.addOption("a", true, "Short option a");
        options.addOption("b", 1, "Short option b with argument");
        String[] arguments = {"-a", "--b", "value"};
        PosixParser parser = new PosixParser();
        String[] expected = {"-a", "--b", "value"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptions() {
        Options options = new Options();
        options.addOption("longOptionA", false, "Long option A with no argument");
        options.addOption("longOptionB", 1, "Long option B with argument");
        String[] arguments = {"--longOptionA", "--longOptionB=value"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--longOptionA", "--longOptionB=value"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithMixedOptions() {
        Options options = new Options();
        options.addOption("a", true, "Short option a");
        options.addOption("b", 1, "Short option b with argument");
        options.addOption("longOptionA", false, "Long option A with no argument");
        options.addOption("longOptionB", 1, "Long option B with argument");
        String[] arguments = {"-a", "--longOptionA", "--b=value", "-"};
        PosixParser parser = new PosixParser();
        String[] expected = {"-a", "--longOptionA", "--b=value"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithTrailingArguments() {
        Options options = new Options();
        options.addOption("a", true, "Short option a");
        options.addOption("b", 1, "Short option b with argument");
        String[] arguments = {"-a", "--b=value", "trailingA", "trailingB"};
        PosixParser parser = new PosixParser();
        String[] expected = {"-a", "--b=value", "trailingA", "trailingB"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongTrailingArguments() {
        Options options = new Options();
        options.addOption("a", true, "Short option a");
        options.addOption("b", 1, "Short option b with argument");
        String[] arguments = {"-a", "--b=value", "--long-option-with-no-argument", "--long-option-with-argument=value"};
        PosixParser parser = new PosixParser();
        String[] expected = {"-a", "--b=value", "--long-option-with-no-argument", "--long-option-with-argument=value"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Short option a");
        options.addOption("b", 1, "Short option b with argument");
        String[] arguments = {"-a", "--nonOption", "-b", "value"};
        PosixParser parser = new PosixParser();
        String[] expected = {"-a", "--nonOption"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }
}