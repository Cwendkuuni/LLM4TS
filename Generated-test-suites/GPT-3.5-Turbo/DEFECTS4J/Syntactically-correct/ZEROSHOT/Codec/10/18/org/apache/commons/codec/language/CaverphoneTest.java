import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("test");
        assertEquals("1111111111", result);
    }

    @Test
    public void testEncodeObject() {
        Caverphone caverphone = new Caverphone();
        try {
            String result = (String) caverphone.encode("test");
            assertEquals("1111111111", result);
        } catch (EncoderException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.encode("test");
        assertEquals("1111111111", result);
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("test1", "test2");
        assertFalse(result);
    }
}