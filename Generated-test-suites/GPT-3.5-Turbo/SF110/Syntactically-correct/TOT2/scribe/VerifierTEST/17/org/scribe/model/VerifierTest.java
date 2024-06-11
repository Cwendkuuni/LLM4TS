package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class VerifierTest {

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("test");
        assertEquals("test", verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValue() {
        new Verifier(null);
    }
}