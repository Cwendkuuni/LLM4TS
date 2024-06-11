package org.scribe.model;

import org.junit.Test;
import org.scribe.utils.Preconditions;
import static org.junit.Assert.*;

public class VerifierTest {

    @Test
    public void testConstructorNonNullValue() {
        String value = "test";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullValue() {
        Verifier verifier = new Verifier(null);
    }

    @Test
    public void testGetValue() {
        String value = "test";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }
}