package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Assert.*;

public class MetaphoneTest {

    

    Metaphone metaphone = new Metaphone();

    @Test
    public void testMetaphoneEmptyString() {
        assertEquals("", metaphone.metaphone(""));
    }

    @Test
    public void testMetaphoneNullString() {
        try {
            metaphone.metaphone(null);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // expected
        }
    }

    @Test
    public void testMetaphoneSingleCharacter() {
        assertEquals("A", metaphone.metaphone("A"));
    }

    @Test
    public void testMetaphoneMultipleCharacters() {
        assertEquals("BA", metaphone.metaphone("Banana"));
    }

    @Test
    public void testMetaphoneSpecialCases() {
        assertEquals("SA", metaphone.metaphone("Apple"));
        assertEquals("KN", metaphone.metaphone("Kangaroo"));
        assertEquals("AE", metaphone.metaphone("Aardvark"));
        assertEquals("WR", metaphone.metaphone("Water"));
        assertEquals("X", metaphone.metaphone("Chinchilla"));
        assertEquals("S", metaphone.metaphone("Xylophone"));
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneInvalidInput() {
        metaphone.metaphone("123Abc");
    }

    @Test
    public void testIsMetaphoneEqual() {
        assertTrue(metaphone.isMetaphoneEqual("Banana", "Apple")); // false
        assertTrue(metaphone.isMetaphoneEqual("Banana", "Banana")); // true
        assertTrue(metaphone.isMetaphoneEqual("Banana", "Bananana")); // true
    }

    @Test
    public void testEncode() {
        assertEquals("BA", metaphone.encode("Banana"));
        assertEquals("A", metaphone.encode("A"));
    }

    @Test
    public void testEncodeNull() {
        try {
            metaphone.encode(null);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // expected
        }
    }

    @Test
    public void testIsMetaphoneEqualNull() {
        assertFalse(metaphone.isMetaphoneEqual(null, "Banana"));
    }

    @Test
    public void testGetMaxCodeLen() {
        assertEquals(4, metaphone.getMaxCodeLen());
    }

    @Test
    public void testSetMaxCodeLen() {
        metaphone.setMaxCodeLen(5);
        assertEquals(5, metaphone.getMaxCodeLen());
    }

    
}