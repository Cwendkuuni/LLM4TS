package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for the PosixParser class.
 */
public class PosixParserTest {

    /**
     * Test case for the flatten method with no arguments.
     */
    @Test
    public void testFlattenNoArguments() {
        Options options = new Options();

        String[] arguments = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList("--", "--");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    /**
     * Test case for the flatten method with single argument.
     */
    @Test
    public void testFlattenSingleArgument() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option value");
        options.addOption(option);

        String[] arguments = {"arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList("o", "arg");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    /**
     * Test case for the flatten method with long option.
     */
    @Test
    public void testFlattenLongOption() {
        Options options = new Options();
        Option option = new Option("long-option", "Long option", false, "Description");
        options.addOption(option);

        String[] arguments = {"--long-option"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList("--long-option");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    /**
     * Test case for the flatten method with long option with value.
     */
    @Test
    public void testFlattenLongOptionWithValue() {
        Options options = new Options();
        Option option = new Option("long-option", "Long option", true, "Description");
        options.addOption(option);

        String[] arguments = {"--long-option", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList("--long-option", "value");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    /**
     * Test case for the flatten method with multiple options.
     */
    @Test
    public void testFlattenMultipleOptions() {
        Options options = new Options();
        Option option1 = new Option("o1", "Option 1", false, "Option 1 description");
        Option option2 = new Option("o2", "Option 2", true, "Option 2 description");
        options.addOption(option1);
        options.addOption(option2);

        String[] arguments = {"o1", "arg1", "--o2", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList("o1", "arg1", "o2", "arg2");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    /**
     * Test case for the flatten method with stopAtNonOption.
     */
    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        Option option = new Option("o", "Option", false, "Option description");
        options.addOption(option);

        String[] arguments = {"arg1", "-n", "arg3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        List<String> expected = Arrays.asList("o", "arg1", "-n", "arg3");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    /**
     * Test case for the burstToken method with a valid option.
     */
    @Test
    public void testBurstTokenValidOption() {
        Options options = new Options();
        Option option = new Option("o", "Option", false, "Option description");
        options.addOption(option);

        String token = "-o";
        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);

        List<String> expected = Arrays.asList("-o");
        Assert.assertEquals(expected, parser.tokens);
    }

    /**
     * Test case for the burstToken method with an invalid option.
     */
    @Test
    public void testBurstTokenInvalidOption() {
        Options options = new Options();

        String token = "-x";
        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);

        List<String> expected = Arrays.asList("--", "-x");
        Assert.assertEquals(expected, parser.tokens);
    }

    /**
     * Test case for the burstToken method with stopAtNonOption.
     */
    @Test
    public void testBurstTokenStopAtNonOption() {
        Options options = new Options();

        String token = "-n";
        PosixParser parser = new PosixParser();
        parser.burstToken(token, true);

        List<String> expected = Arrays.asList("--", "-n");
        Assert.assertEquals(expected, parser.tokens);
    }
}