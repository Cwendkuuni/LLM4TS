import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten_EmptyArgs() {
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] args = {};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testFlatten_SingleArg() {
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] args = {"--arg"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--arg"}, result);
    }

    @Test
    public void testFlatten_SingleArg_WithValue() {
        Options options = new Options();
        Parser parser = new PosixParser();
        Option option = new Option("a", "arg", false, "a value");
        options.addOption(option);
        String[] args = {"--arg=a value"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--arg", "a value"}, result);
    }

    @Test
    public void testFlatten_MultipleArgs() {
        Options options = new Options();
        Parser parser = new PosixParser();
        Option option1 = new Option("a", "arg1", false);
        Option option2 = new Option("b", "arg2", true);
        options.addOption(option1);
        options.addOption(option2);
        String[] args = {"--arg1", "--arg2", "value1", "value2"};

        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList(
                "--arg1",
                "--arg2",
                "value1",
                "value2"
        );
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_LongArg() {
        Options options = new Options();
        Parser parser = new PosixParser();
        Option option = new Option("a", "arg", false, "a long argument value");
        options.addOption(option);
        String[] args = {"--arg=a long argument value"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--arg", "a long argument value"}, result);
    }

    @Test
    public void testFlatten_OptionWithValue() {
        Options options = new Options();
        Parser parser = new PosixParser();
        Option option = new Option("a", "arg", true, "a value");
        options.addOption(option);
        String[] args = {"--arg=value"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--arg", "value"}, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        Parser parser = new PosixParser();
        Option option = new Option("a", "arg", false);
        options.addOption(option);
        String[] args = {"--arg", "nonOption", "--arg", "value"};

        String[] result = parser.flatten(options, args, true);
        List<String> expected = Arrays.asList(
                "--arg",
                "nonOption",
                "--arg",
                "value"
        );
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] args = {"-"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-"}, result);
    }

    @Test
    public void testFlatten_DoubleHyphen() {
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] args = {"--"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--"}, result);
    }

    @Test
    public void testFlatten_DoubleDashWithValue() {
        Options options = new Options();
        Parser parser = new PosixParser();
        Option option = new Option("a", "arg", true);
        options.addOption(option);
        String[] args = {"--arg=value"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--arg", "value"}, result);
    }

    @Test
    public void testFlatten_LongOption() {
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] args = {"--long-option"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--long-option"}, result);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        Options options = new Options();
        Parser parser = new PosixParser();
        Option option = new Option("a", "long-option", true);
        options.addOption(option);
        String[] args = {"--long-option=value"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--long-option", "value"}, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_InvalidOption() {
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] args = {"--invalid-option"};

        parser.flatten(options, args, false);
    }
}