package org.apache.commons.cli2.option;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArgumentImplTest {

    ArgumentImpl argument;

    @Before
    public void setUp() throws Exception {
        argument = new ArgumentImpl("name", "description", 1, 5, '\0', '\0', null, "--", null, 0);
    }

    @Test
    public void getPreferredName() {
        assertEquals("name", argument.getPreferredName());
    }

    @Test
    public void getInitialSeparator() {
        assertEquals('\0', argument.getInitialSeparator());
    }

    @Test
    public void getSubsequentSeparator() {
        assertEquals('\0', argument.getSubsequentSeparator());
    }

    @Test
    public void getConsumeRemaining() {
        assertEquals("--", argument.getConsumeRemaining());
    }

    @Test
    public void getDefaultValues() {
        assertNull(argument.getDefaultValues());
    }

    @Test
    public void getValidator() {
        assertNull(argument.getValidator());
    }

    @Test
    public void process() {
        // TODO: implement test case
    }

    @Test
    public void validate() {
        // TODO: implement test case
    }

    @Test
    public void getDescription() {
        assertEquals("description", argument.getDescription());
    }

    @Test
    public void getMaximum() {
        assertEquals(5, argument.getMaximum());
    }

    @Test
    public void getMinimum() {
        assertEquals(1, argument.getMinimum());
    }

    @Test
    public void isRequired() {
        assertTrue(argument.isRequired());
    }

    @Test
    public void defaults() {
        // TODO: implement test case
    }
}
