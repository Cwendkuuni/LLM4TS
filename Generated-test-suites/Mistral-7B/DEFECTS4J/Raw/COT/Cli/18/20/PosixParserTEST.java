package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten_EmptyArguments() {
        String[] arguments = new String[0];
        String[] expected = {};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_NoOptions() {
        String[] arguments = {"arg1", "arg2"};
        String[] expected = {"arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SimpleOptions() {
        options.addOption("option1", true, "o1");
        options.addOption("option2", 1, "o2");
        String[] arguments = {"-o1", "--option2=value"};
        String[] expected = {"option1", "option2", "value"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        options.addOption("option1", true, "o1");
        String[] arguments = {"arg1", "--", "arg3", "-o1", "--option2=value"};
        String[] expected = {"arg1", "--", "arg3", "option1"};
        try {
            parser.flatten(options, arguments, true);
        } catch (ParseException e) {
            // expected
        }
        Assert.assertArrayEquals(expected, parser.getTokens());
    }

    @Test
    public void testFlatten_LongOption() {
        options.addOption("longOption", true, "lo");
        String[] arguments = {"--longOption"};
        String[] expected = {"longOption"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        options.addOption("option1", true, "o1");
        String[] arguments = {"--invalidOption"};
        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidLongOption() {
        options.addOption("option1", true, "o1");
        String[] arguments = {"--invalid-long-option"};
        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_MissingArgOption() {
        options.addOption("option1", true, "o1");
        options.addOption("option2", 1, "o2");
        String[] arguments = {"-o1", "--option2"};
        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_ExtraArgOption() {
        options.addOption("option1", true, "o1");
        options.addOption("option2", 1, "o2");
        String[] arguments = {"-o1", "--option2", "extra"};
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testBurstToken_SimpleOption() {
        String token = "-o1";
        String[] expected = {"o1"};
        String[] result = parser.burstToken(token, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken_OptionWithArg() {
        String token = "--option2=value";
        String[] expected = {"option2", "value"};
        String[] result = parser.burstToken(token, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken_InvalidOption() {
        String token = "--invalidOption";
        String[] expected = {};
        try {
            parser.burstToken(token, false);
        } catch (ParseException e) {
            // expected
        }
        Assert.assertArrayEquals(expected, parser.getTokens());
    }

    @Test
    public void testBurstToken_LongOption() {
        String token = "--longOption";
        String[] expected = {"longOption"};
        String[] result = parser.burstToken(token, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken_ValidOptionWithArgAndExtra() {
        String token = "--option2=value--extra";
        String[] expected = {"option2", "value", "extra"};
        String[] result = parser.burstToken(token, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken_StopAtNonOption() {
        String token = "--";
        String[] expected = {};
        try {
            parser.burstToken(token, true);
        } catch (ParseException e) {
            // expected
        }
        Assert.assertArrayEquals(expected, parser.getTokens());
    }

    private List<String> getTokens() {
        return (List<String>) parser.getTokens();
    }
}