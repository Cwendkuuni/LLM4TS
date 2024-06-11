package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenSingleArgument() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"-a"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-a"}, result);
    }

    @Test
    public void testFlattenSingleArgumentWithValue() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"-a", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-a", "value"}, result);
    }

    @Test
    public void testFlattenMultipleArguments() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        String[] arguments = {"-a", "-b", "value1", "value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-a", "-b", "value1", "value2"}, result);
    }

    @Test
    public void testFlattenSingleHyphen() {
        Options options = new Options();
        String[] arguments = {"-"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-"}, result);
    }

    @Test
    public void testFlattenDoubleHyphen() {
        Options options = new Options();
        String[] arguments = {"--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--"}, result);
    }

    @Test
    public void testFlattenTwoCharacterOption() {
        Options options = new Options();
        options.addOption("ab", true, "Option AB");
        String[] arguments = {"-ab"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-ab"}, result);
    }

    @Test
    public void testFlattenTwoCharacterOptionWithValue() {
        Options options = new Options();
        options.addOption("ab", true, "Option AB");
        String[] arguments = {"-ab", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-ab", "value"}, result);
    }

    @Test
    public void testFlattenLongOption() {
        Options options = new Options();
        options.addOption("longOption", true, "Long option");
        String[] arguments = {"--longOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--longOption"}, result);
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        Options options = new Options();
        options.addOption("longOption", true, "Long option");
        String[] arguments = {"--longOption", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--longOption", "value"}, result);
    }

    @Test
    public void testFlattenGroupOption() {
        Options options = new Options();
        Option group = new Option("g", "group");
        group.addOption("a", true, "Option GA");
        group.addOption("b", true, "Option GB");
        options.addOption(group);
        String[] arguments = {"-g", "-g-a", "1", "-g-b", "2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-g", "-g-a", "1", "-g-b", "2"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"-a", "non-option"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"-a", "non-option"}, result);
    }

    @Test
    public void testFlattenWithInvalidOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"-invalidOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--invalidOption"}, result);
    }

    @Test
    public void testFlattenWithInvalidOptionAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"-invalidOption", "non-option"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"-invalidOption", "non-option"}, result);
    }
}