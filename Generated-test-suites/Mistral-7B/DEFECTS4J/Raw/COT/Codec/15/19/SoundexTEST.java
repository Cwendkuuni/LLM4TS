package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

import static org.junit.Assert.assertEquals;

public class SoundexTest {

    

    @Test
    public void testUS_ENGLISH_defaultConstructor() {
        Soundex soundex = new Soundex();
        assertEquals("01230120022455012623010202", new String(soundex.getSoundexMapping()));
    }

    @Test
    public void testUS_ENGLISH_mappingConstructor() {
        Soundex soundex = new Soundex(Soundex.US_ENGLISH_MAPPING);
        assertEquals("01230120022455012623010202", new String(soundex.getSoundexMapping()));
    }

    @Test
    public void testCustomMappingConstructor() {
        char[] customMapping = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        Soundex soundex = new Soundex(customMapping);
        String customMappingString = new String(customMapping);
        assertEquals(customMappingString.substring(0, 26), new String(soundex.getSoundexMapping(), 0, 26));
    }

    @Test
    public void testEmptyString() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("", soundex.encode(""));
    }

    @Test
    public void testEncodeSingleLetter() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("A", soundex.encode("A"));
        assertEquals("B", soundex.encode("B"));
        // ...
        assertEquals("Z", soundex.encode("Z"));
    }

    @Test
    public void testEncodeMultipleLetters() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("A2", soundex.encode("APPLE"));
        assertEquals("B32", soundex.encode("BANANA"));
        // ...
        assertEquals("S543", soundex.encode("SISTER"));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeNonString() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.encode(new Object());
    }

    @Test
    public void testEncodeWithHWRule() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("B001", soundex.encode("BARRY"));
        assertEquals("B001", soundex.encode("BARRY WATSON"));
        assertEquals("B001", soundex.encode("BARRY WATSON FORD"));
    }

    @Test
    public void testEncodeWithMultipleHWRule() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("S200", soundex.encode("SHAW SHILLING"));
        assertEquals("S200", soundex.encode("SHAW SHILLING FISHER"));
        assertEquals("S200", soundex.encode("SHAW SHILLING FISHER KING"));
    }

    @Test
    public void testEncodeWithMultipleDifferentLetters() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("F253", soundex.encode("FISHER"));
        assertEquals("S200", soundex.encode("SHILLING"));
        assertEquals("F253200", soundex.encode("FISHER SHILLING"));
    }

    @Test
    public void testEncodeWithMultipleSameLetters() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("A2", soundex.encode("APPLE"));
        assertEquals("A2A2A2A2", soundex.encode("APPLEAPPLEAPPLEAPPLE"));
    }

    @Test
    public void testEncodeWithDigits() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("S43", soundex.encode("SIXTY"));
        assertEquals("S433", soundex.encode("SIXTY SIX"));
        assertEquals("S4333", soundex.encode("SIXTY SIXTY SIXTY"));
    }

    @Test
    public void testEncodeWithPunctuation() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("S133", soundex.encode("SIXTY!"));
        assertEquals("S1333", soundex.encode("SIXTY SIXTY SIXTY!"));
        assertEquals("S13333", soundex.encode("SIXTY SIXTY SIXTY SIXTY!"));
    }

    @Test
    public void testEncodeWithSpecialCharacters() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("S33", soundex.encode("SIXTY#"));
        assertEquals("S333", soundex.encode("SIXTY SIXTY#"));
        assertEquals("S3333", soundex.encode("SIXTY SIXTY SIXTY#"));
    }

    @Test
    public void testEncodeWithUpperCaseLetters() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("F253", soundex.encode("FISHER"));
        assertEquals("S200", soundex.encode("SHILLING"));
        assertEquals("F253200", soundex.encode("FISHER SHILLING"));

        Soundex soundexUpper = new Soundex();
        assertEquals("F253", soundexUpper.encode("FISHER"));
        assertEquals("S200", soundexUpper.encode("SHILLING"));
        assertEquals("F253200", soundexUpper.encode("FISHER SHILLING"));
    }

    @Test
    public void testEncodeWithEmptyStringAsArguments() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("", soundex.difference("", ""));
        assertEquals("", soundex.difference("", "APPLE"));
        assertEquals("", soundex.difference("APPLE", ""));
        assertEquals("4", soundex.difference("APPLE", "APPLE"));
    }

    @Test
    public void testEncodeWithSingleLetterAsArguments() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("0", soundex.difference('A', 'A'));
        assertEquals("1", soundex.difference('A', 'B'));
        assertEquals("1", soundex.difference('B', 'A'));
        assertEquals("4", soundex.difference('A', 'B'));
    }

    @Test
    public void testEncodeWithDifferentStrings() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("2", soundex.difference("APPLE", "BANANA"));
        assertEquals("3", soundex.difference("BANANA", "APPLE"));
        assertEquals("2", soundex.difference("APPLE", "BANANA"));
    }

    @Test
    public void testEncodeWithSameStrings() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("4", soundex.difference("APPLE", "APPLE"));
        assertEquals("4", soundex.difference("BANANA", "BANANA"));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeWithNullStringAsArguments() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.difference(null, "APPLE");
    }

    @Test(expected = EncoderException.class)
    public void testEncodeWithNullStringAsFirstArgument() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.difference("", null);
    }

    
}