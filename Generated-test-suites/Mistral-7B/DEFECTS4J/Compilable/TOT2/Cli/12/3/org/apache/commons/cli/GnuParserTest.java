package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.Assert;

public class GnuParserTest {

    @Test
    public void testFlatten_NormalCase() throws ParseException {
        String[] args = new String[]{"-Dproperty=value", "-option", "argument"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(new Options(), args, false);
        Assert.assertArrayEquals(new String[]{"-D", "property", "value", "-option", "argument"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_MissingOption() throws ParseException {
        String[] args = new String[]{"-invalidOption", "argument"};
        GnuParser parser = new GnuParser();
        // The test should throw a ParseException as the method does not find the option.
        parser.flatten(new Options(), args, false);
    }

    @Test
    public void testFlatten_DoubleDash() throws ParseException {
        String[] args = new String[]{"--", "argument"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(new Options(), args, false);
        Assert.assertArrayEquals(new String[]{"--"}, result);
    }
}