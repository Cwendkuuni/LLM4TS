package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphoneEncoderInstance() {
        Metaphone metaphone = new Metaphone();
        assertInstanceOf(StringEncoder.class, metaphone);
    }

    @Test
    public void testEmptyString() {
        Metaphone metaphone = new Metaphone();
        assertEquals("", metaphone.metaphone(""));
    }

    @Test
    public void testSingleCharacter() {
        Metaphone metaphone = new Metaphone();
        assertEquals("A", metaphone.metaphone("A"));
        assertEquals("B", metaphone.metaphone("B"));
        // More single character test cases
    }

    @Test
    public void testMetaphoneWithInitialExceptions() {
        Metaphone metaphone = new Metaphone();
        assertEquals("KN", metaphone.metaphone("Kn"));
        assertEquals("AE", metaphone.metaphone("Ae"));
        assertEquals("WR", metaphone.metaphone("Wr"));
        assertEquals("W", metaphone.metaphone("Wh"));
        assertEquals("SX", metaphone.metaphone("X"));
    }

    @Test
    public void testMetaphoneWithDoubleLetterExceptions() {
        Metaphone metaphone = new Metaphone();
        assertEquals("K", metaphone.metaphone("Kn"));
        assertEquals("AE", metaphone.metaphone("Ae"));
        assertEquals("WH", metaphone.metaphone("Wh"));
        assertEquals("W", metaphone.metaphone("W"));
    }

    @Test
    public void testMetaphoneWithMultipleCharacters() {
        Metaphone metaphone = new Metaphone();
        assertEquals("BAE", metaphone.metaphone("Bae"));
        assertEquals("APPLE", metaphone.metaphone("Apple"));
        assertEquals("APP", metaphone.metaphone("App"));
        assertEquals("CHAIR", metaphone.metaphone("Chair"));
        assertEquals("TCHOCOLATE", metaphone.metaphone("Tchocolat"));
        // More test cases with multiple characters
    }

    @Test(expected = EncoderException.class)
    public void testEncodeNullString() {
        Metaphone metaphone = new Metaphone();
        metaphone.encode(null);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeEmptyString() {
        Metaphone metaphone = new Metaphone();
        metaphone.encode("");
    }

    @Test(expected = EncoderException.class)
    public void testEncodeNonStringObject() {
        Metaphone metaphone = new Metaphone();
        metaphone.encode(new Object());
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        assertTrue(metaphone.isMetaphoneEqual("Apple", "Apple"));
        assertFalse(metaphone.isMetaphoneEqual("Apple", "Banana"));
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