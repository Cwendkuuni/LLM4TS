import org.apache.commons.codec.encoder.Base64;
import org.apache.commons.codec.encoder.UTF8;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "Hello World!";
        String expected = "HELLO";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    @Test
    public void testClean_NullInput() {
        String input = null;
        String expected = "";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    @Test
    public void testClean_EmptyInput() {
        String input = "";
        String expected = "";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    @Test
    public void testClean_SpecialCharacters() {
        String input = "Hello@World!";
        String expected = "HELLO";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    @Test
    public void testDifference() throws Exception {
        StringEncoder encoder = new UTF8();
        String str1 = "Apple";
        String str2 = "Orange";
        int expected = 0;
        int result = SoundexUtils.difference(encoder, str1, str2);
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testDifference_NullEncoder() throws Exception {
        StringEncoder encoder = null;
        String str1 = "Apple";
        String str2 = "Orange";
        SoundexUtils.difference(encoder, str1, str2);
    }

    @Test
    public void testDifference_DifferentEncoders() throws Exception {
        StringEncoder utf8 = new UTF8();
        StringEncoder base64 = new Base64();
        String str1 = "Apple";
        String str2 = "Orange";
        int expected = 0;
        int result = SoundexUtils.difference(utf8, str1, str2);
        int result2 = SoundexUtils.difference(base64, str1, str2);
        assertEquals(expected, result);
        assertEquals(expected, result2);
    }

    @Test(expected = EncoderException.class)
    public void testDifference_EncodingErrors() throws Exception {
        StringEncoder encoder = new UTF8();
        String str1 = "Apple";
        String str2 = "Orange";
        encoder.encode(str2); // Intentionally throw an exception here
        int expected = 0;
        int result = SoundexUtils.difference(encoder, str1, str2);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceEncoded() {
        String expected = "A123";
        String es1 = "Apple";
        String es2 = "Apple";
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected.length(), result);
    }

    @Test
    public void testDifferenceEncoded_NullInput() {
        String es1 = null;
        String es2 = "A123";
        int expected = 0;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceEncoded_EmptyInput() {
        String es1 = "";
        String es2 = "A123";
        int expected = 0;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceEncoded_NullEncoder() {
        String es1 = "A123";
        String es2 = "B456";
        int expected = 0;
        SoundexUtils soundexUtils = new SoundexUtils(); // Create an instance of the class
        int result = soundexUtils.differenceEncoded(null, es1, es2);
        assertEquals(expected, result);
    }
}