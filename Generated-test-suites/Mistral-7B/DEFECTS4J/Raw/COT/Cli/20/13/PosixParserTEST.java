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
    public void setup() {
        parser = new PosixParser();
        options = new Options();
    }

    

    @Test
    public void testFlatten_EmptyArguments() {
        String[] arguments = {};
        String[] expected = {};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleArgument() {
        String[] arguments = {"arg1"};
        String[] expected = {"arg1"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_TwoArguments() {
        String[] arguments = {"arg1", "arg2"};
        String[] expected = {"arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOption() {
        options.addOption("longOption", false, null, "long-option");
        String[] arguments = {"--long-option"};
        String[] expected = {"--longOption"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        options.addOption("longOption", true, null, "long-option");
        String[] arguments = {"--long-option", "value"};
        String[] expected = {"--longOption", "value"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ShortOption() {
        options.addOption("o", false, null, "option");
        String[] arguments = {"-o"};
        String[] expected = {"-o", "option"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ShortOptionWithValue() {
        options.addOption("o", true, null, "option");
        String[] arguments = {"-o", "value"};
        String[] expected = {"-o", "value", "option"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleShortOptions() {
        options.addOption("o", false, null, "option");
        options.addOption("a", false, null, "another");
        String[] arguments = {"-o", "-a"};
        String[] expected = {"-o", "option", "-a", "another"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleShortOptionsWithValues() {
        options.addOption("o", true, null, "option");
        options.addOption("a", true, null, "another");
        String[] arguments = {"-o", "value1", "-a", "value2"};
        String[] expected = {"-o", "value1", "option", "-a", "value2", "another"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ShortOptionWithLongOption() {
        options.addOption("o", false, null, "option");
        options.addOption("lo", true, null, "long-option");
        String[] arguments = {"-o", "--lo", "value"};
        String[] expected = {"-o", "option", "--longOption", "value"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleShortOptionsWithLongOptions() {
        options.addOption("o", false, null, "option");
        options.addOption("lo", true, null, "long-option");
        String[] arguments = {"-o", "--lo1", "value1", "-o", "--lo2", "value2"};
        String[] expected = {"-o", "option", "--longOption", "value1", "-o", "option", "--longOption", "value2"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_NonOptionWithStopAtNonOption() {
        options.addOption("o", false, null, "option");
        String[] arguments = {"arg1"};
        parser.flatten(options, arguments, true);
    }

    @Test
    public void testFlatten_ShortOptionWithArgumentAndStopAtNonOption() {
        options.addOption("o", true, null, "option");
        String[] arguments = {"-o", "arg"};
        String[] expected = {"-o", "option", "arg"};
        String[] result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_ShortOptionWithoutArgumentAndStopAtNonOption() {
        options.addOption("o", true, null, "option");
        String[] arguments = {"-o"};
        parser.flatten(options, arguments, true);
    }

    @Test
    public void testFlatten_LongOptionWithArgument() {
        options.addOption("longOption", true, null, "longOption");
        String[] arguments = {"--longOption", "arg"};
        String[] expected = {"--longOption", "longOption", "arg"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_LongOptionWithoutArgumentAndStopAtNonOption() {
        options.addOption("longOption", false, null, "longOption");
        String[] arguments = {"--longOption"};
        parser.flatten(options, arguments, true);
    }

    @Test
    public void testFlatten_MixedShortAndLongOptions() {
        options.addOption("o", false, null, "option");
        options.addOption("lo", true, null, "long-option");
        String[] arguments = {"-o", "--lo", "--longOption", "-o"};
        String[] expected = {"-o", "option", "--longOption", "-o"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MixedShortAndLongOptionsWithArguments() {
        options.addOption("o", true, null, "option");
        options.addOption("lo", true, null, "long-option");
        String[] arguments = {"-o", "value1", "--lo", "value2", "-o", "value3"};
        String[] expected = {"-o", "value1", "option", "--longOption", "value2", "-o", "value3"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleShortOptionsWithArguments() {
        options.addOption("o", true, null, "option");
        String[] arguments = {"-o1", "value1", "-o2", "value2"};
        String[] expected = {"-o1", "value1", "option", "-o2", "value2"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleLongOptionsWithArguments() {
        options.addOption("lo1", true, null, "longOption1");
        options.addOption("lo2", true, null, "longOption2");
        String[] arguments = {"--lo1", "value1", "--lo2", "value2"};
        String[] expected = {"--lo1", "longOption1", "value1", "--lo2", "longOption2", "value2"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MixedShortAndLongOptionsWithArgumentsAndStopAtNonOption() {
        options.addOption("o", true, null, "option");
        options.addOption("lo", true, null, "long-option");
        String[] arguments = {"-o1", "value1", "--lo", "value2", "-o2"};
        String[] expected = {"-o1", "option", "--longOption", "value2", "-o2"};
        String[] result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken_SingleCharacterOption() {
        String token = "-o";
        String[] expected = {"-o", "option"};
        String[] result = parser.burstToken(token, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken_SingleCharacterOptionWithArgument() {
        String token = "-o";
        options.addOption("o", true, null, "option");
        String[] expected = {"-o", "option"};
        String[] result = parser.burstToken(token, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken_TwoCharacterOption() {
        String token = "--lo";
        String[] expected = {"--lo", "long-option"};
        String[] result = parser.burstToken(token, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken_TwoCharacterOptionWithArgument() {
        String token = "--lo";
        options.addOption("lo", true, null, "long-option");
        String[] expected = {"--lo", "long-option"};
        String[] result = parser.burstToken(token, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken_LongTokenWithSingleCharacterOption() {
        String token = "--long-option-o";
        String[] expected = {"--long-option-o", "-o", "option"};
        String[] result = parser.burstToken(token, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken_LongTokenWithTwoCharacterOption() {
        String token = "--long-option--lo";
        String[] expected = {"--long-option--lo", "--lo", "long-option"};
        String[] result = parser.burstToken(token, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken_LongTokenWithTwoCharacterOptionAndArgument() {
        String token = "--long-option--lo";
        options.addOption("lo", true, null, "long-option");
        String[] expected = {"--long-option--lo", "--lo", "long-option"};
        String[] result = parser.burstToken(token, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken_LongTokenWithMultipleOptions() {
        String token = "--long-option-o1-o2";
        options.addOption("o1", true, null, "option1");
        options.addOption("o2", true, null, "option2");
        String[] expected = {"--long-option-o1-o2", "-o1", "option1", "-o2", "option2"};
        String[] result = parser.burstToken(token, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken_LongTokenWithMultipleOptionsAndArguments() {
        String token = "--long-option-o1-o2";
        options.addOption("o1", true, null, "option1");
        options.addOption("o2", true, null, "option2");
        String[] expected = {"--long-option-o1-o2", "-o1", "option1", "-o2", "option2"};
        String[] result = parser.burstToken(token, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testBurstToken_InvalidToken() {
        String token = "invalid";
        parser.burstToken(token, false);
    }

    @Test(expected = ParseException.class)
    public void testBurstToken_LongTokenWithInvalidOption() {
        String token = "--invalid-option";
        parser.burstToken(token, false);
    }

    
}