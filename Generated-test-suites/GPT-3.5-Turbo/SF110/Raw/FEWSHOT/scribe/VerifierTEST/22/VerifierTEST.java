package org.scribe.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class VerifierTest {
    @Test
    public void testConstructor() {
        Verifier verifier = new Verifier("test");
        assertNotNull(verifier);
        assertEquals("test", verifier.getValue());
    }
}