package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class VerifierTest {

    @Test
    public void testConstructorValidString() {
        String value = "testValue";
        Verifier verifier = new Verifier(value);

        assertEquals(value, verifier.getValue());
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructorNullString() {
        String value = null;
        Verifier verifier = new Verifier(value);
    }

    @Test
    public void testGetValue() {
        String value = "testValue";
        Verifier verifier = new Verifier(value);

        assertEquals(value, verifier.getValue());
    }
}