import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten_EmptyArguments() {
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] arguments = {};

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlatten_SingleArgument() {
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] arguments = {"arg1"};

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"arg1"}, result);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] arguments = {"-"};

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-"}, result);
    }

    @Test
    public void testFlatten_DoubleHyphen() {
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] arguments = {"--"};

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--"}, result);
    }

    @Test
    public void testFlatten_OptionWithNoArgument() {
        Options options = new Options();
        options.addOption("option1", false, "o", "opt1");
        Parser parser = new PosixParser();
        String[] arguments = {"-o"};

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"option1"}, result);
    }

    @Test
    public void testFlatten_OptionWithRequiredArgument() {
        Options options = new Options();
        options.addOption("option1", true, "o", "opt1");
        Parser parser = new PosixParser();
        String[] arguments = {"-o", "arg1"};

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"option1", "arg1"}, result);
    }

    @Test
    public void testFlatten_OptionWithOptionalArgument() {
        Options options = new Options();
        options.addOption("option1", true, "o", "opt1");
        Parser parser = new PosixParser();
        String[] arguments = {"-o", "arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"option1", "arg1", "arg2"}, result);
    }

    @Test
    public void testFlatten_OptionWithLongArgument() {
        Options options = new Options();
        options.addOption("longOption1", true, "l", "longOpt1");
        Parser parser = new PosixParser();
        String[] arguments = {"-l", "longArgument"};

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"longOption1", "longArgument"}, result);
    }

    @Test
    public void testFlatten_OptionWithShortAndLongName() {
        Options options = new Options();
        options.addOption("option1", true, "o", "opt1");
        options.addOption("option1", true, "l", "longOpt1");
        Parser parser = new PosixParser();
        String[] arguments = {"-o", "--longOpt1", "arg1"};

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"option1", "arg1"}, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_InvalidOption() {
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] arguments = {"-x"};

        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        options.addOption("option1", true, "o", "opt1");
        Parser parser = new PosixParser();
        String[] arguments = {"-o", "arg1", "nonOption"};

        String[] result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(new String[]{"option1", "arg1"}, result);
    }

    @Test
    public void testFlatten_MultipleOptionsAndArguments() {
        Options options = new Options();
        options.addOption("option1", true, "o", "opt1");
        options.addOption("option2", true, "i", "opt2");
        Parser parser = new PosixParser();
        String[] arguments = {"-o", "arg1", "-i", "arg2"};

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"option1", "arg1", "option2", "arg2"}, result);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        Options options = new Options();
        options.addOption("longOption1", true, null, "value1");
        Parser parser = new PosixParser();
        String[] arguments = {"--longOption1", "value1"};

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"longOption1", "value1"}, result);
    }

    @Test
    public void testFlatten_LongOptionWithValueAndArguments() {
        Options options = new Options();
        options.addOption("longOption1", true, null, "value1");
        options.addOption("option2", true, "i", "opt2");
        Parser parser = new PosixParser();
        String[] arguments = {"--longOption1", "value1", "-i", "arg2"};

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"longOption1", "value1", "option2", "arg2"}, result);
    }
}