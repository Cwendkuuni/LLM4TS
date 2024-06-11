package org.apache.commons.cli;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {
    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten_withNoArgumentsAndNoOptions_shouldReturnEmptyArray() {
        String[] arguments = {};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_withOneArgumentAndNoOptions_shouldReturnArrayWithOneArgument() {
        String[] arguments = {"arg1"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(1, result.length);
        assertEquals("arg1", result[0]);
    }

    @Test
    public void testFlatten_withMultipleArgumentsAndNoOptions_shouldReturnArrayWithAllArguments() {
        String[] arguments = {"arg1", "arg2", "arg3"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(3, result.length);
        assertEquals("arg1", result[0]);
        assertEquals("arg2", result[1]);
        assertEquals("arg3", result[2]);
    }

    @Test
    public void testFlatten_withOneOptionAndNoArguments_shouldReturnEmptyArray() {
        options.addOption("o", false, "option");
        String[] arguments = {};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_withOneOptionAndOneArgument_shouldReturnArrayWithOptionAndArgument() {
        options.addOption("o", true, "option");
        String[] arguments = {"arg1"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(2, result.length);
        assertEquals("-o", result[0]);
        assertEquals("arg1", result[1]);
    }

    @Test
    public void testFlatten_withOneOptionAndMultipleArguments_shouldReturnArrayWithOptionAndAllArguments() {
        options.addOption("o", true, "option");
        String[] arguments = {"arg1", "arg2", "arg3"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(4, result.length);
        assertEquals("-o", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("arg2", result[2]);
        assertEquals("arg3", result[3]);
    }

    @Test
    public void testFlatten_withMultipleOptionsAndNoArguments_shouldReturnEmptyArray() {
        options.addOption("o1", false, "option1");
        options.addOption("o2", false, "option2");
        options.addOption("o3", false, "option3");
        String[] arguments = {};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_withMultipleOptionsAndOneArgument_shouldReturnArrayWithAllOptionsAndArgument() {
        options.addOption("o1", false, "option1");
        options.addOption("o2", false, "option2");
        options.addOption("o3", true, "option3");
        String[] arguments = {"arg1"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(4, result.length);
        assertEquals("-o1", result[0]);
        assertEquals("-o2", result[1]);
        assertEquals("-o3", result[2]);
        assertEquals("arg1", result[3]);
    }

    @Test
    public void testFlatten_withMultipleOptionsAndMultipleArguments_shouldReturnArrayWithAllOptionsAndAllArguments() {
        options.addOption("o1", false, "option1");
        options.addOption("o2", true, "option2");
        options.addOption("o3", true, "option3");
        String[] arguments = {"arg1", "arg2", "arg3"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(6, result.length);
        assertEquals("-o1", result[0]);
        assertEquals("-o2", result[1]);
        assertEquals("arg1", result[2]);
        assertEquals("-o3", result[3]);
        assertEquals("arg2", result[4]);
        assertEquals("arg3", result[5]);
    }

    @Test
    public void testFlatten_withStopAtNonOptionEnabledAndNonOptionArgument_shouldReturnArrayWithOptionAndStopTokenAndNonOptionArgument() {
        options.addOption("o", true, "option");
        String[] arguments = {"-o", "nonOption"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(3, result.length);
        assertEquals("-o", result[0]);
        assertEquals("--", result[1]);
        assertEquals("nonOption", result[2]);
    }

    @Test
    public void testFlatten_withStopAtNonOptionDisabledAndNonOptionArgument_shouldReturnArrayWithOptionAndNonOptionArgument() {
        options.addOption("o", true, "option");
        String[] arguments = {"-o", "nonOption"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(2, result.length);
        assertEquals("-o", result[0]);
        assertEquals("nonOption", result[1]);
    }

    @Test
    public void testFlatten_withBurstTokenAndOption_shouldReturnArrayWithBurstedOption() {
        options.addOption("o", false, "option");
        String[] arguments = {"-oa"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(3, result.length);
        assertEquals("-o", result[0]);
        assertEquals("-a", result[1]);
        assertEquals("a", result[2]);
    }
}