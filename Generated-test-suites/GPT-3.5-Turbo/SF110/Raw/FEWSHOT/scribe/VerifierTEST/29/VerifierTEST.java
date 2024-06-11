package org.scribe.model;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class VerifierTest {
    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("foo");
        assertEquals("foo", verifier.getValue());
        
        verifier = new Verifier("bar");
        assertEquals("bar", verifier.getValue());
    }
}