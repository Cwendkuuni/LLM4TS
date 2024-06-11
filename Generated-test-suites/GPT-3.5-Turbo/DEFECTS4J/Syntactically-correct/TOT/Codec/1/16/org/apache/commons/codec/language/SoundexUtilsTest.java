import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;

public class SoundexUtilsTest {
    
    @Test
    public void testClean_WithLowerCaseString_ShouldReturnUpperCaseString() {
        String dirtyString = "abcde";
        String expected = "ABCDE";
        String actual = SoundexUtils.clean(dirtyString);
        assertEquals(expected, actual);
    }

    @Test
    public void testClean_WithMixedCaseString_ShouldReturnUpperCaseLetters() {
        String dirtyString = "AbCDe";
        String expected = "ABCDE";
        String actual = SoundexUtils.clean(dirtyString);
        assertEquals(expected, actual);
    }

    @Test
    public void testClean_WithNonAlphabeticCharacters_ShouldRemoveNonAlphabeticCharacters() {
        String dirtyString = "A!b.C@D#e$";
        String expected = "ABCDE";
        String actual = SoundexUtils.clean(dirtyString);
        assertEquals(expected, actual);
    }

    @Test
    public void testClean_WithEmptyString_ShouldReturnEmptyString() {
        String dirtyString = "";
        String expected = "";
        String actual = SoundexUtils.clean(dirtyString);
        assertEquals(expected, actual);
    }

    @Test
    public void testClean_WithNullString_ShouldReturnNullString() {
        String dirtyString = null;
        String expected = null;
        String actual = SoundexUtils.clean(dirtyString);
        assertEquals(expected, actual);
    }

    @Test
    public void testDifference_WithSameEncodedString_ShouldReturnSameLength() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "Abcde";
        String s2 = "ABCDE";
        int expected = encoder.encode(s1).length();
        int actual = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, actual);
    }

    @Test
    public void testDifference_WithDifferentEncodedString_ShouldReturnDifferentLength() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "Abcde";
        String s2 = "XYZ";
        int expected = 0;
        int actual = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceEncoded_WithSameEncodedString_ShouldReturnSameLength() {
        String es1 = "A123";
        String es2 = "B456";
        int expected = 0;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceEncoded_WithDifferentEncodedString_ShouldReturnDifferentLength() {
        String es1 = "A123";
        String es2 = "A124";
        int expected = 3;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceEncoded_WithDifferentLengthEncodedString_ShouldOnlyCompareMatchingLength() {
        String es1 = "A12345";
        String es2 = "A12";
        int expected = 3;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceEncoded_WithNullEncodedString_ShouldReturnZero() {
        String es1 = null;
        String es2 = "A124";
        int expected = 0;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
    }
}