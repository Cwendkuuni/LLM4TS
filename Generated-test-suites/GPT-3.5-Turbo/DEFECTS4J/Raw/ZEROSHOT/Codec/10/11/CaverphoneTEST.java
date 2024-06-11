import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();

        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("cccc2f1", caverphone.caverphone("cough"));
        assertEquals("r2f1", caverphone.caverphone("rough"));
        assertEquals("tou2f1", caverphone.caverphone("tough"));
        assertEquals("enou2f1", caverphone.caverphone("enough"));
        assertEquals("trou2f1", caverphone.caverphone("trough"));
        assertEquals("2n1", caverphone.caverphone("gn"));
        assertEquals("mb1", caverphone.caverphone("mb"));
        assertEquals("2q1", caverphone.caverphone("cq"));
        assertEquals("si1", caverphone.caverphone("ci"));
        assertEquals("se1", caverphone.caverphone("ce"));
        assertEquals("sy1", caverphone.caverphone("cy"));
        assertEquals("2ch1", caverphone.caverphone("tch"));
        assertEquals("k1", caverphone.caverphone("c"));
        assertEquals("k1", caverphone.caverphone("q"));
        assertEquals("k1", caverphone.caverphone("x"));
        assertEquals("f1", caverphone.caverphone("v"));
        assertEquals("2g1", caverphone.caverphone("dg"));
        assertEquals("sio1", caverphone.caverphone("tio"));
        assertEquals("sia1", caverphone.caverphone("tia"));
        assertEquals("t1", caverphone.caverphone("d"));
        assertEquals("fh1", caverphone.caverphone("ph"));
        assertEquals("p1", caverphone.caverphone("b"));
        assertEquals("s21", caverphone.caverphone("sh"));
        assertEquals("s1", caverphone.caverphone("z"));
        assertEquals("A", caverphone.caverphone("a"));
        assertEquals("1111111111", caverphone.caverphone("A"));
        assertEquals("3", caverphone.caverphone("e"));
        assertEquals("3", caverphone.caverphone("i"));
        assertEquals("3", caverphone.caverphone("o"));
        assertEquals("3", caverphone.caverphone("u"));
        assertEquals("y", caverphone.caverphone("j"));
        assertEquals("A", caverphone.caverphone("y"));
        assertEquals("3gh3", caverphone.caverphone("3gh3"));
        assertEquals("22", caverphone.caverphone("gh"));
        assertEquals("k1", caverphone.caverphone("g"));
        assertEquals("S", caverphone.caverphone("s"));
        assertEquals("T", caverphone.caverphone("t"));
        assertEquals("P", caverphone.caverphone("p"));
        assertEquals("K", caverphone.caverphone("k"));
        assertEquals("F", caverphone.caverphone("f"));
        assertEquals("M", caverphone.caverphone("m"));
        assertEquals("N", caverphone.caverphone("n"));
        assertEquals("W3", caverphone.caverphone("w3"));
        assertEquals("2", caverphone.caverphone("w"));
        assertEquals("A", caverphone.caverphone("h"));
        assertEquals("2", caverphone.caverphone("r"));
        assertEquals("2", caverphone.caverphone("l"));
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();

        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("cccc2f1", caverphone.encode("cough"));
        assertEquals("r2f1", caverphone.encode("rough"));
        assertEquals("tou2f1", caverphone.encode("tough"));
        assertEquals("enou2f1", caverphone.encode("enough"));
        assertEquals("trou2f1", caverphone.encode("trough"));
        assertEquals("2n1", caverphone.encode("gn"));
        assertEquals("mb1", caverphone.encode("mb"));
        assertEquals("2q1", caverphone.encode("cq"));
        assertEquals("si1", caverphone.encode("ci"));
        assertEquals("se1", caverphone.encode("ce"));
        assertEquals("sy1", caverphone.encode("cy"));
        assertEquals("2ch1", caverphone.encode("tch"));
        assertEquals("k1", caverphone.encode("c"));
        assertEquals("k1", caverphone.encode("q"));
        assertEquals("k1", caverphone.encode("x"));
        assertEquals("f1", caverphone.encode("v"));
        assertEquals("2g1", caverphone.encode("dg"));
        assertEquals("sio1", caverphone.encode("tio"));
        assertEquals("sia1", caverphone.encode("tia"));
        assertEquals("t1", caverphone.encode("d"));
        assertEquals("fh1", caverphone.encode("ph"));
        assertEquals("p1", caverphone.encode("b"));
        assertEquals("s21", caverphone.encode("sh"));
        assertEquals("s1", caverphone.encode("z"));
        assertEquals("A", caverphone.encode("a"));
        assertEquals("1111111111", caverphone.encode("A"));
        assertEquals("3", caverphone.encode("e"));
        assertEquals("3", caverphone.encode("i"));
        assertEquals("3", caverphone.encode("o"));
        assertEquals("3", caverphone.encode("u"));
        assertEquals("y", caverphone.encode("j"));
        assertEquals("A", caverphone.encode("y"));
        assertEquals("3gh3", caverphone.encode("3gh3"));
        assertEquals("22", caverphone.encode("gh"));
        assertEquals("k1", caverphone.encode("g"));
        assertEquals("S", caverphone.encode("s"));
        assertEquals("T", caverphone.encode("t"));
        assertEquals("P", caverphone.encode("p"));
        assertEquals("K", caverphone.encode("k"));
        assertEquals("F", caverphone.encode("f"));
        assertEquals("M", caverphone.encode("m"));
        assertEquals("N", caverphone.encode("n"));
        assertEquals("W3", caverphone.encode("w3"));
        assertEquals("2", caverphone.encode("w"));
        assertEquals("A", caverphone.encode("h"));
        assertEquals("2", caverphone.encode("r"));
        assertEquals("2", caverphone.encode("l"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        assertEquals(false, caverphone.isCaverphoneEqual(null, null));
        assertEquals(true, caverphone.isCaverphoneEqual("", ""));
        assertEquals(true, caverphone.isCaverphoneEqual("cough", "cough"));
        assertEquals(false, caverphone.isCaverphoneEqual("cough", "coughs"));
        assertEquals(true, caverphone.isCaverphoneEqual("rough", "rough"));
        assertEquals(false, caverphone.isCaverphoneEqual("rough", "ruffs"));
    }
}