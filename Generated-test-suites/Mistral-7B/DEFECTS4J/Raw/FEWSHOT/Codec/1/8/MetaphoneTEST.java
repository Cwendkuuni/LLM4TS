package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphoneEncode() throws EncoderException {
        Metaphone metaphone = new Metaphone();

        String original = "apple";
        String encoded = metaphone.encode(original);
        assertEquals("APL", encoded);

        original = "banana";
        encoded = metaphone.encode(original);
        assertEquals("BAN", encoded);

        original = "cat";
        encoded = metaphone.encode(original);
        assertEquals("KAT", encoded);

        original = "dog";
        encoded = metaphone.encode(original);
        assertEquals("DOG", encoded);

        original = "elephant";
        encoded = metaphone.encode(original);
        assertEquals("ELP", encoded);
    }

    @Test
    public void testMetaphoneIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();

        String str1 = "apple";
        String str2 = "appel";
        assertFalse("Metaphones of 'apple' and 'appel' should not be equal", metaphone.isMetaphoneEqual(str1, str2));

        str1 = "banana";
        str2 = "banane";
        assertTrue("Metaphones of 'banana' and 'banane' should be equal", metaphone.isMetaphoneEqual(str1, str2));

        str1 = "cat";
        str2 = "kat";
        assertTrue("Metaphones of 'cat' and 'kat' should be equal", metaphone.isMetaphoneEqual(str1, str2));

        str1 = "dog";
        str2 = "dog";
        assertTrue("Metaphones of 'dog' and 'dog' should be equal", metaphone.isMetaphoneEqual(str1, str2));

        str1 = "elephant";
        str2 = "elefant";
        assertTrue("Metaphones of 'elephant' and 'elefant' should be equal", metaphone.isMetaphoneEqual(str1, str2));

        str1 = "elephant";
        str2 = "elephent";
        assertFalse("Metaphones of 'elephant' and 'elephent' should not be equal", metaphone.isMetaphoneEqual(str1, str2));
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneEncodeInvalidInput() throws EncoderException {
        Metaphone metaphone = new Metaphone();

        // Null input
        metaphone.encode(null);

        // Empty input
        String empty = "";
        metaphone.encode(empty);

        // Input with non-alphanumeric characters
        String invalid1 = "apple#";
        metaphone.encode(invalid1);

        // Input with numbers
        String invalid2 = "apple1";
        metaphone.encode(invalid2);
    }
}