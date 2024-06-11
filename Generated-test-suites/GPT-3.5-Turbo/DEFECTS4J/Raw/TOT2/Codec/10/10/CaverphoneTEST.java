import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("text", caverphone.caverphone("text"));
        assertEquals("slr2f", caverphone.caverphone("slough"));
    }

    @Test
    public void testEncodeObject() {
        Caverphone caverphone = new Caverphone();
        try {
            caverphone.encode(123); // Invalid input type
            assertFalse(true);      // Execution should not reach here
        } catch (EncoderException e) {
            assertTrue(true);       // EncoderException should be thrown
        }
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.encode("")); // Empty string input
        assertEquals("text", caverphone.encode("text"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertTrue(caverphone.isCaverphoneEqual("abc", "ABC"));
        assertTrue(caverphone.isCaverphoneEqual("slough", "sluff"));
        assertFalse(caverphone.isCaverphoneEqual("testing", "test"));
    }

}