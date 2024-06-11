import org.junit.Test;
import org.apache.commons.codec.EncoderException;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("test");
        assertEquals("tst1111111", result);
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.encode("test");
        assertEquals("tst1111111", result);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeObjectWithNonString() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(123);
    }

    @Test
    public void testEncodeObjectWithString() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        String result = (String) caverphone.encode("test");
        assertEquals("tst1111111", result);
    }

    @Test
    public void testIsCaverphoneEqualWithEqualStrings() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("test", "test");
        assertTrue(result);
    }

    @Test
    public void testIsCaverphoneEqualWithDifferentStrings() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("test1", "test2");
        assertFalse(result);
    }

    // Additional test cases for edge cases and exceptions
    @Test
    public void testCaverphoneWithEmptyString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("");
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphoneWithNullString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone(null);
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphoneWithOtherCharacters() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("123!@#$");
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphoneWithMultipleStartOptions() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("coughtest");
        assertEquals("2st1111111", result);
    }

    @Test
    public void testCaverphoneWithFinalE() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("test");
        assertEquals("tst1111111", result);
    }
}