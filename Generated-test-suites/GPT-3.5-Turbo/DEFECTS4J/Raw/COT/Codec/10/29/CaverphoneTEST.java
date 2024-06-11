
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    private Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphone() {
        // Basic functionality test
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));

        assertEquals("A1111", caverphone.caverphone("a"));
        assertEquals("A111111111", caverphone.caverphone("enough"));
        assertEquals("A22k23r111111m2", caverphone.caverphone("Apache Software"));
        assertEquals("A3r3", caverphone.caverphone("Are"));

        // Test for replacements
        assertEquals("k3n3b23s1122k3n3s", caverphone.caverphone("kenobiskenos"));
        assertEquals("Sk3nSk3", caverphone.caverphone("SkenSken"));
        assertEquals("MN313M", caverphone.caverphone("Mn3m3"));

        // Test for removals
        assertEquals("ks", caverphone.caverphone("xx"));
        assertEquals("A", caverphone.caverphone("xxx"));
        assertEquals("", caverphone.caverphone("xxxxxx"));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeWithNonStringObject() throws EncoderException {
        caverphone.encode(123); // Testing for non-string object
    }

    @Test
    public void testEncodeString() {
        assertEquals("A111111111", caverphone.encode("enough"));
        assertEquals("A22k23r111111m2", caverphone.encode("Apache Software"));
        assertEquals("A3r3", caverphone.encode("Are"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual("hello", "hello"));
        assertFalse(caverphone.isCaverphoneEqual("hello", "world"));
    }

}
