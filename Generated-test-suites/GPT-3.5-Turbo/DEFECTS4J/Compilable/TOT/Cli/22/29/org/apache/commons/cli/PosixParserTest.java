package org.apache.commons.cli;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        String[] args = {"-a", "value1", "-b", "-c", "--", "-d", "-e"};

        String[] result = parser.flatten(options, args, true);

        Assert.assertNotNull(result);
        Assert.assertEquals(4, result.length);
        Assert.assertEquals("-a", result[0]);
        Assert.assertEquals("value1", result[1]);
        Assert.assertEquals("-b", result[2]);
        Assert.assertEquals("-c", result[3]);
    }

    @Test
    public void testFlatten_DoNotStopAtNonOption() {
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        String[] args = {"-a", "value1", "-b", "-c", "--", "-d", "-e"};

        String[] result = parser.flatten(options, args, false);

        Assert.assertNotNull(result);
        Assert.assertEquals(8, result.length);
        Assert.assertEquals("-a", result[0]);
        Assert.assertEquals("value1", result[1]);
        Assert.assertEquals("-b", result[2]);
        Assert.assertEquals("-c", result[3]);
        Assert.assertEquals("--", result[4]);
        Assert.assertEquals("-d", result[5]);
        Assert.assertEquals("-e", result[6]);
    }

    @Test
    public void testFlatten_LongOptionWithEqualSign() {
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        String[] args = {"--a=value1", "--b"};

        String[] result = parser.flatten(options, args, true);

        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.length);
        Assert.assertEquals("--a", result[0]);
        Assert.assertEquals("value1", result[1]);
    }

    @Test
    public void testFlatten_LongOptionWithoutEqualSign() {
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        String[] args = {"--a", "value1", "--b"};

        String[] result = parser.flatten(options, args, true);

        Assert.assertNotNull(result);
        Assert.assertEquals(3, result.length);
        Assert.assertEquals("--a", result[0]);
        Assert.assertEquals("value1", result[1]);
        Assert.assertEquals("--b", result[2]);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        String[] args = {"-"};

        String[] result = parser.flatten(options, args, true);

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.length);
        Assert.assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_SingleCharacterOption() {
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        String[] args = {"-a"};

        String[] result = parser.flatten(options, args, true);

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.length);
        Assert.assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_SingleCharacterOption_InvalidOption() {
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        String[] args = {"-c"};

        String[] result = parser.flatten(options, args, true);

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.length);
        Assert.assertEquals("-c", result[0]);
    }

    @Test
    public void testFlatten_MoreThanTwoCharacterOption() {
        Option option = new Option("abc", true, "Option abc");
        options.addOption(option);

        String[] args = {"-abc=value1"};

        String[] result = parser.flatten(options, args, true);

        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.length);
        Assert.assertEquals("-abc", result[0]);
        Assert.assertEquals("value1", result[1]);
    }

    @Test
    public void testFlatten_MoreThanTwoCharacterOption_InvalidOption() {
        options.addOption("a", false, "Option a");

        String[] args = {"-abc=value1"};

        String[] result = parser.flatten(options, args, true);

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.length);
        Assert.assertEquals("-abc=value1", result[0]);
    }

    @Test
    public void testFlatten_InvalidOption_StopAtNonOption() {
        options.addOption("a", false, "Option a");

        String[] args = {"-a", "-b"};

        String[] result = parser.flatten(options, args, true);

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.length);
        Assert.assertEquals("-b", result[0]);
    }

    @Test
    public void testFlatten_InvalidOption_DoNotStopAtNonOption() {
        options.addOption("a", false, "Option a");

        String[] args = {"-a", "-b"};

        String[] result = parser.flatten(options, args, false);

        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.length);
        Assert.assertEquals("-a", result[0]);
        Assert.assertEquals("-b", result[1]);
    }

    @Test
    public void testFlatten_NonOptionToken_StopAtNonOption() {
        options.addOption("a", false, "Option a");

        String[] args = {"value1"};

        String[] result = parser.flatten(options, args, true);

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.length);
        Assert.assertEquals("value1", result[0]);
    }

    @Test
    public void testFlatten_NonOptionToken_DoNotStopAtNonOption() {
        options.addOption("a", false, "Option a");

        String[] args = {"value1"};

        String[] result = parser.flatten(options, args, false);

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.length);
        Assert.assertEquals("value1", result[0]);
    }

    @Test
    public void testFlatten_EmptyArgs_StopAtNonOption() {
        options.addOption("a", false, "Option a");

        String[] args = {};

        String[] result = parser.flatten(options, args, true);

        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_EmptyArgs_DoNotStopAtNonOption() {
        options.addOption("a", false, "Option a");

        String[] args = {};

        String[] result = parser.flatten(options, args, false);

        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_NullArgs_StopAtNonOption() {
        options.addOption("a", false, "Option a");

        String[] args = null;

        String[] result = parser.flatten(options, args, true);

        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_NullArgs_DoNotStopAtNonOption() {
        options.addOption("a", false, "Option a");

        String[] args = null;

        String[] result = parser.flatten(options, args, false);

        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.length);
    }
}