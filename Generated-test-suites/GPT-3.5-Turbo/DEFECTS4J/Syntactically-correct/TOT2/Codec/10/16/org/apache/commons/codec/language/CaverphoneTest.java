import org.junit.Test;

import static org.junit.Assert.*;

public class CaverphoneTest {

    private Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphone_basicFunctionality() {
        String result = caverphone.caverphone("hello");
        assertEquals("Expected result not achieved", "H1111111111", result);
    }

    @Test
    public void testEncode_Object_StringObject() {
        Object result = caverphone.encode((Object) "hello");
        assertEquals("Expected result not achieved", "H1111111111", result);
    }

    @Test
    public void testEncode_String_basicFunctionality() {
        String result = caverphone.encode("world");
        assertEquals("Expected result not achieved", "WLD1111111", result);
    }

    @Test
    public void testIsCaverphoneEqual_identicalStrings() {
        boolean result = caverphone.isCaverphoneEqual("hello", "hello");
        assertTrue("Expected caverphones to be equal", result);
    }

    @Test
    public void testIsCaverphoneEqual_differentStrings() {
        boolean result = caverphone.isCaverphoneEqual("hello", "world");
        assertFalse("Expected caverphones to be different", result);
    }

    // Add test cases for edge cases and error scenarios here

}