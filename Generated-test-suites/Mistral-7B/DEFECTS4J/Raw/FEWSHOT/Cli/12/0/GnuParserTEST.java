package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testGnuParser_Flatten_EmptyArgs() {
        Options options = new Options();
        String[] arguments = new String[0];
        String[] expected = new String[0];

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_Flatten_SingleDash() {
        Options options = new Options();
        String[] arguments = new String[]{"-"};
        String[] expected = new String[]{"-"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_Flatten_SingleOption() {
        Options options = new Options();
        Option option = new Option("o", "option");
        options.addOption(option);
        String[] arguments = new String[]{"-o"};
        String[] expected = new String[]{"-o"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_Flatten_OptionWithValue() {
        Options options = new Options();
        Option option = new Option("o", "option");
        option.setArgName("value");
        options.addOption(option);
        String[] arguments = new String[]{"-o", "value"};
        String[] expected = new String[]{"-o", "value"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_Flatten_SpecialOption() {
        Options options = new Options();
        Option option = new Option("D", "property");
        option.setValueSeparator('=');
        options.addOption(option);
        String[] arguments = new String[]{"-Dproperty=value"};
        String[] expected = new String[]{"-D", "property", "value"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_Flatten_DoubleDash() {
        Options options = new Options();
        String[] arguments = new String[]{"--"};
        String[] expected = new String[]{"--"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_Flatten_EmptyDoubleDash() {
        Options options = new Options();
        String[] arguments = new String[]{"--"};
        String[] expected = new String[0];

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_Flatten_EmptyDoubleDashWithArguments() {
        Options options = new Options();
        String[] arguments = new String[]{"--", "arg1", "arg2"};
        String[] expected = new String[]{"arg1", "arg2"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_Flatten_MixedOptionsAndArguments() {
        Options options = new Options();
        Option option = new Option("o", "option");
        options.addOption(option);
        String[] arguments = new String[]{"-o", "value1", "-o", "value2", "--", "arg1", "arg2"};
        String[] expected = new String[]{"-o", "value1", "-o", "value2", "arg1", "arg2"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_Flatten_LongOption() {
        Options options = new Options();
        Option longOption = new Option(null, "long-option");
        longOption.setLongOptName("longOption");
        options.addOption(longOption);
        String[] arguments = new String[]{"--longOption"};
        String[] expected = new String[]{"--longOption"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_Flatten_LongOptionWithValue() {
        Options options = new Options();
        Option longOption = new Option(null, "long-option");
        longOption.setLongOptName("longOption");
        longOption.setArgName("value");
        options.addOption(longOption);
        String[] arguments = new String[]{"--longOption", "value"};
        String[] expected = new String[]{"--longOption", "value"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_Flatten_StopAtNonOption() {
        Options options = new Options();
        Option option = new Option("o", "option");
        options.addOption(option);
        String[] arguments = new String[]{"-o", "value1", "arg1", "arg2"};
        String[] expected = new String[]{"-o", "value1"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGnuParser_Flatten_InvalidOption() {
        Options options = new Options();
        String[] arguments = new String[]{"-x"};
        GnuParser parser = new GnuParser();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testGnuParser_Flatten_NullArguments() {
        Options options = new Options();
        String[] arguments = null;
        String[] expected = new String[0];

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_Flatten_NullOptions() {
        String[] arguments = new String[]{"arg1", "arg2"};
        GnuParser parser = new GnuParser();
        assertNull(parser.flatten(null, arguments, false));
    }
}