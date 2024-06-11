package org.junit.platform.commons.testing.generative.junit4;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.junit.Assert;
import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlattenMethod() {
        Parser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = new String[]{"-Dproperty=value", "--", "-option"};

        String[] expected = new String[]{"-D", "property", "value", "--", "-option"};
        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testNoOptions() {
        Parser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = new String[]{"", ""};

        String[] expected = new String[]{""};
        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testStopAtNonOption() {
        Parser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = new String[]{"-Dproperty=value", "--", "nonOption"};

        String[] expected = new String[]{"-Dproperty=value"};
        String[] actual = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testArgumentWithMultipleHyphens() {
        Parser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = new String[]{"--option"};

        String[] expected = new String[]{"--option"};
        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testArgumentWithSingleDash() {
        Parser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = new String[]{"-"};

        String[] expected = new String[]{"-"};
        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testArgumentStartsWithDoubleDash() {
        Parser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = new String[]{"--argument"};

        String[] expected = new String[]{"--argument"};
        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testLongOptionName() {
        Parser parser = new GnuParser();
        Options options = new Options();

        options.addOption("longOptionName", true, "l");

        String[] arguments = new String[]{"-l"};

        String[] expected = new String[]{"longOptionName"};
        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testOptionWithValue() {
        Parser parser = new GnuParser();
        Options options = new Options();

        options.addOption("optionWithValue", true, "o");

        String[] arguments = new String[]{"-o", "value"};

        String[] expected = new String[]{"optionWithValue", "value"};
        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testArgumentWithMultipleWords() {
        Parser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = new String[]{"multipleWords"};

        String[] expected = new String[]{"multipleWords"};
        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testRepeatedOption() {
        Parser parser = new GnuParser();
        Options options = new Options();

        options.addOption("repeatedOption", false, "r");

        String[] arguments = new String[]{"-r", "-r"};

        String[] expected = new String[]{"repeatedOption", "repeatedOption"};
        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }
}

// Test ends here