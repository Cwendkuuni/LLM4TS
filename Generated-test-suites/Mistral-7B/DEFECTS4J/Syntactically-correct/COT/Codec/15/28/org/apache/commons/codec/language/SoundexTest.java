package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

public class SoundexTest {

    

    @Test
    public void testDefaultSoundexInstance() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "Apple";
        String expectedOutput = "A123";
        String actualOutput = soundex.soundex(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCustomSoundexInstance() {
        char[] customMapping = {'0', '1', '2', '2', '3', '3', '4', '4', '5', '5', '5', '6', '6', '7', '7', '7', '7', '8', '8', '8', '9', '9'};
        Soundex soundex = new Soundex(customMapping);
        String input = "Example";
        String expectedOutput = "E251";
        String actualOutput = soundex.soundex(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testEncodeObject() {
        Soundex soundex = new Soundex();
        Object input = "Orange";
        String expectedOutput = "O161";
        String actualOutput = soundex.encode(input).toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testEncodeString() {
        Soundex soundex = new Soundex();
        String input = "Grapefruit";
        String expectedOutput = "G131";
        String actualOutput = soundex.encode(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeNonStringInput() {
        Soundex soundex = new Soundex();
        int input = 5;
        soundex.encode(input);
    }

    @Test
    public void testDifference() {
        Soundex soundex = Soundex.US_ENGLISH;
        String str1 = "Apple";
        String str2 = "Appaloosa";
        int expectedOutput = 2;
        int actualOutput = soundex.difference(str1, str2);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testGetMaxLength() {
        Soundex soundex = new Soundex();
        int expectedOutput = 4;
        int actualOutput = soundex.getMaxLength();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testGetSoundexMapping() {
        Soundex soundex = new Soundex();
        char[] expectedOutput = {'0', '1', '2', '2', '3', '3', '4', '4', '5', '5', '5', '6', '6', '7', '7', '7', '7', '8', '8', '8', '9', '9'};
        char[] actualOutput = soundex.getSoundexMapping();
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMap() {
        Soundex soundex = new Soundex();
        char input = 'A';
        char expectedOutput = '1';
        char actualOutput = soundex.map(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapUnmappedCharacter() {
        Soundex soundex = new Soundex();
        char input = 'X';
        soundex.map(input);
    }

    @Test
    public void testSoundexWithEmptyString() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "";
        String expectedOutput = "";
        String actualOutput = soundex.soundex(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSoundexWithSingleCharacter() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "A";
        String expectedOutput = "A1";
        String actualOutput = soundex.soundex(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSoundexWithMultipleCharacters() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "Apple";
        String expectedOutput = "A123";
        String actualOutput = soundex.soundex(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSoundexWithDoubleLetter() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "Bookkeeper";
        String expectedOutput = "B162";
        String actualOutput = soundex.soundex(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSoundexWithDoubleLetterAndHWRule() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "Bright";
        String expectedOutput = "B132";
        String actualOutput = soundex.soundex(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSoundexWithDoubleLetterAndHWRuleDifferentPosition() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "Weight";
        String expectedOutput = "W232";
        String actualOutput = soundex.soundex(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSoundexWithDoubleLetterAndHWRuleSameCharacter() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "Bubble";
        String expectedOutput = "B131";
        String actualOutput = soundex.soundex(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSoundexWithDoubleLetterAndHWRuleDifferentCharacters() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "Bubblegum";
        String expectedOutput = "B131";
        String actualOutput = soundex.soundex(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSoundexWithDoubleLetterAndDifferentCodeGroups() {
        Soundex soundex = new Soundex(new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'});
        String input = "AppleBanana";
        String expectedOutput = "A123B123";
        String actualOutput = soundex.soundex(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSoundexWithDifferentCase() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "ApPle";
        String expectedOutput = "A123";
        String actualOutput = soundex.soundex(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSoundexWithAccentedLetters() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "Lëon";
        String expectedOutput = "L131";
        String actualOutput = soundex.soundex(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSoundexWithDiacriticalMarks() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "Mëlancolique";
        String expectedOutput = "M263";
        String actualOutput = soundex.soundex(input);
        assertEquals(expectedOutput, actualOutput);
    }

    
}