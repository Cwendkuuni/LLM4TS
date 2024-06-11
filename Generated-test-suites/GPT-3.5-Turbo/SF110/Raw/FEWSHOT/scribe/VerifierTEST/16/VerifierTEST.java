package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class VerifierTest {
    
    @Test
    public void testConstructorNotNull() {
        Verifier verifier = new Verifier("value");
        assertNotNull(verifier);
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructorNull() {
        Verifier verifier = new Verifier(null);
    }
    
    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("value");
        assertEquals("value", verifier.getValue());
    }
    
}