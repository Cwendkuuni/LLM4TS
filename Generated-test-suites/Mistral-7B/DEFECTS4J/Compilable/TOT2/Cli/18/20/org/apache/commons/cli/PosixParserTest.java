package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenWithEmptyArguments() {
        PosixParser parser = new PosixParser();
        String[] arguments = new String[0];
        Options options = new Options();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenWithSingleOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] arguments = new String[]{"-a"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-a"}, result);
    }

    @Test
    public void testFlattenWithMultipleOptions() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = new String[]{"-a", "-b"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-a", "-b"}, result);
    }

    @Test
    public void testFlattenWithLongOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("long-option", true, "Long Option");

        String[] arguments = new String[]{"--long-option"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--long-option"}, result);
    }

    @Test
    public void testFlattenWithOptionWithValue() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] arguments = new String[]{"-a", "value"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-a", "value"}, result);
    }

    @Test
    public void testFlattenWithOptionWithoutValue() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");

        String[] arguments = new String[]{"-a"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-a"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");

        String[] arguments = new String[]{"-z"};
        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidLongOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("long-option", true, "Long Option");

        String[] arguments = new String[]{"--invalid-long-option"};
        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidShortOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");

        String[] arguments = new String[]{"-z"};
        parser.flatten(options, arguments, false);
    }
}