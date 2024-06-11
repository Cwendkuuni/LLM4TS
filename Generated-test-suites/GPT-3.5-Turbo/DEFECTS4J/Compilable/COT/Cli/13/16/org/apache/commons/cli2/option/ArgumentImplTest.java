package org.apache.commons.cli2.option;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArgumentImplTest {

    private ArgumentImpl argument;

    @Before
    public void setUp() {
        argument = new ArgumentImpl("arg", "Description", 0, 1, '\0', '\0', null, "", null, 0);
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("arg", argument.getPreferredName());
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals('\0', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        assertEquals('\0', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetConsumeRemaining() {
        assertEquals("", argument.getConsumeRemaining());
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
    public void testGetMaximum() {
        assertEquals(1, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(0, argument.getMinimum());
    }

    @Test
    public void testIsRequired() {
        assertFalse(argument.isRequired());
    }

    @Test
    public void testProcessValues() {
        // TODO: Write test cases for ArgumentImpl.processValues()
    }

    @Test
    public void testCanProcess() {
        assertTrue(argument.canProcess(null, ""));
    }

    @Test
    public void testGetPrefixes() {
        assertNotNull(argument.getPrefixes());
        assertTrue(argument.getPrefixes().isEmpty());
    }

    @Test
    public void testGetTriggers() {
        assertNotNull(argument.getTriggers());
        assertTrue(argument.getTriggers().isEmpty());
    }

    @Test
    public void testValidate() {
        // TODO: Write test cases for ArgumentImpl.validate()
    }

    @Test
    public void testAppendUsage() {
        // TODO: Write test cases for ArgumentImpl.appendUsage()
    }

    @Test
    public void testGetDescription() {
        assertEquals("Description", argument.getDescription());
    }

    @Test
    public void testHelpLines() {
        // TODO: Write test cases for ArgumentImpl.helpLines()
    }

    @Test
    public void testProcess() {
        // TODO: Write test cases for ArgumentImpl.process()
    }

    @Test
    public void testDefaultValues() {
        // TODO: Write test cases for ArgumentImpl.defaultValues()
    }

    @Test
    public void testDefaults() {
        // TODO: Write test cases for ArgumentImpl.defaults()
    }
}