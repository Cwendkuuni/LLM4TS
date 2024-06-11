import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    private Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphone_NormalCase() {
        assertEquals("1111111111", caverphone.caverphone("test"));
    }

    @Test
    public void testCaverphone_EmptyString() {
        assertEquals("1111111111", caverphone.caverphone(""));
    }

    @Test
    public void testCaverphone_WithSpecialCharacters() {
        assertEquals("2222222222", caverphone.caverphone("!@#$%^&*()"));
    }

    @Test
    public void testEncode_ObjectOfWrongType_ExceptionThrown() {
        try {
            caverphone.encode(1234);
            fail("Expected EncoderException to be thrown");
        } catch (EncoderException e) {
            // Expected
        }
    }

    @Test
    public void testEncode_String_ObjectReturned() {
        assertEquals("1111111111", caverphone.encode("test"));
    }

    @Test
    public void testIsCaverphoneEqual_EqualStrings_True() {
        assertTrue(caverphone.isCaverphoneEqual("test", "test"));
    }

    @Test
    public void testIsCaverphoneEqual_DifferentStrings_False() {
        assertFalse(caverphone.isCaverphoneEqual("test", "best"));
    }

    // Add more test cases as needed for edge cases and error scenarios

}