import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CaverphoneTest {

    // Test caverphone(String txt) method
    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();

        // Basic functionality test
        String expected = "1111111111";
        String actual = caverphone.caverphone(null);
        assertEquals(expected, actual);

        // Test lowercase conversion
        expected = "3111311111";
        actual = caverphone.caverphone("hello");
        assertEquals(expected, actual);

        // Test removal of non-alphabetic characters
        expected = "3111311111";
        actual = caverphone.caverphone("hello123");
        assertEquals(expected, actual);

        // Test removal of final "e"
        expected = "311131111";
        actual = caverphone.caverphone("goodbye");
        assertEquals(expected, actual);

        // Test handling of start options
        expected = "2121111111";
        actual = caverphone.caverphone("coughing");
        assertEquals(expected, actual);

        // Test handling of replacements
        expected = "3k3113f";
        actual = caverphone.caverphone("quick");
        assertEquals(expected, actual);

        // Test handling of multiple replacements
        expected = "3113kk221";
        actual = caverphone.caverphone("gazette");
        assertEquals(expected, actual);

        // Test handling of repeated characters
        expected = "F";
        actual = caverphone.caverphone("ffffff");
        assertEquals(expected, actual);
    }

    // Test encode(Object pObject) method
    @Test
    public void testEncodeObject() {
        Caverphone caverphone = new Caverphone();

        // Test encoding of string object
        String expected = "3111311111";
        Object obj = "hello";
        try {
            Object actual = caverphone.encode(obj);
            assertEquals(expected, actual);
        } catch (EncoderException e) {
            e.printStackTrace();
        }

        // Test encoding of non-string object
        Object nonString = new Object();
        try {
            caverphone.encode(nonString);
        } catch (EncoderException e) {
            assertEquals("Parameter supplied to Caverphone encode is not of type java.lang.String", e.getMessage());
        }
    }

    // Test encode(String pString) method
    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();

        // Test encoding of string
        String expected = "3111311111";
        String actual = caverphone.encode("hello");
        assertEquals(expected, actual);
    }

    // Test isCaverphoneEqual(String str1, String str2) method
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        // Test with equal caverphones
        boolean expected = true;
        boolean actual = caverphone.isCaverphoneEqual("hello", "hall");
        assertEquals(expected, actual);

        // Test with different caverphones
        expected = false;
        actual = caverphone.isCaverphoneEqual("hello", "goodbye");
        assertEquals(expected, actual);
    }
}