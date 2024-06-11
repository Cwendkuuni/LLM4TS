import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

/**
 * Test class for GnuParser
 */
public class GnuParserTest {

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
     * with valid options and arguments
     * @throws ParseException
     */
    @Test
    public void testFlattenValidOptionsAndArguments() throws ParseException {
        String[] args = {"-Dproperty=value", "--longOption", "-shortOption", "argument"};
        Options options = new Options();
        options.addOption("D", "property", true, "property value");
        options.addOption("l", "longOption", false, "has long option");
        options.addOption("s", "shortOption", false, "has short option");

        List<String> expected = new ArrayList<>();
        expected.add("D");
        expected.add("property");
        expected.add("value");
        expected.add("--");
        expected.add("longOption");
        expected.add("shortOption");
        expected.add("argument");

        List<String> actual = new GnuParser().flatten(options, args, false);

        Assert.assertArrayEquals(expected.toArray(), actual);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
     * with an option that doesn't exist
     * @throws ParseException
     */
    @Test(expected = ParseException.class)
    public void testFlattenNonExistingOption() throws ParseException {
        String[] args = {"-nonexistingOption", "argument"};
        Options options = new Options();
        options.addOption("D", "property", true, "property value");

        new GnuParser().flatten(options, args, false);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
     * with a long option that doesn't require an argument
     * @throws ParseException
     */
    @Test
    public void testFlattenLongOptionNoArgument() throws ParseException {
        String[] args = {"--longOption", "argument"};
        Options options = new Options();
        options.addOption("l", "longOption", false, "has long option");

        List<String> expected = new ArrayList<>();
        expected.add("--");
        expected.add("longOption");
        expected.add("argument");

        List<String> actual = new GnuParser().flatten(options, args, false);

        Assert.assertArrayEquals(expected.toArray(), actual);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
     * with a long option that requires an argument
     * @throws ParseException
     */
    @Test
    public void testFlattenLongOptionWithArgument() throws ParseException {
        String[] args = {"--longOption=value", "argument"};
        Options options = new Options();
        options.addOption("l", "longOption", true, "long option with argument");

        List<String> expected = new ArrayList<>();
        expected.add("--");
        expected.add("longOption");
        expected.add("value");
        expected.add("argument");

        List<String> actual = new GnuParser().flatten(options, args, false);

        Assert.assertArrayEquals(expected.toArray(), actual);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
     * with a short option that doesn't require an argument
     * @throws ParseException
     */
    @Test
    public void testFlattenShortOptionNoArgument() throws ParseException {
        String[] args = {"-shortOption", "argument"};
        Options options = new Options();
        options.addOption("s", "shortOption", false, "short option without argument");

        List<String> expected = new ArrayList<>();
        expected.add("-");
        expected.add("s");
        expected.add("argument");

        List<String> actual = new GnuParser().flatten(options, args, false);

        Assert.assertArrayEquals(expected.toArray(), actual);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
     * with a short option that requires an argument
     * @throws ParseException
     */
    @Test
    public void testFlattenShortOptionWithArgument() throws ParseException {
        String[] args = {"-shortOption=value", "argument"};
        Options options = new Options();
        options.addOption("s", "shortOption", true, "short option with argument");

        List<String> expected = new ArrayList<>();
        expected.add("-");
        expected.add("s");
        expected.add("value");
        expected.add("argument");

        List<String> actual = new GnuParser().flatten(options, args, false);

        Assert.assertArrayEquals(expected.toArray(), actual);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
     * with options that have no arguments and a non-option argument at the end
     * @throws ParseException
     */
    @Test
    public void testFlattenOptionsWithNonOptionArgument() throws ParseException {
        String[] args = {"-Dproperty", "--longOption", "argument"};
        Options options = new Options();
        options.addOption("D", "property", true, "property value");
        options.addOption("l", "longOption", false, "has long option");

        List<String> expected = new ArrayList<>();
        expected.add("D");
        expected.add("property");
        expected.add("--");
        expected.add("longOption");
        expected.add("argument");

        List<String> actual = new GnuParser().flatten(options, args, false);

        Assert.assertArrayEquals(expected.toArray(), actual);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
     * with options that have no arguments, a non-option argument at the end, and the stopAtNonOption flag set to true
     * @throws ParseException
     */
    @Test
    public void testFlattenOptionsWithNonOptionArgumentAndStopAtNonOption() throws ParseException {
        String[] args = {"-Dproperty", "--longOption", "argument"};
        Options options = new Options();
        options.addOption("D", "property", true, "property value");
        options.addOption("l", "longOption", false, "has long option");

        List<String> expected = new ArrayList<>();
        expected.add("D");
        expected.add("property");

        List<String> actual = new GnuParser().flatten(options, args, true);

        Assert.assertArrayEquals(expected.toArray(), actual);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
     * with empty arguments array
     * @throws ParseException
     */
    @Test
    public void testFlattenEmptyArguments() throws ParseException {
        String[] args = {};
        Options options = new Options();

        List<String> expected = new ArrayList<>();

        List<String> actual = new GnuParser().flatten(options, args, false);

        Assert.assertEquals(0, actual.size());
    }
}