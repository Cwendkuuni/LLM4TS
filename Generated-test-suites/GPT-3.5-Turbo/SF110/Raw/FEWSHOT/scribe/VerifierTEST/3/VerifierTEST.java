package org.scribe.model;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class VerifierTest {

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("abc123");
        assertEquals("abc123", verifier.getValue());
    }
}