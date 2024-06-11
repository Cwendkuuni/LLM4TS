package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlattenEmptyArguments() {
        Options options = new Options();
        String[] arguments = new String[0];
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenSingleArgument() {
        Options options = new Options();
        String[] arguments = new String[]{"arg"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"arg"}, result);
    }

    @Test
    public void testFlattenSingleOption() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "An option");
        options.addOption(option);
        String[] arguments = new String[]{"-o"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-o"}, result);
    }

    @Test
    public void testFlattenOptionWithValue() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "An option");
        options.addOption(option);
        String[] arguments = new String[]{"-o", "value"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-o", "value"}, result);
    }

    @Test
    public void testFlattenDoubleHyphen() {
        Options options = new Options();
        String[] arguments = new String[]{"--"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--"}, result);
    }

    @Test
    public void testFlattenDoubleHyphenWithArgument() {
        Options options = new Options();
        String[] arguments = new String[]{"--arg"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--arg"}, result);
    }

    @Test
    public void testFlattenDoubleDashFollowedBySingleHyphen() {
        Options options = new Options();
        String[] arguments = new String[]{"-- -"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--", "-"}, result);
    }

    @Test
    public void testFlattenDoubleDashFollowedByTwoHyphens() {
        Options options = new Options();
        String[] arguments = new String[]{"-- --"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--"}, result);
    }

    @Test
    public void testFlattenDoubleDashFollowedByOption() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "An option");
        options.addOption(option);
        String[] arguments = new String[]{"--o"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--o"}, result);
    }

    @Test
    public void testFlattenDoubleDashFollowedByOptionWithValue() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "An option");
        options.addOption(option);
        String[] arguments = new String[]{"--o", "value"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--o", "value"}, result);
    }

    @Test
    public void testFlattenOptionWithLongName() {
        Options options = new Options();
        Option option = new Option("l", "longOption", true, "A long option");
        options.addOption(option);
        String[] arguments = new String[]{"-l", "longOptionValue"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-l", "longOptionValue"}, result);
    }

    @Test
    public void testFlattenOptionWithLongNameAndValue() {
        Options options = new Options();
        Option option = new Option("l", "longOption", true, "A long option");
        options.addOption(option);
        String[] arguments = new String[]{"-l", "longOptionValue=value"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-l", "longOptionValue"}, result);
    }

    @Test
    public void testFlattenOptionWithShortNameAndLongName() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "An option");
        Option longOption = new Option("l", "longOption", true, "A long option");
        options.addOption(option);
        options.addOption(longOption);
        String[] arguments = new String[]{"-o", "--longOption", "value"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-o", "--longOption"}, result);
    }

    @Test
    public void testFlattenOptionWithShortNameAndLongNameWithValue() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "An option");
        Option longOption = new Option("l", "longOption", true, "A long option");
        options.addOption(option);
        options.addOption(longOption);
        String[] arguments = new String[]{"-o", "--longOption=value"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-o", "--longOption=value"}, result);
    }

    @Test
    public void testFlattenMultipleOptionsWithShortNames() {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = new String[]{"-o1", "-o2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-o1", "-o2"}, result);
    }

    @Test
    public void testFlattenMultipleOptionsWithShortNamesAndValues() {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = new String[]{"-o1", "value1", "-o2", "value2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-o1", "value1", "-o2", "value2"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "An option");
        options.addOption(option);
        String[] arguments = new String[]{"-o", "arg"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"-o"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndSingleOption() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "An option");
        options.addOption(option);
        String[] arguments = new String[]{"-o"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"-o"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndManyOptions() {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = new String[]{"-o1", "-o2", "arg"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"-o1", "-o2"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndSingleNonOption() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "An option");
        options.addOption(option);
        String[] arguments = new String[]{"arg"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertNull(result);
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndManyNonOptions() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "An option");
        options.addOption(option);
        String[] arguments = new String[]{"arg1", "arg2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertNull(result);
    }
}