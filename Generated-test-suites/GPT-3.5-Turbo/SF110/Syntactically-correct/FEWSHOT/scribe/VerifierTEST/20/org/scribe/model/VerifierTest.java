package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class VerifierTest {
    @Test
    public void testConstructor() {
        Verifier verifier = new Verifier("value");
        assertNotNull(verifier);
        assertEquals("value", verifier.getValue());
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullValue() {
        new Verifier(null);
    }
    
    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("value");
        assertEquals("value", verifier.getValue());
    }
}