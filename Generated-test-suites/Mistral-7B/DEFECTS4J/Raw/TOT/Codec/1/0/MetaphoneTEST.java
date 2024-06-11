package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone encoder = new Metaphone();

        // Test empty string
        assertEquals("", encoder.metaphone(""));

        // Test single character string
        assertEquals("A", encoder.metaphone("A"));
        assertEquals("B", encoder.metaphone("B"));

        // Test basic cases
        assertEquals("A0", encoder.metaphone("APPLE"));
        assertEquals("A0", encoder.metaphone("apple"));
        assertEquals("A0", encoder.metaphone("Apple"));

        assertEquals("B3", encoder.metaphone("BANANA"));
        assertEquals("B3", encoder.metaphone("banana"));
        assertEquals("B3", encoder.metaphone("Banana"));

        assertEquals("C2", encoder.metaphone("CAT"));
        assertEquals("C2", encoder.metaphone("cat"));
        assertEquals("C2", encoder.metaphone("Cat"));

        assertEquals("CH", encoder.metaphone("CHAIR"));
        assertEquals("CH", encoder.metaphone("chair"));
        assertEquals("CH", encoder.metaphone("Chair"));

        assertEquals("D2", encoder.metaphone("DOG"));
        assertEquals("D2", encoder.metaphone("dog"));
        assertEquals("D2", encoder.metaphone("Dog"));

        assertEquals("ED", encoder.metaphone("ELEPHANT"));
        assertEquals("ED", encoder.metaphone("elephant"));
        assertEquals("ED", encoder.metaphone("Elephant"));

        assertEquals("EI", encoder.metaphone("EGO"));
        assertEquals("EI", encoder.metaphone("ego"));
        assertEquals("EI", encoder.metaphone("Ego"));

        assertEquals("EK", encoder.metaphone("EGG"));
        assertEquals("EK", encoder.metaphone("egg"));
        assertEquals("EK", encoder.metaphone("Egg"));

        // Test edge cases
        assertEquals("AX", encoder.metaphone("AARDVARK"));
        assertEquals("AX", encoder.metaphone("aardvark"));
        assertEquals("AX", encoder.metaphone("Aardvark"));

        assertEquals("A0", encoder.metaphone("AAABBBBBCCCCCC"));
        assertEquals("A0", encoder.metaphone("aaabbbbbcccccc"));
        assertEquals("A0", encoder.metaphone("AAA bbb CCcCcC"));

        assertEquals("B3", encoder.metaphone("AApple"));
        assertEquals("B3", encoder.metaphone("aapple"));
        assertEquals("B3", encoder.metaphone("A apple"));

        assertEquals("B3", encoder.metaphone("AppleApple"));
        assertEquals("B3", encoder.metaphone("appleapple"));
        assertEquals("B3", encoder.metaphone("Apple apple"));

        // Test error cases
        assertThrows(EncoderException.class, () -> encoder.metaphone(null));
        assertThrows(EncoderException.class, () -> encoder.metaphone(""));
        assertThrows(EncoderException.class, () -> encoder.metaphone("123"));
        assertThrows(EncoderException.class, () -> encoder.metaphone("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
    }

}