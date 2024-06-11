import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class CaverphoneTest {

    private Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphoneBasicFunctionality() {
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("A111111111", caverphone.caverphone("test"));
        assertEquals("AA11111111", caverphone.caverphone("testtest"));
    }

    @Test
    public void testCaverphoneEdgeCases() {
        assertEquals("A111111111", caverphone.caverphone("Test"));
        assertEquals("11A1111111", caverphone.caverphone("tESt"));
        assertEquals("A111111111", caverphone.caverphone("gasywhywh3r"));
    }

    @Test
    public void testCaverphoneSpecialCases() {
        assertEquals("11A1111111", caverphone.caverphone("tESty3"));
        assertEquals("A111111111", caverphone.caverphone("gassy"));
    }

    @Test
    public void testEncode() {
        assertEquals("A111111111", caverphone.encode("Test"));
        assertEquals("11A1111111", caverphone.encode("tESt"));
    }

    @Test
    public void testEncodeObjectException() {
        try {
            caverphone.encode(new Object());
        } catch (EncoderException e) {
            assertEquals("Parameter supplied to Caverphone encode is not of type java.lang.String", e.getMessage());
        }
    }

    @Test
    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual("Test", "test"));
        assertFalse(caverphone.isCaverphoneEqual("Test", "SomethingElse"));
    }
}