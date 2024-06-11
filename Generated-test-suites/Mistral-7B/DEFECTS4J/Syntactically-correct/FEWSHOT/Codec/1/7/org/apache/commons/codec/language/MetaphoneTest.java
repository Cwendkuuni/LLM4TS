package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();

        assertEquals("BAQE", metaphone.metaphone("Banana"));
        assertEquals("K1EH", metaphone.metaphone("Apple"));
        assertEquals("F1AH", metaphone.metaphone("Ape"));
        assertEquals("KE0E", metaphone.metaphone("Cat"));
        assertEquals("S2IH", metaphone.metaphone("Sea"));

        assertEquals("A0E", metaphone.metaphone("Aardvark"));
        assertEquals("S1AH", metaphone.metaphone("Shark"));
        assertEquals("S1AH", metaphone.metaphone("Sparrow"));
        assertEquals("S2A", metaphone.metaphone("Star"));
        assertEquals("A0E", metaphone.metaphone("Artichoke"));
        assertEquals("S2IH", metaphone.metaphone("Seagull"));
        assertEquals("S1AH", metaphone.metaphone("Apricot"));

        assertEquals("", metaphone.metaphone(""));
        assertEquals("", metaphone.metaphone(null));

        assertNull(metaphone.encode(null));
    }

    @Test
    public void testEncodeDecode() {
        Metaphone metaphone = new Metaphone();

        String testString = "Banana";
        String encoded = metaphone.encode(testString);
        String decoded = (String) metaphone.decode(encoded);

        assertEquals(testString, decoded);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeException() {
        Metaphone metaphone = new Metaphone();

        metaphone.encode(123);
    }
}