package org.apache.commons.codec.language;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class SoundexTest {

    private static final Soundex US_ENGLISH = Soundex.US_ENGLISH;

    @Test
    public void testDifference_sameStrings() {
        assertEquals(4, US_ENGLISH.difference("cat", "cat"));
    }

    @Test
    public void testDifference_emptyStrings() {
        assertEquals(0, US_ENGLISH.difference("", ""));
    }

    @Test
    public void testDifference_differentStrings() {
        assertEquals(0, US_ENGLISH.difference("apple", "banana"));
    }

    @Test(expected = EncoderException.class)
    public void testDifference_nonStringArgument() {
        US_ENGLISH.difference(1, "banana");
    }

    @Test(expected = EncoderException.class)
    public void testDifference_nonStringSecondArgument() {
        US_ENGLISH.difference("apple", new Object());
    }

    @Test
    public void testEncode_nullInput() {
        assertNull(US_ENGLISH.encode(null));
    }

    @Test
    public void testEncode_emptyInput() {
        assertEquals("", US_ENGLISH.encode(""));
    }

    @Test
    public void testEncode_singleCharacter() {
        assertEquals("B", US_ENGLISH.encode("A"));
    }

    @Test
    public void testEncode_singleCharacterWithMapping() {
        final Soundex customMapping = new Soundex(new char[]{'A', 'B'});
        assertEquals("B", customMapping.encode("A"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncode_nonStringArgument() {
        US_ENGLISH.encode(1);
    }

    @Test
    public void testGetMappingCode_validCharacter() {
        for (int i = 0; i < 26; i++) {
            final char ch = (char) (i + 'A');
            assertEquals(US_ENGLISH_MAPPING[i], US_ENGLISH.getMappingCode("abcdefghijklmnopqrstuvwxyz".toCharArray(), i));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMappingCode_invalidCharacter() {
        US_ENGLISH.getMappingCode("abcdefghijklmnopqrstuvwxyz".toCharArray(), 26);
    }

    @Test
    public void testSoundex_emptyInput() {
        assertEquals("", US_ENGLISH.soundex(""));
    }

    @Test
    public void testSoundex_singleCharacter() {
        assertEquals("B", US_ENGLISH.soundex("A"));
    }

    @Test
    public void testSoundex_multipleCharacters() {
        assertEquals("K232", US_ENGLISH.soundex("Kate"));
        assertEquals("K132", US_ENGLISH.soundex("Karin"));
        assertEquals("K123", US_ENGLISH.soundex("Kathryn"));
        assertEquals("K120", US_ENGLISH.soundex("Kathleen"));
        assertEquals("K200", US_ENGLISH.soundex("Kathy"));
        assertEquals("K130", US_ENGLISH.soundex("Kathy"));
        assertEquals("K230", US_ENGLISH.soundex("Katherine"));
        assertEquals("K230", US_ENGLISH.soundex("Kathryn"));
    }

    @Test
    public void testSoundex_multipleCharactersWithCustomMapping() {
        final Soundex customMapping = new Soundex(new char[]{'A', 'B'});
        assertEquals("B", customMapping.soundex("A"));
        assertEquals("K1B", customMapping.soundex("Kate"));
        assertEquals("K1A", customMapping.soundex("Karin"));
        assertEquals("K1A", customMapping.soundex("Kathryn"));
        assertEquals("K1A", customMapping.soundex("Kathleen"));
        assertEquals("K1B", customMapping.soundex("Kathy"));
    }

    @Test
    public void testSoundex_multipleCharactersWithHWconsecutive() {
        assertEquals("K120", Soundex.US_ENGLISH.soundex("Kathleen"));
        assertEquals("K123", Soundex.US_ENGLISH.soundex("Kathryn"));
        assertEquals("K130", Soundex.US_ENGLISH.soundex("Kathy"));
        assertEquals("K132", Soundex.US_ENGLISH.soundex("Kate"));
    }

    @Test
    public void testSoundex_multipleCharactersWithHWseparated() {
        assertEquals("K1W0", Soundex.US_ENGLISH.soundex("Kathleen"));
        assertEquals("K1W2", Soundex.US_ENGLISH.soundex("Kathryn"));
        assertEquals("K1W1", Soundex.US_ENGLISH.soundex("Kathy"));
        assertEquals("K1W3", Soundex.US_ENGLISH.soundex("Kate"));
    }

    @Test
    public void testSoundex_multipleCharactersWithMultipleConsecutiveHW() { // GHOTH, QUITQUIT
        assertEquals("G1W0T0H", Soundex.US_ENGLISH.soundex("Ghoth"));
        assertEquals("Q1W0I0T0Q", Soundex.US_ENGLISH.soundex("Quitquit"));
    }

    @Test
    public void testSoundex_multipleCharactersWithMultipleConsecutiveHWandDifferentLast() { // QUITQUEEN
        assertEquals("Q1W0I0T0N", Soundex.US_ENGLISH.soundex("Quitqueen"));
    }

    @Test
    public void testSoundex_multipleCharactersWithMultipleConsecutiveHWandSameLast() { // QUITQUEEN, QUITQUEEN
        assertEquals("Q1W0I0T0N", Soundex.US_ENGLISH.soundex("Quitqueen"));
        assertEquals("Q1W0I0T0N", Soundex.US_ENGLISH.soundex("Quitqueen"));
    }

    @Test
    public void testSoundex_multipleCharactersWithMultipleConsecutiveHWandSameLastAndMappedToSame() { // QUITQUEEN, QUITQUEEN
        final Soundex customMapping = new Soundex(new char[]{'Q', 'U', 'I', 'T'});
        assertEquals("Q1W0I0T0N", customMapping.soundex("Quitqueen"));
        assertEquals("Q1W0I0T0N", customMapping.soundex("Quitqueen"));
    }

    @Test
    public void testSoundex_multipleCharactersWithMultipleConsecutiveHWandSameLastAndMappedToDifferent() { // QUITQUEEN, QUITQUEEN
        final Soundex customMapping = new Soundex(new char[]{'A', 'B', 'I', 'T'});
        assertEquals("A1W0I0T0N", customMapping.soundex("Quitqueen"));
        assertEquals("A1W0I0T0N", customMapping.soundex("Quitqueen"));
    }
}