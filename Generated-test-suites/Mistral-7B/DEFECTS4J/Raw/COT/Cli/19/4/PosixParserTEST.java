package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test cases for the PosixParser class.
 */
public class PosixParserTest {

    

    @Test
    public void testFlattenEmptyArguments() {
        PosixParser parser = new PosixParser();
        String[] arguments = new String[0];
        String[] expected = new String[0];

        String[] result = parser.flatten(new Options(), arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleArgument() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"arg"};
        String[] expected = {"arg"};

        String[] result = parser.flatten(new Options(), arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", true, "Opt");
        String[] arguments = {"-o"};
        String[] expected = {"-o", "--option"};

        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleShortOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("o", true, "Opt");
        String[] arguments = {"-o"};
        String[] expected = {"-o", "--option"};

        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleLongOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOption", true, "Opt");
        String[] arguments = {"--longOption"};
        String[] expected = {"--longOption", "--longOption"};

        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleOptionWithArgument() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", true, "Opt");
        String[] arguments = {"-o", "arg"};
        String[] expected = {"-o", "--option", "arg"};

        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleLongOptionWithArgument() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOption", true, "Opt");
        String[] arguments = {"--longOption", "arg"};
        String[] expected = {"--longOption", "--longOption", "arg"};

        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptions() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", true, "Opt1");
        options.addOption("option2", true, "Opt2");
        String[] arguments = {"-o1", "-o2"};
        String[] expected = {"-o1", "--option1", "-o2", "--option2"};

        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptionsWithArguments() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", true, "Opt1");
        options.addOption("option2", true, "Opt2");
        String[] arguments = {"-o1", "arg1", "-o2", "arg2"};
        String[] expected = {"-o1", "--option1", "arg1", "-o2", "--option2", "arg2"};

        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testFlattenInvalidOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-x"};

        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", true, "Opt");
        String[] arguments = {"arg1", "-o", "arg2"};
        String[] expected = {"arg1", "--option", "arg2"};

        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstTokenEmptyToken() {
        PosixParser parser = new PosixParser();
        String token = "";

        parser.burstToken(token, false);

        // No tokens should be added to the list
        Assert.assertEquals(0, parser.tokens.size());
    }

    @Test
    public void testBurstTokenSingleCharacterOption() {
        PosixParser parser = new PosixParser();
        String token = "-o";

        parser.burstToken(token, false);

        String[] expected = {"-o"};

        Assert.assertArrayEquals(expected, (String[]) parser.tokens.toArray(new String[parser.tokens.size()]));
    }

    @Test
    public void testBurstTokenSingleCharacterOptionWithArgument() {
        PosixParser parser = new PosixParser();
        String token = "-o arg";

        parser.burstToken(token, false);

        String[] expected = {"-o", "--option", "arg"};

        Assert.assertArrayEquals(expected, (String[]) parser.tokens.toArray(new String[parser.tokens.size()]));
    }

    @Test
    public void testBurstTokenMultiCharacterOption() {
        PosixParser parser = new PosixParser();
        String token = "--longOption";

        parser.burstToken(token, false);

        String[] expected = {"--longOption", "--longOption"};

        Assert.assertArrayEquals(expected, (String[]) parser.tokens.toArray(new String[parser.tokens.size()]));
    }

    @Test
    public void testBurstTokenMultiCharacterOptionWithArgument() {
        PosixParser parser = new PosixParser();
        String token = "--longOption arg";

        parser.burstToken(token, false);

        String[] expected = {"--longOption", "--longOption", "arg"};

        Assert.assertArrayEquals(expected, (String[]) parser.tokens.toArray(new String[parser.tokens.size()]));
    }

    @Test
    public void testBurstTokenInvalidOption() {
        PosixParser parser = new PosixParser();
        String token = "-x";

        parser.burstToken(token, false);

        // No tokens should be added to the list
        Assert.assertEquals(0, parser.tokens.size());
    }

    @Test
    public void testBurstTokenStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String token = "arg";

        parser.burstToken(token, true);

        String[] expected = {"arg"};

        Assert.assertArrayEquals(expected, (String[]) parser.tokens.toArray(new String[parser.tokens.size()]));
    }

    
}