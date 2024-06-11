package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenEmpty() {
        Options options = new Options();
        String[] arguments = {};
        PosixParser parser = new PosixParser();

        String[] expected = {};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSimple() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"-a"};
        PosixParser parser = new PosixParser();

        String[] expected = {"-a"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithOptionAndArgument() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"-a", "value"};
        PosixParser parser = new PosixParser();

        String[] expected = {"-a", "value"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithMultipleOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        String[] arguments = {"-a", "-b"};
        PosixParser parser = new PosixParser();

        String[] expected = {"-a", "-b"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOption() {
        Options options = new Options();
        options.addOption("longOption", true, "Long option");
        String[] arguments = {"--longOption"};
        PosixParser parser = new PosixParser();

        String[] expected = {"--longOption"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptionAndArgument() {
        Options options = new Options();
        options.addOption("longOption", true, "Long option");
        String[] arguments = {"--longOption=value"};
        PosixParser parser = new PosixParser();

        String[] expected = {"--longOption", "value"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithDoubleHyphen() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"--", "-a"};
        PosixParser parser = new PosixParser();

        String[] expected = {"--", "-a"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithDoubleDashAndOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"--", "-a"};
        PosixParser parser = new PosixParser();

        String[] expected = {"--", "-a"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithDoubleDashAndArgument() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"--", "value"};
        PosixParser parser = new PosixParser();

        String[] expected = {"--", "value"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithDoubleDashAndMultipleArguments() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"--", "arg1", "arg2"};
        PosixParser parser = new PosixParser();

        String[] expected = {"--", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptionAndValueContainingHyphen() {
        Options options = new Options();
        options.addOption("longOption", true, "Long option");
        String[] arguments = {"--longOption=option-value"};
        PosixParser parser = new PosixParser();

        String[] expected = {"--longOption", "option-value"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptionAndMultipleArguments() {
        Options options = new Options();
        options.addOption("longOption", false, "Long option");
        options.addOption("longOptionValue", true, "Long option value");
        String[] arguments = {"--longOption", "--longOptionValue=value1", "--longOptionValue=value2"};
        PosixParser parser = new PosixParser();

        String[] expected = {"--longOption", "--longOptionValue", "value1", "value2"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithArgumentAfterLongOption() {
        Options options = new Options();
        options.addOption("longOption", false, "Long option");
        String[] arguments = {"--longOption", "arg"};
        PosixParser parser = new PosixParser();

        String[] expected = {"--longOption", "arg"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithArgAfterOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"-a", "arg"};
        PosixParser parser = new PosixParser();

        String[] expected = {"-a", "arg"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithArgAndLongOption() {
        Options options = new Options();
        options.addOption("longOption", false, "Long option");
        options.addOption("a", true, "Option A");
        String[] arguments = {"--longOption", "-a", "arg"};
        PosixParser parser = new PosixParser();

        String[] expected = {"--longOption", "-a", "arg"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithOptionAndLongOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("longOption", false, "Long option");
        String[] arguments = {"-a", "--longOption"};
        PosixParser parser = new PosixParser();

        String[] expected = {"-a", "--longOption"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithOptionAndMultipleLongOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("longOption1", false, "Long option 1");
        options.addOption("longOption2", false, "Long option 2");
        String[] arguments = {"-a", "--longOption1", "--longOption2"};
        PosixParser parser = new PosixParser();

        String[] expected = {"-a", "--longOption1", "--longOption2"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithMultipleOptionsAndArguments() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        String[] arguments = {"-a", "argA", "-b", "argB", "-c", "argC"};
        PosixParser parser = new PosixParser();

        String[] expected = {"-a", "argA", "-b", "argB", "-c", "argC"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"--", "-a", "arg"};
        PosixParser parser = new PosixParser();

        String[] expected = {"--", "-a"};
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithInvalidToken() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"-invalidToken"};
        PosixParser parser = new PosixParser();

        String[] expected = {"-invalidToken"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }
}