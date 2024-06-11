import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten_EmptyArguments_NoOptions() {
        String[] arguments = {};
        String[] expected = {};

        Assert.assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_EmptyArguments_WithOptions() {
        options.addOption("a", true, "Option A");
        String[] arguments = {};
        String[] expected = {"--a"};

        Assert.assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_SingleArgument_NoOptions() {
        String[] arguments = {"arg"};
        String[] expected = {"arg"};

        Assert.assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_SingleArgument_WithOptions() {
        options.addOption("a", true, "Option A");
        String[] arguments = {"arg"};
        String[] expected = {"arg"};

        Assert.assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_TwoArguments_NoOptions() {
        String[] arguments = {"arg1", "arg2"};
        String[] expected = {"arg1", "arg2"};

        Assert.assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_TwoArguments_WithOptions() {
        options.addOption("a", true, "Option A");
        String[] arguments = {"arg1", "arg2"};
        String[] expected = {"arg1", "--a", "arg2"};

        Assert.assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_SingleDash_NoOptions() {
        String[] arguments = {"-"};
        String[] expected = {"--"};

        Assert.assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_SingleDash_WithOptions() {
        options.addOption("a", true, "Option A");
        String[] arguments = {"-"};
        String[] expected = {"--"};

        Assert.assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_DoubleDash_NoOptions() {
        String[] arguments = {"--"};
        String[] expected = {"--"};

        Assert.assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_DoubleDashWithValue_NoOptions() {
        String[] arguments = {"--option", "value"};
        String[] expected = {"--option", "value"};

        Assert.assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_DoubleDashWithValue_WithOptions() {
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        String[] arguments = {"--option", "value"};
        String[] expected = {"--option", "value"};

        Assert.assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption_NoOptions() {
        String[] arguments = {"--invalidOption"};
        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption_WithOptions() {
        options.addOption("a", true, "Option A");
        String[] arguments = {"--invalidOption"};

        try {
            parser.flatten(options, arguments, false);
        } catch (ParseException e) {
            Assert.assertEquals("Unrecognized option: --invalidOption", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testFlatten_StopAtNonOption_SingleArgument() {
        String[] arguments = {"arg"};
        String[] expected = {"arg"};

        Assert.assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }

    @Test
    public void testFlatten_StopAtNonOption_TwoArguments() {
        String[] arguments = {"arg1", "arg2"};
        String[] expected = {"arg1", "arg2"};

        Assert.assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }

    @Test
    public void testFlatten_StopAtNonOption_SingleDash() {
        String[] arguments = {"-"};
        String[] expected = {};

        Assert.assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }

    @Test
    public void testFlatten_StopAtNonOption_DoubleDash() {
        String[] arguments = {"--"};
        String[] expected = {};

        Assert.assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }

    @Test
    public void testFlatten_StopAtNonOption_InvalidOption() {
        String[] arguments = {"--invalidOption"};
        String[] expected = {};

        Assert.assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }
}