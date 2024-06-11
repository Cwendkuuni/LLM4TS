import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {
    
    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("APAPAPAPAP", caverphone.caverphone("apple"));
        // Add more test cases for different input strings
    }

    @Test
    public void testEncodeObject() {
        Caverphone caverphone = new Caverphone();
        try {
            assertEquals("APAPAPAPAP", caverphone.encode("apple"));
        } catch (EncoderException e) {
            fail("EncoderException should not be thrown");
        }
        // Add more test cases, including passing a non-String object
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("APAPAPAPAP", caverphone.encode("apple"));
        // Add more test cases for different input strings
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertTrue(caverphone.isCaverphoneEqual("apple", "apple"));
        assertFalse(caverphone.isCaverphoneEqual("apple", "banana"));
        // Add more test cases for comparing caverphones of different strings
    }
}