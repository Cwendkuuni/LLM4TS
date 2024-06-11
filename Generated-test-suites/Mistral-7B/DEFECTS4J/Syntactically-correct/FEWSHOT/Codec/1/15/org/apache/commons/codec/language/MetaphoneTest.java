package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();

        assertEquals("0", metaphone.metaphone("thetha"));
        assertEquals("A0", metaphone.metaphone("athlete"));
        assertEquals("BA", metaphone.metaphone("banana"));
        assertEquals("KN1", metaphone.metaphone("knickerbocker"));
        assertEquals("S0", metaphone.metaphone("sometimes"));
        assertEquals("S1", metaphone.metaphone("sugar"));
        assertEquals("S2", metaphone.metaphone("spaghetti"));
        assertEquals("S3", metaphone.metaphone("station"));
        assertEquals("T2", metaphone.metaphone("tomato"));
        assertEquals("T3", metaphone.metaphone("tuna"));
        assertEquals("X0", metaphone.metaphone("zero"));
        assertEquals("X2", metaphone.metaphone("xylophone"));

        assertNull(metaphone.metaphone(null));
        assertNull(metaphone.metaphone(""));
    }

    @Test
    public void testEncode() throws EncoderException {
        Metaphone metaphone = new Metaphone();

        assertEquals("0", metaphone.encode("thetha"));
        assertEquals("A0", metaphone.encode("athlete"));
        assertEquals("BA", metaphone.encode("banana"));
        assertEquals("KN1", metaphone.encode("knickerbocker"));
        assertEquals("S0", metaphone.encode("sometimes"));
        assertEquals("S1", metaphone.encode("sugar"));
        assertEquals("S2", metaphone.encode("spaghetti"));
        assertEquals("S3", metaphone.encode("station"));
        assertEquals("T2", metaphone.encode("tomato"));
        assertEquals("T3", metaphone.encode("tuna"));
        assertEquals("X0", metaphone.encode("zero"));
        assertEquals("X2", metaphone.encode("xylophone"));

        assertNull(metaphone.encode(null));
        assertNull(metaphone.encode(""));
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();

        assertTrue(metaphone.isMetaphoneEqual("athlete", "athlete"));
        assertTrue(metaphone.isMetaphoneEqual("banana", "banana"));
        assertTrue(metaphone.isMetaphoneEqual("knickerbocker", "knickerbocker"));
        assertTrue(metaphone.isMetaphoneEqual("sometimes", "sometimes"));
        assertTrue(metaphone.isMetaphoneEqual("sugar", "sugar"));
        assertTrue(metaphone.isMetaphoneEqual("spaghetti", "spaghetti"));
        assertTrue(metaphone.isMetaphoneEqual("station", "station"));
        assertTrue(metaphone.isMetaphoneEqual("tomato", "tomato"));
        assertTrue(metaphone.isMetaphoneEqual("tuna", "tuna"));
        assertTrue(metaphone.isMetaphoneEqual("zero", "zero"));
        assertTrue(metaphone.isMetaphoneEqual("xylophone", "xylophone"));

        assertFalse(metaphone.isMetaphoneEqual("athlete", "banana"));
        assertFalse(metaphone.isMetaphoneEqual("knickerbocker", "sometimes"));
        assertFalse(metaphone.isMetaphoneEqual("sugar", "spaghetti"));
        assertFalse(metaphone.isMetaphoneEqual("station", "tomato"));
        assertFalse(metaphone.isMetaphoneEqual("tomato", "tuna"));
        assertFalse(metaphone.isMetaphoneEqual("zero", "xylophone"));

        assertFalse(metaphone.isMetaphoneEqual(null, null));
        assertFalse(metaphone.isMetaphoneEqual("", ""));
        assertFalse(metaphone.isMetaphoneEqual("athlete", ""));
        assertFalse(metaphone.isMetaphoneEqual("", "athlete"));
    }

    @Test
    public void testMaxCodeLen() {
        Metaphone metaphone = new Metaphone();

        assertEquals(4, metaphone.getMaxCodeLen());

        metaphone.setMaxCodeLen(5);
        assertEquals(5, metaphone.getMaxCodeLen());
    }
}