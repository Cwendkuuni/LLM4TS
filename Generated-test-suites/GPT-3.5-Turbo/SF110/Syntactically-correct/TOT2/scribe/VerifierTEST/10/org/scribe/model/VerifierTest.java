package org.scribe.model;
import org.junit.Test;
import static org.junit.Assert.*;

public class VerifierTest {
    
    Verifier verifier;
    
    @Test
    public void testVerifierConstructor() {
        verifier = new Verifier("test");
        assertEquals("test", verifier.getValue());
    }
    
    @Test
    public void testGetValue() {
        verifier = new Verifier("test");
        assertEquals("test", verifier.getValue());
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullVerifierConstructor() {
        verifier = new Verifier(null);
    }
}