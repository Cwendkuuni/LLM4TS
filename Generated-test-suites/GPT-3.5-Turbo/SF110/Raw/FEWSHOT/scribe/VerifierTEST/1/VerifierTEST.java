package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class VerifierTest {
    @Test
    public void testConstructorNonNullValue() {
        String value = "test";
        Verifier verifier = new Verifier(value);
        assertNotNull(verifier);
        assertEquals(value, verifier.getValue());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullValue() {
        new Verifier(null);
    }
    
    @Test
    public void testGetValue() {
        String value = "test";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }
}