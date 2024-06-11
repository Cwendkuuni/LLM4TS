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
     * Test the flatten method with empty arguments.
     * @throws ParseException If the test fails.
     */
    @Test
    public void testFlattenEmpty() throws ParseException {
        PosixParser parser = new PosixParser();
        String[] arguments = {};
        Options options = new Options();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    /**
     * Test the flatten method with single arguments.
     * @throws ParseException If the test fails.
     */
    @Test
    public void testFlattenSingle() throws ParseException {
        PosixParser parser = new PosixParser();
        String[] arguments = {"arg1"};
        Options options = new Options();
        String[] result = parser.flatten(options, arguments, false);
        List expected = Arrays.asList("arg1");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    /**
     * Test the flatten method with two arguments.
     * @throws ParseException If the test fails.
     */
    @Test
    public void testFlattenTwo() throws ParseException {
        PosixParser parser = new PosixParser();
        String[] arguments = {"arg1", "arg2"};
        Options options = new Options();
        String[] result = parser.flatten(options, arguments, false);
        List expected = Arrays.asList("arg1", "arg2");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    /**
     * Test the flatten method with special tokens.
     * @throws ParseException If the test fails.
     */
    @Test
    public void testFlattenSpecialTokens() throws ParseException {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--arg1", "-a", "-e", "--arg2=value"};
        Options options = new Options();
        Option arg1 = new Option("arg1", "arg1 help", true);
        Option arg2 = new Option("arg2", "arg2 help", true).hasArg();
        options.addOption(arg1);
        options.addOption(arg2);
        String[] result = parser.flatten(options, arguments, false);
        List expected = Arrays.asList("--arg1", "-a", "-e", "--arg2=value");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    /**
     * Test the flatten method with stopAtNonOption.
     * @throws ParseException If the test fails.
     */
    @Test
    public void testFlattenStopAtNonOption() throws ParseException {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--arg1", "-a", "-e", "--arg2=value", "nonOption"};
        Options options = new Options();
        Option arg1 = new Option("arg1", "arg1 help", true);
        Option arg2 = new Option("arg2", "arg2 help", true).hasArg();
        options.addOption(arg1);
        options.addOption(arg2);
        String[] result = parser.flatten(options, arguments, true);
        List expected = Arrays.asList("--arg1", "-a", "-e", "--arg2=value");
        Assert.assertArrayEquals(expected.toArray(new String[0]), Arrays.copyOf(result, 4));
        Assert.assertEquals("nonOption", result[4]);
    }

    /**
     * Test the burstToken method with a single option.
     * @throws ParseException If the test fails.
     */
    @Test
    public void testBurstTokenSingleOption() throws ParseException {
        PosixParser parser = new PosixParser();
        String token = "-a";
        String stopAtNonOption = "false";
        parser.burstToken(token, Boolean.parseBoolean(stopAtNonOption));
        List expected = Arrays.asList("-a");
        Assert.assertEquals(expected, parser.tokens);
    }

    /**
     * Test the burstToken method with multiple options.
     * @throws ParseException If the test fails.
     */
    @Test
    public void testBurstTokenMultipleOptions() throws ParseException {
        PosixParser parser = new PosixParser();
        String token = "-a-b";
        String stopAtNonOption = "false";
        parser.burstToken(token, Boolean.parseBoolean(stopAtNonOption));
        List expected = Arrays.asList("-a", "-b");
        Assert.assertEquals(expected, parser.tokens);
    }

    /**
     * Test the burstToken method with a single non-option.
     * @throws ParseException If the test fails.
     */
    @Test(expected = ParseException.class)
    public void testBurstTokenSingleNonOption() throws ParseException {
        PosixParser parser = new PosixParser();
        String token = "nonOption";
        String stopAtNonOption = "false";
        parser.burstToken(token, Boolean.parseBoolean(stopAtNonOption));
    }

    /**
     * Test the burstToken method with a non-option and stopAtNonOption.
     * @throws ParseException If the test fails.
     */
    @Test
    public void testBurstTokenNonOptionAndStopAtNonOption() throws ParseException {
        PosixParser parser = new PosixParser();
        String token = "nonOption";
        String stopAtNonOption = "true";
        parser.burstToken(token, Boolean.parseBoolean(stopAtNonOption));
        Assert.assertEquals(Arrays.asList("nonOption"), parser.tokens);
    }

    /**
     * Test the init method.
     */
    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        Assert.assertFalse(parser.eatTheRest);
        Assert.assertTrue(parser.tokens.isEmpty());
        Assert.assertNull(parser.currentOption);
    }
}