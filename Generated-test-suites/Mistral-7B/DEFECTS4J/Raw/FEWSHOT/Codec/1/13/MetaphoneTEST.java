package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();

        // Test empty and null strings
        assertEquals("", metaphone.metaphone(""));
        assertNull(metaphone.metaphone(null));

        // Test single character strings
        assertEquals("A", metaphone.metaphone("A"));
        assertEquals("B", metaphone.metaphone("B"));

        // Test simple words
        assertEquals("A0", metaphone.metaphone("athlete"));
        assertEquals("B3", metaphone.metaphone("banana"));
        assertEquals("F00", metaphone.metaphone("thetha"));
        assertEquals("K0", metaphone.metaphone("zero"));

        // Test words with initial exceptions
        assertEquals("A0", metaphone.metaphone("athlete")); // KN exception
        assertEquals("AE", metaphone.metaphone("apple")); // AE exception
        assertEquals("W2", metaphone.metaphone("water")); // WR exception
        assertEquals("SX", metaphone.metaphone("xylophone")); // X exception

        // Test words with double letters
        assertEquals("AE", metaphone.metaphone("bake")); // duplicate A
        assertEquals("AE", metaphone.metaphone("bake")); // duplicate A, test case insensitivity
        assertEquals("S", metaphone.metaphone("babble")); // duplicate B
        assertEquals("S", metaphone.metaphone("babble")); // duplicate B, test case insensitivity

        // Test words with initial silent letters
        assertEquals("J0", metaphone.metaphone("grape")); // silent G
        assertEquals("J0", metaphone.metaphone("grape")); // silent G, test case insensitivity

        // Test words with initial silent letters and vowels
        assertEquals("J0E", metaphone.metaphone("bough")); // silent G + vowel
        assertEquals("J0E", metaphone.metaphone("bough")); // silent G + vowel, test case insensitivity

        // Test words with final silent letters
        assertEquals("K2", metaphone.metaphone("bungle")); // silent G
        assertEquals("K2", metaphone.metaphone("bungle")); // silent G, test case insensitivity

        // Test words with final silent letters and vowels
        assertEquals("J2E", metaphone.metaphone("cough")); // silent G + vowel
        assertEquals("J2E", metaphone.metaphone("cough")); // silent G + vowel, test case insensitivity

        // Test words with double final silent letters
        assertEquals("J2", metaphon.metaphone("hushush")); // silent H
        assertEquals("J2", metaphon.metaphone("hushush")); // silent H, test case insensitivity

        // Test words with multiple consonant clusters
        assertEquals("S0X", metaphone.metaphone("psychopath"));

        // Test words with "sh" sound
        assertEquals("X", metaphone.metaphone("shoe"));
        assertEquals("X", metaphone.metaphone("shoes"));
        assertEquals("X", metaphone.metaphone("shoulder"));
        assertEquals("X", metaphone.metaphone("shoulders"));

        // Test words with "si" and "sion" sounds
        assertEquals("X", metaphone.metaphone("sion"));
        assertEquals("SX", metaphone.metaphone("ision"));
        assertEquals("SX", metaphone.metaphone("sit"));
        assertEquals("SX", metaphone.metaphone("sits"));

        // Test words with "tch" sound
        assertEquals("0", metaphone.metaphone("theatre"));
        assertEquals("0", metaphone.metaphone("theaters"));
        assertEquals("0", metaphone.metaphone("thetic"));
        assertEquals("0", metaphone.metaphone("thetics"));

        // Test words with "ch" sound
        assertEquals("X", metaphone.metaphone("church"));
        assertEquals("X", metaphone.metaphone("churches"));
        assertEquals("X", metaphone.metaphone("chimney"));
        assertEquals("X", metaphone.metaphone("chimneys"));

        // Test words with "qu" sound
        assertEquals("K", metaphone.metaphone("queue"));
        assertEquals("K", metaphone.metaphone("queues"));
        assertEquals("K", metaphone.metaphone("quilt"));
        assertEquals("K", metaphone.metaphone("quilts"));

        // Test words with "th" sound
        assertEquals("0", metaphone.metaphone("this"));
        assertEquals("0", metaphone.metaphone("these"));
        assertEquals("0", metaphone.metaphone("those"));
        assertEquals("0", metaphone.metaphone("those"));

        // Test words with "ph" sound
        assertEquals("F", metaphone.metaphone("phone"));
        assertEquals("F", metaphone.metaphone("phones"));
        assertEquals("F", metaphone.metaphone("philosophy"));
        assertEquals("F", metaphone.metaphone("philosophies"));

        // Test words with "gh" sound
        assertEquals("J", metaphone.metaphone("ough"));
        assertEquals("J", metaphone.metaphone("oughs"));
        assertEquals("J", metaphone.metaphone("bright"));
        assertEquals("J", metaphone.metaphone("brights"));

        // Test words with "ng" sound
        assertEquals("NG", metaphone.metaphone("sing"));
        assertEquals("NG", metaphone.metaphone("sings"));
        assertEquals("NG", metaphone.metaphone("ring"));
        assertEquals("NG", metaphone.metaphone("rings"));

        // Test words with "ch" and "qu" sounds
        assertEquals("KX", metaphone.metaphone("quichotte"));

        // Test words with "ng" and "qu" sounds
        assertEquals("NGK", metaphone.metaphone("quangos"));
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();

        assertEquals(true, metaphone.isMetaphoneEqual("athlete", "athlete"));
        assertEquals(true, metaphone.isMetaphoneEqual("athlete", "athlete")); // test case insensitivity

        assertEquals(false, metaphone.isMetaphoneEqual("athlete", "banana"));
        assertEquals(false, metaphone.isMetaphoneEqual("athlete", "banana")); // test case insensitivity

        assertEquals(true, metaphone.isMetaphoneEqual("apple", "apple"));
        assertEquals(true, metaphone.isMetaphoneEqual("apple", "apple")); // test case insensitivity

        assertEquals(false, metaphone.isMetaphoneEqual("apple", "banana"));
        assertEquals(false, metaphone.isMetaphoneEqual("apple", "banana")); // test case insensitivity

        assertEquals(true, metaphone.isMetaphoneEqual("water", "water"));
        assertEquals(true, metaphone.isMetaphoneEqual("water", "water")); // test case insensitivity

        assertEquals(false, metaphone.isMetaphoneEqual("water", "bubble"));
        assertEquals(false, metaphone.isMetaphoneEqual("water", "bubble")); // test case insensitivity

        assertEquals(true, metaphone.isMetaphoneEqual("bough", "bough"));
        assertEquals(true, metaphone.isMetaphoneEqual("bough", "bough")); // test case insensitivity

        assertEquals(false, metaphone.isMetaphoneEqual("bough", "bubble"));
        assertEquals(false, metaphone.isMetaphoneEqual("bough", "bubble")); // test case insensitivity
    }
}