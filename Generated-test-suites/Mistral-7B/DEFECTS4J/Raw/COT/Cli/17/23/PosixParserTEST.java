import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten_EmptyArgs() {
        PosixParser parser = new PosixParser();
        String[] args = {};
        Options options = new Options();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testFlatten_SingleArgument() {
        PosixParser parser = new PosixParser();
        String[] args = {"arg1"};
        Options options = new Options();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"arg1"}, result);
    }

    @Test
    public void testFlatten_DoubleDash() {
        PosixParser parser = new PosixParser();
        String[] args = {"--"};
        Options options = new Options();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--"}, result);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        PosixParser parser = new PosixParser();
        String[] args = {"-"};
        Options options = new Options();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-"}, result);
    }

    @Test
    public void testFlatten_SingleOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-o"};
        Options options = new Options();
        options.addOption("o", "output", true, "output file");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-o"}, result);
    }

    @Test
    public void testFlatten_SingleOptionWithValue() {
        PosixParser parser = new PosixParser();
        String[] args = {"-o", "output.txt"};
        Options options = new Options();
        options.addOption("o", "output", true, "output file");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-o", "output.txt"}, result);
    }

    @Test
    public void testFlatten_OptionWithLongName() {
        PosixParser parser = new PosixParser();
        String[] args = {"--long-option"};
        Options options = new Options();
        options.addOption("longOption", "long option", false, "long option description");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--long-option"}, result);
    }

    @Test
    public void testFlatten_OptionWithLongNameAndValue() {
        PosixParser parser = new PosixParser();
        String[] args = {"--long-option", "value"};
        Options options = new Options();
        options.addOption("longOption", "long option", false, "long option description");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--long-option", "value"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_UnknownOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--unknown"};
        Options options = new Options();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "--", "arg3"};
        Options options = new Options();
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"arg1", "--", "arg3"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-ab"};
        Options options = new Options();
        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOptionWithValue() {
        PosixParser parser = new PosixParser();
        String[] args = {"-o=value", "arg1"};
        Options options = new Options();
        options.addOption("o", "output", true, "output file");
        parser.flatten(options, args, false);
    }
}