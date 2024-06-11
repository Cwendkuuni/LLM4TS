package org.scribe.model;

import org.junit.*;
import static org.junit.Assert.*;

public class VerifierTest {
    
    @Test
    public void testConstructor_ValidValue() {
        String value = "testVerifier";
        Verifier verifier = new Verifier(value);
        assertNotNull(verifier);
        assertEquals(value, verifier.getValue());
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructor_NullValue() {
        String value = null;
        Verifier verifier = new Verifier(value);
    }
    
    @Test
    public void testGetValue() {
        String value = "testVerifier";
        Verifier verifier = new Verifier(value);
        assertNotNull(verifier);
        assertEquals(value, verifier.getValue());
    }
    
    @Test
    public void testGetValue_EmptyValue() {
        String value = "";
        Verifier verifier = new Verifier(value);
        assertNotNull(verifier);
        assertEquals(value, verifier.getValue());
    }
    
    @Test
    public void testGetValue_LongValue() {
        String value = "ThisIsAReallyLongValueThatExceedsTheMaximumLength";
        Verifier verifier = new Verifier(value);
        assertNotNull(verifier);
        assertEquals(value, verifier.getValue());
    }
    
    @Test
    public void testGetValue_SpecialCharacters() {
        String value = "!@#$%^&*()";
        Verifier verifier = new Verifier(value);
        assertNotNull(verifier);
        assertEquals(value, verifier.getValue());
    }
    
    @Test
    public void testGetValue_UnicodeCharacters() {
        String value = "éçñ";
        Verifier verifier = new Verifier(value);
        assertNotNull(verifier);
        assertEquals(value, verifier.getValue());
    }
}