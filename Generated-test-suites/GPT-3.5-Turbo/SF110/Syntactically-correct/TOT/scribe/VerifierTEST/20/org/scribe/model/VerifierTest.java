package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class VerifierTest {

    @Test
    public void testConstructor_ValidValue() {
        Verifier verifier = new Verifier("valid");
        assertNotNull(verifier);
        assertEquals("valid", verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_NullValue() {
        Verifier verifier = new Verifier(null);
    }

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("value");
        assertEquals("value", verifier.getValue());
    }
}