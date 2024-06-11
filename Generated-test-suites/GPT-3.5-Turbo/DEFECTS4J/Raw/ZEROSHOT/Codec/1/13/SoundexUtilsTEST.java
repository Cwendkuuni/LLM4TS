import org.junit.Test;

import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean_NullInput_ReturnNull() {
        assertNull(SoundexUtils.clean(null));
    }

    @Test
    public void testClean_EmptyInput_ReturnEmptyString() {
        assertEquals("", SoundexUtils.clean(""));
    }

    @Test
    public void testClean_InputWithLowerCaseLetters_ReturnUpperCaseString() {
        assertEquals("HELLO", SoundexUtils.clean("hElLo"));
    }

    @Test
    public void testClean_InputWithNonLetterCharacters_ReturnOnlyLettersString() {
        assertEquals("ABCD", SoundexUtils.clean("A!B3C4d"));
    }

    @Test
    public void testDifference_SameEncodedStrings_ReturnStringLength() throws EncoderException {
        StringEncoder encoder = new MyEncoder();
        String s1 = encoder.encode("hello");
        String s2 = encoder.encode("hello");
        assertEquals(s1.length(), SoundexUtils.difference(encoder, s1, s2));
    }

    @Test
    public void testDifference_DifferentEncodedStrings_ReturnNumberOfMatchingCharacters() throws EncoderException {
        StringEncoder encoder = new MyEncoder();
        String s1 = encoder.encode("hello");
        String s2 = encoder.encode("hallo");
        assertEquals(3, SoundexUtils.difference(encoder, s1, s2));
    }

    @Test
    public void testDifference_NullEncodedStrings_ReturnZero() throws EncoderException {
        StringEncoder encoder = new MyEncoder();
        assertEquals(0, SoundexUtils.difference(encoder, null, null));
    }

    private class MyEncoder implements StringEncoder {

        @Override
        public String encode(String str) throws EncoderException {
            // Custom encoding implementation for testing purposes
            return str;
        }
    }

    private class MyEncoderDifference implements StringEncoder {

        @Override
        public String encode(String str) throws EncoderException {
            // Custom encoding implementation for testing purposes
            return str + "123";
        }
    }

    @Test(expected = EncoderException.class)
    public void testDifference_EncodingException_ThrowEncoderException() throws EncoderException {
        StringEncoder encoder = new MyEncoderDifference();
        String s1 = "hello";
        String s2 = "hello";
        SoundexUtils.difference(encoder, s1, s2);
    }

    @Test
    public void testDifferenceEncoded_SameEncodedStrings_ReturnStringLength() {
        String es1 = "abcd";
        String es2 = "abcd";
        assertEquals(es1.length(), SoundexUtils.differenceEncoded(es1, es2));
    }

    @Test
    public void testDifferenceEncoded_DifferentEncodedStrings_ReturnNumberOfMatchingCharacters() {
        String es1 = "abcd";
        String es2 = "afcd";
        assertEquals(3, SoundexUtils.differenceEncoded(es1, es2));
    }

    @Test
    public void testDifferenceEncoded_NullEncodedStrings_ReturnZero() {
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
    }

    @Test
    public void testDifferenceEncoded_StringsWithDifferentLengths_ReturnNumberOfMatchingCharactersBasedOnShorterLength() {
        String es1 = "abcd";
        String es2 = "abcdef";
        assertEquals(4, SoundexUtils.differenceEncoded(es1, es2));
    }

    @Test
    public void testDifferenceEncoded_StringsWithDifferentLengths_ReturnNumberOfMatchingCharactersBasedOnShorterLengthReverseOrder() {
        String es1 = "abcdef";
        String es2 = "abcd";
        assertEquals(4, SoundexUtils.differenceEncoded(es1, es2));
    }

}