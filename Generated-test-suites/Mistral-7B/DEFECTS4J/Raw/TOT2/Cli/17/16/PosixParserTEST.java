import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

public class PosixParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setup() {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");
        options.addOption("c", 3, "Option C");
        parser = new PosixParser();
    }

    @Test
    public void testFlatten_OneArg() {
        String[] args = {"-a"};
        String[] expected = {"-a"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_TwoArgs() {
        String[] args = {"-a", "--foo=bar"};
        String[] expected = {"-a", "--foo", "bar"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        String[] args = {"-a", "--foo=bar", "-b", "-c", "--c=123"};
        String[] expected = {"-a", "--foo", "bar", "-b", "--c", "123"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        String[] args = {"--invalid"};
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] args = {"-a", "--foo=bar", "abc"};
        String[] expected = {"-a", "--foo", "bar", "abc"};
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleArguments() {
        String[] args = {"-a", "--foo=bar", "--foo=baz"};
        String[] expected = {"-a", "--foo", "bar", "--foo", "baz"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOption() {
        String[] args = {"--long-option"};
        String[] expected = {"--long-option"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithArgument() {
        String[] args = {"--long-option=value"};
        String[] expected = {"--long-option", "value"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_DuplicateOption() {
        options.addOption("a", true, "Option A");
        String[] args = {"-a", "-a"};
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_MixedLongAndShortOptions() {
        String[] args = {"--long-option", "-a"};
        String[] expected = {"--long-option", "-a"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }
}