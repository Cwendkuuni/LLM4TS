package org.apache.commons.cli.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.Test;

public class ArgumentImplTest {
    
    private static ArgumentImpl argument;
    
    @BeforeClass
    public static void setUp() {
        argument = new ArgumentImpl("arg", "description", 1, 5, '-', ';', null, "--", null, 0);
    }
    
    @Test
    public void testGetPreferredName() {
        assertEquals("arg", argument.getPreferredName());
    }
    
    @Test
    public void testGetInitialSeparator() {
        assertEquals('-', argument.getInitialSeparator());
    }
    
    @Test
    public void testGetSubsequentSeparator() {
        assertEquals(';', argument.getSubsequentSeparator());
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
    public void testGetMaximum() {
        assertEquals(5, argument.getMaximum());
    }
    
    @Test
    public void testGetMinimum() {
        assertEquals(1, argument.getMinimum());
    }
    
    @Test
    public void testIsRequired() {
        assertTrue(argument.isRequired());
    }
}