package org.scribe.model;

import org.junit.*;

import static org.junit.Assert.*;

public class VerifierTest {

    // Test cases for constructor
    
    @Test(expected = NullPointerException.class)
    public void testConstructor_NullValue() {
        new Verifier(null);
    }
    
    @Test
    public void testConstructor_ValidValue() {
        String value = "test";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }

    // Test case for getValue

    @Test
    public void testGetValue() {
        String value = "test";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }
    
}