
package org.scribe.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class VerifierTest {
    
    @Test(expected = NullPointerException.class)
    public void testConstructor_NullValue() {
        new Verifier(null);
    }
    
    @Test
    public void testConstructor_ValidValue() {
        String value = "test";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }
    
    @Test
    public void testGetValue() {
        String value = "test";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }
}
