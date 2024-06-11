import org.junit.Test;
import org.apache.commons.codec.EncoderException;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        // Test if the method can encode a string into a Caverphone value
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("test");
        String expected = "1111111111";
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testCaverphone_NullInput() {
        // Test if the method handles null input correctly
        Caverphone caverphone = new Caverphone();
        caverphone.caverphone(null);
    }

    @Test
    public void testEncode_Object() throws EncoderException {
        // Test if the method encodes an object using the Caverphone algorithm
        Caverphone caverphone = new Caverphone();
        Object result = caverphone.encode("test");
        String expected = "1111111111";
        assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testEncode_Object_InvalidType() throws EncoderException {
        // Test if the method throws an EncoderException when the parameter is not of type java.lang.String
        Caverphone caverphone = new Caverphone();
        Object obj = new Object();
        caverphone.encode(obj);
    }

    @Test
    public void testEncode_String() {
        // Test if the method encodes a string using the Caverphone algorithm
        Caverphone caverphone  = new Caverphone();
        String result = caverphone.encode("test");
        String expected = "1111111111";
        assertEquals(expected, result);
    }

    @Test
    public void testIsCaverphoneEqual_True() {
        // Test if the method correctly identifies that the caverphones of two strings are identical
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("hello", "hello");
        assertTrue(result);
    }

    @Test
    public void testIsCaverphoneEqual_False() {
        // Test if the method correctly identifies that the caverphones of two strings are not identical
        Caverphone caverphone = new Caverphone ();
        boolean result = caverphone.isCaverphoneEqual("hello", "world");
        assertFalse(result);
    }

}