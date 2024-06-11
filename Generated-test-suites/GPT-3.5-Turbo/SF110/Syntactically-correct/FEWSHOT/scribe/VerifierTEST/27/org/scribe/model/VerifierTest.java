package org.scribe.model;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class VerifierTest {
    @Test
    public void testConstructor() {
        Verifier verifier = new Verifier("abcd");
        assertEquals("abcd", verifier.getValue());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullValue() {
        Verifier verifier = new Verifier(null);
    }
    
    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("abcd");
        assertEquals("abcd", verifier.getValue());
        
        verifier = new Verifier("xyz");
        assertEquals("xyz", verifier.getValue());
    }
}