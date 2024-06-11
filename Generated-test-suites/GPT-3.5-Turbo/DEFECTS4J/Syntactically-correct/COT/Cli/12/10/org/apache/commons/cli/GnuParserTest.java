
import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-a", "value"};
        boolean stopAtNonOption = false;
        String[] expected = {"-a", "value"};
        String[] actual = parser.flatten(options, arguments, stopAtNonOption);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-a", "value", "nonOption"};
        boolean stopAtNonOption = true;
        String[] expected = {"-a", "value", "nonOption"};
        String[] actual = parser.flatten(options, arguments, stopAtNonOption);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenWithDoubleDash() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"--", "-a", "value"};
        boolean stopAtNonOption = false;
        String[] expected = {"--", "-a", "value"};
        String[] actual = parser.flatten(options, arguments, stopAtNonOption);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenWithSingleDash() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-"};
        boolean stopAtNonOption = false;
        String[] expected = {"-"};
        String[] actual = parser.flatten(options, arguments, stopAtNonOption);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenWithExistingOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("a", false, "an option");
        String[] arguments = {"-a"};
        boolean stopAtNonOption = false;
        String[] expected = {"-a"};
        String[] actual = parser.flatten(options, arguments, stopAtNonOption);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenWithFormattedOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("D", true, "a property option");
        String[] arguments = {"-Dproperty=value"};
        boolean stopAtNonOption = false;
        String[] expected = {"-D", "property=value"};
        String[] actual = parser.flatten(options, arguments, stopAtNonOption);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenWithNonOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"nonOption"};
        boolean stopAtNonOption = false;
        String[] expected = {"nonOption"};
        String[] actual = parser.flatten(options, arguments, stopAtNonOption);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenWithMultipleNonOptions() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"nonOption1", "nonOption2"};
        boolean stopAtNonOption = false;
        String[] expected = {"nonOption1", "nonOption2"};
        String[] actual = parser.flatten(options, arguments, stopAtNonOption);
        Assert.assertArrayEquals(expected, actual);
    }
}
