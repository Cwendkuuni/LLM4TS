import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class GnuParserTest {

    @Test
    public void testFlattenWithSingleOption() throws ParseException {
        String[] args = {"-Dtest"};
        GnuParser parser = new GnuParser();
        String[] expected = {"-D", "test"};
        String[] result = parser.flatten(new Options(), args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithEmptyArguments() {
        String[] args = {};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(new Options(), args, false);
        Assert.fail();
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithNullArguments() {
        String[] args = null;
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(new Options(), args, false);
        Assert.fail();
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithNullOptions() {
        Options options = null;
        String[] args = {"-Dtest"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);
        Assert.fail();
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithIllegalOptionFormat() {
        String[] args = {"-abcd"};
        GnuParser parser = new GnuParser();
        GnuParser.Options options = new Options();
        String[] result = parser.flatten(options, args, false);
        Assert.fail();
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithMissingOption() {
        String[] args = {"-a"};
        GnuParser parser = new GnuParser();
        GnuParser.Options options = new Options();
        String[] result = parser.flatten(options, args, false);
        Assert.fail();
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithOverlappingOptions() {
        String[] args = {"-Dfoo", "-Dbar"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(new Options(), args, false);
        Assert.fail();
    }
}