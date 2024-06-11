package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class GnuParserTest {

    @Test
    public void testFlatten_SingleOption_NoHyphen() {
        String[] arguments = {"-DmyOption"};
        Options options = new Options();
        String[] expected = {"-D", "myOption"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleOption_WithHyphen() {
        String[] arguments = {"-DmyOption"};
        Options options = new Options();
        String[] expected = {"-D", "myOption"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleArgument() {
        String[] arguments = {"myArgument"};
        Options options = new Options();
        String[] expected = {"myArgument"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_DashDash() {
        String[] arguments = {"--"};
        Options options = new Options();
        String[] expected = {"--"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleOption_WithValue() throws ParseException {
        String[] arguments = {"-DmyOption=value"};
        Options options = new Options();
        String[] expected = {"-D", "myOption", "value"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleOptions_NoValue() {
        String[] arguments = {"-DmyOption1", "-DmyOption2"};
        Options options = new Options();
        String[] expected = {"-D", "myOption1", "-D", "myOption2"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleOptions_WithValue() throws ParseException {
        String[] arguments = {"-DmyOption1=value1", "-DmyOption2=value2"};
        Options options = new Options();
        String[] expected = {"-D", "myOption1", "value1", "-D", "myOption2", "value2"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_UnknownOption() throws ParseException {
        String[] arguments = {"-unknowOption"};
        Options options = new Options();

        GnuParser parser = new GnuParser();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] arguments = {"-DmyOption", "nonOption"};
        Options options = new Options();
        String[] expected = {"-D", "myOption"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, result);
    }
}