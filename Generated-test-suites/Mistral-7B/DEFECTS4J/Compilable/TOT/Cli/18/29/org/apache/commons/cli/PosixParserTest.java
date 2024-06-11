package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        Options options = new Options();
        String[] args = {"--foo", "-bar", "-baz=value"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--foo", "-bar", "-baz=value"}, parsedArgs);
    }

    @Test
    public void testFlattenWithNoArgOption() {
        Options options = new Options();
        options.addOption("b", false, "boolean option");
        String[] args = {"--foo", "-b"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--foo", "-b"}, parsedArgs);
    }

    @Test
    public void testFlattenWithArgOption() {
        Options options = new Options();
        options.addOption("a", true, "an option with an argument");
        String[] args = {"--foo", "-a", "arg"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--foo", "-a", "arg"}, parsedArgs);
    }

    @Test
    public void testFlattenWithMultipleArgOption() {
        Options options = new Options();
        options.addOption("a", true, "an option with multiple arguments");
        String[] args = {"--foo", "-a", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--foo", "-a", "arg1", "arg2"}, parsedArgs);
    }

    @Test
    public void testFlattenWithArgOptionAndNoArgOption() {
        Options options = new Options();
        options.addOption("a", true, "an option with an argument");
        options.addOption("b", false, "boolean option");
        String[] args = {"--foo", "-a", "arg1", "-b"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--foo", "-a", "arg1", "-b"}, parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() {
        Options options = new Options();
        String[] args = {"--foo", "-x", "--"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOptionAndArg() {
        Options options = new Options();
        String[] args = {"--foo", "-x=arg", "--"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOptionAndArgAndArg() {
        Options options = new Options();
        String[] args = {"--foo", "-x=arg1", "arg2", "--"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }
}