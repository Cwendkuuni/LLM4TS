package org.apache.commons.codec.language;

import org.junit.Test;

import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        assertEquals("PPRT", metaphone.metaphone("party"));
        assertEquals("HLBRK", metaphone.metaphone("hamburger"));
        assertEquals("STRNKR", metaphone.metaphone("strength"));
        assertEquals("FLRXTNKTON", metaphone.metaphone("flexington"));
    }

    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();
        assertEquals("PPRT", metaphone.encode("party"));
        assertEquals("HLBRK", metaphone.encode("hamburger"));
        assertEquals("STRNKR", metaphone.encode("strength"));
        assertEquals("FLRXTNKTON", metaphone.encode("flexington"));
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        assertTrue(metaphone.isMetaphoneEqual("party", "parti"));
        assertTrue(metaphone.isMetaphoneEqual("hamburger", "hamberger"));
        assertFalse(metaphone.isMetaphoneEqual("strength", "strenth"));
        assertTrue(metaphone.isMetaphoneEqual("flexington", "fletcher"));
    }

    @Test
    public void testGetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        assertEquals(4, metaphone.getMaxCodeLen());
    }

    @Test
    public void testSetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        metaphone.setMaxCodeLen(6);
        assertEquals(6, metaphone.getMaxCodeLen());
    }
}