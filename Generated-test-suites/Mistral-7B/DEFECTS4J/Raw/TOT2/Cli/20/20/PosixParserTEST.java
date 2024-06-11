import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    /**
     * Test flatten method with valid options and arguments.
     */
    @Test
    public void testFlattenValidOptions() {
        PosixParser parser = new PosixParser();

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an integer argument");
        options.addOption("c", 1, "Option C with a required argument");
        options.addOption("d", 2, "Option D with a variable number of arguments");

        String[] args = {"-a", "--b", "1", "-c", "value", "--d", "arg1", "arg2"};
        String[] expected = {"-a", "--b", "1", "-c", "value"};

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with invalid options.
     */
    @Test
    public void testFlattenInvalidOptions() {
        PosixParser parser = new PosixParser();

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an integer argument");

        String[] args = {"--foo", "-c", "value", "--d", "arg1", "arg2"};

        try {
            parser.flatten(options, args, false);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        List<String> tokens = Arrays.asList(args);
        Assert.assertEquals("--foo, -c, value, --d, arg1, arg2", tokens.toString());
    }

    /**
     * Test flatten method with -- at the beginning of the arguments.
     */
    @Test
    public void testFlattenDoubleDash() {
        PosixParser parser = new PosixParser();

        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"--", "-a", "value"};

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--", "-a", "value"}, result);
    }

    /**
     * Test flatten method with stopping at a non-option.
     */
    @Test
    public void testFlattenStopAtNonOption() {
        PosixParser parser = new PosixParser();

        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"-a", "--nonOption", "value"};

        String[] result = parser.flatten(options, args, true);

        Assert.assertArrayEquals(new String[]{"-a", "--nonOption"}, result);
    }

    /**
     * Test burstToken method with valid options.
     */
    @Test
    public void testBurstTokenValidOptions() {
        PosixParser parser = new PosixParser();

        Options options = new Options();
        options.addOption("ab", true, "Option AB");
        options.addOption("cd", 2, "Option CD with two arguments");

        String token = "abcd";

        List<String> expected = Arrays.asList("-a", "-b", "--cd", "cd");

        parser.burstToken(token, false);

        List<String> result = (List<String>) parser.tokens;

        Assert.assertEquals(expected, result);
    }

    /**
     * Test burstToken method with stopping at a non-option.
     */
    @Test
    public void testBurstTokenStopAtNonOption() {
        PosixParser parser = new PosixParser();

        Options options = new Options();
        options.addOption("ab", true, "Option AB");

        String token = "ab--foo";

        List<String> expected = Arrays.asList("-ab", "--foo");

        parser.burstToken(token, true);

        List<String> result = (List<String>) parser.tokens;

        Assert.assertEquals(expected, result);
    }
}