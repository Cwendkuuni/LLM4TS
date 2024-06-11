package org.apache.commons.cli;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlattenNormalCase() throws ParseException {
        String[] arguments = {"-a", "1", "--option", "value", "argument"};
        Options options = new Options();
        GnuParser parser = new GnuParser();
        String[] flattened = parser.flatten(options, arguments, false);
        String[] expected = {"-a", "--option=value", "argument"};
        Assert.assertArrayEquals(expected, flattened);
    }

    @Test
    public void testFlattenDoubleHyphen() throws ParseException {
        String[] arguments = {"--", "argument1", "--option1", "argument2"};
        Options options = new Options();
        GnuParser parser = new GnuParser();
        String[] flattened = parser.flatten(options, arguments, false);
        String[] expected = {"--", "--option1=argument1", "argument2"};
        Assert.assertArrayEquals(expected, flattened);
    }

    @Test
    public void testFlattenLongOption() throws ParseException {
        String[] arguments = {"--longOption", "value"};
        Options options = new Options();
        GnuParser parser = new GnuParser();
        String[] flattened = parser.flatten(options, arguments, false);
        String[] expected = {"--longOption=value"};
        Assert.assertArrayEquals(expected, flattened);
    }

    @Test
    public void testFlattenSingleCharacterOption() throws ParseException {
        String[] arguments = {"-a", "--option", "-b"};
        Options options = new Options();
        GnuParser parser = new GnuParser();
        String[] flattened = parser.flatten(options, arguments, false);
        String[] expected = {"-a", "--option", "-b"};
        Assert.assertArrayEquals(expected, flattened);
    }

    @Test(expected = ParseException.class)
    public void testFlattenUnknownOption() throws ParseException {
        String[] arguments = {"--unknownOption", "value"};
        Options options = new Options();
        GnuParser parser = new GnuParser();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlattenNoOptions() throws ParseException {
        String[] arguments = {"argument"};
        Options options = new Options();
        GnuParser parser = new GnuParser();
        String[] flattened = parser.flatten(options, arguments, false);
        String[] expected = {"argument"};
        Assert.assertArrayEquals(expected, flattened);
    }

    @Test
    public void testFlattenStopAtNonOption() throws ParseException {
        String[] arguments = {"-a", "1", "--", "option", "value", "argument"};
        Options options = new Options();
        GnuParser parser = new GnuParser();
        String[] flattened = parser.flatten(options, arguments, true);
        String[] expected = {"-a", "1", "--option=value"};
        Assert.assertArrayEquals(expected, flattened);
    }
}