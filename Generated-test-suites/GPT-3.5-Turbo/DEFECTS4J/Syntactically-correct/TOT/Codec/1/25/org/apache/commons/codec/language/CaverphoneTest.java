import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use cases
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("rml", caverphone.caverphone("cancer"));
        assertEquals("sn3a", caverphone.caverphone("snae"));
        assertEquals("pblc", caverphone.caverphone("public"));
        assertEquals("gldmn", caverphone.caverphone("goldman"));
        assertEquals("c2pr", caverphone.caverphone("cipher"));
        assertEquals("klymnt", caverphone.caverphone("climent"));
        assertEquals("mxcl", caverphone.caverphone("michael"));

        // Edge cases
        assertEquals("3", caverphone.caverphone("a"));
        assertEquals("3", caverphone.caverphone("e"));
        assertEquals("3", caverphone.caverphone("i"));
        assertEquals("3", caverphone.caverphone("o"));
        assertEquals("3", caverphone.caverphone("u"));
        assertEquals("B", caverphone.caverphone("ae"));
        assertEquals("B", caverphone.caverphone("startwithconsonant"));
        assertEquals("3kh3", caverphone.caverphone("ought")); 

        // Error scenarios
        assertEquals("1111111111", caverphone.caverphone("!@#$%^&*()"));
        assertEquals("1111111111", caverphone.caverphone("         "));
        assertEquals("1111111111", caverphone.caverphone("1234567890"));
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();

        // Typical use cases
        assertEquals("rml", caverphone.encode(null));
        assertEquals("rml", caverphone.encode(""));
        assertEquals("rml", caverphone.encode("cancer"));
        assertEquals("sn3a", caverphone.encode("snae"));
        assertEquals("pblc", caverphone.encode("public"));
        assertEquals("gldmn", caverphone.encode("goldman"));
        assertEquals("c2pr", caverphone.encode("cipher"));
        assertEquals("klymnt", caverphone.encode("climent"));
        assertEquals("mxcl", caverphone.encode("michael"));

        // Edge cases
        assertEquals("3", caverphone.encode("a"));
        assertEquals("3", caverphone.encode("e"));
        assertEquals("3", caverphone.encode("i"));
        assertEquals("3", caverphone.encode("o"));
        assertEquals("3", caverphone.encode("u"));
        assertEquals("B", caverphone.encode("ae"));
        assertEquals("B", caverphone.encode("startwithconsonant"));
        assertEquals("3kh3", caverphone.encode("ought")); 

        // Error scenarios
        try {
            caverphone.encode(new Integer(5));
        } catch (EncoderException e) {
            assertEquals("Parameter supplied to Caverphone encode is not of type java.lang.String", e.getMessage());
        }
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        // Typical use cases
        assertEquals(true, caverphone.isCaverphoneEqual(null, null));
        assertEquals(true, caverphone.isCaverphoneEqual("", ""));
        assertEquals(true, caverphone.isCaverphoneEqual("cancer", "rml"));
        assertEquals(true, caverphone.isCaverphoneEqual("snae", "sn3a"));
        assertEquals(true, caverphone.isCaverphoneEqual("public", "pblc"));
        assertEquals(true, caverphone.isCaverphoneEqual("goldman", "gldmn"));
        assertEquals(true, caverphone.isCaverphoneEqual("cipher", "c2pr"));
        assertEquals(true, caverphone.isCaverphoneEqual("climent", "klymnt"));
        assertEquals(true, caverphone.isCaverphoneEqual("michael", "mxcl"));

        // Edge cases
        assertEquals(true, caverphone.isCaverphoneEqual("a", "3"));
        assertEquals(true, caverphone.isCaverphoneEqual("e", "3"));
        assertEquals(true, caverphone.isCaverphoneEqual("i", "3"));
        assertEquals(true, caverphone.isCaverphoneEqual("o", "3"));
        assertEquals(true, caverphone.isCaverphoneEqual("u", "3"));
        assertEquals(true, caverphone.isCaverphoneEqual("ae", "B"));
        assertEquals(true, caverphone.isCaverphoneEqual("startwithconsonant", "B"));
        assertEquals(true, caverphone.isCaverphoneEqual("ought", "3kh3")); 

        // Error scenarios
        assertEquals(false, caverphone.isCaverphoneEqual("", null));
        assertEquals(false, caverphone.isCaverphoneEqual(null, ""));
        assertEquals(false, caverphone.isCaverphoneEqual("cancer", ""));
        assertEquals(false, caverphone.isCaverphoneEqual("snae", null));

    }
    
}