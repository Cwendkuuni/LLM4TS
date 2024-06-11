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
    public void testCaverphone() {
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("1111111111", caverphone.caverphone("1234!@#$"));
        assertEquals("20f", caverphone.caverphone("Test Phrase"));
    }

    @Test
    public void testEncode_Object() {
        try {
            Object obj = new Object();
            caverphone.encode(obj);
            fail("Expected EncoderException to be thrown");
        } catch (EncoderException ee) {
            // Expected
        }
        String encoded = caverphone.encode("Test");
        assertEquals("20s1111111", encoded);
    }

    @Test
    public void testEncode_String() {
        String encoded = caverphone.encode("Test");
        assertEquals("20s1111111", encoded);
    }

    @Test
    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual("Test", "Tast"));
        assertFalse(caverphone.isCaverphoneEqual("Test", "Phrases"));
    }

}