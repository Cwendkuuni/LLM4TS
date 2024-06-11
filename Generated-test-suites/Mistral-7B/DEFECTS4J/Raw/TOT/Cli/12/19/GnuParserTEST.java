import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for the GnuParser class.
 */
public class GnuParserTest {

    /**
     * Test method for the flatten method with simple options.
     */
    @Test
    public void testFlattenSimpleOptions() {
        String[] args = {"-a", "-b", "-c", "--"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        GnuParser parser = new GnuParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "--", "-b", "-c"}, flattenedArgs);
    }

    /**
     * Test method for the flatten method with long options.
     */
    @Test
    public void testFlattenLongOptions() {
        String[] args = {"--foo", "value", "--bar", "value2"};
        Options options = new Options();
        options.addOption("foo", true, "Option foo");
        options.addOption("bar", true, "Option bar");

        GnuParser parser = new GnuParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo", "value", "--bar", "value2"}, flattenedArgs);
    }

    /**
     * Test method for the flatten method with invalid options.
     */
    @Test(expected = ParseException.class)
    public void testFlattenInvalidOptions() {
        String[] args = {"--invalidOption", "value"};
        Options options = new Options();
        options.addOption("foo", true, "Option foo");

        GnuParser parser = new GnuParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.fail("Expected ParseException but received " + flattenedArgs.length + " elements.");
    }

    /**
     * Test method for the flatten method with no options.
     */
    @Test
    public void testFlattenNoOptions() {
        String[] args = {"arg1", "arg2"};
        Options options = new Options();

        GnuParser parser = new GnuParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, flattenedArgs);
    }

    /**
     * Test method for the flatten method with stopAtNonOption set to true.
     */
    @Test
    public void testFlattenStopAtNonOption() {
        String[] args = {"-a", "option", "value", "--"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        GnuParser parser = new GnuParser();
        String[] flattenedArgs = parser.flatten(options, args, true);

        Assert.assertArrayEquals(new String[]{"-a"}, flattenedArgs);
    }

    /**
     * Test method for the flatten method with double hyphen (--).
     */
    @Test
    public void testFlattenDoubleHyphen() {
        String[] args = {"--", "arg1", "--"};
        Options options = new Options();

        GnuParser parser = new GnuParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--", "arg1", "--"}, flattenedArgs);
    }

    /**
     * Test method for the flatten method with empty arguments array.
     */
    @Test
    public void testFlattenEmptyArguments() {
        String[] args = {};
        Options options = new Options();

        GnuParser parser = new GnuParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[0], flattenedArgs);
    }
}