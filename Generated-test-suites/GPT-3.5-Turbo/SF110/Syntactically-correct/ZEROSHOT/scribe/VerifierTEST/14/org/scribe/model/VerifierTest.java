package org.scribe.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class VerifierTest {
    private Verifier verifier;

    @Before
    public void setUp() {
        verifier = new Verifier("testVerifier");
    }

    @Test
    public void testGetValue() {
        assertEquals("testVerifier", verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullValue() {
        new Verifier(null);
    }
    
    @Test
    public void testConstructorWithValue() {
        Verifier newVerifier = new Verifier("newTestVerifier");
        assertEquals("newTestVerifier", newVerifier.getValue());
    }
}