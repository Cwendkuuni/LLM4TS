package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class VerifierTest {
    @Test
    public void testConstructorNullValue() {
        try {
            Verifier verifier = new Verifier(null);
            assertNull(verifier.getValue());
        } catch (Exception e) {
            assertEquals("Must provide a valid string as verifier", e.getMessage());
        }
    }
    
    @Test
    public void testConstructorNonNullValue() {
        Verifier verifier = new Verifier("test");
        assertEquals("test", verifier.getValue());
    }
}