import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class PosixParserTest {

    private Options options;
    private PosixParser parser;

    @Before
    public void setup() {
        options = new Options();
        parser = new PosixParser();

        options.addOption("a", false, "Option without argument");
        options.addOption("b", true, "Option with argument");
        options.addOption("c", true, "Option with multiple arguments");
    }

    @Test
    public void testFlattenOptionWithoutArgument() {
        String[] args = {"-a"};

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(args, result);
    }

    @Test
    public void testFlattenOptionWithArgument() {
        String[] args = {"-b", "value"};

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(args, result);
    }

    @Test
    public void testFlattenOptionWithMultipleArguments() {
        String[] args = {"-c", "value1", "value2"};

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(args, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] args = {"-a", "non-option"};

        String[] result = parser.flatten(options, args, true);

        String[] expected = {"-a", "non-option"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSpecialToken() {
        String[] args = {"--special"};

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(args, result);
    }

    @Test
    public void testFlattenSingleHyphen() {
        String[] args = {"-"};

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(args, result);
    }

    @Test
    public void testFlattenInvalidOption() {
        String[] args = {"-d"};

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(args, result);
    }

    @Test
    public void testFlattenInvalidOptionStopAtNonOption() {
        String[] args = {"-d", "non-option"};

        String[] result = parser.flatten(options, args, true);

        String[] expected = {"-d", "non-option"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenBurstToken() {
        String[] args = {"-abc"};

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(args, result);
    }

    @Test
    public void testFlattenBurstTokenStopAtNonOption() {
        String[] args = {"-abc", "non-option"};

        String[] result = parser.flatten(options, args, true);

        String[] expected = {"-abc", "non-option"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenRemainingTokens() {
        String[] args = {"-a", "arg1", "arg2"};

        String[] result = parser.flatten(options, args, false);

        String[] expected = {"-a", "arg1", "arg2"};
        Assert.assertArrayEquals(expected, result);
    }
}