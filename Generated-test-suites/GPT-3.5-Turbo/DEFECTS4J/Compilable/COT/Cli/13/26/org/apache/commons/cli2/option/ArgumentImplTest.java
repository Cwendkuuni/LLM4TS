package org.apache.commons.cli2.option;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ArgumentImplTest {

    private ArgumentImpl argument;

    @Before
    public void setUp() {
        argument = new ArgumentImpl("arg", "Argument description", 1, 3, '\0', '\0', null, "--", null, 0);
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
        assertEquals("--", argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        assertNull(argument.getDefaultValues());
    }

    @Test
    public void testIsRequired() {
        assertTrue(argument.isRequired());
    }

    @Test
    public void testProcessValues() {
        // TODO: Write test cases for processValues()
    }

    @Test
    public void testCanProcess() {
        // TODO: Write test cases for canProcess()
    }

    @Test
    public void testGetPrefixes() {
        assertTrue(argument.getPrefixes().isEmpty());
    }

    @Test
    public void testProcess() {
        // TODO: Write test cases for process()
    }

    @Test
    public void testGetTriggers() {
        assertTrue(argument.getTriggers().isEmpty());
    }

    @Test
    public void testValidate() {
        // TODO: Write test cases for validate()
    }

    @Test
    public void testAppendUsage() {
        // TODO: Write test cases for appendUsage()
    }

    @Test
    public void testGetDescription() {
        assertEquals("Argument description", argument.getDescription());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(3, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, argument.getMinimum());
    }

    @Test
    public void testDefaults() {
        // TODO: Write test cases for defaults()
    }
}