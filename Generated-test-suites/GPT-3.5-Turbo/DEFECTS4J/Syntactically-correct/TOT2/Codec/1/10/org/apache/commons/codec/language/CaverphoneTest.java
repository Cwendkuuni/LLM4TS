import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {
    
    @Test
    public void testCaverphone_basic() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
    }

    @Test
    public void testCaverphone_typicalCases() {
        Caverphone caverphone = new Caverphone();
        assertEquals("KM11111111", caverphone.caverphone("Kumar"));
        assertEquals("A111111111", caverphone.caverphone("Apple"));
    }

    @Test
    public void testCaverphone_edgeCases() {
        Caverphone caverphone = new Caverphone();
        assertEquals("XXXX111111", caverphone.caverphone("x??#$*"));
        assertEquals("MUN1111111", caverphone.caverphone(" mUn  "));
    }

    @Test
    public void testCaverphone_exceptions() {
        Caverphone caverphone = new Caverphone();
        try {
            caverphone.caverphone("123"); // Invalid input
            fail("Should have thrown an exception");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testEncode_Object() {
        Caverphone caverphone = new Caverphone();
        try {
            caverphone.encode(123); // Invalid input
            fail("Should have thrown an exception");
        } catch (Exception e) {
            assertTrue(e instanceof EncoderException);
        }
    }
    
    @Test
    public void testEncode_String() {
        Caverphone caverphone = new Caverphone();
        assertEquals("KM11111111", caverphone.encode("Kumar"));
        assertEquals("A111111111", caverphone.encode("Apple"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertTrue(caverphone.isCaverphoneEqual("apple", "aappl"));
        assertFalse(caverphone.isCaverphoneEqual("test", "cased"));
    }
}