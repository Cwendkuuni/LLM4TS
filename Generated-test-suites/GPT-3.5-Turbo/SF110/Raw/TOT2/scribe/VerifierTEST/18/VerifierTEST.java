package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class VerifierTest {

    @Test
    public void testConstructor() {
        Verifier verifier = new Verifier("test");
        assertEquals("test", verifier.getValue());
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructorWithNull() {
        new Verifier(null);
    }
    
}