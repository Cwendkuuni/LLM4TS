package org.scribe.model;
import static org.junit.Assert.*;
import org.junit.Test;
import org.scribe.model.Verifier;

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
    public void testConstructorWithNullValue() {
        new Verifier(null);
    }

}