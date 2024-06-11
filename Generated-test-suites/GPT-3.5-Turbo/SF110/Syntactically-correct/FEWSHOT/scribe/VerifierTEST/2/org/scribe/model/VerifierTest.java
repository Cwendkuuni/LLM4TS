
package org.scribe.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class VerifierTest {

    @Test
    public void testConstructorWithValue() {
        Verifier verifier = new Verifier("test");
        assertEquals("test", verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullValue() {
        Verifier verifier = new Verifier(null);
    }

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("test");
        assertEquals("test", verifier.getValue());
    }
}

