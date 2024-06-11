package org.apache.commons.cli.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.ArgumentImpl;
import org.junit.Before;
import org.junit.Test;

public class ArgumentImplTest {
    private ArgumentImpl argument;

    @Before
    public void setUp() {
        argument = new ArgumentImpl("arg", "Argument description", 1, 2, ' ', ',', null, "--", null, 0);
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("arg", argument.getPreferredName());
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals(' ', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        assertEquals(',', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetConsumeRemaining() {
        assertEquals("--", argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        assertNull(argument.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        assertNull(argument.getValidator());
    }

    @Test
    public void testIsRequired() {
        assertTrue(argument.isRequired());
    }

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.addSwitch(null, argument);

        argument.process(commandLine, null);

        assertEquals(1, commandLine.getValues(argument).size());
    }

    @Test
    public void testValidate() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.addSwitch(null, argument);

        commandLine.addValue(argument, "value");

        argument.validate(commandLine);

        commandLine.removeValue(argument, "value");
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");

        argument.validate(commandLine);

        commandLine.removeValue(argument, "value2");

        boolean exceptionThrown = false;
        try {
            argument.validate(commandLine);
        } catch (OptionException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, null, null);

        assertEquals("arg", buffer.toString());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Argument description", argument.getDescription());
    }

    @Test
    public void testHelpLines() {
        assertEquals(1, argument.helpLines(0, null, null).size());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(2, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, argument.getMinimum());
    }

    @Test
    public void testStripBoundaryQuotes() {
        assertEquals("value", argument.stripBoundaryQuotes("\"value\""));
        assertEquals("value", argument.stripBoundaryQuotes("\"value"));
        assertEquals("value", argument.stripBoundaryQuotes("value\""));
        assertEquals("\"value\"", argument.stripBoundaryQuotes("\"value\"\""));
    }

    @Test
    public void testIsEnabled() {
        assertTrue(argument.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        assertFalse(argument.isEnabled());

        argument.setEnabled(true);

        assertTrue(argument.isEnabled());

        argument.setEnabled(false);

        assertFalse(argument.isEnabled());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.addSwitch(null, argument);

        argument.defaults(commandLine);

        assertEquals(0, commandLine.getValues(argument).size());
    }
}