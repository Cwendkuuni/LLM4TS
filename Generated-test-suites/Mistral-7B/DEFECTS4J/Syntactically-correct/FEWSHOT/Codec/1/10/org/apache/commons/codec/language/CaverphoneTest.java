package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneNullInput() {
        Caverphone caverphone = new Caverphone();
        assertNull(caverphone.caverphone(null));
    }

    @Test
    public void testCaverphoneEmptyInput() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(""));
    }

    @Test
    public void testCaverphoneLowerCaseInput() {
        Caverphone caverphone = new Caverphone();
        assertEquals("DDDDDDDDDDDDD", caverphone.caverphone("ddddddddddddd"));
        assertEquals("RRRRRRRRRRRRR", caverphone.caverphone("rRRRRRRRRRRrR"));
    }

    @Test
    public void testCaverphoneHandleStartOptions() {
        Caverphone caverphone = new Caverphone();
        assertEquals("cou2f", caverphone.caverphone("cough"));
        assertEquals("rou2f", caverphone.caverphone("rough"));
        assertEquals("tou2f", caverphone.caverphone("tough"));
        assertEquals("enou2f", caverphone.caverphone("enough"));
        assertEquals("trou2f", caverphone.caverphone("trough"));
        assertEquals("2n", caverphone.caverphone("gn"));
        assertEquals("m2", caverphone.caverphone("mb"));
    }

    @Test
    public void testCaverphoneHandleReplacements() {
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
        // 2.0 only
        assertEquals("Y3", caverphone.caverphone("^y3"));
        assertEquals("A", caverphone.caverphone("^y"));
        assertEquals("3", caverphone.caverphone("y"));
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
        // 1.0 only
        //assertEquals("Wy", caverphone.caverphone("wy"));
        assertEquals("Wh3", caverphone.caverphone("wh3"));
        assertEquals("3", caverphone.caverphone("w"));
        //assertEquals("Why", caverphone.caverphone("why"));
        assertEquals("2", caverphone.caverphone("w"));
        assertEquals("A", caverphone.caverphone("^h"));
        assertEquals("2", caverphone.caverphone("h"));
        assertEquals("R3", caverphone.caverphone("r3"));
        assertEquals("3", caverphone.caverphone("r$"));
        //assertEquals("Ry", caverphone.caverphone("ry"));
        assertEquals("2", caverphone.caverphone("r"));
        assertEquals("L3", caverphone.caverphone("l3"));
        assertEquals("3", caverphone.caverphone("l$"));
        //assertEquals("Ly", caverphone.caverphone("ly"));
        assertEquals("2", caverphone.caverphone("l"));
        //assertEquals("j", caverphone.caverphone("y"));
        //assertEquals("Y3", caverphone.caverphone("y3"));
        //assertEquals("y", caverphone.caverphone("y"));
    }

    @Test
    public void testCaverphoneHandleRemovals() {
        Caverphone caverphone = new Caverphone();
        assertEquals("", caverphone.caverphone("2"));
        assertEquals("A", caverphone.caverphone("3$"));
        assertEquals("", caverphone.caverphone("3"));
    }

    @Test
    public void testCaverphoneHandleAppend() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111111111", caverphone.caverphone("abcdefg"));
    }

    @Test
    public void testCaverphoneHandleSubstring() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone("abcdefg").substring(0, 10));
    }

    @Test
    public void testCaverphoneEncodeObject() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        String str = "test";
        assertEquals(caverphone.caverphone(str), caverphone.encode(str));
    }

    @Test
    public void testCaverphoneIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        String str1 = "test1";
        String str2 = "test1";
        assertTrue(caverphone.isCaverphoneEqual(str1, str2));
        String str3 = "test2";
        assertFalse(caverphone.isCaverphoneEqual(str1, str3));
    }

}