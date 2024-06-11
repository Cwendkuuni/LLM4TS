import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.cli.Options;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("D", true, "property");
        String[] arguments = {"--foo=value", "-Dproperty=value", "arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"--foo=value", "-D", "property=value", "arg1", "arg2"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("D", true, "property");
        String[] arguments = {"--foo=value", "-Dproperty=value", "arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, true);

        String[] expected = {"--foo=value", "-D", "property=value"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithOnlyHyphen() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("D", true, "property");
        String[] arguments = {"--", "-"};

        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"--", "-"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithDoubleHyphen() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("D", true, "property");
        String[] arguments = {"--", "--"};

        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"--", "--"};
        Assert.assertArrayEquals(expected, result);
    }
}