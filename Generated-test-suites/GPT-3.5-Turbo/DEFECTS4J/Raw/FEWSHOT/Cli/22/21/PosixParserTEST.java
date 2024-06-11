package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    private Options options;
    private PosixParser parser;

    @Before
    public void setup() {
        options = new Options();
        parser = new PosixParser();
    }

    @Test
    public void testFlatten_stopAtNonOption() {
        String[] result = parser.flatten(options, new String[]{"--option1", "value1", "--option2=value2", "--", "nonOption"}, true);
        assertEquals(4, result.length);
        assertEquals("--option1", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("--option2=value2", result[2]);
        assertEquals("nonOption", result[3]);
    }

    @Test
    public void testFlatten_notStopAtNonOption() {
        String[] result = parser.flatten(options, new String[]{"--option1", "value1", "--option2=value2", "--", "nonOption"}, false);
        assertEquals(6, result.length);
        assertEquals("--option1", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("--option2=value2", result[2]);
        assertEquals("--", result[3]);
        assertEquals("nonOption", result[4]);
        assertEquals("nonOption", result[5]);
    }

    @Test
    public void testFlatten_noArguments() {
        String[] result = parser.flatten(options, new String[]{}, true);
        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_singleHyphen() {
        String[] result = parser.flatten(options, new String[]{"-"}, true);
        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_singleOption() {
        Option option = new Option("o", "option", false, "option");
        options.addOption(option);
        String[] result = parser.flatten(options, new String[]{"-o"}, true);
        assertEquals(1, result.length);
        assertEquals("-o", result[0]);
    }

    @Test
    public void testFlatten_singleOptionWithArgument() {
        Option option = new Option("o", "option", true, "option");
        options.addOption(option);
        String[] result = parser.flatten(options, new String[]{"-o", "value"}, true);
        assertEquals(2, result.length);
        assertEquals("-o", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_invalidOption() {
        String[] result = parser.flatten(options, new String[]{"-x"}, true);
        assertEquals(1, result.length);
        assertEquals("-x", result[0]);
    }

    @Test
    public void testFlatten_noOption() {
        String[] result = parser.flatten(options, new String[]{"value"}, true);
        assertEquals(1, result.length);
        assertEquals("value", result[0]);
    }

    @Test
    public void testFlatten_noOption_stopAtNonOption() {
        String[] result = parser.flatten(options, new String[]{"value", "--"}, true);
        assertEquals(2, result.length);
        assertEquals("value", result[0]);
        assertEquals("--", result[1]);
    }

    @Test
    public void testFlatten_burstToken() {
        Option option = new Option("o", "option", true, "option");
        options.addOption(option);
        String[] result = parser.flatten(options, new String[]{"-ovalue"}, true);
        assertEquals(2, result.length);
        assertEquals("-o", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_burstToken_multipleOptions() {
        Option option1 = new Option("o", "option1", true, "option1");
        Option option2 = new Option("t", "option2", false, "option2");
        options.addOption(option1);
        options.addOption(option2);
        String[] result = parser.flatten(options, new String[]{"-ot"}, true);
        assertEquals(2, result.length);
        assertEquals("-o", result[0]);
        assertEquals("-t", result[1]);
    }
}