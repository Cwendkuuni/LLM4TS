import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlattenEmptyArray() {
        String[] arguments = new String[0];
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenSimpleOption() {
        String[] arguments = {"--option"};
        options.addOption("option", true, "opt");
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--option"}, result);
    }

    @Test
    public void testFlattenOptionWithArg() {
        String[] arguments = {"--option", "arg"};
        options.addOption("option", true, "opt");
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--option", "arg"}, result);
    }

    @Test
    public void testFlattenMultipleOptions() {
        String[] arguments = {"--option1", "--option2"};
        options.addOption("option1", true, "opt1");
        options.addOption("option2", true, "opt2");
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--option1", "--option2"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        String[] arguments = {"--invalidOption"};
        options.addOption("option", true, "opt");
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] arguments = {"--option", "arg", "nonOption"};
        options.addOption("option", true, "opt");
        String[] result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(new String[]{"--option", "arg"}, result);
        Assert.assertEquals("nonOption", result[result.length - 1]);
    }
}