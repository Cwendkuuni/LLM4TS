import org.junit.Before;
import org.junit.Test;
import org.apache.commons.codec.EncoderException;

import static org.junit.Assert.*;

public class CaverphoneTest {

    private Caverphone caverphone;

    @Before
    public void setup() {
        caverphone = new Caverphone();
    }

    @Test
    public void testCaverphone() {
        assertEquals("1111111111", caverphone.caverphone("test"));
    }

    @Test
    public void testEncode() throws EncoderException {
        assertEquals("1111111111", (String) caverphone.encode("test"));
    }

    @Test
    public void testEncodeString() {
        assertEquals("1111111111", caverphone.encode("test"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual("hello", "hello"));
    }

    @Test
    public void testCaverphoneEmptyInput() {
        assertEquals("1111111111", caverphone.caverphone(""));
    }

    @Test
    public void testCaverphoneNullInput() {
        assertEquals("1111111111", caverphone.caverphone(null));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeNonStringObject() throws EncoderException {
        caverphone.encode(123);
    }

    @Test
    public void testEncodeNullInput() {
        assertNull(caverphone.encode(null));
    }

    @Test
    public void testEncodeEmptyInput() {
        assertEquals("", caverphone.encode(""));
    }

    @Test
    public void testIsCaverphoneEqualEmptyStrings() {
        assertTrue(caverphone.isCaverphoneEqual("", ""));
    }

    @Test
    public void testIsCaverphoneEqualSameResult() {
        assertTrue(caverphone.isCaverphoneEqual("hello", "hello"));
    }

    @Test
    public void testIsCaverphoneEqualDifferentResult() {
        assertFalse(caverphone.isCaverphoneEqual("abc", "def"));
    }
}