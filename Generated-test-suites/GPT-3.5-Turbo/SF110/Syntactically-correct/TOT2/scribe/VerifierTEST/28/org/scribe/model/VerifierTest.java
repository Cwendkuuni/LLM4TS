package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class VerifierTest {

    @Test
    public void testConstructor() {
        Verifier verifier = new Verifier("test");
        assertEquals("test", verifier.getValue());
    }

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("test");
        assertEquals("test", verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullParameter() {
        Verifier verifier = new Verifier(null);
    }

}