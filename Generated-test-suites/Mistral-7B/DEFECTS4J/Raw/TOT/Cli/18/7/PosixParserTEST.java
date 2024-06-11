package org.apache.commons.cli.test;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        String[] args = new String[]{"--foo", "--bar=baz", "--baz=qux", "-abc", "-d", "-e=f", "1", "2", "3"};

        Options options = new Options();
        options.addOption("foo", true, "Help for foo.");
        options.addOption("bar", true, "Help for bar.");
        options.addOption("baz", true, "Help for baz.");

        DefaultParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo", "--bar", "baz", "-abc", "-d", "--baz", "qux", "1", "2", "3"}, parsedArgs);

        // test with stopAtNonOption = true
        args = new String[]{"--foo", "--bar=baz", "--baz=qux", "-abc", "-d", "-e=f", "1", "2", "3", "nonOption"};
        parsedArgs = parser.parse(options, args, true);

        Assert.assertArrayEquals(new String[]{"--foo", "--bar", "baz", "-abc", "-d", "--baz", "qux", "1", "2", "3", "nonOption"}, parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() throws ParseException {
        String[] args = new String[]{"--invalid"};

        Options options = new Options();

        DefaultParser parser = new PosixParser();
        parser.parse(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testDuplicateOption() throws ParseException {
        String[] args = new String[]{"--foo", "--foo"};

        Options options = new Options();
        options.addOption("foo", true, "Help for foo.");

        DefaultParser parser = new PosixParser();
        parser.parse(options, args, false);
    }

    @Test
    public void testLongOptionWithValue() throws ParseException {
        String[] args = new String[]{"--longOption=value"};

        Options options = new Options();
        options.addOption("longOption", true, "Help for longOption.");

        DefaultParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"--longOption"}, parsedArgs);
        Assert.assertEquals("value", options.getOption("longOption").getValue());
    }

    @Test
    public void testShortOptionWithValue() throws ParseException {
        String[] args = new String[]{"-o", "value"};

        Options options = new Options();
        options.addOption("o", true, "Help for option o.");

        DefaultParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"-o"}, parsedArgs);
        Assert.assertEquals("value", options.getOption("o").getValue());
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws ParseException {
        String[] args = new String[]{"--missingOption"};

        Options options = new Options();

        DefaultParser parser = new PosixParser();
        parser.parse(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testEmptyOption() throws ParseException {
        String[] args = new String[]{"--"};

        Options options = new Options();

        DefaultParser parser = new PosixParser();
        parser.parse(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testEmptyArguments() throws ParseException {
        String[] args = new String[]{"--foo="};

        Options options = new Options();
        options.addOption("foo", true, "Help for foo.");

        DefaultParser parser = new PosixParser();
        parser.parse(options, args, false);
    }

    @Test
    public void testBursting() throws ParseException {
        String[] args = new String[]{"--abcd", "--efgh=ijkl"};

        Options options = new Options();
        options.addOption("abcd", true, "Help for abcd.");
        options.addOption("efgh", true, "Help for efgh.");

        DefaultParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"--abc", "d", "--efgh"}, parsedArgs);
    }
}