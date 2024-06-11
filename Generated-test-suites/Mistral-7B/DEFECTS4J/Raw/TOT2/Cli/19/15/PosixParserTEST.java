import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        String[] args = {"java", "--foo=bar", "--qux", "-a", "-b", "-c"};
        Options options = new Options();
        options.addOption("foo", true, "An option with an argument");
        options.addOption("qux", ValueConstants.NO_VALUE, "An option without an argument");
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("c", false, "Option C");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"java", "--foo", "bar", "--qux", "-a", "-b", "-c"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        String[] args = {"java", "--longOption=longValue"};
        Options options = new Options();
        options.addOption("longOption", true, "A long option with an argument");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"java", "--longOption", "longValue"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOptionWithoutValue() {
        String[] args = {"java", "--longOption"};
        Options options = new Options();
        options.addOption("longOption", false, "A long option without an argument");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"java", "--longOption"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        String[] args = {"java", "--invalidOption"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.fail(); // This test should fail with a ParseException
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] args = {"java", "--foo", "--qux", "-a", "-b", "-c", "unknownArg"};
        Options options = new Options();
        options.addOption("foo", true, "An option with an argument");
        options.addOption("qux", ValueConstants.NO_VALUE, "An option without an argument");
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("c", false, "Option C");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        String[] expected = {"java", "--foo", "bar", "--qux", "-a", "-b", "-c"};
        Assert.assertArrayEquals(expected, Arrays.copyOf(result, result.length - 1));

        Assert.assertEquals("unknownArg", result[result.length - 1]);
    }

    @Test
    public void testFlattenBurstOption() {
        String[] args = {"java", "-f", "--optionA", "--optionB=value", "value2"};
        Options options = new Options();
        options.addOption("optionA", false, "Option A");
        options.addOption("optionB", true, "Option B with an argument");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"java", "-f", "-optionA", "-optionB", "value", "value2"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testBurstInvalidOption() {
        String[] args = {"java", "-f", "invalidOption", "value"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.fail(); // This test should fail with a ParseException
    }

    @Test
    public void testBurstNoOption() {
        String[] args = {"java", "-f", "invalidOption", "value"};
        Options options = new Options();
        options.addOption("option", false, "Option");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertEquals("--invalidOption=value", Arrays.toString(result));
    }

    @Test
    public void testBurstOptionWithValue() {
        String[] args = {"java", "-f", "optionA", "valueA", "valueB"};
        Options options = new Options();
        options.addOption("optionA", true, "Option A with an argument");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"java", "-f", "-optionA", "valueA", "valueB"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstOptionWithValues() {
        String[] args = {"java", "-f", "optionA", "valueA1", "valueA2"};
        Options options = new Options();
        options.addOption("optionA", ValueConstants.UNLIMITED_VALUES, "Option A with multiple values");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"java", "-f", "-optionA", "valueA1", "valueA2"};
        Assert.assertArrayEquals(expected, result);
    }
}