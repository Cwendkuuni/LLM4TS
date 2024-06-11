package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten_EmptyArgs() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {};

        String[] result = parser.flatten(options, args, false);
        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_NoOptions() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"arg0"};

        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("arg0", result[0]);
    }

    @Test
    public void testFlatten_SingleOption() {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Default value");
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String[] args = {"-o"};

        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("-o", result[0]);

        result = parser.flatten(options, args, true);
        assertEquals(1, result.length);
        assertEquals("-o", result[0]);
    }

    @Test
    public void testFlatten_SingleOptionWithValue() {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Default value");
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String[] args = {"-o", "value"};

        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("-o", result[0]);
        assertEquals("value", result[1]);

        result = parser.flatten(options, args, true);
        assertEquals(1, result.length);
        assertEquals("-o value", result[0]);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        Options options = new Options();
        Option option1 = new Option("o1", "opt1", true, "Default value1");
        Option option2 = new Option("o2", "opt2", true, "Default value2");
        options.addOption(option1);
        options.addOption(option2);
        PosixParser parser = new PosixParser();
        String[] args = {"-o1", "value1", "-o2", "value2"};

        String[] result = parser.flatten(options, args, false);
        assertEquals(4, result.length);
        assertEquals("-o1", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-o2", result[2]);
        assertEquals("value2", result[3]);

        result = parser.flatten(options, args, true);
        assertEquals(2, result.length);
        assertEquals("-o1 value1", result[0]);
        assertEquals("-o2 value2", result[1]);
    }

    @Test
    public void testFlatten_LongOption() {
        Options options = new Options();
        Option longOption = new Option("lo", "longOption", true, "Default value");
        options.addOption(longOption);
        PosixParser parser = new PosixParser();
        String[] args = {"--longOption", "value"};

        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("--longOption", result[0]);
        assertEquals("value", result[1]);

        result = parser.flatten(options, args, true);
        assertEquals(1, result.length);
        assertEquals("--longOption value", result[0]);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        Options options = new Options();
        Option longOption = new Option("lo", "longOption", true, "Default value");
        options.addOption(longOption);
        PosixParser parser = new PosixParser();
        String[] args = {"--longOption", "value1", "--longOption", "value2"};

        String[] result = parser.flatten(options, args, false);
        assertEquals(4, result.length);
        assertEquals("--longOption", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("--longOption", result[2]);
        assertEquals("value2", result[3]);

        result = parser.flatten(options, args, true);
        assertEquals(2, result.length);
        assertEquals("--longOption value1", result[0]);
        assertEquals("--longOption value2", result[1]);
    }

    @Test
    public void testFlatten_OptionWithArguments() {
        Options options = new Options();
        Option option = new Option("o", "opt", 1, "Argument1,Argument2");
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String[] args = {"-o", "arg1", "arg2"};

        String[] result = parser.flatten(options, args, false);
        assertEquals(3, result.length);
        assertEquals("-o", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("arg2", result[2]);

        result = parser.flatten(options, args, true);
        assertEquals(2, result.length);
        assertEquals("-o arg1 arg2", result[0]);
    }

    @Test
    public void testFlatten_OptionWithLongArguments() {
        Options options = new Options();
        Option option = new Option("o", "opt", 1, "Argument1 Argument2");
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String[] args = {"-o", "Argument1 Argument2"};

        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("-o", result[0]);
        assertEquals("Argument1 Argument2", result[1]);

        result = parser.flatten(options, args, true);
        assertEquals(1, result.length);
        assertEquals("-o Argument1 Argument2", result[0]);
    }

    @Test
    public void testFlatten_Bursting() {
        Options options = new Options();
        Option option = new Option("o", "opt");
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String[] args = {"-oabc"};

        String[] result = parser.flatten(options, args, false);
        assertEquals(3, result.length);
        assertEquals("-o", result[0]);
        assertEquals("a", result[1]);
        assertEquals("b", result[2]);

        result = parser.flatten(options, args, true);
        assertEquals(2, result.length);
        assertEquals("-o a b", result[0]);
    }

    @Test
    public void testFlatten_BurstingWithStopAtNonOption() {
        Options options = new Options();
        Option option = new Option("o", "opt");
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String[] args = {"-o", "abc", "def"};

        String[] result = parser.flatten(options, args, false);
        assertEquals(3, result.length);
        assertEquals("-o", result[0]);
        assertEquals("abc", result[1]);
        assertEquals("def", result[2]);

        result = parser.flatten(options, args, true);
        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("abc def", result[1]);
    }

    @Test
    public void testFlatten_EmptyOptionValue() {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "");
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String[] args = {"-o"};

        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("-o", result[0]);

        result = parser.flatten(options, args, true);
        assertEquals(1, result.length);
        assertEquals("-o", result[0]);
    }

    @Test
    public void testFlatten_LongOptionWithEmptyValue() {
        Options options = new Options();
        Option longOption = new Option("lo", "longOption", true, "");
        options.addOption(longOption);
        PosixParser parser = new PosixParser();
        String[] args = {"--longOption"};

        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("--longOption", result[0]);

        result = parser.flatten(options, args, true);
        assertEquals(1, result.length);
        assertEquals("--longOption", result[0]);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        Option option = new Option("o", "opt");
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String[] args = {"-o", "arg", "non-option"};

        String[] result = parser.flatten(options, args, false);
        assertEquals(3, result.length);
        assertEquals("-o", result[0]);
        assertEquals("arg", result[1]);
        assertEquals("non-option", result[2]);

        result = parser.flatten(options, args, true);
        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("arg non-option", result[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_InvalidOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"-x"};

        parser.flatten(options, args, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_InvalidOptionWithValue() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"-x", "value"};

        parser.flatten(options, args, false);
    }

    @Test
    public void testBurstToken_OptionExists() {
        Options options = new Options();
        Option option = new Option("o", "opt");
        options.addOption(option);
        PosixParser parser = new PosixParser();

        String[] result = parser.burstToken("-oabcd", false);
        assertEquals(3, result.length);
        assertEquals("-o", result[0]);
        assertEquals("a", result[1]);
        assertEquals("bcd", result[2]);

        result = parser.burstToken("-oabcd", true);
        assertEquals(1, result.length);
        assertEquals("--", result[0]);
        assertEquals("a bcd", result[1]);
    }

    @Test
    public void testBurstToken_OptionDoesNotExist() {
        Options options = new Options();
        PosixParser parser = new PosixParser();

        String[] result = parser.burstToken("-xabcd", false);
        assertEquals(1, result.length);
        assertEquals("-xabcd", result[0]);

        result = parser.burstToken("-xabcd", true);
        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("xabcd", result[1]);
    }

    @Test
    public void testBurstToken_OptionDoesNotExistWithStopAtNonOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();

        String[] result = parser.burstToken("-x", true);
        assertEquals(1, result.length);
        assertEquals("--", result[0]);
        assertEquals("x", result[1]);
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();

        parser.process("-o", false);
        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);

        parser.process("--longOption", false);
        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);

        parser.process("-o", true);
        assertTrue(parser.eatTheRest);
        assertNull(parser.currentOption);

        parser.process("--longOption", true);
        assertTrue(parser.eatTheRest);
        assertNull(parser.currentOption);

        parser.process("arg", false);
        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);

        parser.process("--", false);
        assertTrue(parser.eatTheRest);
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();

        Options options = new Options();
        Option option = new Option("o", "opt");
        options.addOption(option);

        parser.processOptionToken("-o", options, false);
        assertSame(option, parser.currentOption);

        parser.processOptionToken("-o", options, true);
        assertNull(parser.currentOption);

        parser.processOptionToken("--longOption", options, false);
        assertSame(null, parser.currentOption);

        parser.processOptionToken("--longOption", options, true);
        assertSame(null, parser.currentOption);

        parser.processOptionToken("arg", options, false);
        assertNull(parser.currentOption);

        parser.processOptionToken("--", options, false);
        assertNull(parser.currentOption);
    }
}