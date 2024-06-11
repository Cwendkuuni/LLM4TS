import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    private Caverphone caverphone;

    @Before
    public void setUp() {
        caverphone = new Caverphone();
    }

    @Test
    public void testCaverphone_WithNull_Returns1111111111() {
        String result = caverphone.caverphone(null);
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphone_WithEmptyString_Returns1111111111() {
        String result = caverphone.caverphone("");
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphone_WithValidString_ReturnsCaverphoneCode() {
        String result = caverphone.caverphone("example");
        assertEquals("Ek2mpl1111", result);
    }

    @Test
    public void testEncode_WithNonStringObject_ThrowsEncoderException() {
        Object object = new Object();
        try {
            caverphone.encode(object);
            fail("Expected EncoderException to be thrown");
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testEncode_WithStringObject_ReturnsCaverphoneCode() {
        String object = "example";
        String result = caverphone.encode(object);
        assertEquals("Ek2mpl1111", result);
    }

    @Test
    public void testIsCaverphoneEqual_WithEqualStrings_ReturnsTrue() {
        String str1 = "example";
        String str2 = "exampl";
        boolean result = caverphone.isCaverphoneEqual(str1, str2);
        assertTrue(result);
    }

    @Test
    public void testIsCaverphoneEqual_WithDifferentStrings_ReturnsFalse() {
        String str1 = "example";
        String str2 = "different";
        boolean result = caverphone.isCaverphoneEqual(str1, str2);
        assertFalse(result);
    }

    @Test
    public void testIsCaverphoneEqual_WithNullString_ReturnsFalse() {
        String str1 = "example";
        String str2 = null;
        boolean result = caverphone.isCaverphoneEqual(str1, str2);
        assertFalse(result);
    }

    @Test
    public void testIsCaverphoneEqual_WithEmptyStrings_ReturnsTrue() {
        String str1 = "";
        String str2 = "";
        boolean result = caverphone.isCaverphoneEqual(str1, str2);
        assertTrue(result);
    }

}