package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlatten_NoOptions() {
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        Options options = new Options();
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("arg1");
        expected.add("arg2");
        expected.add("arg3");
        assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_SingleOption_LongFormat() {
        String[] args = new String[]{"--option"};
        Options options = new Options();
        options.addOption("option", true, "o", "opt");
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("option");
        assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_SingleOption_ShortFormat() {
        String[] args = new String[]{"-o"};
        Options options = new Options();
        options.addOption("option", true, "o", "opt");
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("option");
        assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_SingleOption_WithValue() {
        String[] args = new String[]{"--option", "value"};
        Options options = new Options();
        options.addOption("option", true, "o", "opt");
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("option");
        expected.add("value");
        assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_SingleOption_WithValue_ShortFormat() {
        String[] args = new String[]{"-o", "value"};
        Options options = new Options();
        options.addOption("option", true, "o", "opt");
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("option");
        expected.add("value");
        assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        String[] args = new String[]{"--option1", "--option2", "arg"};
        Options options = new Options();
        options.addOption("option1", true, "o1", "opt1");
        options.addOption("option2", true, "o2", "opt2");
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("option1");
        expected.add("arg");
        expected.add("option2");
        assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_MultipleOptions_WithValues() {
        String[] args = new String[]{"--option1", "value1", "--option2", "value2"};
        Options options = new Options();
        options.addOption("option1", true, "o1", "opt1");
        options.addOption("option2", true, "o2", "opt2");
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("option1");
        expected.add("value1");
        expected.add("option2");
        expected.add("value2");
        assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] args = new String[]{"--option", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("option", true, "o", "opt");
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, true);

        List<String> expected = new ArrayList<>();
        expected.add("option");
        assertArrayEquals(expected.toArray(new String[0]), Arrays.copyOfRange(result, 0, 1));
        assertEquals("arg1, arg2", Arrays.toString(Arrays.copyOfRange(result, 1, result.length)));
    }

    @Test
    public void testFlatten_DoubleDash() {
        String[] args = new String[]{"--"};
        Options options = new Options();
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("--");
        assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_DoubleDashWithOptions() {
        String[] args = new String[]{"--", "--option"};
        Options options = new Options();
        options.addOption("option", true, "o", "opt");
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("option");
        assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_DoubleDashWithArguments() {
        String[] args = new String[]{"--", "arg1", "arg2"};
        Options options = new Options();
        GnuParser parser = new GnuParser();
        assertArrayEquals(new String[0], parser.flatten(options, args, false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_EmptyArguments() {
        String[] args = new String[0];
        Options options = new Options();
        GnuParser parser = new GnuParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_NullArguments() {
        String[] args = null;
        Options options = new Options();
        GnuParser parser = new GnuParser();
        assertNull(parser.flatten(options, args, false));
    }
}