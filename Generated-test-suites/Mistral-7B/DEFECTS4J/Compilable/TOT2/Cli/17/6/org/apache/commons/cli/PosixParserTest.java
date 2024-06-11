package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        String[] arguments = {"--foo", "--bar=value", "-a", "-b", "-c", "-d=value", "-e"};
        Options options = new Options();
        options.addOption("foo", true, "A boolean option.");
        options.addOption("bar", true, "An option with an argument.");
        options.addOption("c", false, "An option without an argument.");
        options.addOption("d", true, "An option with an argument.");

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, false);

        String[] expected = {"--foo", "--bar=value", "-", "-a", "-c", "-d=value", "-e"};
        Assert.assertArrayEquals(expected, flattened);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithNonOption() throws ParseException {
        String[] arguments = {"arg1", "arg2"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        parser.flatten(options, arguments, true);
    }
}