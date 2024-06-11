package org.scribe.model;

import org.junit.Assert;
import org.junit.Test;

public class VerifierTest {
    @Test
    public void testConstructorWithValidValue() {
        String value = "validValue";
        Verifier verifier = new Verifier(value);
        Assert.assertEquals(value, verifier.getValue());
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullValue() {
        Verifier verifier = new Verifier(null);
    }
    
    @Test
    public void testGetValue() {
        String value = "validValue";
        Verifier verifier = new Verifier(value);
        Assert.assertEquals(value, verifier.getValue());
    }
}