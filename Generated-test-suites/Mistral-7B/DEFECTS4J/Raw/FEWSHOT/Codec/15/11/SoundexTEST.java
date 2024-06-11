package org.apache.commons.codec.language;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class SoundexTest {

    @Test
    public void testSoundexUSEnglishMapping() {
        Soundex soundex = Soundex.US_ENGLISH;

        // Test encode method
        assertEquals("K250", soundex.encode("Kate"));
        assertEquals("T113", soundex.encode("Tom"));
        assertEquals("B535", soundex.encode("Bobby"));
        assertEquals("S124", soundex.encode("Sally"));
        assertEquals("N322", soundex.encode("Nancy"));
        assertEquals("M530", soundex.encode("Martin"));
        assertEquals("F251", soundex.encode("Frank"));
        assertEquals("P231", soundex.encode("Paul"));
        assertEquals("R260", soundex.encode("Robert"));
        assertEquals("D242", soundex.encode("David"));
        assertEquals("G220", soundex.encode("George"));
        assertEquals("C540", soundex.encode("Charlie"));
        assertEquals("E210", soundex.encode("Edward"));
        assertEquals("J522", soundex.encode("John"));
        assertEquals("L130", soundex.encode("Linda"));
        assertEquals("A260", soundex.encode("Anna"));

        // Test difference method
        assertEquals(4, soundex.difference("Kate", "Kathy"));
        assertEquals(3, soundex.difference("Tom", "Tim"));
        assertEquals(3, soundex.difference("Tom", "Tina"));
        assertEquals(3, soundex.difference("Tom", "Toni"));
        assertEquals(3, soundex.difference("Bobby", "Bob"));
        assertEquals(2, soundex.difference("Sally", "Sara"));
        assertEquals(2, soundex.difference("Sally", "Samantha"));
        assertEquals(1, soundex.difference("Nancy", "Ann"));
        assertEquals(1, soundex.difference("Nancy", "Anne"));
        assertEquals(1, soundex.difference("Martin", "Marvin"));
        assertEquals(1, soundex.difference("Martin", "Michael"));
        assertEquals(1, soundex.difference("Martin", "Mark"));
        assertEquals(1, soundex.difference("Frank", "Fred"));
        assertEquals(1, soundex.difference("Frank", "Frederick"));
        assertEquals(1, soundex.difference("Paul", "Peter"));
        assertEquals(1, soundex.difference("Paul", "Patrick"));
        assertEquals(1, soundex.difference("Robert", "Rob"));
        assertEquals(1, soundex.difference("Robert", "Robin"));
        assertEquals(1, soundex.difference("David", "Dave"));
        assertEquals(1, soundex.difference("David", "Davis"));
        assertEquals(1, soundex.difference("George", "Jorge"));
        assertEquals(1, soundex.difference("George", "Jordan"));
        assertEquals(1, soundex.difference("Charlie", "Carl"));
        assertEquals(1, soundex.difference("Charlie", "Carlos"));
        assertEquals(1, soundex.difference("Edward", "Eddie"));
        assertEquals(1, soundex.difference("Edward", "Eddie"));
        assertEquals(1, soundex.difference("John", "Jack"));
        assertEquals(1, soundex.difference("John", "James"));
        assertEquals(1, soundex.difference("Linda", "Lynn"));
        assertEquals(1, soundex.difference("Linda", "Lynne"));
        assertEquals(1, soundex.difference("Anna", "Annabelle"));
        assertEquals(1, soundex.difference("Anna", "Anne"));

        // Test encode with custom mapping
        Soundex customSoundex = new Soundex(new String("123456789012301230120212455012623010202").toCharArray());
        assertEquals("A260", customSoundex.encode("Anna"));
        assertEquals("C540", customSoundex.encode("Charlie"));
        assertEquals("D242", customSoundex.encode("David"));
        assertEquals("F251", customSoundex.encode("Frank"));
        assertEquals("G220", customSoundex.encode("George"));
        assertEquals("J522", customSoundex.encode("John"));
        assertEquals("K250", customSoundex.encode("Kate"));
        assertEquals("L130", customSoundex.encode("Linda"));
        assertEquals("M530", customSoundex.encode("Martin"));
        assertEquals("N322", customSoundex.encode("Nancy"));
        assertEquals("P231", customSoundex.encode("Paul"));
        assertEquals("R260", customSoundex.encode("Robert"));
        assertEquals("T113", customSoundex.encode("Tom"));

        // Test difference with custom mapping
        assertEquals(3, customSoundex.difference("Tom", "Tim"));
        assertEquals(3, customSoundex.difference("Tom", "Tina"));
        assertEquals(3, customSoundex.difference("Tom", "Toni"));
        assertEquals(2, customSoundex.difference("Sally", "Sara"));
        assertEquals(2, customSoundex.difference("Sally", "Samantha"));
        assertEquals(1, customSoundex.difference("Nancy", "Ann"));
        assertEquals(1, customSoundex.difference("Nancy", "Anne"));
        assertEquals(1, customSoundex.difference("Martin", "Marvin"));
        assertEquals(1, customSoundex.difference("Martin", "Michael"));
        assertEquals(1, customSoundex.difference("Martin", "Mark"));
        assertEquals(1, customSoundex.difference("Frank", "Fred"));
        assertEquals(1, customSoundex.difference("Frank", "Frederick"));
        assertEquals(1, customSoundex.difference("Paul", "Peter"));
        assertEquals(1, customSoundex.difference("Paul", "Patrick"));
        assertEquals(1, customSoundex.difference("Robert", "Rob"));
        assertEquals(1, customSoundex.difference("Robert", "Robin"));
        assertEquals(1, customSoundex.difference("David", "Dave"));
        assertEquals(1, customSoundex.difference("David", "Davis"));
        assertEquals(1, customSoundex.difference("George", "Jorge"));
        assertEquals(1, customSoundex.difference("George", "Jordan"));
        assertEquals(1, customSoundex.difference("Charlie", "Carl"));
        assertEquals(1, customSoundex.difference("Charlie", "Carlos"));
        assertEquals(1, customSoundex.difference("Edward", "Eddie"));
        assertEquals(1, customSoundex.difference("Edward", "Eddie"));
        assertEquals(1, customSoundex.difference("John", "Jack"));
        assertEquals(1, customSoundex.difference("John", "James"));
        assertEquals(1, customSoundex.difference("Linda", "Lynn"));
        assertEquals(1, customSoundex.difference("Linda", "Lynne"));
        assertEquals(1, customSoundex.difference("Anna", "Annabelle"));
        assertEquals(1, customSoundex.difference("Anna", "Anne"));
    }

    @Test
    public void testSoundexNullInput() {
        Soundex soundex = Soundex.US_ENGLISH;
        assertNull(soundex.encode(null));
    }

    @Test(expected = EncoderException.class)
    public void testSoundexNonStringInput() {
        Soundex soundex = Soundex.US_ENGLISH;
        soundex.encode(new Object());
    }
}