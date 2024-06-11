import org.junit.Assert;
import org.junit.Test;

public class SoundexTest {

    private final Soundex soundex = new Soundex();

    @Test
    public void testSoundex_NullInput_ReturnsNull() {
        String result = soundex.soundex(null);

        Assert.assertNull(result);
    }

    @Test
    public void testSoundex_EmptyInput_ReturnsEmptyString() {
        String result = soundex.soundex("");

        Assert.assertEquals("", result);
    }

    @Test
    public void testSoundex_UnmappedCharacters_IgnoreCharacters() {
        String result = soundex.soundex("abcde");

        Assert.assertEquals("a123", result);
    }

    @Test
    public void testSoundex_LowercaseCharacters_ConvertToUppercase() {
        String result = soundex.soundex("lower");

        Assert.assertEquals("L600", result);
    }

    @Test
    public void testSoundex_NumbersAndSpecialCharacters_IgnoreCharacters() {
        String result = soundex.soundex("1#&@$");

        Assert.assertEquals("0", result);
    }

    @Test
    public void testSoundex_LongInput_TruncateToMaxLength() {
        String result = soundex.soundex("alongstring");

        Assert.assertEquals("A524", result);
    }
}