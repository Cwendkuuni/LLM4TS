
package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;
import org.scribe.utils.Preconditions;

public class VerifierTest {
    
    @Test
    public void testConstructor() {
        Verifier verifier = new Verifier("sample");
        assertNotNull(verifier);
    }
    
    @Test
    public void testGetValue() {
        String value = "sample";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullConstructor() {
        new Verifier(null);
    }
}
