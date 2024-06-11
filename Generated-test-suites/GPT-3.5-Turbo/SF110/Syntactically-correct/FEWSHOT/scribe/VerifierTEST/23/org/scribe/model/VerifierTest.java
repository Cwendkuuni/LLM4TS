package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class VerifierTest {

    @Test
    public void testConstructor() {
        Verifier verifier = new Verifier("testValue");
        assertEquals("testValue", verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNull() {
        Verifier verifier = new Verifier(null);
    }

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("testValue");
        assertEquals("testValue", verifier.getValue());
    }
}