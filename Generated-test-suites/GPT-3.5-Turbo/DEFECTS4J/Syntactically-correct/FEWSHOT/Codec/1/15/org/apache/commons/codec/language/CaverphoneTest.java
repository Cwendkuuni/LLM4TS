package org.apache.commons.codec.language;

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
        assertEquals("1111111111", caverphone.caverphone(" "));

        assertEquals("kuf", caverphone.caverphone("cough"));
        assertEquals("ruf", caverphone.caverphone("rough"));
        assertEquals("tuf", caverphone.caverphone("tough"));
        assertEquals("kunuf", caverphone.caverphone("enough"));
        assertEquals("truf", caverphone.caverphone("trough"));
        assertEquals("2n", caverphone.caverphone("gn"));
        assertEquals("m2", caverphone.caverphone("mb"));

        assertEquals("2k", caverphone.caverphone("cq"));
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
        assertEquals("S", caverphone.caverphone("ss"));
        assertEquals("T", caverphone.caverphone("tt"));
        assertEquals("P", caverphone.caverphone("pp"));
        assertEquals("K", caverphone.caverphone("kk"));
        assertEquals("F", caverphone.caverphone("ff"));
        assertEquals("M", caverphone.caverphone("mm"));
        assertEquals("N", caverphone.caverphone("nn"));
        assertEquals("W3", caverphone.caverphone("w3"));
        assertEquals("W3", caverphone.caverphone("w"));
        assertEquals("2", caverphone.caverphone("w"));
        assertEquals("A", caverphone.caverphone("h"));
        assertEquals("2", caverphone.caverphone("h"));
        assertEquals("R3", caverphone.caverphone("r3"));
        assertEquals("R3", caverphone.caverphone("r"));
        assertEquals("2", caverphone.caverphone("r"));
        assertEquals("L3", caverphone.caverphone("l3"));
        assertEquals("L3", caverphone.caverphone("l"));
        assertEquals("2", caverphone.caverphone("l"));

        assertEquals("1111111111", caverphone.caverphone("wy"));
        assertEquals("Wh3", caverphone.caverphone("wh3"));
        assertEquals("3", caverphone.caverphone("w"));

        assertEquals("1111111111", caverphone.caverphone("why"));

        assertEquals("2", caverphone.caverphone("j"));
        assertEquals("1111111111", caverphone.caverphone("y3"));
        assertEquals("A", caverphone.caverphone("y"));
        assertEquals("3", caverphone.caverphone("y"));
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("1111111111", caverphone.caverphone(" "));

        assertEquals("1652895654", caverphone.caverphone("Caverphone"));
        assertEquals("126954", caverphone.caverphone("caver"));
    }
    
    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("1111111111", caverphone.encode(" "));

        assertEquals("1652895654", caverphone.encode("Caverphone"));
        assertEquals("126954", caverphone.encode("caver"));
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        assertTrue(caverphone.isCaverphoneEqual(null, null));
        assertFalse(caverphone.isCaverphoneEqual(null, ""));
        assertFalse(caverphone.isCaverphoneEqual("", ""));
        assertTrue(caverphone.isCaverphoneEqual("Caverphone", "Caverphone"));
        assertFalse(caverphone.isCaverphoneEqual("Caverphone", "caverphone"));
    }
}