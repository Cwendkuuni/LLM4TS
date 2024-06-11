package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MetaphoneTest {
    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        assertEquals("HT", metaphone.metaphone("hate"));
        assertEquals("KST", metaphone.metaphone("cast"));
        assertEquals("F", metaphone.metaphone("fight"));
        assertEquals("SNT", metaphone.metaphone("sent"));
        assertEquals("JXL", metaphone.metaphone("jacksal"));
    }

    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();
        
        assertEquals("HT", metaphone.encode("hate"));
        assertEquals("KST", metaphone.encode("cast"));
        assertEquals("F", metaphone.encode("fight"));
        assertEquals("SNT", metaphone.encode("sent"));
        assertEquals("JXL", metaphone.encode("jacksal"));
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        
        assertEquals(true, metaphone.isMetaphoneEqual("hate", "8"));
        assertEquals(true, metaphone.isMetaphoneEqual("cast", "KST"));
        assertEquals(false, metaphone.isMetaphoneEqual("fight", "F"));
        assertEquals(false, metaphone.isMetaphoneEqual("sent", "DNT"));
        assertEquals(false, metaphone.isMetaphoneEqual("jacksal", "JKSL"));
    }
}