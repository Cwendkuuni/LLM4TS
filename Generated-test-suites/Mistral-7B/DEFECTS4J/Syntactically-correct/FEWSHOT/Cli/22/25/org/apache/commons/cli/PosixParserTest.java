package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten_Empty() {
        Options options = new Options();
        String[] arguments = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertNull(result);
    }

    @Test
    public void testFlatten_Simple() {
        Options options = new Options();
        String[] arguments = {"--foo", "--bar=baz"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--foo", "--bar", "baz"}, result);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        Options options = new Options();
        String[] arguments = {"-f"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"-f"}, result);
    }

    @Test
    public void testFlatten_SingleOption() {
        Options options = new Options();
        options.addOption("f", false, null, "Force");
        String[] arguments = {"--f"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--f"}, result);
        assertEquals(options.getOption("f"), parser.getOption());
    }

    @Test
    public void testFlatten_SingleOptionWithArg() {
        Options options = new Options();
        options.addOption("f", true, "arg", "Force with argument");
        String[] arguments = {"--f", "arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--f", "arg"}, result);
        assertEquals(options.getOption("f"), parser.getOption());
        assertEquals("arg", parser.getOptionValue());
    }

    @Test
    public void testFlatten_MultipleOptions() {
        Options options = new Options();
        options.addOption("f", false, null, "Force");
        options.addOption("b", false, null, "Bar");
        String[] arguments = {"--f", "--b"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--f", "--b"}, result);
        assertSame(options.getOption("f"), parser.getOption());
        assertSame(options.getOption("b"), null);
    }

    @Test
    public void testFlatten_MultipleOptionsWithArgs() {
        Options options = new Options();
        options.addOption("f", true, null, "Force with argument");
        options.addOption("b", true, null, "Bar with argument");
        String[] arguments = {"--f", "argF", "--b", "argB"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--f", "argF", "--b", "argB"}, result);
        assertEquals(options.getOption("f"), parser.getOption());
        assertEquals("argF", parser.getOptionValue());
        assertEquals(options.getOption("b"), parser.getOption());
        assertEquals("argB", parser.getOptionValue());
    }

    @Test
    public void testFlatten_OptionsWithSameLetter() {
        Options options = new Options();
        options.addOption("f", false, null, "Force 1");
        options.addOption("f", false, null, "Force 2");
        String[] arguments = {"--f", "--f"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--f", "--f"}, result);
        assertSame(options.getOption("f"), parser.getOption());
        List<Option> optionsList = options.getOptions();
        Option option1 = optionsList.get(0);
        Option option2 = optionsList.get(1);
        assertTrue(option1.isAllowed(null));
        assertTrue(option2.isAllowed(null));
        assertFalse(option1.isAllowed(""));
        assertFalse(option2.isAllowed(""));
        assertNull(parser.getOptionValue());
    }

    @Test
    public void testFlatten_MultipleOptionsWithArgs_StopAtNonOption() {
        Options options = new Options();
        options.addOption("f", true, null, "Force with argument");
        options.addOption("b", true, null, "Bar with argument");
        String[] arguments = {"--f", "argF", "nonOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(new String[]{"--f", "argF", "nonOption"}, result);
    }

    @Test
    public void testFlatten_DashAsArgument() {
        Options options = new Options();
        options.addOption("f", true, null, "Force with argument");
        String[] arguments = {"--f=-"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--f=-"}, result);
        assertEquals("--", parser.getOptionValue());
    }

    @Test
    public void testFlatten_DashBeforeOption() {
        Options options = new Options();
        options.addOption("f", true, null, "Force with argument");
        String[] arguments = {"-", "--f", "argF"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"-", "--f", "argF"}, result);
        assertEquals(null, parser.getOption());
        assertEquals("argF", parser.getOptionValue());
    }

    @Test
    public void testFlatten_DashAfterOption() {
        Options options = new Options();
        options.addOption("f", true, null, "Force with argument");
        String[] arguments = {"--f", "argF-"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--f", "argF-"}, result);
        assertEquals(options.getOption("f"), parser.getOption());
        assertEquals("argF", parser.getOptionValue());
        assertEquals("-", result[result.length - 1]);
    }

    @Test
    public void testFlatten_EmptyOptionValue() {
        Options options = new Options();
        options.addOption("f", true, null, "Force with argument");
        String[] arguments = {"--f", ""};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--f", ""}, result);
        assertEquals(options.getOption("f"), parser.getOption());
        assertEquals("", parser.getOptionValue());
    }

    @Test
    public void testFlatten_EmptyOptionValueWithArg() {
        Options options = new Options();
        options.addOption("f", true, null, "Force with argument");
        String[] arguments = {"--f", "", "arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--f", "", "arg"}, result);
        assertEquals(options.getOption("f"), parser.getOption());
        assertEquals("", parser.getOptionValue());
        assertEquals("arg", result[result.length - 1]);
    }

    @Test
    public void testFlatten_EmptyArgumentArray() {
        Options options = new Options();
        String[] arguments = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertNull(result);
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        String token = "--foo--bar=baz--qux";
        List<String> result = parser.burstToken(token, false);
        assertEquals(Arrays.asList("--foo", "--bar", "baz", "--qux"), result);
    }

    @Test
    public void testBurstToken_StopAtNonOption() {
        PosixParser parser = new PosixParser();
        String token = "--foo--bar=baz--qux--nonOption";
        List<String> result = parser.burstToken(token, true);
        assertEquals(Arrays.asList("--foo", "--bar", "baz", "--qux", "--nonOption"), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBurstToken_EmptyToken() {
        PosixParser parser = new PosixParser();
        parser.burstToken("", false);
    }

    private Option getOption() {
        Options options = new Options();
        options.addOption("f", false, null, "Force");
        PosixParser parser = new PosixParser();
        parser.flatten(options, new String[]{"--f"}, false);
        return parser.getOption();
    }
}