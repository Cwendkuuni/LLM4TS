package org.scribe.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    @Test
    public void testConstructor() {
        Verifier verifier = new Verifier("test");
        assertEquals("test", verifier.getValue());
    }

    @Test
    public void testConstructorNullValue() {
        try {
            Verifier verifier = new Verifier(null);
        } catch (NullPointerException e) {
            assertEquals("Must provide a valid string as verifier", e.getMessage());
        }
    }

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("test");
        assertEquals("test", verifier.getValue());
    }
}