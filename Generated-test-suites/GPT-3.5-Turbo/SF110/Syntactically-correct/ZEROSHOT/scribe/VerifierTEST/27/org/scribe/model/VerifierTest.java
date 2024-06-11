package org.scribe.model;

import org.junit.*;
import static org.junit.Assert.*;

public class VerifierTest {

    @Test
    public void testConstructor() {
        Verifier verifier = new Verifier("test");
        assertNotNull(verifier);
    }
    
    @Test
    public void testConstructorNullValue() {
        try {
            Verifier verifier = new Verifier(null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Must provide a valid string as verifier", e.getMessage());
        }    
    }
    
    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("test");
        assertEquals("test", verifier.getValue());
    }
}