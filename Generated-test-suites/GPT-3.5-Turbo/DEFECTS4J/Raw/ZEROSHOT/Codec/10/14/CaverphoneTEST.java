import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

        assertEquals("kmckausk", caverphone.caverphone("Cough"));
        assertEquals("rm2a", caverphone.caverphone("ROUGH"));
        assertEquals("t2f", caverphone.caverphone("tough"));
        assertEquals("2n", caverphone.caverphone("gn"));
        assertEquals("s2m", caverphone.caverphone("sham"));
        assertEquals("1111111111", caverphone.caverphone("mb"));

        assertEquals("s2c", caverphone.caverphone("cq"));
        assertEquals("si", caverphone.caverphone("ci"));
        assertEquals("se", caverphone.caverphone("ce"));
        assertEquals("sy", caverphone.caverphone("cy"));
        assertEquals("2ch", caverphone.caverphone("tch"));
        assertEquals("k", caverphone.caverphone("c"));
        assertEquals("k", caverphone.caverphone("q"));
        assertEquals("k", caverphone.caverphone("x"));
        assertEquals("f", caverphone.caverphone("v"));
        assertEquals("2g", caverphone.caverphone("dg"));
        assertEquals("s2o", caverphone.caverphone("tio"));
        assertEquals("s2a", caverphone.caverphone("tia"));
        assertEquals("t", caverphone.caverphone("d"));
        assertEquals("fh", caverphone.caverphone("ph"));
        assertEquals("p", caverphone.caverphone("b"));
        assertEquals("s2", caverphone.caverphone("sh"));
        assertEquals("s", caverphone.caverphone("z"));
        assertEquals("A", caverphone.caverphone("a"));
        assertEquals("3", caverphone.caverphone("e"));
        assertEquals("y", caverphone.caverphone("j"));
        assertEquals("A3", caverphone.caverphone("y3"));
        assertEquals("A", caverphone.caverphone("y"));
        assertEquals("3", caverphone.caverphone("y"));
        assertEquals("3kh3", caverphone.caverphone("3gh3"));
        assertEquals("22", caverphone.caverphone("gh"));
        assertEquals("k", caverphone.caverphone("g"));
        assertEquals("S", caverphone.caverphone("s"));
        assertEquals("T", caverphone.caverphone("t"));
        assertEquals("P", caverphone.caverphone("p"));
        assertEquals("K", caverphone.caverphone("k"));
        assertEquals("F", caverphone.caverphone("f"));
        assertEquals("M", caverphone.caverphone("m"));
        assertEquals("N", caverphone.caverphone("n"));
        assertEquals("W3", caverphone.caverphone("w3"));
        assertEquals("Wh3", caverphone.caverphone("wh3"));
        assertEquals("3", caverphone.caverphone("w"));
        assertEquals("A", caverphone.caverphone("h"));
        assertEquals("2", caverphone.caverphone("r"));
        assertEquals("3", caverphone.caverphone("r"));
        assertEquals("2", caverphone.caverphone("l"));
        assertEquals("3", caverphone.caverphone("l"));

        assertEquals("1111111111", caverphone.caverphone("RandomString"));
    }

    @Test
    public void testEncode() {
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("kmckausk", caverphone.encode("Cough"));
        assertEquals("rm2a", caverphone.encode("ROUGH"));
        assertEquals("t2f", caverphone.encode("tough"));
        assertEquals("2n", caverphone.encode("gn"));
        assertEquals("s2m", caverphone.encode("sham"));
        assertEquals("1111111111", caverphone.encode("mb"));
        assertEquals("1111111111", caverphone.encode("RandomString"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual(null, null));
        assertFalse(caverphone.isCaverphoneEqual(null, ""));
        assertFalse(caverphone.isCaverphoneEqual("", null));
        assertTrue(caverphone.isCaverphoneEqual("", ""));
        assertTrue(caverphone.isCaverphoneEqual("Test", "Test"));
        assertFalse(caverphone.isCaverphoneEqual("Test", "Test1"));
    }
}