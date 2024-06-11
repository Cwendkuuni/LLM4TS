import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class GnuParserTest {

    @Test
    public void testFlatten() throws ParseException {
        String[] args = new String[]{"-Dproperty1=value1", "--foo", "arg1", "-Dproperty2=value2", "-foo=value3"};
        Options options = new Options();

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = new String[]{"-D", "property1", "value1", "--", "foo", "arg1", "-D", "property2", "value2", "-foo", "value3"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        String[] args = new String[]{"-Dproperty1=value1", "--foo", "arg1", "-Dproperty2=value2", "-foo=value3", "nonOption"};
        Options options = new Options();

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, true);

        String[] expected = new String[]{"-D", "property1", "value1", "--", "foo", "arg1", "-D", "property2", "value2", "-foo", "value3"};
        Assert.assertArrayEquals(expected, new String[args.length - 1]); // exclude nonOption
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithEmptyArgs() {
        String[] args = new String[0];
        Options options = new Options();

        GnuParser parser = new GnuParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() throws ParseException {
        String[] args = new String[]{"-Dproperty1=value1", "--foo", "arg1", "-Dproperty2=value2", "-foo=value3", "--invalidOption"};
        Options options = new Options();

        GnuParser parser = new GnuParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidLongOption() throws ParseException {
        String[] args = new String[]{"-Dproperty1=value1", "--invalid-long-option", "arg1", "-Dproperty2=value2", "-foo=value3"};
        Options options = new Options();

        GnuParser parser = new GnuParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenWithValidLongOption() throws ParseException {
        String[] args = new String[]{"-Dproperty1=value1", "--foo", "arg1", "-Dproperty2=value2", "-foo=value3", "--valid-long-option"};
        Options options = new Options();
        options.addOption("valid-long-option", "Description of the valid long option");

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = new String[]{"-D", "property1", "value1", "--", "foo", "arg1", "-D", "property2", "value2", "-foo", "value3", "--valid-long-option"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithInvalidShortOption() throws ParseException {
        String[] args = new String[]{"-Dproperty1=value1", "--foo", "arg1", "-Dproperty2=value2", "-foo=value3", "-i"}; // invalid short option
        Options options = new Options();

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = new String[]{"-D", "property1", "value1", "--", "foo", "arg1", "-D", "property2", "value2", "-foo", "value3"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithValidShortOption() throws ParseException {
        String[] args = new String[]{"-Dproperty1=value1", "--foo", "arg1", "-Dproperty2=value2", "-foo=value3", "-i", "inputFile"};
        Options options = new Options();
        options.addOption("i", "inputFile", true, "Input file to read from.");

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = new String[]{"-D", "property1", "value1", "--", "foo", "arg1", "-D", "property2", "value2", "-foo", "value3", "-i", "inputFile"};
        Assert.assertArrayEquals(expected, result);
    }
}