package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        Options options = new Options();
        String[] arguments = {"--foo", "--bar=baz", "--baz", "-c", "arg1", "-d", "--", "-e", "arg2"};
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList(
                "--foo",
                "--bar",
                "baz",
                "-c",
                "--",
                "-e"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), flattened);
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        String[] arguments = {"--foo", "--bar=baz", "--baz", "-c", "arg1", "-d", "--", "-e", "arg2", "nonOption"};
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, true);

        List<String> expected = Arrays.asList(
                "--foo",
                "--bar",
                "baz",
                "--",
                "nonOption"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), flattened);
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() throws ParseException {
        Options options = new Options();
        String[] arguments = {"--invalidOption"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testInvalidOptionWithArg() throws ParseException {
        Options options = new Options();
        String[] arguments = {"--invalidOption=arg"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testLongOptionWithArg() throws ParseException {
        Options options = new Options();
        String[] arguments = {"--longOptionWithArg=value"};
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList(
                "--longOptionWithArg",
                "value"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), flattened);
    }

    @Test
    public void testLongOptionWithoutArg() throws ParseException {
        Options options = new Options();
        String[] arguments = {"--longOptionWithoutArg"};
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList(
                "--longOptionWithoutArg"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), flattened);
    }

    @Test
    public void testShortOptionWithArg() throws ParseException {
        Options options = new Options();
        options.addOption("c", "shortOptionWithArg", true, "Short option with arg.");
        String[] arguments = {"-c", "value"};
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList(
                "-c"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), Arrays.copyOfRange(flattened, 0, 1));

        List<String> args = Arrays.asList(Arrays.copyOfRange(flattened, 1, flattened.length));
        Assert.assertEquals("value", args.get(0));
    }

    @Test
    public void testShortOptionWithoutArg() throws ParseException {
        Options options = new Options();
        options.addOption("c", "shortOptionWithoutArg", false, "Short option without arg.");
        String[] arguments = {"-c"};
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList(
                "-c"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), flattened);
    }
}