package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MetaphoneTest {
    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        assertEquals("SFKS", metaphone.metaphone("sphinx"));
        assertEquals("HL", metaphone.metaphone("hello"));
        assertEquals("STRN", metaphone.metaphone("string"));
        assertEquals("WKTB", metaphone.metaphone("workbench"));
        assertEquals("", metaphone.metaphone(""));
        assertEquals("TKS", metaphone.metaphone("thanks"));
        assertEquals("SNTRTL", metaphone.metaphone("centralt"));
        assertEquals("NLN", metaphone.metaphone("newlin"));
        assertEquals("TRML", metaphone.metaphone("terminal"));
        assertEquals("LRG", metaphone.metaphone("large"));
    }

    @Test
    public void testEncode() throws Exception {
        Metaphone metaphone = new Metaphone();
        assertEquals("HLO", metaphone.encode("hello"));
        assertEquals("SFKS", metaphone.encode("sphinx"));
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        assertTrue(metaphone.isMetaphoneEqual("hello", "helo"));
        assertFalse(metaphone.isMetaphoneEqual("hello", "world"));
    }

    @Test(expected = org.apache.commons.codec.EncoderException.class)
    public void testEncode_Exception() throws Exception {
        Metaphone metaphone = new Metaphone();
        metaphone.encode(new Integer(1));
    }
}