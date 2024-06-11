package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class VerifierTest {
    @Test
    public void testConstructorWithValue() {
        Verifier verifier = new Verifier("test");
        assertNotNull(verifier);
        assertEquals("test", verifier.getValue());
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullValue() {
        Verifier verifier = new Verifier(null);
    }
    
    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("test");
        assertEquals("test", verifier.getValue());
    }
}