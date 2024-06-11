package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

public class SoundexTest {

    @Test
    public void testDefaultSoundexInstance() {
        Soundex soundex = Soundex.US_ENGLISH;
        String expected = "K321";
        String actual = soundex.encode("KEY");
        assertEquals(expected, actual);
    }

    @Test
    public void testCustomSoundexInstance() {
        char[] customMapping = new char[]{'B', '1', 'A', '1', 'C', '2', 'D', '2', 'E', '1', 'F', '1', 'G', '1', 'H', '1', 'I', '1', 'J', '1', 'K', '1', 'L', '1', 'M', '1', 'N', '2', 'O', '1', 'P', '1', 'Q', '1', 'R', '1', 'S', '1', 'T', '1', 'U', '1', 'V', '1', 'W', '1', 'X', '1', 'Y', '1', 'Z', '1'};
        Soundex soundex = new Soundex(customMapping);
        String expected = "B11";
        String actual = soundex.encode("BILL");
        assertEquals(expected, actual);
    }

    @Test
    public void testSoundexDifference() throws EncoderException {
        Soundex soundex = Soundex.US_ENGLISH;
        int difference = soundex.difference("BILL", "Will");
        assertEquals(3, difference);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeInvalidInput() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = null;
        soundex.encode(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapInvalidInput() {
        Soundex soundex = new Soundex(Soundex.US_ENGLISH_MAPPING);
        char invalidChar = '\u0001';
        soundex.map(invalidChar);
    }

    @Test
    public void testEncodeEmptyString() {
        Soundex soundex = Soundex.US_ENGLISH;
        String actual = soundex.encode("");
        assertEquals("", actual);
    }

    @Test
    public void testEncodeSingleCharacter() {
        Soundex soundex = Soundex.US_ENGLISH;
        String actual = soundex.encode("A");
        assertEquals("A000", actual);
    }

    @Test
    public void testEncodeMultipleIdenticalCharacters() {
        Soundex soundex = Soundex.US_ENGLISH;
        String actual = soundex.encode("AAAA");
        assertEquals("A000", actual);
    }

    @Test
    public void testEncodeDifferentFirstLetter() {
        Soundex soundex = Soundex.US_ENGLISH;
        String expected = "B200";
        String actual = soundex.encode("BILL");
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodeTrailingH() {
        Soundex soundex = Soundex.US_ENGLISH;
        String expected = "F200";
        String actual = soundex.encode("HARRIS");
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodeTrailingW() {
        Soundex soundex = Soundex.US_ENGLISH;
        String expected = "F200";
        String actual = soundex.encode("BARRY");
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodeMultipleSequentialIdenticalCharacters() {
        Soundex soundex = Soundex.US_ENGLISH;
        String expected = "F200";
        String actual = soundex.encode("HELLLO");
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodeMultipleSequentialDifferentCharacters() {
        Soundex soundex = Soundex.US_ENGLISH;
        String expected = "F121";
        String actual = soundex.encode("HELLO");
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodeMultipleSequentialDifferentLastCharacters() {
        Soundex soundex = Soundex.US_ENGLISH;
        String expected1 = "F121";
        String actual1 = soundex.encode("HELLO");
        String expected2 = "F121";
        String actual2 = soundex.encode("HELLT");
        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    @Test
    public void testEncodeUpperCaseLetters() {
        Soundex soundex = Soundex.US_ENGLISH;
        String expected = "F200";
        String actual = soundex.encode("HARRIS");
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodeLowerCaseLetters() {
        Soundex soundex = Soundex.US_ENGLISH;
        String expected = "F200";
        String actual = soundex.encode("harris");
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodeMultibyteCharacters() {
        Soundex soundex = new Soundex("\u00E9\u0065\u0062\u0061\u0063\u0068\u0065"); // échosis
        String expected = "S511"; // S is the Soundex code for "é"
        String actual = soundex.encode("échosis");
        assertEquals(expected, actual);
    }
}