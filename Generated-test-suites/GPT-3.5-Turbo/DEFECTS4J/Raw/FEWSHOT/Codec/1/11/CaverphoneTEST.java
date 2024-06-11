import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

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
        assertEquals("31", caverphone.caverphone("a"));
        assertEquals("SS", caverphone.caverphone("s"));
        assertEquals("AKSATA", caverphone.caverphone("acshad"));
        assertEquals("MFAF", caverphone.caverphone("mbphf"));
        assertEquals("WKA", caverphone.caverphone("wear"));
        assertEquals("KA", caverphone.caverphone("we"));
        assertEquals("WAA", caverphone.caverphone("wea"));
        assertEquals("AO", caverphone.caverphone("ro"));
        assertEquals("S3A", caverphone.caverphone("sia"));
        assertEquals("S3", caverphone.caverphone("si"));
        assertEquals("A", caverphone.caverphone("i"));
        assertEquals("21", caverphone.caverphone("g"));
        assertEquals("S", caverphone.caverphone("s"));
        assertEquals("R3", caverphone.caverphone("ra"));
        assertEquals("L3", caverphone.caverphone("le"));
    }

    @Test
    public void testEncode() {
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("31", caverphone.encode("a"));
        assertEquals("SS", caverphone.encode("s"));
        assertEquals("AKSATA", caverphone.encode("acshad"));
        assertEquals("MFAF", caverphone.encode("mbphf"));
        assertEquals("WKA", caverphone.encode("wear"));
        assertEquals("KA", caverphone.encode("we"));
        assertEquals("WAA", caverphone.encode("wea"));
        assertEquals("AO", caverphone.encode("ro"));
        assertEquals("S3A", caverphone.encode("sia"));
        assertEquals("S3", caverphone.encode("si"));
        assertEquals("A", caverphone.encode("i"));
        assertEquals("21", caverphone.encode("g"));
        assertEquals("S", caverphone.encode("s"));
        assertEquals("R3", caverphone.encode("ra"));
        assertEquals("L3", caverphone.encode("le"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual(null, null));
        assertTrue(caverphone.isCaverphoneEqual("", ""));
        assertTrue(caverphone.isCaverphoneEqual("acshad", "akshad"));
        assertTrue(caverphone.isCaverphoneEqual("mbphf", "mfpf"));
        assertFalse(caverphone.isCaverphoneEqual("wear", "we"));
        assertFalse(caverphone.isCaverphoneEqual("ro", "ro"));
        assertFalse(caverphone.isCaverphoneEqual("sia", "si"));
        assertTrue(caverphone.isCaverphoneEqual("i", "i"));
        assertTrue(caverphone.isCaverphoneEqual("g", "g"));
        assertTrue(caverphone.isCaverphoneEqual("s", "s"));
        assertFalse(caverphone.isCaverphoneEqual("ra", "re"));
        assertFalse(caverphone.isCaverphoneEqual("le", "lo"));
    }
}