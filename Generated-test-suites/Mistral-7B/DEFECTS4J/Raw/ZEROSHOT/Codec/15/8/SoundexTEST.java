package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Soundex;

/**
 * Test class for Soundex.java
 */
public class SoundexTest {

    /**
     * Test method for {@link Soundex#Soundex()}.
     */
    @org.junit.Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        String result = soundex.encode("apple");
        assertEquals("K123", result);
    }

    /**
     * Test method for {@link Soundex#Soundex(char[])}.
     */
    @org.junit.Test
    public void testSoundexCharArray() {
        Soundex soundex = new Soundex(US_ENGLISH_MAPPING);
        String result = soundex.encode("apple");
        assertEquals("K123", result);
    }

    /**
     * Test method for {@link Soundex#Soundex(String)}.
     */
    @org.junit.Test
    public void testSoundexString() {
        Soundex soundex = new Soundex("US_ENGLISH_MAPPING_STRING");
        String result = soundex.encode("apple");
        assertEquals("K123", result);
    }

    /**
     * Test method for {@link Soundex#difference(StringEncoder, String, String)}.
     */
    @org.junit.Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        int difference = soundex.difference("apple", "appel");
        assertEquals(3, difference);
    }

    /**
     * Test method for {@link Soundex#encode(Object)}.
     */
    @org.junit.Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        String result = soundex.encode("apple");
        assertEquals("K123", result);
    }

    /**
     * Test method for {@link Soundex#encode(String)}.
     */
    @org.junit.Test
    public void testEncodeString() {
        Soundex soundex = new Soundex();
        String result = soundex.encode("apple");
        assertEquals("K123", result);
    }

    /**
     * Test method for {@link Soundex#getMappingCode(String, int)}.
     */
    @org.junit.Test
    public void testGetMappingCode() throws EncoderException {
        Soundex soundex = new Soundex();
        char mappingCode = soundex.getMappingCode("apple", 0);
        assertEquals('K', mappingCode);
        mappingCode = soundex.getMappingCode("apple", 1);
        assertEquals('1', mappingCode);
        mappingCode = soundex.getMappingCode("apple", 2);
        assertEquals('2', mappingCode);
        mappingCode = soundex.getMappingCode("apple", 3);
        assertEquals('3', mappingCode);
    }

    /**
     * Test method for {@link Soundex#soundex(String)}.
     */
    @org.junit.Test
    public void testSoundexStringInternal() {
        Soundex soundex = new Soundex();
        String result = soundex.soundex("apple");
        assertEquals("K123", result);
    }

    /**
     * Test method for {@link Soundex#getMaxLength()}.
     */
    @org.junit.Test
    public void testGetMaxLength() {
        Soundex soundex = new Soundex();
        assertEquals(4, soundex.getMaxLength());
        soundex = new Soundex();
        // setMaxLength is deprecated
        soundex.setMaxLength(5);
        assertEquals(4, soundex.getMaxLength());
    }

    /**
     * Test method for {@link Soundex#getSoundexMapping()}.
     */
    @org.junit.Test
    public void testGetSoundexMapping() {
        Soundex soundex = new Soundex();
        char[] result = soundex.getSoundexMapping();
        assertEquals("01230120022455012623010202", new String(result));
    }

    /**
     * Test method for {@link Soundex#map(char)}.
     */
    @org.junit.Test
    public void testMap() throws EncoderException {
        Soundex soundex = new Soundex();
        char mappingCode = soundex.map('A');
        assertEquals('K', mappingCode);
        mappingCode = soundex.map('B');
        assertEquals('P', mappingCode);
        mappingCode = soundex.map('C');
        assertEquals('K', mappingCode);
        mappingCode = soundex.map('D');
        assertEquals('T', mappingCode);
        mappingCode = soundex.map('E');
        assertEquals('S', mappingCode);
        mappingCode = soundex.map('F');
        assertEquals('F', mappingCode);
        mappingCode = soundex.map('G');
        assertEquals('G', mappingCode);
        mappingCode = soundex.map('H');
        assertEquals('H', mappingCode);
        mappingCode = soundex.map('I');
        assertEquals('N', mappingCode);
        mappingCode = soundex.map('J');
        assertEquals('J', mappingCode);
        mappingCode = soundex.map('K');
        assertEquals('K', mappingCode);
        mappingCode = soundex.map('L');
        assertEquals('L', mappingCode);
        mappingCode = soundex.map('M');
        assertEquals('M', mappingCode);
        mappingCode = soundex.map('N');
        assertEquals('D', mappingCode);
        mappingCode = soundex.map('O');
        assertEquals('O', mappingCode);
        mappingCode = soundex.map('P');
        assertEquals('P', mappingCode);
        mappingCode = soundex.map('Q');
        assertEquals('Q', mappingCode);
        mappingCode = soundex.map('R');
        assertEquals('R', mappingCode);
        mappingCode = soundex.map('S');
        assertEquals('S', mappingCode);
        mappingCode = soundex.map('T');
        assertEquals('T', mappingCode);
        mappingCode = soundex.map('U');
        assertEquals('W', mappingCode);
        mappingCode = soundex.map('V');
        assertEquals('V', mappingCode);
        mappingCode = soundex.map('W');
        assertEquals('W', mappingCode);
        mappingCode = soundex.map('X');
        assertEquals('X', mappingCode);
        mappingCode = soundex.map('Y');
        assertEquals('Y', mappingCode);
        mappingCode = soundex.map('Z');
        assertEquals('Z', mappingCode);

        mappingCode = soundex.map(' ');
        assertNull(mappingCode);

        mappingCode = soundex.map('ä');
        throw new EncoderException("The character is not mapped: ä");
    }

}