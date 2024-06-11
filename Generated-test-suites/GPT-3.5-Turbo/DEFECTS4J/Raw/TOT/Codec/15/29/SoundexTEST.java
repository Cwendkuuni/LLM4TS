import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.codec.EncoderException;

public class SoundexTest {

    private Soundex soundex;

    @Before
    public void setUp() {
        soundex = new Soundex();
    }

    // Test cases for encode(Object obj) method
    
    @Test
    public void testEncode_Object_Successful() throws EncoderException {
        Object obj = "Hello";
        String expected = "H400";
        assertEquals(expected, soundex.encode(obj));
    }

    @Test(expected = EncoderException.class)
    public void testEncode_Object_Error() throws EncoderException {
        Object obj = 123;
        soundex.encode(obj);
    }

    // Test cases for encode(String str) method
    
    @Test
    public void testEncode_String_Successful() {
        String str = "Hello";
        String expected = "H400";
        assertEquals(expected, soundex.encode(str));
    }

    @Test
    public void testEncode_String_EmptyString() {
        String str = "";
        String expected = "";
        assertEquals(expected, soundex.encode(str));
    }

    // Test cases for difference(String s1, String s2) method
    
    @Test
    public void testDifference_Successful() throws EncoderException {
        String s1 = "Hello";
        String s2 = "He";
        int expected = 2;
        assertEquals(expected, soundex.difference(s1, s2));
    }

    @Test(expected = EncoderException.class)
    public void testDifference_Error() throws EncoderException {
        String s1 = "Hello";
        Integer s2 = 123;
        soundex.difference(s1, s2);
    }

    // Additional test cases
    
    @Test
    public void testSoundex_Successful() {
        String str = "Hello";
        String expected = "H400";
        assertEquals(expected, soundex.soundex(str));
    }

    @Test
    public void testSoundex_EmptyString() {
        String str = "";
        String expected = "";
        assertEquals(expected, soundex.soundex(str));
    }

    @Test
    public void testSoundex_EdgeCase() {
        String str = "Zebra";
        String expected = "Z160";
        assertEquals(expected, soundex.soundex(str));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSoundex_InvalidCharacter() {
        String str = "123";
        soundex.soundex(str);
    }

}