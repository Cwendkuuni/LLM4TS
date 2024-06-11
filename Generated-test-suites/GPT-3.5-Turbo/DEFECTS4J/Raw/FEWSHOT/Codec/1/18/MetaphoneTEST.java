package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class MetaphoneTest {

    private Metaphone metaphone;

    @Before
    public void setUp() {
        metaphone = new Metaphone();
    }

    @Test
    public void testMetaphone() {
        assertEquals("MTFN", metaphone.metaphone("Metaphone"));
        assertEquals("FNDR", metaphone.metaphone("Founder"));
        assertEquals("JNTRNL", metaphone.metaphone("Internal"));
        assertEquals("", metaphone.metaphone(null));
        assertEquals("", metaphone.metaphone(""));
    }

    @Test
    public void testEncode() {
        assertEquals("MTFN", metaphone.encode("Metaphone"));
        assertEquals("FNDR", metaphone.encode("Founder"));
        assertEquals("JNTRNL", metaphone.encode("Internal"));
        assertEquals("", metaphone.encode(null));
        assertEquals("", metaphone.encode(""));
    }

    @Test
    public void testIsMetaphoneEqual() {
        assertTrue(metaphone.isMetaphoneEqual("Metaphone", "Metaphone"));
        assertFalse(metaphone.isMetaphoneEqual("Metaphone", "Phoneme"));
        assertTrue(metaphone.isMetaphoneEqual("", ""));
        assertFalse(metaphone.isMetaphoneEqual(null, "Null"));
    }

    @Test
    public void testGetMaxCodeLen() {
        assertEquals(4, metaphone.getMaxCodeLen());
    }

    @Test
    public void testSetMaxCodeLen() {
        metaphone.setMaxCodeLen(6);
        assertEquals(6, metaphone.getMaxCodeLen());
    }
}