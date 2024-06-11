package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test cases for {@link PosixParser} class.
 */
public class PosixParserTest {

    @Test
    public void testSimpleOptions() throws ParseException {
        String[] args = {"--option1", "--option2=value2", "-o", "option3"};

        Options options = new Options();
        options.addOption("o", "optionOption", true, "Option option");
        options.addOption("option1", true, "shortOption1", "Option 1");
        options.addOption("option2", true, "shortOption2", "Option 2");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"--option1", "", "--option2=value2", "--optionOption=option3"}, parsedArgs);
    }

    @Test
    public void testLongOptions() throws ParseException {
        String[] args = {"--longOption1", "--longOption2=value2", "--longOption3", "--longOption4=value4"};

        Options options = new Options();
        options.addOption("longOption1", true, "longOpt1", "Long option 1");
        options.addOption("longOption2", true, "longOpt2", "Long option 2");
        options.addOption("longOption3", true, "longOpt3", "Long option 3");
        options.addOption("longOption4", true, "longOpt4", "Long option 4");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"--longOption1", "", "--longOption2=value2", "--longOption3", "--longOption4=value4"}, parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() throws ParseException {
        String[] args = {"--invalidOption", "value"};

        Options options = new Options();
        options.addOption("option1", true, "shortOption1", "Option 1");

        PosixParser parser = new PosixParser();
        parser.parse(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testInvalidOptionWithValue() throws ParseException {
        String[] args = {"--invalidOption=value"};

        Options options = new Options();
        options.addOption("option1", true, "shortOption1", "Option 1");

        PosixParser parser = new PosixParser();
        parser.parse(options, args, false);
    }

    @Test
    public void testBursting() throws ParseException {
        String[] args = {"--long-option-with-value"};

        Options options = new Options();
        options.addOption("longOptionWithValue", true, "longOptWithValue", "Long option with value");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"--long-option-with-value"}, parsedArgs);
    }

    @Test
    public void testBurstingWithMultipleArgs() throws ParseException {
        String[] args = {"--long-option-with-multiple-args=arg1 arg2 arg3"};

        Options options = new Options();
        options.addOption("longOptionWithMultipleArgs", true, "longOptWithMultipleArgs", "Long option with multiple args");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"--long-option-with-multiple-args=arg1", "arg2", "arg3"}, parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testBurstingWithInvalidOption() throws ParseException {
        String[] args = {"--invalidOption=arg"};

        Options options = new Options();
        options.addOption("longOptionWithArgs", true, "longOptWithArgs", "Long option with args");

        PosixParser parser = new PosixParser();
        parser.parse(options, args, false);
    }
}