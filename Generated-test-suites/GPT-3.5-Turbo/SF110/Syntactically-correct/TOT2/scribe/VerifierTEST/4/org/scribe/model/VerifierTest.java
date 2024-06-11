package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class VerifierTest {

    @Test // Test for constructor
    public void testConstructor() {
        Verifier verifier = new Verifier("test");
        assertNotNull("Verifier object should not be null", verifier);
        assertEquals("Constructor value should be set correctly", "test", verifier.getValue());
    }

    @Test // Test for getValue method
    public void testGetValue() {
        Verifier verifier = new Verifier("test");
        assertEquals("getValue should return the correct value", "test", verifier.getValue());
    }

    @Test // Test for null value in constructor
    public void testNullConstructor() {
        try {
            Verifier verifier = new Verifier(null); // Should throw NullPointerException
            fail("Constructor with null value should throw NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

}