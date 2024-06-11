package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten_EmptyArgs() {
        Options options = new Options();
        String[] args = new String[0];
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlatten_SingleDash() {
        Options options = new Options();
        String[] args = { "-" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[] { "--" }, result);
    }

    @Test
    public void testFlatten_SingleOption() {
        Options options = new Options();
        options.addOption("a", true, "a help message");
        String[] args = { "-a" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[] { "-a" }, result);
    }

    @Test
    public void testFlatten_SingleOptionWithValue() {
        Options options = new Options();
        options.addOption("a", true, "a help message");
        String[] args = { "-a", "value" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[] { "-a", "value" }, result);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        Options options = new Options();
        options.addOption("a", true, "a help message");
        options.addOption("b", true, "b help message");
        String[] args = { "-a", "-b" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[] { "-a", "-b" }, result);
    }

    @Test
    public void testFlatten_OptionWithValue() {
        Options options = new Options();
        options.addOption("a", true, "a help message");
        options.addOption("b", true, "b help message");
        String[] args = { "--a", "value", "-b" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[] { "--a", "value", "-b" }, result);
    }

    @Test
    public void testFlatten_OptionWithLongName() {
        Options options = new Options();
        options.addOption("longOption", true, "a help message");
        String[] args = { "--longOption" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[] { "--longOption" }, result);
    }

    @Test
    public void testFlatten_OptionWithLongNameAndValue() {
        Options options = new Options();
        options.addOption("longOption", true, "a help message");
        String[] args = { "--longOption=value" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[] { "--longOption=value" }, result);
    }

    @Test
    public void testFlatten_MultipleOptionsWithValues() {
        Options options = new Options();
        options.addOption("a", true, "a help message");
        options.addOption("b", true, "b help message");
        String[] args = { "--a=value1", "--b=value2" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[] { "--a=value1", "--b=value2" }, result);
    }

    @Test
    public void testFlatten_SingleDashWithStopAtNonOption() {
        Options options = new Options();
        String[] args = { "-" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[] { "--" }, result);
    }

    @Test
    public void testFlatten_MultipleTokensWithStopAtNonOption() {
        Options options = new Options();
        String[] args = { "--", "token1", "token2" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[] { "--token1", "--token2" }, result);
    }

    @Test
    public void testFlatten_OptionWithLongNameAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("longOption", true, "a help message");
        String[] args = { "--longOption", "--" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[] { "--longOption" }, result);
    }

    @Test
    public void testFlatten_OptionWithValueAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "a help message");
        String[] args = { "--a", "--" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[] { "--a" }, result);
    }

    @Test
    public void testFlatten_OptionWithLongNameValueAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("longOption", true, "a help message");
        String[] args = { "--longOption=value", "--" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[] { "--longOption=value" }, result);
    }

    @Test
    public void testFlatten_OptionWithArgumentAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", 1, "a help message", "arg");
        String[] args = { "--a", "arg1", "--" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[] { "--a=arg1" }, result);
    }

    @Test
    public void testFlatten_OptionWithArgumentsAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", 1, "a help message", "arg1 arg2");
        String[] args = { "--a", "arg1", "arg2", "--" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[] { "--a=arg1", "--a=arg2" }, result);
    }

    @Test
    public void testFlatten_OptionWithLongNameArgumentAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("longOption", 1, "a help message", "longArg");
        String[] args = { "--longOption", "longArg1 longArg2", "--" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[] { "--longOption=longArg1", "--longOption=longArg2" }, result);
    }

    @Test
    public void testFlatten_MultipleOptionsWithArgumentsAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", 1, "a help message", "arg");
        options.addOption("b", 1, "b help message", "arg");
        String[] args = { "--a=arg1", "arg2", "--b=arg3" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[] { "--a=arg1", "--b=arg3" }, result);
    }

    @Test
    public void testFlatten_MultiTokenOption() {
        Options options = new Options();
        options.addOption("multiToken", false, "a help message", "multi");
        String[] args = { "--multiToken=multiToken1 multiToken2" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[] { "--multiToken=multiToken1", "--multiToken=multiToken2" }, result);
    }

    @Test
    public void testFlatten_MultiTokenOptionWithValues() {
        Options options = new Options();
        options.addOption("multiToken", false, "a help message", "multiToken1 multiToken2");
        String[] args = { "--multiToken=multiToken1 value1", "--multiToken=multiToken2 value2" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[] { "--multiToken=multiToken1=value1", "--multiToken=multiToken2=value2" }, result);
    }

    @Test
    public void testFlatten_MultiTokenOptionWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("multiToken", false, "a help message", "multiToken1 multiToken2");
        String[] args = { "--multiToken=multiToken1 multiToken2", "--" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[] { "--multiToken=multiToken1" }, result);
    }

    @Test
    public void testFlatten_MultiTokenOptionWithValuesAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("multiToken", false, "a help message", "multiToken1 multiToken2");
        String[] args = { "--multiToken=multiToken1 value1 multiToken2=value2", "--" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[] { "--multiToken=multiToken1=value1" }, result);
    }

    @Test
    public void testFlatten_CustomOptionClass() {
        Options options = new Options();
        options.addOption(new CustomOption("customOption", true, "a help message"));
        String[] args = { "--customOption" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[] { "--customOption" }, result);
    }

    @Test
    public void testFlatten_CustomOptionClassWithValue() {
        Options options = new Options();
        options.addOption(new CustomOption("customOption", true, "a help message"));
        String[] args = { "--customOption=value" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[] { "--customOption=value" }, result);
    }

    @Test
    public void testFlatten_CustomOptionClassWithStopAtNonOption() {
        Options options = new Options();
        options.addOption(new CustomOption("customOption", true, "a help message"));
        String[] args = { "--customOption=value", "--" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[] { "--customOption=value" }, result);
    }

    @Test
    public void testFlatten_CombinedCases() {
        Options options = new Options();
        options.addOption("a", true, "a help message");
        options.addOption("b", true, "b help message");
        options.addOption("c", 1, "c help message", "arg");
        options.addOption("d", 1, "d help message", "arg1 arg2");
        String[] args = { "--a", "--b", "-c", "arg", "--d=arg1 arg2" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[] { "--a", "--b", "--c=arg", "--d=arg1", "--d=arg2" }, result);
    }

    @Test
    public void testFlatten_CombinedCasesWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "a help message");
        options.addOption("b", true, "b help message");
        options.addOption("c", 1, "c help message", "arg");
        options.addOption("d", 1, "d help message", "arg1 arg2");
        String[] args = { "--a", "--b", "-c", "arg", "--d=arg1 arg2", "--" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[] { "--a", "--b", "--c=arg" }, result);
    }

    private static class CustomOption extends Option {
        public CustomOption(String longOpt, boolean required, String optDescription) {
            super(longOpt, required, optDescription, new String[0]);
        }
    }
}