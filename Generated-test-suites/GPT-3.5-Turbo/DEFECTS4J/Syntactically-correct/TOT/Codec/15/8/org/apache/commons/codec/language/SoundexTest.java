
import org.junit.Assert;
import org.junit.Test;

public class SoundexTest {

    @Test
    public void testDifference_WhenSameString_ThenShouldReturn4() throws Exception {
        Soundex soundex = new Soundex();
        int difference = soundex.difference("hello", "hello");
        Assert.assertEquals(4, difference);
    }

    @Test
    public void testDifference_WhenDifferentStrings_ThenShouldReturnDifferentValues() throws Exception {
        Soundex soundex = new Soundex();
        int difference1 = soundex.difference("cat", "dog");
        int difference2 = soundex.difference("friend", "enemy");
        int difference3 = soundex.difference("apple", "banana");
        Assert.assertNotEquals(difference1, difference2);
        Assert.assertNotEquals(difference2, difference3);
        Assert.assertNotEquals(difference1, difference3);
    }

    @Test
    public void testEncode_WhenValidString_ThenShouldReturnValidEncodedString() throws Exception {
        Soundex soundex = new Soundex();
        String encodedString = soundex.encode("soundex");
        Assert.assertEquals("S532", encodedString);
    }

    @Test(expected = EncoderException.class)
    public void testEncode_WhenNonStringObject_ThenShouldThrowEncoderException() throws Exception {
        Soundex soundex = new Soundex();
        soundex.encode(123);
    }

    @Test
    public void testSoundex_WhenValidString_ThenShouldReturnValidSoundexCode() throws Exception {
        Soundex soundex = new Soundex();
        String soundexCode1 = soundex.soundex("Robert");
        String soundexCode2 = soundex.soundex("Rupert");
        String soundexCode3 = soundex.soundex("Rubin");
        Assert.assertEquals("R163", soundexCode1);
        Assert.assertEquals("R163", soundexCode2);
        Assert.assertEquals("R150", soundexCode3);
    }

    @Test
    public void testSoundex_WhenEmptyString_ThenShouldReturnEmptyString() throws Exception {
        Soundex soundex = new Soundex();
        String soundexCode = soundex.soundex("");
        Assert.assertEquals("", soundexCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSoundex_WhenCharacterNotMapped_ThenShouldThrowIllegalArgumentException() throws Exception {
        Soundex soundex = new Soundex();
        String soundexCode = soundex.soundex("#$%#");
    }
}

