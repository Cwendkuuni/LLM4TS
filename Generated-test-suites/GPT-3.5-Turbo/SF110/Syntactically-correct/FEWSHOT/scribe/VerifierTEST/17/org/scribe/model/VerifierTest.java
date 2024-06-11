package org.scribe.model;

import org.junit.Assert;
import org.junit.Test;

public class VerifierTest {

    @Test
    public void testConstructor() {
        String validValue = "validValue";
        Verifier verifier = new Verifier(validValue);
        Assert.assertEquals(validValue, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testNullConstructor() {
        new Verifier(null);
    }

    @Test
    public void testGetValue() {
        String value = "value";
        Verifier verifier = new Verifier(value);
        Assert.assertEquals(value, verifier.getValue());
    }

}