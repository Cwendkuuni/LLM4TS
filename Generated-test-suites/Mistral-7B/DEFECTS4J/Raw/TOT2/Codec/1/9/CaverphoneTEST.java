import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    Caverphone caverphoneInstance = new Caverphone();

    @Test
    public void testCaverphone() {
        String input = "HelloWorld";
        String expected = "H2h2e2l2o2L2w2o2r2l2d2";
        String result = caverphoneInstance.caverphone(input);
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneEmptyString() {
        String input = "";
        String expected = "1111111111";
        String result = caverphoneInstance.caverphone(input);
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneNullString() {
        String input = null;
        String expected = "1111111111";
        String result = caverphoneInstance.caverphone(input);
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneSingleLetter() {
        String input = "a";
        String expected = "A";
        String result = caverphoneInstance.caverphone(input);
        assertEquals(expected, result);
    }

    @Test
    public void testEncodeEmptyString() {
        String input = "";
        String expected = "1111111111";
        String result = caverphoneInstance.encode(input);
        assertEquals(expected, result);
    }

    @Test
    public void testEncodeNullString() {
        String input = null;
        try {
            caverphoneInstance.encode(input);
            fail("Expected an EncoderException");
        } catch (EncoderException e) {
            // Expected
        }
    }

    @Test
    public void testIsCaverphoneEqualTrue() {
        String str1 = "HelloWorld";
        String str2 = "HelloWorld";
        boolean expected = true;
        boolean result = caverphoneInstance.isCaverphoneEqual(str1, str2);
        assertEquals(expected, result);
    }

    @Test
    public void testIsCaverphoneEqualFalse() {
        String str1 = "HelloWorld";
        String str2 = "HelloWorld1";
        boolean expected = false;
        boolean result = caverphoneInstance.isCaverphoneEqual(str1, str2);
        assertEquals(expected, result);
    }

    // Edge cases and exception handling to be added in the next steps

}