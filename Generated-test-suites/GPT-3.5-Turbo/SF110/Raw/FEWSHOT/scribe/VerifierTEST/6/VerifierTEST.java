package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class VerifierTest {
    @Test
    public void testVerifierConstructor() {
        Verifier verifier = new Verifier("test");
        assertNotNull(verifier);
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierConstructorWithNullValue() {
        Verifier verifier = new Verifier(null);
    }

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("test");
        assertEquals("test", verifier.getValue());
    }
}