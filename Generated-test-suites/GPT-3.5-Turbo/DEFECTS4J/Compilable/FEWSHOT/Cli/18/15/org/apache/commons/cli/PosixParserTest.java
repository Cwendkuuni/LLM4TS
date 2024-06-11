package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    private PosixParser parser;
    private Options options;

    @Before
    public void setup() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten_NoOptions() {
        String[] args = {"arg1", "arg2"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(args, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] args = {"-a", "value1", "arg1", "arg2"};
        String[] result = parser.flatten(options, args, true);
        assertEquals(args, result);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        String[] args = {"-"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(args, result);
    }

    @Test
    public void testFlatten_ExistingOption() {
        Option option = new Option("a", true, "option a");
        options.addOption(option);

        String[] args = {"-a", "value1"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(args, result);
    }

    @Test
    public void testFlatten_UnknownOption_StopAtNonOption() {
        String[] args = {"-a", "value1", "arg1", "arg2"};
        String[] result = parser.flatten(options, args, true);
        assertEquals(args, result);
    }

    @Test
    public void testFlatten_UnknownOption_NoStopAtNonOption() {
        String[] args = {"-a", "value1"};
        String[] expectedResult = {"-a"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFlatten_BurstToken_NoStopAtNonOption() {
        Option option = new Option("a", true, "option a");
        options.addOption(option);

        String[] args = {"-abc"};
        String[] expectedResult = {"-a", "-b", "-c"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFlatten_BurstToken_StopAtNonOption() {
        String[] args = {"-abc", "value", "arg1"};
        String[] result = parser.flatten(options, args, true);
        assertEquals(args, result);
    }

    @Test
    public void testFlatten_SpecialTokenWithValue() {
        String[] args = {"--option=value"};
        String[] expectedResult = {"--option", "value"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFlatten_SpecialTokenWithoutValue() {
        String[] args = {"--option"};
        String[] expectedResult = {"--option"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFlatten_NullOptionArgument() {
        Option option = new Option("a", true, "option a");
        options.addOption(option);

        String[] args = {"-a", null};
        String[] result = parser.flatten(options, args, false);
        assertEquals(args, result);
    }
}