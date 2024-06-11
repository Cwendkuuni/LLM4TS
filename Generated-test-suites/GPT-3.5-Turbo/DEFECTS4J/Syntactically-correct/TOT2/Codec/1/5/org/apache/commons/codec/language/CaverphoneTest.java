import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(null)); // Testing null input
        assertEquals("mp2ryt", caverphone.caverphone("mystery")); // Typical use case
        assertEquals("mp2ryt", caverphone.caverphone("MYSTERY")); // Case insensitivity
    }

    @Test
    public void testEncodeObject() {
        Caverphone caverphone = new Caverphone();
        try {
            String encoded = (String) caverphone.encode("testing");
            assertEquals("t3st3nk1111111111", encoded); // Typical use case
        } catch (EncoderException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("t3st3nk1111111111", caverphone.encode("testing")); // Typical use case
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertTrue(caverphone.isCaverphoneEqual("mystery", "MYSTERY")); // Testing case insensitivity
        assertFalse(caverphone.isCaverphoneEqual("hello", "world")); // Testing when caverphones are not equal
    }
}