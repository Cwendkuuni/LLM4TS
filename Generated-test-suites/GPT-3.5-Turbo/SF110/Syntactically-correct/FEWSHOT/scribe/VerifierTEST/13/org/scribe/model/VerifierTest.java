package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class VerifierTest {
    @Test
    public void testConstructor() {
        String value = "test";
        Verifier verifier = new Verifier(value);
        
        assertNotNull(verifier);
        assertEquals(value, verifier.getValue());
    }
}