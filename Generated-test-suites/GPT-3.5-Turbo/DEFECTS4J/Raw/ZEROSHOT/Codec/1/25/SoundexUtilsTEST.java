import org.junit.Test;
import org.apache.commons.codec.EncoderException;

public class SoundexUtilsTest {

    @Test
    public void testClean_NullString() {
        String result = SoundexUtils.clean(null);
        assertNull(result);
    }
    
    @Test
    public void testClean_EmptyString() {
        String result = SoundexUtils.clean("");
        assertEquals("", result);
    }
    
    @Test
    public void testClean_NoLetters() {
        String result = SoundexUtils.clean("123");
        assertEquals("", result);
    }
    
    @Test
    public void testClean_AllLetters() {
        String result = SoundexUtils.clean("aBcDeF");
        assertEquals("ABCDEF", result);
    }
    
    @Test
    public void testClean_PartialLetters() {
        String result = SoundexUtils.clean("A1b2c3D4");
        assertEquals("ABCD", result);
    }
    
    @Test
    public void testDifference_Range0To4() throws EncoderException {
        StringEncoder encoder = new Soundex();
        int result = SoundexUtils.difference(encoder, "Soundex", "Sound");
        assertTrue(result >= 0 && result <= 4);
    }
    
    @Test
    public void testDifference_EncodedNullStrings() {
        int result = SoundexUtils.differenceEncoded(null, null);
        assertEquals(0, result);
    }
    
    @Test
    public void testDifference_EncodedStrings() {
        int result = SoundexUtils.differenceEncoded("S532", "S560");
        assertEquals(2, result);
    }
    
}