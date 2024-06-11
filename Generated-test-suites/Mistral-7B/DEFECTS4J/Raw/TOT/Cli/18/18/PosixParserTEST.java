import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PosixParser
 */
public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        String[] args = {"--foo", "-o", "option1", "--bar=value", "-a", "value1", "-b", "value2", "--baz"};
        Options options = new Options();
        options.addRequiredOption("foo", true, "f");
        options.addOption("o", "opt", true, "o");
        options.addOption("b", "bar", true, "b");
        options.addOption("baz", false, null);

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo", "-o", "option1", "--bar=value", "-a", "value1", "-b", "value2", "--baz"}, result);

        // Test with stopAtNonOption
        args = new String[]{"--foo", "-o", "option1", "--", "value1", "-b", "value2"};
        result = parser.flatten(options, args, true);

        Assert.assertArrayEquals(new String[]{"--foo", "-o", "option1", "--", "value1", "-b", "value2"}, result);
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() throws ParseException {
        String[] args = {"--foo", "-z", "--bar=value"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("o", "opt", true, "o");
        options.addOption("b", "bar", true, "b");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);

        // The test should fail with a ParseException
    }
}