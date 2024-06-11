package org.apache.commons.cli.test;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class GnuParserTest {

    @Test
    public void testFlatten() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{"-Dprop=val1", "--", "-opt1", "-opt2=val2", "arg"};
        GnuParser parser = new GnuParser();
        String[] result = parser.parse(options, arguments, true);

        List<String> expected = new ArrayList<>();
        expected.add("-D");
        expected.add("prop");
        expected.add("val1");
        expected.add("--");
        expected.add("-opt1");
        expected.add("-opt2");
        expected.add("val2");
        expected.add("arg");

        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidArgument() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{"invalid"};
        GnuParser parser = new GnuParser();
        parser.parse(options, arguments, true);
    }

    @Test
    public void testFlattenWithLongOption() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{"--longOption"};
        GnuParser parser = new GnuParser();
        String[] result = parser.parse(options, arguments, true);

        List<String> expected = new ArrayList<>();
        expected.add("--longOption");

        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlattenWithDashOnlyOption() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{"-"};
        GnuParser parser = new GnuParser();
        String[] result = parser.parse(options, arguments, true);

        List<String> expected = new ArrayList<>();
        expected.add("-");

        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlattenWithDashAndOption() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{"-opt"};
        GnuParser parser = new GnuParser();
        String[] result = parser.parse(options, arguments, true);

        List<String> expected = new ArrayList<>();
        expected.add("-opt");

        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlattenWithNoOption() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{"arg"};
        GnuParser parser = new GnuParser();
        String[] result = parser.parse(options, arguments, true);

        List<String> expected = new ArrayList<>();
        expected.add("arg");

        Assert.assertArrayEquals(expected.toArray(), result);
    }
}