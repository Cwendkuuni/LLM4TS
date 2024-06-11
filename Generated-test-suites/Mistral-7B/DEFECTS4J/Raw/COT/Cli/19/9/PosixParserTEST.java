package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenEmptyArgs() {
        PosixParser parser = new PosixParser();
        String[] args = new String[0];
        Options options = new Options();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenSingleOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--option"};
        Options options = new Options();
        options.addOption("option", true, "Option description");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option"}, result);
    }

    @Test
    public void testFlattenSingleOptionWithValue() {
        PosixParser parser = new PosixParser();
        String[] args = {"--option", "value"};
        Options options = new Options();
        options.addOption("option", true, "Option description");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option", "value"}, result);
    }

    @Test
    public void testFlattenMultipleOptions() {
        PosixParser parser = new PosixParser();
        String[] args = {"--option1", "--option2", "--option3", "value"};
        Options options = new Options();
        options.addOption("option1", true, "Option1 description");
        options.addOption("option2", false, "Option2 description");
        options.addOption("option3", true, "Option3 description");
        String[] result = parser.flatten(options, args, false);
        List expected = new ArrayList();
        expected.add("--option1");
        expected.add("--option2");
        expected.add("--option3");
        expected.add("value");
        Assert.assertArrayEquals((String[]) expected.toArray(new String[expected.size()]), result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--invalidOption"};
        Options options = new Options();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--option", "-f", "file.txt"};
        Options options = new Options();
        options.addOption("option", true, "Option description");
        options.addOption("f", true, "File description");
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"--option", "-f", "file.txt"}, result);
    }

    @Test
    public void testFlattenLongOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--long-option"};
        Options options = new Options();
        options.addOption("longOption", true, "Long option description");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--long-option"}, result);
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        PosixParser parser = new PosixParser();
        String[] args = {"--long-option=value"};
        Options options = new Options();
        options.addOption("longOption", true, "Long option description");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--long-option=value"}, result);
    }

    @Test
    public void testFlattenSingleHyphen() {
        PosixParser parser = new PosixParser();
        String[] args = {"-"};
        Options options = new Options();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-"}, result);
    }

    @Test
    public void testFlattenTwoHyphens() {
        PosixParser parser = new PosixParser();
        String[] args = {"--"};
        Options options = new Options();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidLongOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--invalid-long-option"};
        Options options = new Options();
        parser.flatten(options, args, false);
    }
}