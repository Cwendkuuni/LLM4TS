package org.scribe.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class VerifierTest {

    private Verifier verifier;

    @Before
    public void setUp() {
        verifier = new Verifier("test");
    }

    @Test
    public void testGetValue() {
        assertEquals("test", verifier.getValue());
    }
}