import static org.junit.Assert.*;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("test");
        assertEquals("tst1111111", result);
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.encode("test");
        assertEquals("tst1111111", result);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeWithNonStringObject() {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(123);
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("test", "test");
        assertTrue(result);
    }
}