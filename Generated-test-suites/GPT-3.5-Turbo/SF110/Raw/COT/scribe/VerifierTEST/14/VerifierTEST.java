
package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class VerifierTest {

    @Test
    public void testGetValue() {
        String value = "Test Value";
        Verifier verifier = new Verifier(value);
        
        String result = verifier.getValue();
        
        assertEquals(value, result);
    }
}
