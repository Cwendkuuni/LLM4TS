import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for GnuParser.
 */
public class GnuParserTest {

    @Test
    public void testFlatten() {
        String[] args = new String[]{"-Dprop=value", "-a", "--", "-b", "-c=-d", "e", "f--g=h"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", true, "Option D");
        options.addOption("e", true, "Option E");
        options.addOption("f", true, "Option F");
        options.addOption("g", true, "Option G");

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = new String[]{"-D", "prop", "value", "-a", "--", "-b", "-c", "-d", "e", "f", "--g", "h"};
        Assert.assertArrayEquals(expected, result);

        // Test with stopAtNonOption = true
        result = parser.flatten(options, args, true);
        expected = new String[]{"-D", "prop", "value", "-a", "-b", "e"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() {
        String[] args = new String[]{"-I", "invalid", "-a", "--", "-b"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        GnuParser parser = new GnuParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenWithDashArg() {
        String[] args = new String[]{"--", "-a", "-b", "-c", "arg--", "-d"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = new String[]{"--", "-a", "-b", "-c", "--", "arg--", "-d"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithDoubleDashArg() {
        String[] args = new String[]{"--", "--double-dash-arg--", "-a", "-b"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = new String[]{"--", "--double-dash-arg--", "--"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithEmptyArgs() {
        String[] args = new String[]{};
        Options options = new Options();

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[0], result);
    }
}