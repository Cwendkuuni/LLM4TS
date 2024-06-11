package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class VerifierTest {

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("value");
        assertEquals("value", verifier.getValue());

        verifier = new Verifier("");
        assertEquals("", verifier.getValue());

        verifier = new Verifier(null);
        assertNull(verifier.getValue());
    }
}