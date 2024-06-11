package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.test.TestHelper;
import org.junit.BeforeClass;
import org.junit.Test;

public class ArgumentImplTest {
    // 

    private static Argument argument;
    private static WriteableCommandLine commandLine;
    private static List<String> arguments;
    private static Option option;

    @BeforeClass
    public static void setUpBeforeClass() {
        option = new OptionImpl(1, "testOption", true, "Test option");
        argument = new ArgumentImpl("testArg", "Test argument", 1, 1, ' ', ' ', null, "", Collections.emptyList(), 0);
        commandLine = new TestCommandLine();
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("testArg", argument.getPreferredName());
    }

    @Test
    public void testProcessValues() throws OptionException {
        argument.processValues(commandLine, arguments.listIterator(), option);
        assertEquals(1, commandLine.getValues(option).size());
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesWithInvalidArgumentException() throws OptionException {
        String[] args = {"--testOption", "testValue1", "--testOption", "testValue2", "testValue3"};
        TestHelper.setSystemProperty("argline.separator.char", ';');
        commandLine = new TestCommandLine(args);
        arguments = commandLine.parseCommandLineArgs(args);
        argument.processValues(commandLine, arguments.listIterator(), option);

        List<String> values = commandLine.getValues(option);
        assertEquals(2, values.size());
        assertEquals("testValue1", values.get(0));
        assertEquals("testValue2", values.get(1));

        option.validate(commandLine);

        // Test for InvalidArgumentException when a value does not match the validator
        String invalidValue = "invalidValue";
        argument.defaultValues(commandLine, option);
        commandLine.addValue(option, invalidValue);
        option.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesWithMaximumValueExceededException() throws OptionException {
        String[] args = {"--testOption", "--testArg", "--testArg", "--testArg", "--testArg"};
        TestHelper.setSystemProperty("argline.separator.char", ';');
        commandLine = new TestCommandLine(args);
        arguments = commandLine.parseCommandLineArgs(args);
        argument.processValues(commandLine, arguments.listIterator(), option);

        List<String> values = commandLine.getValues(option);
        assertEquals(3, values.size());
        assertEquals("--testArg", values.get(2));

        option.validate(commandLine);
    }

    @Test
    public void testCanProcess() throws OptionException {
        assertTrue(argument.canProcess(commandLine, "testValue"));
    }

    @Test
    public void testGetPrefixes() {
        assertTrue(argument.getPrefixes().isEmpty());
    }

    @Test
    public void testProcess() throws OptionException {
        argument.process(commandLine, arguments.listIterator());
        assertEquals(1, commandLine.getValues(option).size());
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals(' ', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        assertEquals(' ', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetTriggers() {
        assertTrue(argument.getTriggers().isEmpty());
    }

    @Test
    public void testGetConsumeRemaining() {
        assertEquals("", argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        List<String> defaultValues = argument.getDefaultValues();
        assertTrue(defaultValues.isEmpty());
    }

    @Test
    public void testGetValidator() {
        assertNull(argument.getValidator());
    }

    @Test
    public void testValidate() throws OptionException {
        // Test empty argument
        commandLine.clearValues(option);
        argument.validate(commandLine);

        // Test valid argument
        commandLine.addValue(option, "testValue");
        argument.validate(commandLine);

        // Test argument with more values than maximum
        commandLine.addValue(option, "testValue1");
        commandLine.addValue(option, "testValue2");
        commandLine.addValue(option, "testValue3");
        argument.validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, null, null);
        assertEquals("testArg", buffer.toString());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Test argument", argument.getDescription());
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = argument.helpLines(1, null, null);
        assertEquals(1, helpLines.size());
        HelpLine helpLine = helpLines.get(0);
        assertEquals("--testArg <testArg>               Test argument\n", helpLine.getHelp());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(1, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, argument.getMinimum());
    }

    // 
}