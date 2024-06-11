import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("2211111111", caverphone.caverphone("test"));
        assertEquals("2211111111", caverphone.caverphone("TEST123"));
    }

    @Test
    public void testEncodeObject() {
        Caverphone caverphone = new Caverphone();
        try {
            assertEquals("2211111111", caverphone.encode("test"));
            caverphone.encode(new Integer(123)); // should throw EncoderException
            fail("Should have thrown an EncoderException");
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("2211111111", caverphone.encode("test"));
        assertEquals("11111111A1", caverphone.encode("edge"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertTrue(caverphone.isCaverphoneEqual("test", "TEST"));
        assertFalse(caverphone.isCaverphoneEqual("test", "edge"));
    }

}