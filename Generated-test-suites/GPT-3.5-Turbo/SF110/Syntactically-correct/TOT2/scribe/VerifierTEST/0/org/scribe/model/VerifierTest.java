package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    @Test
    public void testConstructor() {
        Verifier verifier = new Verifier("testValue");
        assertNotNull(verifier);
    }

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("testValue");
        assertEquals("testValue", verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullValue() {
        new Verifier(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEmptyValue() {
        new Verifier("");
    }
}