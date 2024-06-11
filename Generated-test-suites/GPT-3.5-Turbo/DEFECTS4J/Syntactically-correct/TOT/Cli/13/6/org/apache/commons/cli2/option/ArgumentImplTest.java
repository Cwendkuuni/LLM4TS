package org.apache.commons.cli2.option;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class ArgumentImplTest {

    @Test
    public void testGetPreferredName() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--", null, 0);
        Assert.assertEquals("name", argument.getPreferredName());
    }

    @Test
    public void testProcessValues() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 3, '\0', '\0', null, "--", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator arguments = Collections.singletonList("value1").listIterator();

        argument.processValues(commandLine, arguments, argument);
        Assert.assertEquals("value1", commandLine.getValues(argument).get(0));
    }

    @Test
    public void testCanProcess() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Assert.assertTrue(argument.canProcess(commandLine, "value1"));
    }

    @Test
    public void testGetInitialSeparator() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '-', '\0', null, "--", null, 0);
        Assert.assertEquals('-', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', ',', null, "--", null, 0);
        Assert.assertEquals(',', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetConsumeRemaining() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--", null, 0);
        Assert.assertEquals("--", argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        List defaultValues = Collections.singletonList("defaultValue");
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--", defaultValues, 0);
        Assert.assertEquals(defaultValues, argument.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        Validator validator = new ValidatorImpl();
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', validator, "--", null, 0);
        Assert.assertEquals(validator, argument.getValidator());
    }

    @Test
    public void testValidateWithValidValues() throws OptionException {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(argument, "value");
        argument.validate(commandLine, argument);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInsufficientValues() throws OptionException {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 2, 3, '\0', '\0', null, "--", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(argument, "value");
        argument.validate(commandLine, argument);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithExcessiveValues() throws OptionException {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 2, '\0', '\0', null, "--", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        commandLine.addValue(argument, "value3");
        argument.validate(commandLine, argument);
    }

    @Test
    public void testValidateWithValidator() throws OptionException {
        Validator validator = new ValidatorImpl();
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', validator, "--", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(argument, "value");
        argument.validate(commandLine, argument);
    }

    @Test
    public void testAppendUsage() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 2, '\0', '\0', null, "--", null, 0);
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_OPTIONAL), null);
        Assert.assertEquals("name [name2]", buffer.toString());
    }

    @Test
    public void testGetDescription() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--", null, 0);
        Assert.assertEquals("description", argument.getDescription());
    }

    @Test
    public void testHelpLines() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--", null, 0);
        List helpLines = argument.helpLines(0, Collections.EMPTY_SET, null);
        Assert.assertEquals(1, helpLines.size());
        Assert.assertNotNull(helpLines.get(0));
    }

    @Test
    public void testGetMaximum() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 3, '\0', '\0', null, "--", null, 0);
        Assert.assertEquals(3, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 2, 3, '\0', '\0', null, "--", null, 0);
        Assert.assertEquals(2, argument.getMinimum());
    }

    @Test
    public void testStripBoundaryQuotes() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 2, 2, '\0', '\0', null, "--", null, 0);
        Assert.assertEquals("value", argument.stripBoundaryQuotes("\"value\""));
    }

    @Test
    public void testIsRequired() {
        ArgumentImpl argument1 = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--", null, 0);
        Assert.assertTrue(argument1.isRequired());

        ArgumentImpl argument2 = new ArgumentImpl("name", "description", 0, 1, '\0', '\0', null, "--", null, 0);
        Assert.assertFalse(argument2.isRequired());
    }

    @Test
    public void testDefaults() {
        List defaultValues = Collections.singletonList("defaultValue");
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--", defaultValues, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        argument.defaults(commandLine);
        Assert.assertEquals(defaultValues, commandLine.getValues(argument));
    }

    @Test
    public void testDefaultValues() {
        List defaultValues = Collections.singletonList("defaultValue");
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null, "--", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        argument.defaultValues(commandLine, argument);
        Assert.assertNull(commandLine.getDefaultValues(argument));
    }

}