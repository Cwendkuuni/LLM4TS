package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    private PosixParser parser;
    private Options options;

    @Before
    public void setup() {
        parser = new PosixParser();
        options = new Options();
        options.addOption("f", false, "");
        options.addOption("foo", true, "");
        options.addOption("bar", false, "");
    }

    @Test
    public void testFlatten_StopAtNonOption_True() {
        String[] arguments = { "-f", "--", "arg1", "arg2" };
        String[] result = parser.flatten(options, arguments, true);
        assertEquals("-f", result[0]);
        assertEquals("--", result[1]);
        assertEquals("arg1", result[2]);
        assertEquals("arg2", result[3]);
    }

    @Test
    public void testFlatten_StopAtNonOption_False() {
        String[] arguments = { "-f", "--", "arg1", "arg2" };
        String[] result = parser.flatten(options, arguments, false);
        assertEquals("-f", result[0]);
        assertEquals("--", result[1]);
        assertEquals("arg1", result[2]);
        assertEquals("arg2", result[3]);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        String[] arguments = { "-" };
        String[] result = parser.flatten(options, arguments, true);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_ShortOptionWithValue() {
        String[] arguments = { "-foo", "value" };
        String[] result = parser.flatten(options, arguments, true);
        assertEquals("-foo", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_ShortOptionNoValue() {
        String[] arguments = { "-f" };
        String[] result = parser.flatten(options, arguments, true);
        assertEquals("-f", result[0]);
    }

    @Test
    public void testFlatten_ShortOptionNoValue_NoSuchOption_StopAtNonOption_True() {
        String[] arguments = { "-z" };
        String[] result = parser.flatten(options, arguments, true);
        assertEquals("--", result[0]);
        assertEquals("-z", result[1]);
    }

    @Test
    public void testFlatten_ShortOptionNoValue_NoSuchOption_StopAtNonOption_False() {
        String[] arguments = { "-z" };
        String[] result = parser.flatten(options, arguments, false);
        assertEquals("-z", result[0]);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        String[] arguments = { "--foo", "value" };
        String[] result = parser.flatten(options, arguments, true);
        assertEquals("--foo", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_LongOptionNoValue() {
        String[] arguments = { "--bar" };
        String[] result = parser.flatten(options, arguments, true);
        assertEquals("--bar", result[0]);
    }

    @Test
    public void testFlatten_LongOptionNoValue_NoSuchOption_StopAtNonOption_True() {
        String[] arguments = { "--baz" };
        String[] result = parser.flatten(options, arguments, true);
        assertEquals("--", result[0]);
        assertEquals("--baz", result[1]);
    }

    @Test
    public void testFlatten_LongOptionNoValue_NoSuchOption_StopAtNonOption_False() {
        String[] arguments = { "--baz" };
        String[] result = parser.flatten(options, arguments, false);
        assertEquals("--baz", result[0]);
    }

    @Test
    public void testFlatten_MultipleShortOptionsWithValue() {
        String[] arguments = { "-foo", "value1", "-f", "-foo", "value2" };
        String[] result = parser.flatten(options, arguments, true);
        assertEquals("-foo", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-f", result[2]);
        assertEquals("-foo", result[3]);
        assertEquals("value2", result[4]);
    }

    @Test
    public void testFlatten_MultipleShortOptionsNoValue() {
        String[] arguments = { "-f", "-bar", "-f" };
        String[] result = parser.flatten(options, arguments, true);
        assertEquals("-f", result[0]);
        assertEquals("-bar", result[1]);
        assertEquals("-f", result[2]);
    }

    @Test
    public void testFlatten_MixedShortAndLongOptions() {
        String[] arguments = { "-f", "-foo", "value", "--bar" };
        String[] result = parser.flatten(options, arguments, true);
        assertEquals("-f", result[0]);
        assertEquals("-foo", result[1]);
        assertEquals("value", result[2]);
        assertEquals("--bar", result[3]);
    }
}