package org.scribe.model;
import static org.junit.Assert.*;
import org.junit.Test;

public class VerifierTest {
    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("12345");
        assertEquals("12345", verifier.getValue());
    }
    
    @Test
    public void testConstructorNullValue() {
        try {
            Verifier verifier = new Verifier(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testConstructorEmptyValue() {
        try {
            Verifier verifier = new Verifier("");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
}