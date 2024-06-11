import org.junit.Assert;
import org.junit.Test;

public class SoundexTest {

    @Test
    public void testEncode_ObjectOfString_ReturnsSoundexCode() {
        Soundex soundex = new Soundex();
        Assert.assertEquals("A226", soundex.encode("Apache"));
    }

    @Test(expected = EncoderException.class)
    public void testEncode_ObjectOfNonString_ThrowsEncoderException() {
        Soundex soundex = new Soundex();
        soundex.encode(123);
    }

    @Test
    public void testEncode_String_ReturnsSoundexCode() {
        Soundex soundex = new Soundex();
        Assert.assertEquals("A226", soundex.encode("Apache"));
    }

    @Test
    public void testEncode_EmptyString_ReturnsEmptySoundexCode() {
        Soundex soundex = new Soundex();
        Assert.assertEquals("", soundex.encode(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSoundex_NullString_ThrowsIllegalArgumentException() {
        Soundex soundex = new Soundex();
        soundex.soundex(null);
    }

    @Test
    public void testSoundex_StringWithLowercaseChars_ReturnsUppercaseSoundexCode() {
        Soundex soundex = new Soundex();
        Assert.assertEquals("A226", soundex.soundex("apache"));
    }

    @Test
    public void testSoundex_StringWithSpecialChars_ReturnsSoundexCode() {
        Soundex soundex = new Soundex();
        Assert.assertEquals("M016", soundex.soundex("Müller"));
    }

    @Test
    public void testDifference_SameSoundexCode_ReturnsFour() throws EncoderException {
        Soundex soundex = new Soundex();
        Assert.assertEquals(4, soundex.difference("Apache", "Apachu"));
    }

    @Test
    public void testDifference_CompletelyDifferentSoundexCode_ReturnsZero() throws EncoderException {
        Soundex soundex = new Soundex();
        Assert.assertEquals(0, soundex.difference("Apple", "Orange"));
    }

    @Test(expected = EncoderException.class)
    public void testDifference_EncodingError_ThrowsEncoderException() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.difference("Apache", 123);
    }

    @Test
    public void testDifference_EmptyStrings_ReturnsZero() throws EncoderException {
        Soundex soundex = new Soundex();
        Assert.assertEquals(0, soundex.difference("", ""));
    }

}