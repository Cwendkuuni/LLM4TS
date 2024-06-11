import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    private Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphone() {
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("cf", caverphone.caverphone("cough"));
        assertEquals("rf", caverphone.caverphone("rough"));
        assertEquals("tf", caverphone.caverphone("tough"));
        assertEquals("n3", caverphone.caverphone("enough"));
        assertEquals("tr2f", caverphone.caverphone("trough"));
        assertEquals("2n", caverphone.caverphone("gn"));
        assertEquals("b", caverphone.caverphone("mb"));
        assertEquals("2q", caverphone.caverphone("cq"));
        assertEquals("si", caverphone.caverphone("ci"));
        assertEquals("se", caverphone.caverphone("ce"));
        assertEquals("sy", caverphone.caverphone("cy"));
        assertEquals("2ch", caverphone.caverphone("tch"));
        assertEquals("k", caverphone.caverphone("c"));
        assertEquals("k", caverphone.caverphone("q"));
        assertEquals("k", caverphone.caverphone("x"));
        assertEquals("f", caverphone.caverphone("v"));
        assertEquals("2g", caverphone.caverphone("dg"));
        assertEquals("sio", caverphone.caverphone("tio"));
        assertEquals("sia", caverphone.caverphone("tia"));
        assertEquals("t", caverphone.caverphone("d"));
        assertEquals("fh", caverphone.caverphone("ph"));
        assertEquals("p", caverphone.caverphone("b"));
        assertEquals("s2", caverphone.caverphone("sh"));
        assertEquals("s", caverphone.caverphone("z"));
        assertEquals("A", caverphone.caverphone("a"));
        assertEquals("3", caverphone.caverphone("e"));
        assertEquals("3", caverphone.caverphone("i"));
        assertEquals("3", caverphone.caverphone("o"));
        assertEquals("3", caverphone.caverphone("u"));
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
        assertEquals("3", caverphone.caverphone("l"));
        assertEquals("2", caverphone.caverphone("j"));
        assertEquals("3", caverphone.caverphone("y3"));
        assertEquals("A", caverphone.caverphone("y"));
        assertEquals("2", caverphone.caverphone("l3"));
        assertEquals("A", caverphone.caverphone("y3"));
        assertEquals("A1111111111", caverphone.caverphone("why"));
        assertEquals("232", caverphone.caverphone("w2"));
        assertEquals("122", caverphone.caverphone("g2"));
        assertEquals("4414111111", caverphone.caverphone("david"));
    }

    @Test
    public void testEncode() {
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("cf", caverphone.encode("cough"));
        assertEquals("A1111111111", caverphone.encode("why"));
        assertEquals("232", caverphone.encode("w2"));
        assertEquals("122", caverphone.encode("g2"));
        assertEquals("4414111111", caverphone.encode("david"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual(null, null));
        assertTrue(caverphone.isCaverphoneEqual("cough", "coff"));
        assertTrue(caverphone.isCaverphoneEqual("why", "Y"));
        assertFalse(caverphone.isCaverphoneEqual("w2", "w3"));
        assertFalse(caverphone.isCaverphoneEqual("g2", "g3"));
        assertTrue(caverphone.isCaverphoneEqual("david", "daved"));
    }
}