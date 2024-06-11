import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class PosixParserTest {

    @Test
    public void testFlatten_EmptyArgs() {
        String[] args = {};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        List<String> expected = new ArrayList<>();

        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_SingleArg() {
        String[] args = {"arg"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        List<String> expected = new ArrayList<>(Arrays.asList("arg"));

        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_DoubleDash() {
        String[] args = {"--arg"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        List<String> expected = new ArrayList<>(Arrays.asList("--arg"));

        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        String[] args = {"-"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        List<String> expected = new ArrayList<>(Arrays.asList("-"));

        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_OptionWithNoArg() {
        String[] args = {"-o", "arg"};
        Options options = new Options();
        options.addOption("o", true, "optionWithNoArg");
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        List<String> expected = new ArrayList<>(Arrays.asList("-o", "arg"));

        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_OptionWithArg() {
        String[] args = {"-o", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("o", true, "optionWithArg");
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        List<String> expected = new ArrayList<>(Arrays.asList("-o", "arg1", "arg2"));

        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        String[] args = {"-i", "invalidOption"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] args = {"arg1", "-o", "arg2", "arg3"};
        Options options = new Options();
        options.addOption("o", true, "optionWithNoArg");
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, true);
        List<String> expected = new ArrayList<>(Arrays.asList("arg1", "-o", "arg2", "arg3"));

        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_LongOption() {
        String[] args = {"--longOption"};
        Options options = new Options();
        options.addOption("longOption", true, "longOption");
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        List<String> expected = new ArrayList<>(Arrays.asList("--longOption"));

        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_LongOptionWithArg() {
        String[] args = {"--longOption", "arg"};
        Options options = new Options();
        options.addOption("longOption", true, "longOptionWithArg");
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        List<String> expected = new ArrayList<>(Arrays.asList("--longOption", "arg"));

        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }
}