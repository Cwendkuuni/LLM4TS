import static org.junit.Assert.*;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone_basic() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("test");
        assertEquals("T1111111111", result);
    }

    @Test
    public void testEncode_Object() {
        Caverphone caverphone = new Caverphone();
        try {
            Object result = caverphone.encode("test");
            assertEquals("T1111111111", result);
        } catch (EncoderException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testEncode_String() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.encode("test");
        assertEquals("T1111111111", result);
    }

    @Test
    public void testIsCaverphoneEqual_true() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("test", "test");
        assertTrue(result);
    }

    @Test
    public void testIsCaverphoneEqual_false() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("test", "hello");
        assertFalse(result);
    }

    @Test
    public void testCaverphone_edgeCases() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("");
        assertEquals("1111111111", result);

        result = caverphone.caverphone(null);
        assertEquals("1111111111", result);

        result = caverphone.caverphone("123!@#");
        assertEquals("", result);
    }

    @Test
    public void testEncode_Object_exception() {
        Caverphone caverphone = new Caverphone();
        try {
            Object result = caverphone.encode(123);
            fail("Exception should have been thrown");
        } catch (EncoderException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testEncode_String_exception() {
        Caverphone caverphone = new Caverphone();
        try {
            String result = caverphone.encode(123);
            fail("Exception should have been thrown");
        } catch (EncoderException e) {
            assertTrue(true);
        }
    }
}