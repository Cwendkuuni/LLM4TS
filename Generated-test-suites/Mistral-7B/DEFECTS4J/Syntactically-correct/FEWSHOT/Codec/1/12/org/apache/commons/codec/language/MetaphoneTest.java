package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();

        // Test empty string
        assertEquals("", metaphone.metaphone(""));

        // Test single character
        assertEquals("A", metaphone.metaphone("A"));
        assertEquals("B", metaphone.metaphone("B"));

        // Test basic cases
        assertEquals("A0E", metaphone.metaphone("apple"));
        assertEquals("A0P", metaphone.metaphone("appetite"));
        assertEquals("K1N", metaphone.metaphone("kangaroo"));
        assertEquals("P0T", metaphone.metaphone("potato"));

        // Test special cases
        assertEquals("S0X", metaphone.metaphone("scissors"));
        assertEquals("S0X", metaphone.metaphone("schism"));
        assertEquals("A0J", metaphone.metaphone("government"));
        assertEquals("X2S", metaphone.metaphone("metaphone"));

        // Test cases with multiple codes
        assertEquals("A1T", metaphone.metaphone("athlete"));
        assertEquals("K1S", metaphone.metaphone("acoustic"));
        assertEquals("K1S", metaphone.metaphone("assassin"));
        assertEquals("F1X", metaphone.metaphone("afrikaans"));
        assertEquals("A1T", metaphone.metaphone("atlas"));

        // Test exceptions
        try {
            metaphone.encode(null);
            assert false;
        } catch (EncoderException e) {
            // expected
        }
    }

    @Test
    public void testEncode() throws EncoderException {
        Metaphone metaphone = new Metaphone();

        assertEquals("A0E", metaphone.encode("apple"));
        assertEquals("A0P", metaphone.encode("appetite"));
        assertEquals("K1N", metaphone.encode("kangaroo"));
        assertEquals("P0T", metaphone.encode("potato"));
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();

        assertEquals(true, metaphone.isMetaphoneEqual("apple", "apple"));
        assertEquals(true, metaphone.isMetaphoneEqual("appetite", "appetite"));
        assertEquals(true, metaphone.isMetaphoneEqual("kangaroo", "kangaroo"));
        assertEquals(true, metaphone.isMetaphoneEqual("potato", "potato"));
        assertEquals(false, metaphone.isMetaphoneEqual("apple", "orange"));
    }

    @Test
    public void testGetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        assertEquals(4, metaphone.getMaxCodeLen());
    }

    @Test
    public void testSetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        metaphone.setMaxCodeLen(5);
        assertEquals(5, metaphone.getMaxCodeLen());
    }
}