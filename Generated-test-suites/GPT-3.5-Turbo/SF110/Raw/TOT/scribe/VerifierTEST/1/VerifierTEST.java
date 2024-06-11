package org.scribe.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class VerifierTest {

    Verifier verifier;

    @Before
    public void setup() {
        verifier = new Verifier("testVer");
    }

    @Test
    public void testGetValue() {
        String expected = "testVer";
        assertEquals(expected, verifier.getValue());
    }

    @Test
    public void testConstructorWithValidValue() {
        assertNotNull(verifier);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullValue() {
        Verifier verifier = new Verifier(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyValue() {
        Verifier verifier = new Verifier("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithWhitespaceValue() {
        Verifier verifier = new Verifier("  ");
    }

}