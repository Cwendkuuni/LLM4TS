package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

/**
 * Test suite for the Metaphone class.
 */
public class MetaphoneTest {

    @Test
    public void testMetaphoneInstance() {
        Metaphone metaphone = new Metaphone();
        assertNotNull(metaphone);
    }

    @Test
    public void testEmptyStringMetaphone() {
        Metaphone metaphone = new Metaphone();
        assertEquals("", metaphone.metaphone(""));
    }

    @Test
    public void testSingleCharacterMetaphone() {
        Metaphone metaphone = new Metaphone();
        for (char c = 'A'; c <= 'Z'; c++) {
            assertEquals(Character.toString(c), metaphone.metaphone(Character.toString(c)));
        }
    }

    @Test
    public void testSimpleWordsMetaphone() {
        Metaphone metaphone = new Metaphone();
        String[] words = {"apple", "banana", "cat", "dog", "elephant", "frog", "goat", "house", "indigo", "jump"};
        String[] codes = {"APEL", "BANA", "KAT", "DOG", "ELPH", "FROG", "GOT", "HOWSE", "INDIG", "JUMP"};
        for (int i = 0; i < words.length; i++) {
            assertEquals(codes[i], metaphone.metaphone(words[i]));
        }
    }

    @Test
    public void testComplexWordsMetaphone() {
        Metaphone metaphone = new Metaphone();
        String[] words = {"metaphone", "algorithm", "encapsulation", "initialization", "subclassing", "superclassing"};
        String[] codes = {"MTAFA", "ALGORITHM", "ENCAPSULATION", "INITIALIZATION", "SUBCLASSING", "SUPERCLASSING"};
        for (int i = 0; i < words.length; i++) {
            assertEquals(codes[i], metaphone.metaphone(words[i]));
        }
    }

    @Test(expected = EncoderException.class)
    public void testNullStringMetaphone() {
        Metaphone metaphone = new Metaphone();
        metaphone.metaphone(null);
    }

    @Test(expected = EncoderException.class)
    public void testEmptyArrayMetaphone() {
        Metaphone metaphone = new Metaphone();
        String[] strings = {};
        metaphone.isMetaphoneEqual(strings[0], strings[1]);
    }

    @Test
    public void testMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        String[] words = {"apple", "apple", "banana", "banana"};
        for (int i = 0; i < words.length; i++) {
            assertTrue(metaphone.isMetaphoneEqual(words[i], words[i]));
            for (int j = 0; j < words.length; j++) {
                if (i != j) {
                    assertFalse(metaphone.isMetaphoneEqual(words[i], words[j]));
                }
            }
        }
    }

    @Test
    public void testMetaphoneMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        assertEquals(4, metaphone.getMaxCodeLen());
        metaphone.setMaxCodeLen(3);
        String word = "alphabet";
        assertEquals("ALPHABET", metaphone.metaphone(word));
        assertEquals("ALP", metaphone.metaphone(word), "Unexpected length");
    }
}