package org.scribe.model;
import org.junit.Test;
import static org.junit.Assert.*;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    @Test
    public void testConstructor_validStringProvided() {
        Verifier verifier = new Verifier("validString");
        assertNotNull(verifier);
    }

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("testString");
        assertEquals("testString", verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullStringProvided() {
        new Verifier(null);
    }
}