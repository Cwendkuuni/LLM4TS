package org.scribe.model;

import org.scribe.utils.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class VerifierTest {

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("testValue");
        assertEquals("testValue", verifier.getValue());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullValue() {
        Verifier verifier = new Verifier(null);
    }
    
    @Test
    public void testEmptyValue() {
        Verifier verifier = new Verifier("");
        assertEquals("", verifier.getValue());
    }
}