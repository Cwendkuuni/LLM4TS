package org.scribe.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class VerifierTest {
    @Test
    public void testGetValue() {
        String value = "test";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }
}