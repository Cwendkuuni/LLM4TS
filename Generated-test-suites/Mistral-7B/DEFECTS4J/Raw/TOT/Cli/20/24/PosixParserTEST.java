import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for PosixParser class.
 */
public class PosixParserTest {

    @Test
    public void testSimpleOptions() throws ParseException {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");

        String[] args = new String[]{"-a", "--b"};
        String[] expectedArgs = new String[]{"-a", "--b"};
        String[] resultArgs = parser.parse(options, args, false);
        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test
    public void testLongOptions() throws ParseException {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOptionA", true, "Long option A");
        options.addOption("longOptionB", false, "Long option B");

        String[] args = new String[]{"--longOptionA", "--longOptionB"};
        String[] expectedArgs = new String[]{"--longOptionA", "--longOptionB"};
        String[] resultArgs = parser.parse(options, args, false);
        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test
    public void testOptionWithValue() throws ParseException {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("optionA", true, "Option A");
        options.addOption("optionB", true, "Option B");

        String[] args = new String[]{"-optionA", "ValueA", "-optionB", "ValueB"};
        String[] expectedArgs = new String[]{"-optionA", "ValueA", "-optionB", "ValueB"};
        String[] resultArgs = parser.parse(options, args, false);
        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test
    public void testMultipleOptionsWithValues() throws ParseException {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("optionA", true, "Option A");
        options.addOption("optionB", true, "Option B");

        String[] args = new String[]{"-optionA", "ValueA1", "-optionA", "ValueA2", "-optionB", "ValueB1", "-optionB", "ValueB2"};
        String[] expectedArgs = new String[]{"-optionA", "ValueA1", "-optionA", "ValueA2", "-optionB", "ValueB1", "-optionB", "ValueB2"};
        String[] resultArgs = parser.parse(options, args, false);
        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() throws ParseException {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = new String[]{"-invalidOption"};
        parser.parse(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testInvalidOptionWithValue() throws ParseException {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = new String[]{"-aValue"};
        parser.parse(options, args, false);
    }

    @Test
    public void testStopAtNonOption() throws ParseException {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = new String[]{"-a", "arg1", "nonOption", "arg2"};
        String[] expectedArgs = new String[]{"-a", "arg1"};
        String[] resultArgs = parser.parse(options, args, true);
        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }
}