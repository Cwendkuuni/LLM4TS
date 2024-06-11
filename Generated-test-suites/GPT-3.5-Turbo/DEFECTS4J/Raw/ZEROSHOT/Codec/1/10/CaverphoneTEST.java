import org.junit.Test;

import static org.junit.Assert.*;

public class CaverphoneTest {

    private Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphone() {
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("1111111111", caverphone.caverphone("   "));

        assertEquals("C111111111", caverphone.caverphone("Cough"));
        assertEquals("R111111111", caverphone.caverphone("Rough"));
        assertEquals("T111111111", caverphone.caverphone("Tough"));
        assertEquals("GN111111111", caverphone.caverphone("Gn"));
        assertEquals("M111111111", caverphone.caverphone("Mb"));

        assertEquals("S3P2XXF", caverphone.caverphone("SpecialChar@#$"));

        assertEquals("K222222S3", caverphone.caverphone("cqqciqceqcytchcc"));
        assertEquals("2Q3SI3TW3FOBEDN", caverphone.caverphone("cqwcietwtchqhfobedem"));
        assertEquals("K322F", caverphone.caverphone("cxvx"));
        assertEquals("F33222I2T", caverphone.caverphone("vfadgatt"));
        assertEquals("W332221553", caverphone.caverphone("wtfiexgl"));

        assertEquals("1211111111", caverphone.caverphone("e"));
        assertEquals("1211111111", caverphone.caverphone("E"));
        assertEquals("111111l111", caverphone.caverphone("dler"));
        assertEquals("KS2FS", caverphone.caverphone("cshss"));
        assertEquals("FSFS", caverphone.caverphone("ssss"));
        assertEquals("S2T2S", caverphone.caverphone("ssttss"));
    }

    @Test
    public void testEncode() {
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("1111111111", caverphone.encode("   "));

        assertEquals("C111111111", caverphone.encode("Cough"));
        assertEquals("R111111111", caverphone.encode("Rough"));
        assertEquals("T111111111", caverphone.encode("Tough"));
        assertEquals("GN111111111", caverphone.encode("Gn"));
        assertEquals("M111111111", caverphone.encode("Mb"));

        assertEquals("S3P2XXF", caverphone.encode("SpecialChar@#$"));

        assertEquals("K222222S3", caverphone.encode("cqqciqceqcytchcc"));
        assertEquals("2Q3SI3TW3FOBEDN", caverphone.encode("cqwcietwtchqhfobedem"));
        assertEquals("K322F", caverphone.encode("cxvx"));
        assertEquals("F33222I2T", caverphone.encode("vfadgatt"));
        assertEquals("W332221553", caverphone.encode("wtfiexgl"));

        assertEquals("1211111111", caverphone.encode("e"));
        assertEquals("1211111111", caverphone.encode("E"));
        assertEquals("111111l111", caverphone.encode("dler"));
        assertEquals("KS2FS", caverphone.encode("cshss"));
        assertEquals("FSFS", caverphone.encode("ssss"));
        assertEquals("S2T2S", caverphone.encode("ssttss"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual("Cough", "Cough"));
        assertFalse(caverphone.isCaverphoneEqual("Rough", "Tough"));
        assertTrue(caverphone.isCaverphoneEqual("Gn", "Gn"));
        assertFalse(caverphone.isCaverphoneEqual("Mb", "Mm"));
    }
}