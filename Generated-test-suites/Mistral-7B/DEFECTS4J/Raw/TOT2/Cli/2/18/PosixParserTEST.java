import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private Options options;
    private String[] arguments;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", 3, "Option C with argument");
    }

    @Test
    public void testFlatten_EmptyArguments_ReturnsEmptyArray() {
        String[] result = new PosixParser().flatten(options, new String[0], false);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlatten_SimpleOptions_ReturnsCorrectTokens() {
        arguments = new String[]{"-a", "-b"};
        String[] result = new PosixParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-a", "-b"}, result);
    }

    @Test
    public void testFlatten_OptionWithArgument_ReturnsCorrectTokens() {
        arguments = new String[]{"-c", "3"};
        String[] result = new PosixParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-c", "3"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption_ThrowsParseException() {
        arguments = new String[]{"-x", "3"};
        try {
            new PosixParser().flatten(options, arguments, false);
        } catch (ParseException e) {
            Assert.assertEquals("Unrecognized option: -x", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testFlatten_LongOption_ReturnsCorrectTokens() {
        arguments = new String[]{"--long-option"};
        String[] result = new PosixParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--long-option"}, result);
    }

    @Test
    public void testFlatten_LongOptionWithArgument_ReturnsCorrectTokens() {
        arguments = new String[]{"--long-option=value"};
        String[] result = new PosixParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--long-option", "value"}, result);
    }

    @Test
    public void testFlatten_StopAtNonOption_ReturnsRemainingArguments() {
        arguments = new String[]{"--", "arg1", "arg2"};
        String[] result = new PosixParser().flatten(options, arguments, true);
        Assert.assertArrayEquals(new String[]{"--", "arg1", "arg2"}, result);
    }
}