package org.apache.commons.cli;

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
    public void testFlattenWithValidOptionsAndArguments() {
        String[] arguments = {"--optionA", "--optionB=valueB", "--optionC=valueC"};
        Options options = new Options();
        options.addOption("optionA", true, "a");
        options.addOption("optionB", true, "b");
        options.addOption("optionC", true, "c");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--optionA", "--optionB", "--optionC=valueC");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test flatten method with valid options and no arguments.
     */
    @Test
    public void testFlattenWithValidOptionsNoArguments() {
        String[] arguments = {"--optionA", "--optionB"};
        Options options = new Options();
        options.addOption("optionA", true, "a");
        options.addOption("optionB", true, "b");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--optionA", "--optionB");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test flatten method with valid single-character options.
     */
    @Test
    public void testFlattenWithValidSingleCharacterOptions() {
        String[] arguments = {"-a", "-b=valueB", "--optionC"};
        Options options = new Options();
        options.addOption("a", true, "a");
        options.addOption("b", true, "b");
        options.addOption("c", true, "c");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-a", "--optionB", "--optionC");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test flatten method with valid long options.
     */
    @Test
    public void testFlattenWithValidLongOptions() {
        String[] arguments = {"--longOptionA", "--longOptionB=valueB"};
        Options options = new Options();
        options.addOption("longOptionA", true, "a");
        options.addOption("longOptionB", true, "b");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--longOptionA", "--longOptionB");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test flatten method with valid options, arguments, and long option with value.
     */
    @Test
    public void testFlattenWithValidOptionsArgumentsAndLongOptionValue() {
        String[] arguments = {"--optionA", "--optionB=valueB", "--longOptionC=valueC"};
        Options options = new Options();
        options.addOption("optionA", true, "a");
        options.addOption("optionB", true, "b");
        options.addOption("longOptionC", true, "c");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--optionA", "--optionB", "--longOptionC=valueC");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test flatten method with empty arguments.
     */
    @Test
    public void testFlattenWithEmptyArguments() {
        String[] arguments = {};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList();
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test flatten method with stopAtNonOption.
     */
    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] arguments = {"--optionA", "--", "nonOption"};
        Options options = new Options();
        options.addOption("optionA", true, "a");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        List<String> expected = Arrays.asList("--optionA", "--", "nonOption");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test burstToken method with valid option.
     */
    @Test
    public void testBurstTokenWithValidOption() {
        String token = "--optionA";
        boolean stopAtNonOption = false;

        PosixParser parser = new PosixParser();
        parser.burstToken(token, stopAtNonOption);
        List<String> expected = Arrays.asList("-optionA");
        Assert.assertEquals(expected, parser.tokens);
    }

    /**
     * Test burstToken method with valid option and argument.
     */
    @Test
    public void testBurstTokenWithValidOptionAndArgument() {
        String token = "--optionA=valueA";
        boolean stopAtNonOption = false;

        PosixParser parser = new PosixParser();
        parser.burstToken(token, stopAtNonOption);
        List<String> expected = Arrays.asList("-optionA", "valueA");
        Assert.assertEquals(expected, parser.tokens);
    }

    /**
     * Test burstToken method with invalid option.
     */
    @Test
    public void testBurstTokenWithInvalidOption() {
        String token = "--invalidOption";
        boolean stopAtNonOption = false;

        PosixParser parser = new PosixParser();
        parser.burstToken(token, stopAtNonOption);
        List<String> expected = Arrays.asList("--invalidOption");
        Assert.assertEquals(expected, parser.tokens);

        // Check if an empty list is returned when stopAtNonOption is true
        parser.init();
        parser.burstToken(token, true);
        Assert.assertTrue(parser.tokens.isEmpty());
    }

    /**
     * Test burstToken method with invalid option and arguments.
     */
    @Test(expected = ParseException.class)
    public void testBurstTokenWithInvalidOptionAndArguments() {
        String token = "--invalidOption=valueA";
        boolean stopAtNonOption = false;

        PosixParser parser = new PosixParser();
        parser.burstToken(token, stopAtNonOption);

        // This test should throw a ParseException since an invalid option with value is not supported
    }

    /**
     * Test burstToken method with stopAtNonOption.
     */
    @Test
    public void testBurstTokenWithStopAtNonOption() {
        String token = "--optionA";
        boolean stopAtNonOption = true;

        PosixParser parser = new PosixParser();
        parser.burstToken(token, stopAtNonOption);
        List<String> expected = Arrays.asList("--optionA");
        Assert.assertEquals(expected, parser.tokens);

        // Check if the remaining tokens are processed when stopAtNonOption is true
        String[] arguments = {"--optionA", "--optionB=valueB", "nonOption"};
        parser.init();
        parser.burstToken(token, stopAtNonOption);
        parser.burstToken("--optionB", stopAtNonOption);
        parser.process(("nonOption"));

        List<String> actual = Arrays.asList((String[]) parser.tokens.toArray(new String[parser.tokens.size()]));
        List<String> expectedWithRemainingTokens = Arrays.asList("--optionA", "--optionB", "nonOption");
        Assert.assertEquals(expectedWithRemainingTokens, actual);
    }

    
}