import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    @Test
    public void testFlatten_EmptyArgs() {
        String[] args = new String[0];
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlatten_SingleOption() {
        String[] args = {"--option"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", true, "Option description");

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option"}, result);
    }

    @Test
    public void testFlatten_OptionWithValue() {
        String[] args = {"--option", "value"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", true, "Option description");

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option", "value"}, result);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        String[] args = {"--option1", "--option2", "--option3"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", true, "Option description 1");
        options.addOption("option2", true, "Option description 2");
        options.addOption("option3", true, "Option description 3");

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option1", "--option2", "--option3"}, result);
    }

    @Test
    public void testFlatten_LongOption() {
        String[] args = {"--long-option"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOption", true, "Long option description");

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--long-option"}, result);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        String[] args = {"--long-option", "value"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOption", true, "Long option description");

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--long-option", "value"}, result);
    }

    @Test
    public void testFlatten_OptionWithArgument() {
        String[] args = {"--option", "arg"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", false, "Option description");
        options.addOption("option", true, "Option argument");

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option", "arg"}, result);
    }

    @Test
    public void testFlatten_OptionWithMultipleArguments() {
        String[] args = {"--option", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", false, "Option description");
        options.addOption("option", true, "Option multiple arguments");

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option", "arg1", "arg2"}, result);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        String[] args = {"-"};
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-"}, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] args = {"--option", "arg", "--non-option"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", false, "Option description");
        options.addOption("option", true, "Option description");

        try {
            parser.flatten(options, args, true);
            Assert.fail();
        } catch (AssertionError e) {
            // Expected exception
        }

        List expected = Arrays.asList("--option", "arg", "--non-option");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), parser.getTokens().toArray(new String[parser.getTokens().size()]));
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("opt1", true, "Option description 1");
        options.addOption("opt2", true, "Option description 2");
        options.addOption("opt3", true, "Option description 3");

        String[] args = {"-o1o2o3"};
        String[] result = parser.flatten(options, args, false);
        List expected = Arrays.asList("-o1", "-o2", "-o3");

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testBurstToken_LongOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOpt1", true, "Long option description 1");
        options.addOption("longOpt2", true, "Long option description 2");

        String[] args = {"--long-opt1-opt2"};
        String[] result = parser.flatten(options, args, false);
        List expected = Arrays.asList("--long-opt1", "--long-opt2");

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }
}