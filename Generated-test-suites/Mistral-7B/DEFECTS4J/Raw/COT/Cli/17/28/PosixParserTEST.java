package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParsedPosixArgument;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test cases for PosixParser class
 */
public class PosixParserTest {

    @Test
    public void testFlattenEmptyArgs() {
        PosixParser parser = new PosixParser();
        String[] args = {};
        Options options = new Options();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testFlattenSimpleArgs() {
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "arg2", "--option"};
        Options options = new Options();
        options.addOption("option", false, null, "Description");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"arg1", "--option"}, result);
    }

    @Test
    public void testFlattenOptionWithArg() {
        PosixParser parser = new PosixParser();
        String[] args = {"--option", "value"};
        Options options = new Options();
        options.addOption("option", true, "value", "Description");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option", "value"}, result);
    }

    @Test
    public void testFlattenLongOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--long-option"};
        Options options = new Options();
        options.addOption("longOption", false, null, "Description");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--long-option"}, result);
    }

    @Test
    public void testFlattenOptionWithArgAndOtherArgs() {
        PosixParser parser = new PosixParser();
        String[] args = {"--option", "value", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("option", true, "value", "Description");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option", "value", "arg1", "arg2"}, result);
    }

    @Test
    public void testFlattenMultiOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--option1", "--option2"};
        Options options = new Options();
        options.addOption("option1", false, null, "Description1");
        options.addOption("option2", false, null, "Description2");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option1", "--option2"}, result);
    }

    @Test
    public void testFlattenMultiOptionWithArg() {
        PosixParser parser = new PosixParser();
        String[] args = {"--option1", "arg1", "--option2", "arg2"};
        Options options = new Options();
        options.addOption("option1", true, "arg1", "Description1");
        options.addOption("option2", true, "arg2", "Description2");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option1", "arg1", "--option2", "arg2"}, result);
    }

    @Test
    public void testFlattenWithSingleHyphen() {
        PosixParser parser = new PosixParser();
        String[] args = {"-"};
        Options options = new Options();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-"}, result);
    }

    @Test
    public void testFlattenWithDoubleHyphen() {
        PosixParser parser = new PosixParser();
        String[] args = {"--"};
        Options options = new Options();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--"}, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithInvalidOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--invalid-option"};
        Options options = new Options();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "--option", "arg2"};
        Options options = new Options();
        options.addOption("option", false, null, "Description");
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"arg1", "--option"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndOtherArgs() {
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "--option", "arg2", "arg3"};
        Options options = new Options();
        options.addOption("option", false, null, "Description");
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"arg1", "--option"}, result);
    }

    @Test
    public void testBurstTokenEmpty() {
        PosixParser parser = new PosixParser();
        String token = "";
        boolean stopAtNonOption = false;
        parser.burstToken(token, stopAtNonOption);
        Assert.assertFalse(parser.eatTheRest);
        Assert.assertTrue(parser.tokens.isEmpty());
    }

    @Test
    public void testBurstTokenSingleCharacter() {
        PosixParser parser = new PosixParser();
        String token = "a";
        boolean stopAtNonOption = false;
        parser.burstToken(token, stopAtNonOption);
        List<String> expectedTokens = new ArrayList<>();
        expectedTokens.add("a");
        expectedTokens.add("--");
        Assert.assertEquals(expectedTokens, parser.tokens);
        Assert.assertTrue(parser.eatTheRest);
    }

    @Test
    public void testBurstTokenSingleCharacterOption() {
        PosixParser parser = new PosixParser();
        String token = "a";
        boolean stopAtNonOption = false;
        Options options = new Options();
        options.addOption("a", false, null, "Description");
        parser.setOptions(options);
        parser.burstToken(token, stopAtNonOption);
        List<String> expectedTokens = new ArrayList<>();
        expectedTokens.add("-a");
        Assert.assertEquals(expectedTokens, parser.tokens);
        Assert.assertFalse(parser.eatTheRest);
    }

    @Test
    public void testBurstTokenSingleCharacterOptionWithArg() {
        PosixParser parser = new PosixParser();
        String token = "a";
        boolean stopAtNonOption = false;
        Options options = new Options();
        options.addOption("a", true, null, "Description");
        parser.setOptions(options);
        parser.burstToken(token, stopAtNonOption);
        List<String> expectedTokens = new ArrayList<>();
        expectedTokens.add("-a");
        expectedTokens.add("arg");
        Assert.assertEquals(expectedTokens, parser.tokens);
        Assert.assertFalse(parser.eatTheRest);
    }

    @Test
    public void testBurstTokenMultiCharacterOption() {
        PosixParser parser = new PosixParser();
        String token = "ab";
        boolean stopAtNonOption = false;
        Options options = new Options();
        options.addOption("ab", false, null, "Description");
        parser.setOptions(options);
        parser.burstToken(token, stopAtNonOption);
        List<String> expectedTokens = new ArrayList<>();
        expectedTokens.add("-ab");
        Assert.assertEquals(expectedTokens, parser.tokens);
        Assert.assertFalse(parser.eatTheRest);
    }

    @Test
    public void testBurstTokenMultiCharacterOptionWithArg() {
        PosixParser parser = new PosixParser();
        String token = "ab";
        boolean stopAtNonOption = false;
        Options options = new Options();
        options.addOption("ab", true, null, "Description");
        parser.setOptions(options);
        parser.burstToken(token, stopAtNonOption);
        List<String> expectedTokens = new ArrayList<>();
        expectedTokens.add("-ab");
        expectedTokens.add("arg");
        Assert.assertEquals(expectedTokens, parser.tokens);
        Assert.assertFalse(parser.eatTheRest);
    }

    @Test
    public void testBurstTokenOptionWithArgAndOtherArgs() {
        PosixParser parser = new PosixParser();
        String token = "a";
        boolean stopAtNonOption = false;
        Options options = new Options();
        options.addOption("a", true, null, "Description");
        String[] arguments = {"arg1", "arg2"};
        parser.setOptions(options);
        parser.burstToken(token, stopAtNonOption);
        parser.process(arguments[0]);
        List<String> expectedTokens = new ArrayList<>();
        expectedTokens.add("-a");
        expectedTokens.add("arg1");
        Assert.assertEquals(expectedTokens, parser.tokens);
        Assert.assertTrue(parser.eatTheRest);
    }

    @Test
    public void testBurstTokenInvalidOption() {
        PosixParser parser = new PosixParser();
        String token = "x";
        boolean stopAtNonOption = false;
        parser.burstToken(token, stopAtNonOption);
        List<String> expectedTokens = new ArrayList<>();
        expectedTokens.add("--");
        expectedTokens.add("x");
        Assert.assertEquals(expectedTokens, parser.tokens);
        Assert.assertTrue(parser.eatTheRest);
    }

    @Test
    public void testBurstTokenInvalidOptionWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String token = "x";
        boolean stopAtNonOption = true;
        parser.burstToken(token, stopAtNonOption);
        List<String> expectedTokens = new ArrayList<>();
        Assert.assertEquals(expectedTokens, parser.tokens);
        Assert.assertTrue(parser.tokens.isEmpty());
        Assert.assertFalse(parser.eatTheRest);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        String token = "-";
        parser.processSingleHyphen(token);
        List<String> expectedTokens = new ArrayList<>();
        expectedTokens.add("-");
        Assert.assertEquals(expectedTokens, parser.tokens);
        Assert.assertTrue(parser.eatTheRest);
    }
}