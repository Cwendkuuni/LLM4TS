package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class VerifierTest {
    @Test
    public void testConstructorWithValidValue() {
        Verifier verifier = new Verifier("valid");
        assertEquals("valid", verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullValue() {
        new Verifier(null);
    }

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("value");
        assertNotNull(verifier.getValue());
        assertEquals("value", verifier.getValue());
    }
}