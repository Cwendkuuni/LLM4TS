package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenSimpleOptions() throws ParseException {
        Options options = new Options();
        String[] args = {"--option1", "--option2"};
        PosixParser parser = new PosixParser();
        String[] tokens = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option1", "--option2"}, tokens);
    }

    @Test
    public void testFlattenLongOption() throws ParseException {
        Options options = new Options();
        options.addOption("longOption", true, "description of long option");
        String[] args = {"--longOption"};
        PosixParser parser = new PosixParser();
        String[] tokens = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--longOption"}, tokens);
    }

    @Test
    public void testFlattenOptionWithArgument() throws ParseException {
        Options options = new Options();
        options.addOption("option", true, "option with argument");
        String[] args = {"--option", "arg"};
        PosixParser parser = new PosixParser();
        String[] tokens = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option", "arg"}, tokens);
    }

    @Test
    public void testFlattenMultipleOptionsWithArguments() throws ParseException {
        Options options = new Options();
        options.addOption("option1", true, "option1 with argument");
        options.addOption("option2", true, "option2 with argument");
        String[] args = {"--option1", "arg1", "--option2", "arg2"};
        PosixParser parser = new PosixParser();
        String[] tokens = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option1", "arg1", "--option2", "arg2"}, tokens);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() throws ParseException {
        Options options = new Options();
        String[] args = {"--invalidOption"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        String[] args = {"--option", "value", "--invalidOption"};
        PosixParser parser = new PosixParser();
        String[] tokens = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"--option", "value"}, tokens);
    }

    @Test
    public void testFlattenWithBursting() throws ParseException {
        Options options = new Options();
        options.addOption("longOption", true, "description of long option");
        String[] args = {"--long-option"};
        PosixParser parser = new PosixParser();
        String[] tokens = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--long-option"}, tokens);
    }
}