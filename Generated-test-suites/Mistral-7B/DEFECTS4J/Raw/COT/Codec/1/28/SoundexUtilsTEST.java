import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexUtilsTest {

    @Test
    public void testCleanMethod() {
        String input = "aBc Def gHi jKl mNo pQr sTu vWx";
        String expectedOutput = "A E F G H I J K L M N O P Q R S T U";
        String actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceMethodWithSoundexEncoder() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        String str1 = "Apple";
        String str2 = "Orange";
        int expectedResult = 0;
        int actualResult = SoundexUtils.difference(encoder, str1, str2);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDifferenceMethodWithRefinedSoundexEncoder() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.Metaphone();
        String str1 = "Apple";
        String str2 = "Orange";
        int expectedResult = 0;
        int actualResult = SoundexUtils.difference(encoder, str1, str2);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceMethodWithNullString() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        String str1 = null;
        String str2 = "Test";
        SoundexUtils.difference(encoder, str1, str2);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceMethodWithNullEncoder() throws EncoderException {
        String str1 = "Apple";
        String str2 = "Orange";
        SoundexUtils differenceMethod = new SoundexUtils() {};
        differenceMethod.difference(null, str1, str2);
    }

    @Test(expected = NullPointerException.class)
    public void testCleanNullInput() {
        SoundexUtils.clean((String) null);
    }
}