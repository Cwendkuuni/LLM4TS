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
        assertEquals("nm11111111", caverphone.caverphone("Nm"));
        assertEquals("cv11111111", caverphone.caverphone("Cv"));
        assertEquals("rg11111111", caverphone.caverphone("Rg"));
        assertEquals("1111111111", caverphone.caverphone("example"));
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("nm11111111", caverphone.encode("Nm"));
        assertEquals("cv11111111", caverphone.encode("Cv"));
        assertEquals("rg11111111", caverphone.encode("Rg"));
        assertEquals("1111111111", caverphone.encode("example"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertTrue(caverphone.isCaverphoneEqual(null, null));
        assertTrue(caverphone.isCaverphoneEqual("", ""));
        assertTrue(caverphone.isCaverphoneEqual("Nm", "NM"));
        assertTrue(caverphone.isCaverphoneEqual("Cv", "CV"));
        assertTrue(caverphone.isCaverphoneEqual("Rg", "RG"));
        assertFalse(caverphone.isCaverphoneEqual("example", "Example"));
    }
}