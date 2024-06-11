package org.apache.commons.cli;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    

    @Test
    public void testFlatten_NoOptions() {
        String[] args = {"test"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options(), args, false);
        assertEquals(args.length, result.length);
        for (int i = 0; i < args.length; i++) {
            assertEquals(args[i], result[i]);
        }
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        String[] args = {"--invalid"};
        PosixParser parser = new PosixParser();
        parser.flatten(new Options(), args, false);
    }

    @Test
    public void testFlatten_SingleOption() {
        String[] args = {"--option"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options().addOption("option"), args, false);
        assertEquals(args.length, result.length);
        assertEquals("--option", result[0]);
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlatten_OptionWithValue() {
        String[] args = {"--option", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options().addOption("option", true, "value"), args, false);
        assertEquals(args.length, result.length);
        assertEquals("--option", result[0]);
        assertEquals("value", parser.getCurrentOption().getValue());
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlatten_OptionWithMultipleValues() {
        String[] args = {"--option", "value1", "value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options().addOption("option", true, "value").setArgs(new String[]{"value1", "value2"}), args, false);
        assertEquals(args.length, result.length);
        assertEquals("--option", result[0]);
        assertEquals("value1", parser.getCurrentOption().getValue());
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        String[] args = {"--long-option=value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options().addOption("longOption", true, "value"), args, false);
        assertEquals(args.length, result.length);
        assertEquals("--long-option=value", result[0]);
        assertEquals("value", parser.getCurrentOption().getValue());
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlatten_LongOptionWithMultipleValues() {
        String[] args = {"--long-option=value1", "--long-option=value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options().addOption("longOption", true, "value").setArgs(new String[]{"value1", "value2"}), args, false);
        assertEquals(args.length, result.length);
        assertEquals("--long-option=value1", result[0]);
        assertEquals("value1", parser.getCurrentOption().getValue());
        assertNull(parser.getCurrentOption());
        assertEquals("--long-option=value2", result[1]);
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] args = {"--option", "value", "nonOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options().addOption("option", true, "value"), args, true);
        assertEquals(3, result.length);
        assertEquals("--option", result[0]);
        assertEquals("value", parser.getCurrentOption().getValue());
        assertEquals(null, parser.getCurrentOption());
        assertEquals("nonOption", result[2]);
        assertFalse(parser.eatTheRest);
    }

    @Test
    public void testFlatten_StopAtNonOptionWithRemainingArguments() {
        String[] args = {"--option", "value", "nonOption1", "nonOption2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options().addOption("option", true, "value"), args, true);
        assertEquals(4, result.length);
        assertEquals("--option", result[0]);
        assertEquals("value", parser.getCurrentOption().getValue());
        assertNull(parser.getCurrentOption());
        assertEquals("nonOption1", result[2]);
        assertNull(parser.getCurrentOption());
        assertEquals("nonOption2", result[3]);
        assertFalse(parser.eatTheRest);
    }

    @Test
    public void testFlatten_Hyphen() {
        String[] args = {"-"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options(), args, false);
        assertEquals(args.length, result.length);
        assertEquals("-", result[0]);
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlatten_DoubleHyphen() {
        String[] args = {"--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options(), args, false);
        assertEquals(args.length, result.length);
        assertEquals("--", result[0]);
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlatten_DoubleHyphenWithArguments() {
        String[] args = {"--", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options(), args, false);
        assertEquals(args.length, result.length);
        assertEquals("--", result[0]);
        assertNull(parser.getCurrentOption());
        assertEquals("arg1", result[1]);
        assertNull(parser.getCurrentOption());
        assertEquals("arg2", result[2]);
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken("-o", false);
        assertEquals(2, result.length);
        assertEquals("-o", result[0]);
        assertEquals("o", result[1]);
    }

    @Test
    public void testBurstToken_OptionWithValue() {
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken("-o=value", false);
        assertEquals(3, result.length);
        assertEquals("-o", result[0]);
        assertEquals("o", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testBurstToken_StopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken("-o=value nonOption", true);
        assertEquals(3, result.length);
        assertEquals("-o", result[0]);
        assertEquals("o", result[1]);
        assertEquals("value", result[2]);
        assertTrue(parser.eatTheRest);
    }

    @Test
    public void testBurstToken_InvalidOption() {
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken("-i", false);
        assertNull(result);
    }

    @Test
    public void testBurstToken_NoOptionFound() {
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken("-x", false);
        assertNull(result);
    }

    @Test
    public void testBurstToken_MultipleCharacters() {
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken("-xy", false);
        assertEquals(3, result.length);
        assertEquals("-x", result[0]);
        assertEquals("y", result[1]);
        assertNull(result[2]);
    }

    @Test
    public void testBurstToken_LongOptionWithValue() {
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken("--long-option=value", false);
        assertEquals(3, result.length);
        assertEquals("--long-option", result[0]);
        assertEquals("long-option", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testBurstToken_LongOptionWithMultipleValues() {
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken("--long-option=value1,value2", false);
        assertEquals(4, result.length);
        assertEquals("--long-option", result[0]);
        assertEquals("long-option", result[1]);
        assertEquals("value1", result[2]);
        assertEquals("value2", result[3]);
    }

    
}