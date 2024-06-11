package org.scribe.model;

import org.junit.Test;
import org.scribe.utils.Preconditions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class VerifierTest {

    @Test
    public void testConstructor() {
        String value = "verifier";
        Verifier verifier = new Verifier(value);
        assertNotNull(verifier);
        assertEquals(value, verifier.getValue());

        String nullValue = null;
        try {
            new Verifier(nullValue);
        } catch (IllegalArgumentException e) {
            assertEquals("Must provide a valid string as verifier", e.getMessage());
        }
    }

    @Test
    public void testGetValue() {
        String value = "verifier";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }
}