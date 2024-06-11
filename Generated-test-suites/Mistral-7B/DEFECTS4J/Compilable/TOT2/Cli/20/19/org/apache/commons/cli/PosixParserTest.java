package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        String[] args = {"--foo", "--bar=baz", "-x", "-y", "--", "-z"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        options.addOption("x", false, "x");
        options.addOption("z", false, "z");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo", "-", "--bar=baz", "-x", "-y", "--", "-z"}, parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testStopAtNonOption() throws ParseException {
        String[] args = {"--foo", "--bar=baz", "-x", "-", "-y", "--", "-z"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        options.addOption("x", false, "x");
        options.addOption("z", false, "z");
        options.addOption("noOption", false, "n");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, true);

        // This exception should be thrown
    }

    @Test
    public void testBursting() throws ParseException {
        String[] args = {"-foo-bar-baz"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-foo", "-bar", "-baz"}, parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() throws ParseException {
        String[] args = {"--invalid-option"};
        Options options = new Options();
        options.addOption("foo", true, "f");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);

        // This exception should be thrown
    }
}