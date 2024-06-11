import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        parser = new PosixParser();
    }

    @Test
    public void testFlatten_NoOptions_EmptyArray() {
        String[] args = {};
        String[] expected = {};
        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_SingleArgument() {
        String[] args = {"arg1"};
        String[] expected = {"arg1"};
        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_SingleOption() {
        options.addOption("option1", false, null, "Option 1");
        String[] args = {"-option1"};
        String[] expected = {"option1"};
        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_OptionWithArgument() {
        options.addOption("option1", true, null, "Option 1 with argument");
        String[] args = {"-option1", "arg1"};
        String[] expected = {"option1", "arg1"};
        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        options.addOption("option1", false, null, "Option 1");
        options.addOption("option2", true, null, "Option 2 with argument");
        String[] args = {"-option1", "-option2", "arg2"};
        String[] expected = {"option1", "option2", "arg2"};
        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_LongOption() {
        options.addOption("longOption", true, null, "Long Option with argument");
        String[] args = {"--longOption", "arg1"};
        String[] expected = {"longOption", "arg1"};
        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_LongOptionWithArgument() {
        options.addOption("longOptionWithArg", true, null, "Long Option with argument");
        String[] args = {"--longOptionWithArg", "arg1"};
        String[] expected = {"longOptionWithArg", "arg1"};
        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_InvalidOptionWithArgument() {
        options.addOption("invalidOption", true, null, "Invalid Option with argument");
        String[] args = {"-invalidOption", "arg1"};
        parser.flatten(options, args, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_InvalidLongOptionWithArgument() {
        options.addOption("invalidOption", true, null, "Invalid Option with argument");
        String[] args = {"--invalidOption", "arg1"};
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        options.addOption("option1", false, null, "Option 1");
        String[] args = {"arg1", "--", "arg3"};
        String[] expected = {"arg1", "--", "arg3"};
        String[] actual = parser.flatten(options, args, true);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_EmptyTokenList() {
        String[] args = {};
        String[] expected = {};
        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testBurstToken_ValidOption() {
        options.addOption("option1", false, null, "Option 1");
        List<String> tokens = Arrays.asList("-option1");
        String[] expected = {"option1"};
        String[] actual = parser.burstToken(tokens.iterator().next(), false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testBurstToken_InvalidOption() {
        List<String> tokens = Arrays.asList("invalidOption");
        String[] expected = {};
        String[] actual = parser.burstToken(tokens.iterator().next(), false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testBurstToken_StopAtNonOption() {
        options.addOption("option1", false, null, "Option 1");
        List<String> tokens = Arrays.asList("-option1", "--", "nonOption");
        String[] expected = {"--", "nonOption"};
        String[] actual = parser.burstToken(tokens.iterator().next(), true);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testBurstToken_OptionWithArgument() {
        options.addOption("option1", true, null, "Option 1 with argument");
        List<String> tokens = Arrays.asList("-option1", "arg1");
        String[] expected = {"option1", "arg1"};
        String[] actual = parser.burstToken(tokens.iterator().next(), false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testBurstToken_LongOptionWithArgument() {
        options.addOption("longOptionWithArg", true, null, "Long Option with argument");
        List<String> tokens = Arrays.asList("--longOptionWithArg", "arg1");
        String[] expected = {"longOptionWithArg", "arg1"};
        String[] actual = parser.burstToken(tokens.iterator().next(), false);
        Assert.assertArrayEquals(expected, actual);
    }
}