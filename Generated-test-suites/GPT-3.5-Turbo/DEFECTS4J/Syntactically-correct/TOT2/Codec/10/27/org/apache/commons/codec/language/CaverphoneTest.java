import static org.junit.Assert.*;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneBasic() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("A111111111", caverphone.caverphone("abc123"));
        assertEquals("A11111AAA1", caverphone.caverphone("you and me"));
    }

    @Test
    public void testCaverphoneEdgeCases() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A111111111", caverphone.caverphone("y3W3"));
        assertEquals("A111111111", caverphone.caverphone("YWh3"));
        assertEquals("1111111111", caverphone.caverphone("          "));
    }

    @Test
    public void testEncodeObject() {
        Caverphone caverphone = new Caverphone();
        try {
            caverphone.encode(new Object());
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A11111AAA1", caverphone.encode("you and me"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertTrue(caverphone.isCaverphoneEqual("abc", "abc"));
        assertFalse(caverphone.isCaverphoneEqual("abc", "def"));
    }
}