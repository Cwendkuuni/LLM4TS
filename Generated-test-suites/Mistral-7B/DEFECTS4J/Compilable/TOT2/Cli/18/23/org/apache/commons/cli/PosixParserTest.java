package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.PosixParser;

public class PosixParserTest {

    @Test
    public void testFlattenWithEmptyArguments() throws ParseException {
        PosixParser parser = new PosixParser();
        String[] args = {};
        Options options = new Options();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testFlattenWithSingleArgument() throws ParseException {
        PosixParser parser = new PosixParser();
        String[] args = {"arg1"};
        Options options = new Options();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"arg1"}, result);
    }

    @Test
    public void testFlattenWithSingleOption() throws ParseException {
        PosixParser parser = new PosixParser();
        String[] args = {"--option"};
        Options options = new Options();
        options.addOption("option", true, "Description of option");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option"}, result);
    }

    @Test
    public void testFlattenWithOptionAndArgument() throws ParseException {
        PosixParser parser = new PosixParser();
        String[] args = {"--option", "arg"};
        Options options = new Options();
        options.addOption("option", true, "Description of option");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option", "arg"}, result);
    }

    @Test
    public void testFlattenWithMultipleOptions() throws ParseException {
        PosixParser parser = new PosixParser();
        String[] args = {"--option1", "--option2"};
        Options options = new Options();
        options.addOption("option1", true, "Description of option1");
        options.addOption("option2", true, "Description of option2");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option1", "--option2"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() throws ParseException {
        PosixParser parser = new PosixParser();
        String[] args = {"--invalidOption"};
        Options options = new Options();
        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithLongArgument() throws ParseException {
        PosixParser parser = new PosixParser();
        String[] args = {"--longArgument", "This is a long argument"};
        Options options = new Options();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "--option", "arg2"};
        Options options = new Options();
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"arg1", "--option", "arg2"}, result);
    }
}