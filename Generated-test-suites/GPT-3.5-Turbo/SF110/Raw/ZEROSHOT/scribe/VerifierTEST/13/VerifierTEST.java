package org.scribe.model;

import org.junit.Assert;
import org.junit.Test;

public class VerifierTest {

    @Test
    public void testGetValue() {
        String value = "test";
        Verifier verifier = new Verifier(value);
        Assert.assertEquals(value, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierWithNullValue() {
        String value = null;
        Verifier verifier = new Verifier(value);
    }
}