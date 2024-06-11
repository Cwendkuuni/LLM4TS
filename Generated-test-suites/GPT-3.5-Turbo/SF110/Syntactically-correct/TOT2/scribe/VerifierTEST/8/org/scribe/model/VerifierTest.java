package org.scribe.model;
import org.junit.Test;
import static org.junit.Assert.*;

import org.scribe.model.Verifier;

public class VerifierTest {

    @Test
    public void testConstructor() {
        Verifier verifier = new Verifier("test");
        assertNotNull(verifier);
    }

    @Test
    public void testGetValue() {
        String value = "test";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }

}