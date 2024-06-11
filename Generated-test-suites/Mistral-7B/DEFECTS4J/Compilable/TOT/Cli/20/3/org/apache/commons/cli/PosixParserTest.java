package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenWithSingleOption() throws ParseException {
        String[] args = {"--foo"};
        Options options = new Options();
        options.addOption("foo", true, "A test option");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo"}, result);
    }

    @Test
    public void testFlattenWithLongOptionValue() throws ParseException {
        String[] args = {"--foo=Testing123"};
        Options options = new Options();
        options.addOption("foo", true, "A test option");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo", "Testing123"}, result);
    }

    @Test
    public void testFlattenWithShortOption() throws ParseException {
        String[] args = {"-f"};
        Options options = new Options();
        options.addOption("f", true, "A test option");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-f"}, result);
    }

    @Test
    public void testFlattenWithMultipleShortOptions() throws ParseException {
        String[] args = {"-ff"};
        Options options = new Options();
        options.addOption("f", true, "A test option");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-f", "-f"}, result);
    }

    @Test
    public void testFlattenWithMultipleOptions() throws ParseException {
        String[] args = {"--foo", "--bar"};
        Options options = new Options();
        options.addOption("foo", true, "A test option for foo");
        options.addOption("bar", true, "A test option for bar");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo", "--bar"}, result);
    }

    @Test
    public void testFlattenWithMultipleOptionsAndValues() throws ParseException {
        String[] args = {"--foo=value1", "--bar=value2"};
        Options options = new Options();
        options.addOption("foo", true, "A test option for foo");
        options.addOption("bar", true, "A test option for bar");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo", "value1", "--bar", "value2"}, result);
    }

    @Test
    public void testFlattenWithLongOptionAndShortOption() throws ParseException {
        String[] args = {"--foo", "-f"};
        Options options = new Options();
        options.addOption("foo", true, "A test option for foo");
        options.addOption("f", true, "A test option for f");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo", "-f"}, result);
    }

    @Test
    public void testFlattenWithLongOptionAndLongOption() throws ParseException {
        String[] args = {"--foo=value1", "--bar=value2"};
        Options options = new Options();
        options.addOption("foo", true, "A test option for foo");
        options.addOption("bar", true, "A test option for bar");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo", "value1", "--bar", "value2"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() throws ParseException {
        String[] args = {"--baz"};
        Options options = new Options();
        options.addOption("foo", true, "A test option for foo");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenWithInputArg() throws ParseException {
        String[] args = {"input.txt"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"input.txt"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        String[] args = {"--foo", "input.txt"};
        Options options = new Options();
        options.addOption("foo", true, "A test option for foo");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        Assert.assertArrayEquals(new String[]{"--foo", "input.txt"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndInvalidOption() throws ParseException {
        String[] args = {"--baz", "input.txt"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        Assert.assertArrayEquals(new String[]{"--baz", "input.txt"}, result);
    }

    @Test
    public void testFlattenWithComplexArgument() throws ParseException {
        String[] args = {"--foo=input.txt --bar=output.txt --arg1=value1 --arg2=value2"};
        Options options = new Options();
        options.addOption("foo", true, "A test option for foo");
        options.addOption("bar", true, "A test option for bar");
        options.addOption("arg1", true, "A test option for arg1");
        options.addOption("arg2", true, "A test option for arg2");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo", "input.txt", "--bar", "output.txt", "--arg1", "value1", "--arg2", "value2"}, result);
    }
}