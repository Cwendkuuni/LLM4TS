import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Test class for GnuParser.
 */
public class GnuParserTest {

    /**
     * Test flatten method with no options.
     */
    @Test
    public void testFlattenNoOptions() throws ParseException {
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        Options options = new Options();
        GnuParser parser = new GnuParser();
        String[] result = parser.parse(options, args);
        String[] expected = new String[]{"arg1", "arg2", "arg3"};
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with single character option.
     */
    @Test
    public void testFlattenSingleCharacterOption() throws ParseException {
        String[] args = new String[]{"-a", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("a", true, "Single character option a.");
        GnuParser parser = new GnuParser();
        String[] result = parser.parse(options, args);
        String[] expected = new String[]{"-a", "arg1", "arg2"};
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with multi-character option.
     */
    @Test
    public void testFlattenMultiCharacterOption() throws ParseException {
        String[] args = new String[]{"--foo", "value"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option.");
        GnuParser parser = new GnuParser();
        String[] result = parser.parse(options, args);
        String[] expected = new String[]{"--foo", "value"};
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with double-hyphen option.
     */
    @Test
    public void testFlattenDoubleHyphenOption() throws ParseException {
        String[] args = new String[]{"--", "arg1", "arg2"};
        Options options = new Options();
        GnuParser parser = new GnuParser();
        String[] result = parser.parse(options, args);
        String[] expected = new String[]{"--", "arg1", "arg2"};
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with stopAtNonOption flag.
     */
    @Test
    public void testFlattenStopAtNonOption() throws ParseException {
        String[] args = new String[]{"-a", "-b", "arg1", "-c", "arg2"};
        Options options = new Options();
        options.addOption("a", true, "Option A.");
        options.addOption("b", true, "Option B.");
        GnuParser parser = new GnuParser();
        String[] result = parser.parse(options, args, true);
        String[] expected = new String[]{"-a", "-b", "-c", "arg1", "arg2"};
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with special properties option (-D).
     */
    @Test
    public void testFlattenSpecialPropertiesOption() throws ParseException {
        String[] args = new String[]{"-Dproperty=value", "arg1", "arg2"};
        Options options = new Options();
        GnuParser parser = new GnuParser();
        String[] result = parser.parse(options, args);
        String[] expected = new String[]{"-D", "property", "value", "arg1", "arg2"};
        Assert.assertArrayEquals(expected, result);
    }
}