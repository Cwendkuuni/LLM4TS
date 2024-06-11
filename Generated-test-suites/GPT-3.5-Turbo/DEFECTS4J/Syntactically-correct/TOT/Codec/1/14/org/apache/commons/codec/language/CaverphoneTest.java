import org.junit.Before;
import org.junit.Test;
import org.apache.commons.codec.language.Caverphone;

import static org.junit.Assert.*;

public class CaverphoneTest {

    private Caverphone caverphone;

    @Before
    public void setUp() {
        caverphone = new Caverphone();
    }

    // Test cases for caverphone() method
    @Test
    public void testCaverphone_typicalCase() {
        String result = caverphone.caverphone("example");
        assertEquals("7865171111", result);
    }

    @Test
    public void testCaverphone_emptyString() {
        String result = caverphone.caverphone("");
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphone_nullString() {
        String result = caverphone.caverphone(null);
        assertEquals("1111111111", result);
    }

    // Test cases for encode(Object) method
    @Test
    public void testEncode_typicalCase() throws Exception {
        Object result = caverphone.encode("example");
        assertEquals("7865171111", result);
    }

    @Test(expected = EncoderException.class)
    public void testEncode_invalidObject() throws Exception {
        Object result = caverphone.encode(123);
    }

    // Test cases for encode(String) method
    @Test
    public void testEncodeString_typicalCase() {
        String result = caverphone.encode("example");
        assertEquals("7865171111", result);
    }

    @Test
    public void testEncodeString_emptyString() {
        String result = caverphone.encode("");
        assertEquals("1111111111", result);
    }

    @Test
    public void testEncodeString_nullString() {
        String result = caverphone.encode(null);
        assertEquals("1111111111", result);
    }

    // Test cases for isCaverphoneEqual(String, String) method
    @Test
    public void testIsCaverphoneEqual_equalStrings() {
        boolean result = caverphone.isCaverphoneEqual("example", "example");
        assertTrue(result);
    }

    @Test
    public void testIsCaverphoneEqual_notEqualStrings() {
        boolean result = caverphone.isCaverphoneEqual("example", "different");
        assertFalse(result);
    }

    @Test
    public void testIsCaverphoneEqual_emptyStrings() {
        boolean result = caverphone.isCaverphoneEqual("", "");
        assertTrue(result);
    }

    @Test
    public void testIsCaverphoneEqual_nullStrings() {
        boolean result = caverphone.isCaverphoneEqual(null, null);
        assertTrue(result);
    }

    @Test
    public void testIsCaverphoneEqual_nullAndEmptyStrings() {
        boolean result = caverphone.isCaverphoneEqual(null, "");
        assertFalse(result);
    }

}