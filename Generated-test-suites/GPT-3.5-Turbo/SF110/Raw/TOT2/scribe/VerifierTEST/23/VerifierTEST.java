package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;

public class VerifierTest {
    
    @Test
    public void testConstructor() {
        Verifier verifier = new Verifier("test");
        assertNotNull(verifier);
    }
    
    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("test");
        assertEquals("test", verifier.getValue());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullConstructor() {
        Verifier verifier = new Verifier(null);
    }
}