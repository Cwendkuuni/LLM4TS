import org.apache.commons.cli.*;
import org.apache.commons.cli.util.ArgParser;
import org.junit.Assert;
import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlatten_BasicUsage() throws ParseException {
        String[] arguments = new String[]{"-a", "-b", "-c", "--"};
        Options options = new Options();
        String[] expected = new String[]{"-a", "-b", "--"};
        String[] actual = new GnuParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_MissingOption() {
        String[] arguments = new String[]{"-a", "-c"};
        Options options = new Options();
        new GnuParser().flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_UnknownLongOption() {
        String[] arguments = new String[]{"--unknownOption"};
        Options options = new Options();
        new GnuParser().flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_UnknownShortOption() {
        String[] arguments = new String[]{"-z"};
        Options options = new Options();
        new GnuParser().flatten(options, arguments, false);
    }

    @Test
    public void testFlatten_UtilStripLeadingHyphens() {
        String arg = "--foo=value";
        String expected = "foo";
        String actual = new ArgParser().stripLeadingHyphens(arg);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFlatten_StopAtNonOption_BasicUsage() {
        String[] arguments = new String[]{"-a", "-b", "-c", "nonOption"};
        Options options = new Options();
        String[] expected = new String[]{"-a", "-b"};
        String[] actual = new GnuParser().flatten(options, arguments, true);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_StopAtNonOption_MultipleNonOptions() {
        String[] arguments = new String[]{"-a", "-b", "-c", "nonOption1", "nonOption2"};
        Options options = new Options();
        String[] expected = new String[]{"-a", "-b"};
        String[] actual = new GnuParser().flatten(options, arguments, true);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_DoubleDash() {
        String[] arguments = new String[]{"--"};
        Options options = new Options();
        String[] expected = new String[]{"--"};
        String[] actual = new GnuParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_DoubleDash_FollowedByOption() {
        String[] arguments = new String[]{"--", "-a"};
        Options options = new Options();
        String[] expected = new String[]{"--", "-a"};
        String[] actual = new GnuParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_DoubleDash_FollowedByNonOption() {
        String[] arguments = new String[]{"--", "nonOption"};
        Options options = new Options();
        String[] expected = new String[]{"--", "nonOption"};
        String[] actual = new GnuParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, actual);
    }
}