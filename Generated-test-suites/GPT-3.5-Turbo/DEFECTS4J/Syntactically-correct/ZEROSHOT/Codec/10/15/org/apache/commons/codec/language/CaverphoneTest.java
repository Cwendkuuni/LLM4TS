import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("cr2f", caverphone.caverphone("cough"));
        assertEquals("r2f", caverphone.caverphone("rough"));
        assertEquals("t2f", caverphone.caverphone("tough"));
        assertEquals("enou2f", caverphone.caverphone("enough"));
        assertEquals("trou2f", caverphone.caverphone("trough"));
        assertEquals("2n", caverphone.caverphone("gn"));
        assertEquals("m2", caverphone.caverphone("mb"));
        assertEquals("k3", caverphone.caverphone("cq"));
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
        assertEquals("y", caverphone.caverphone("j"));
        assertEquals("Y3", caverphone.caverphone("y3"));
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
        assertEquals("3", caverphone.caverphone("l"));
        assertEquals("2", caverphone.caverphone("j"));
        assertEquals("2", caverphone.caverphone("y"));
        assertEquals("A", caverphone.caverphone("3"));
        assertEquals("A", caverphone.caverphone("3"));
        assertEquals("1111111111", caverphone.caverphone("abc"));
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("cr2f", caverphone.encode("cough"));
        assertEquals("r2f", caverphone.encode("rough"));
        assertEquals("t2f", caverphone.encode("tough"));
        assertEquals("enou2f", caverphone.encode("enough"));
        assertEquals("trou2f", caverphone.encode("trough"));
        assertEquals("2n", caverphone.encode("gn"));
        assertEquals("m2", caverphone.encode("mb"));
        assertEquals("k3", caverphone.encode("cq"));
        assertEquals("si", caverphone.encode("ci"));
        assertEquals("se", caverphone.encode("ce"));
        assertEquals("sy", caverphone.encode("cy"));
        assertEquals("2ch", caverphone.encode("tch"));
        assertEquals("k", caverphone.encode("c"));
        assertEquals("k", caverphone.encode("q"));
        assertEquals("k", caverphone.encode("x"));
        assertEquals("f", caverphone.encode("v"));
        assertEquals("2g", caverphone.encode("dg"));
        assertEquals("sio", caverphone.encode("tio"));
        assertEquals("sia", caverphone.encode("tia"));
        assertEquals("t", caverphone.encode("d"));
        assertEquals("fh", caverphone.encode("ph"));
        assertEquals("p", caverphone.encode("b"));
        assertEquals("s2", caverphone.encode("sh"));
        assertEquals("s", caverphone.encode("z"));
        assertEquals("A", caverphone.encode("a"));
        assertEquals("3", caverphone.encode("e"));
        assertEquals("3", caverphone.encode("i"));
        assertEquals("3", caverphone.encode("o"));
        assertEquals("3", caverphone.encode("u"));
        assertEquals("y", caverphone.encode("j"));
        assertEquals("Y3", caverphone.encode("y3"));
        assertEquals("A", caverphone.encode("y"));
        assertEquals("3", caverphone.encode("y"));
        assertEquals("3kh3", caverphone.encode("3gh3"));
        assertEquals("22", caverphone.encode("gh"));
        assertEquals("k", caverphone.encode("g"));
        assertEquals("S", caverphone.encode("s"));
        assertEquals("T", caverphone.encode("t"));
        assertEquals("P", caverphone.encode("p"));
        assertEquals("K", caverphone.encode("k"));
        assertEquals("F", caverphone.encode("f"));
        assertEquals("M", caverphone.encode("m"));
        assertEquals("N", caverphone.encode("n"));
        assertEquals("W3", caverphone.encode("w3"));
        assertEquals("Wh3", caverphone.encode("wh3"));
        assertEquals("3", caverphone.encode("w"));
        assertEquals("A", caverphone.encode("h"));
        assertEquals("2", caverphone.encode("r"));
        assertEquals("3", caverphone.encode("l"));
        assertEquals("2", caverphone.encode("j"));
        assertEquals("2", caverphone.encode("y"));
        assertEquals("A", caverphone.encode("3"));
        assertEquals("A", caverphone.encode("3"));
        assertEquals("1111111111", caverphone.encode("abc"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertTrue(caverphone.isCaverphoneEqual(null, null));
        assertTrue(caverphone.isCaverphoneEqual("", ""));
        assertTrue(caverphone.isCaverphoneEqual("cough", "cough"));
        assertFalse(caverphone.isCaverphoneEqual("cough", "tough"));
        assertTrue(caverphone.isCaverphoneEqual("abc", "abc"));
        assertFalse(caverphone.isCaverphoneEqual("abc", "def"));
    }
}