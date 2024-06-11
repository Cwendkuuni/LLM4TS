package org.scribe.model;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class VerifierTest {
    @Test
    public void testConstructorWithValidString() {
        String value = "test";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullString() {
        new Verifier(null);
    }

    @Test
    public void testGetValue() {
        String value = "test";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }
}