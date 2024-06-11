import org.junit.Test;
import org.apache.commons.codec.EncoderException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class SoundexUtilsTest {

    @Test
    public void testClean_nullInput() {
        assertNull(SoundexUtils.clean(null));
    }

    @Test
    public void testClean_emptyInput() {
        assertEquals("", SoundexUtils.clean(""));
    }

    @Test
    public void testClean_noLettersInput() {
        assertEquals("", SoundexUtils.clean("1234567890"));
    }

    @Test
    public void testClean_someLettersInput() {
        assertEquals("HELLO", SoundexUtils.clean("h2e4l6l8o0"));
    }

    @Test
    public void testClean_allLettersInput() {
        assertEquals("HELLO", SoundexUtils.clean("hE8lL8oO2"));
    }

    @Test
    public void testClean_upperCaseInput() {
        assertEquals("HELLO", SoundexUtils.clean("HELLO"));
    }

    @Test
    public void testDifference_sameEncodedStrings() throws EncoderException {
        assertEquals(4, SoundexUtils.difference(new Soundex(), "HELLO", "HELLO"));
    }

    @Test
    public void testDifference_differentEncodedStrings() throws EncoderException {
        assertEquals(1, SoundexUtils.difference(new Soundex(), "HELLO", "WORLD"));
    }

    @Test
    public void testDifference_encodedStringWithLengthDifference() throws EncoderException {
        assertEquals(2, SoundexUtils.difference(new Soundex(), "HELLO", "WOR"));
    }

    @Test(expected = EncoderException.class)
    public void testDifference_encoderException() throws EncoderException {
        SoundexUtils.difference(new Soundex(), "HELLO", "WORLD!");
    }

    @Test
    public void testDifferenceEncoded_nullInput() {
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
    }

    @Test
    public void testDifferenceEncoded_emptyInput() {
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
    }

    @Test
    public void testDifferenceEncoded_sameEncodedStrings() {
        assertEquals(4, SoundexUtils.differenceEncoded("HELLO", "HELLO"));
    }

    @Test
    public void testDifferenceEncoded_differentEncodedStrings() {
        assertEquals(0, SoundexUtils.differenceEncoded("HELLO", "WORLD"));
    }

    @Test
    public void testDifferenceEncoded_encodedStringWithLengthDifference() {
        assertEquals(2, SoundexUtils.differenceEncoded("HELLO", "WOR"));
    }

    @Test
    public void testDifferenceEncoded_lengthDifferenceGreaterThanEncodedString() {
        assertEquals(3, SoundexUtils.differenceEncoded("HELLO", "XYZW"));
    }

    @Test
    public void testDifferenceEncoded_lengthDifferenceLessThanEncodedString() {
        assertEquals(4, SoundexUtils.differenceEncoded("HELLO", "W"));
    }

}