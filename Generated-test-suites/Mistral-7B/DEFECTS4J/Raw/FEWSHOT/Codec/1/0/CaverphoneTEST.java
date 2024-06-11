package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEmptyString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(""));
    }

    @Test
    public void testCaverphoneNullString() {
        Caverphone caverphone = new Caverphone();
        assertNull(caverphone.caverphone(null));
    }

    @Test
    public void testCaverphoneLowercaseString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n2n", caverphone.caverphone("The quick brown fox jumps over the lazy dog"));
    }

    @Test
    public void testCaverphoneStartOptions() {
        Caverphone caverphone = new Caverphone();
        assertEquals("cou2f", caverphone.caverphone("cough"));
        assertEquals("cou2f", caverphone.caverphone("cougfh"));
        assertEquals("enou2f", caverphone.caverphone("enough"));
        assertEquals("enou2f", caverphone.caverphone("enougth"));
        assertEquals("rou2f", caverphone.caverphone("rough"));
        assertEquals("tou2f", caverphone.caverphone("tough"));
        assertEquals("trou2f", caverphone.caverphone("trough"));
        assertEquals("trou2f", caverphone.caverphone("trougth"));
        assertEquals("2n", caverphone.caverphone("gn"));
        assertEquals("m2", caverphone.caverphone("mb"));
    }

    @Test
    public void testCaverphoneReplacements() {
        Caverphone caverphone = new Caverphone();
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
        assertEquals("A", caverphone.caverphone("^[aeiou]"));
        assertEquals("3", caverphone.caverphone("[aeiou]"));
        assertEquals("y", caverphone.caverphone("j"));        // 2.0 only
        assertEquals("Y3", caverphone.caverphone("^y3"));    // 2.0 only
        assertEquals("A", caverphone.caverphone("^y"));       // 2.0 only
        assertEquals("3", caverphone.caverphone("y"));        // 2.0 only
        assertEquals("3kh3", caverphone.caverphone("3gh3"));
        assertEquals("22", caverphone.caverphone("gh"));
        assertEquals("k", caverphone.caverphone("g"));
        assertEquals("S", caverphone.caverphone("s+"));
        assertEquals("T", caverphone.caverphone("t+"));
        assertEquals("P", caverphone.caverphone("p+"));
        assertEquals("K", caverphone.caverphone("k+"));
        assertEquals("F", caverphone.caverphone("f+"));
        assertEquals("M", caverphone.caverphone("m+"));
        assertEquals("N", caverphone.caverphone("n+"));
        assertEquals("W3", caverphone.caverphone("w3"));
        //assertEquals("Wy", caverphone.caverphone("wy"));    // 1.0 only
        assertEquals("Wh3", caverphone.caverphone("wh3"));
        assertEquals("3", caverphone.caverphone("w"));
        assertEquals("A", caverphone.caverphone("^h"));
        assertEquals("2", caverphone.caverphone("h"));
        assertEquals("R3", caverphone.caverphone("r3"));
        assertEquals("3", caverphone.caverphone("r$"));       // 2.0 only
        //assertEquals("Ry", caverphone.caverphone("ry"));    // 1.0 only
        assertEquals("2", caverphone.caverphone("r"));
        assertEquals("L3", caverphone.caverphone("l3"));
        assertEquals("3", caverphone.caverphone("l$"));       // 2.0 only
        //assertEquals("Ly", caverphone.caverphone("ly"));    // 1.0 only
        assertEquals("2", caverphone.caverphone("l"));
        //assertEquals("y", caverphone.caverphone("j"));      // 1.0 only
        //assertEquals("Y3", caverphone.caverphone("y3"));    // 1.0 only
        //assertEquals("2", caverphone.caverphone("y"));      // 1.0 only
    }

    @Test
    public void testCaverphoneRemovals() {
        Caverphone caverphone = new Caverphone();
        assertEquals("", caverphone.caverphone("2"));
        assertEquals("A", caverphone.caverphone("3$"));       // 2.0 only
        assertEquals("", caverphone.caverphone("3"));
    }

    @Test
    public void testCaverphoneEncodingObject() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        String str = "The quick brown fox jumps over the lazy dog";
        assertEquals(caverphone.caverphone(str), caverphone.encode(str));
        Object obj = new Object();
        assertThrows(EncoderException.class, () -> caverphone.encode(obj));
    }

    @Test
    public void testCaverphoneEncodeString() {
        Caverphone caverphone = new Caverphone();
        String str1 = "The quick brown fox jumps over the lazy dog";
        String str2 = caverphone.encode(str1);
        assertTrue(caverphone.isCaverphoneEqual(str1, str2));
    }

    @Test
    public void testCaverphoneIsCaverphoneEqualDifferentStrings() {
        Caverphone caverphone = new Caverphone();
        String str1 = "The quick brown fox jumps over the lazy dog";
        String str2 = "The quick brown fox jumps over the quick brown dog";
        assertFalse(caverphone.isCaverphoneEqual(str1, str2));
    }
}