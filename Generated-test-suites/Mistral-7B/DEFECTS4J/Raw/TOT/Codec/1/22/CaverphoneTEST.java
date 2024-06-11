package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testEmptyString() {
        Caverphone encoder = new Caverphone();
        String result = encoder.caverphone("");
        assertEquals("1111111111", result);
    }

    @Test
    public void testNullString() {
        Caverphone encoder = new Caverphone();
        String result = encoder.caverphone(null);
        assertEquals("1111111111", result);
    }

    @Test
    public void testLowercaseString() {
        Caverphone encoder = new Caverphone();
        String input = "hello";
        String result = encoder.caverphone(input);
        String expected = "H21H21H21H21H21";
        assertEquals(expected, result);
    }

    @Test
    public void testStringWithDigits() {
        Caverphone encoder = new Caverphone();
        String input = "hello123";
        String result = encoder.caverphone(input);
        String expected = "H21H21H21H21H21K3K3";
        assertEquals(expected, result);
    }

    @Test
    public void testStringWithSpecialCharacters() {
        Caverphone encoder = new Caverphone();
        String input = "hello@world";
        String result = encoder.caverphone(input);
        String expected = "H21H21H21H21H21";
        assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testNonStringObject() {
        Caverphone encoder = new Caverphone();
        Object nonStringObject = new Object();
        encoder.encode(nonStringObject);
    }

    @Test
    public void testStringWithStartOptions() {
        Caverphone encoder = new Caverphone();
        String input = "coughenough";
        String result = encoder.caverphone(input);
        String expected = "cou2fenaou2f";
        assertEquals(expected, result);
    }

    @Test
    public void testStringWithReplacements() {
        Caverphone encoder = new Caverphone();
        String input = "ceasarcy";
        String result = encoder.caverphone(input);
        String expected = "seasarsi";
        assertEquals(expected, result);
    }

    @Test
    public void testStringWithHandlingOfSounds() {
        Caverphone encoder = new Caverphone();
        String input = "ough";
        String result = encoder.caverphone(input);
        String expected = "rou2f";
        assertEquals(expected, result);
    }

    @Test
    public void testStringWithMultipleSounds() {
        Caverphone encoder = new Caverphone();
        String input = "roughcough";
        String result = encoder.caverphone(input);
        String expected = "rou2ftou2f";
        assertEquals(expected, result);
    }

    @Test
    public void testStringWithRemovals() {
        Caverphone encoder = new Caverphone();
        String input = "two2three";
        String result = encoder.caverphone(input);
        String expected = "23";
        assertEquals(expected, result);
    }

    @Test
    public void testStringWithCombinations() {
        Caverphone encoder = new Caverphone();
        String input = "toughbob";
        String result = encoder.caverphone(input);
        String expected = "tou2fpob";
        assertEquals(expected, result);
    }

    @Test
    public void testStringWithFinalE() {
        Caverphone encoder = new Caverphone();
        String input = "enough";
        String result = encoder.caverphone(input);
        String expected = "enou2f";
        assertEquals(expected, result);
    }

    @Test
    public void testStringWithYSound() {
        Caverphone encoder = new Caverphone();
        String input = "why";
        String result = encoder.caverphone(input);
        String expected = "Why";
        assertEquals(expected, result);
    }

    @Test
    public void testStringWithMultipleYSounds() {
        Caverphone encoder = new Caverphone();
        String input = "whywhy";
        String result = encoder.caverphone(input);
        String expected = "WhyWhy";
        assertEquals(expected, result);
    }

    @Test
    public void testStringsWithIdenticalCaverphoneCodes() {
        Caverphone encoder = new Caverphone();
        String string1 = "hello";
        String string2 = "hello";
        boolean result = encoder.isCaverphoneEqual(string1, string2);
        assertTrue(result);
    }

    @Test
    public void testStringsWithDifferentCaverphoneCodes() {
        Caverphone encoder = new Caverphone();
        String string1 = "hello";
        String string2 = "world";
        boolean result = encoder.isCaverphoneEqual(string1, string2);
        assertFalse(result);
    }

    @Test
    public void testStringsWithNullStringComparison() {
        Caverphone encoder = new Caverphone();
        String string1 = null;
        String string2 = "hello";
        boolean result = encoder.isCaverphoneEqual(string1, string2);
        assertFalse(result);
    }
}