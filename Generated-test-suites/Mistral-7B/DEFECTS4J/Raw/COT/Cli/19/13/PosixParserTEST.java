package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    /**
     * Set up the test.
     */
    @Before
    public void setUp() {
        options = new Options();
        parser = new PosixParser();
    }

    /**
     * Test flatten method with basic arguments.
     */
    @Test
    public void testFlattenBasic() {
        String[] arguments = {"--foo", "-o", "output.txt", "-a", "arg1", "-b=arg2", "--", "arg3"};
        String[] expected = {"--foo", "-o", "output.txt", "-a", null, "--", "arg3"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with stopAtNonOption set to true.
     */
    @Test
    public void testFlattenStopAtNonOption() {
        String[] arguments = {"--foo", "-o", "output.txt", "-a", "arg1", "-b=arg2", "--", "arg3", "nonOption"};
        String[] expected = {"--foo", "-o", "output.txt", "-a", null, "--", "arg3", "nonOption"};
        String[] result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test burstToken method with a valid option.
     */
    @Test
    public void testBurstTokenValidOption() {
        String token = "--foo-bar";
        String[] expected = {"--foo", "-", "bar"};
        parser.burstToken(token, false);
        List list = (List) parser.getTokens();
        String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = (String) list.get(i);
        }
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test burstToken method with an invalid option and stopAtNonOption set to true.
     */
    @Test
    public void testBurstTokenInvalidOptionWithStopAtNonOption() {
        String token = "--foo-bar-baz";
        String[] expected = {"--foo-bar", "baz"};
        parser.burstToken(token, true);
        List list = (List) parser.getTokens();
        String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = (String) list.get(i);
        }
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test burstToken method with an invalid option and stopAtNonOption not set.
     */
    @Test
    public void testBurstTokenInvalidOptionWithoutStopAtNonOption() {
        String token = "--foo-bar-baz";
        String[] expected = {"--foo-bar", "-", "baz"};
        parser.burstToken(token, false);
        List list = (List) parser.getTokens();
        String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = (String) list.get(i);
        }
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test processOptionToken method with a valid option.
     */
    @Test
    public void testProcessOptionTokenValidOption() {
        String token = "--foo";
        parser.processOptionToken(token, false);
        List list = (List) parser.getTokens();
        String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = (String) list.get(i);
        }
        Assert.assertArrayEquals(new String[]{"--foo"}, result);
    }

    /**
     * Test processOptionToken method with an invalid option and stopAtNonOption set to true.
     */
    @Test
    public void testProcessOptionTokenInvalidOptionWithStopAtNonOption() {
        String token = "--foo-bar";
        parser.processOptionToken(token, true);
        List list = (List) parser.getTokens();
        String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = (String) list.get(i);
        }
        Assert.assertArrayEquals(new String[]{"--foo-bar"}, result);
    }

    /**
     * Test processOptionToken method with an invalid option and stopAtNonOption not set.
     */
    @Test
    public void testProcessOptionTokenInvalidOptionWithoutStopAtNonOption() {
        String token = "--foo-bar";
        parser.processOptionToken(token, false);
        List list = (List) parser.getTokens();
        String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = (String) list.get(i);
        }
        Assert.assertArrayEquals(new String[]{"--foo", "-", "bar"}, result);
    }

    /**
     * Test process method with a valid value for a single-valued option.
     */
    @Test
    public void testProcessSingleValuedOption() {
        String token = "-o";
        parser.process("output.txt");
        List list = (List) parser.getTokens();
        String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = (String) list.get(i);
        }
        Assert.assertArrayEquals(new String[]{"-o", "output.txt"}, result);
    }

    /**
     * Test process method with a valid value for a multi-valued option.
     */
    @Test
    public void testProcessMultiValuedOption() {
        Option option = new Option("a", null, true, "a");
        options.addOption(option);
        String token = "-a";
        parser.process("arg1");
        parser.process("arg2");
        List list = (List) parser.getTokens();
        String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = (String) list.get(i);
        }
        Assert.assertArrayEquals(new String[]{"-a", "arg1", "-a", "arg2"}, result);
    }

    /**
     * Test process method with a value for a multi-valued option that requires an argument.
     */
    @Test
    public void testProcessMultiValuedOptionWithArgument() {
        Option option = new Option("b", null, true, "arg");
        option.hasArg(true);
        options.addOption(option);
        String token = "-b";
        parser.process("arg1");
        parser.process("arg2");
        List list = (List) parser.getTokens();
        String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = (String) list.get(i);
        }
        Assert.assertArrayEquals(new String[]{"-b", "arg1", "arg2"}, result);
    }

    /**
     * Test process method with a value for a multi-valued option that accepts multiple values.
     */
    @Test
    public void testProcessMultiValuedOptionWithMultipleValues() {
        Option option = new Option("c", null, true, "arg");
        option.hasArgs(true);
        options.addOption(option);
        String token = "-c";
        parser.process("arg1 arg2 arg3");
        List list = (List) parser.getTokens();
        String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = (String) list.get(i);
        }
        Assert.assertArrayEquals(new String[]{"-c", "arg1", "arg2", "arg3"}, result);
    }

    /**
     * Test process method with a single non-option argument.
     */
    @Test
    public void testProcessSingleNonOptionArgument() {
        String token = "arg1";
        parser.process(token);
        List list = (List) parser.getTokens();
        String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = (String) list.get(i);
        }
        Assert.assertArrayEquals(new String[]{"arg1"}, result);
    }

    /**
     * Test process method with multiple non-option arguments.
     */
    @Test
    public void testProcessMultipleNonOptionArguments() {
        String[] tokens = {"arg1", "arg2", "arg3"};
        for (String token : tokens) {
            parser.process(token);
        }
        List list = (List) parser.getTokens();
        String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = (String) list.get(i);
        }
        Assert.assertArrayEquals(Arrays.copyOf(tokens, tokens.length), result);
    }
}