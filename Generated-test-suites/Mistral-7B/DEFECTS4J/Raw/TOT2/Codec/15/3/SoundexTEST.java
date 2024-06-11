import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {
    @Test
    public void testDefaultSoundexInstance() throws EncoderException {
        Soundex soundex = new Soundex();
        String str1 = "Apple";
        String str2 = "Orange";
        String expectedResult1 = "A123";
        String expectedResult2 = "O152";

        String result1 = soundex.soundex(str1);
        String result2 = soundex.soundex(str2);

        assertEquals(expectedResult1, result1);
        assertEquals(expectedResult2, result2);

        int difference = soundex.difference(str1, str2);
        assertEquals(2, difference);
    }

    @Test(expected = EncoderException.class)
    public void testInvalidArgumentException() throws EncoderException {
        Soundex soundex = new Soundex();
        String str = null;

        soundex.soundex(str);
    }
}