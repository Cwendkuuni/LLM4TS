
package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CaverphoneTest {
    private final Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphone() {
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("1111111111", caverphone.caverphone("   "));

        assertEquals("kfatmr", caverphone.caverphone("coughatm"));
        assertEquals("kfatmr", caverphone.caverphone("rougHATM"));
        assertEquals("kfatmr", caverphone.caverphone("touGhatm"));
        assertEquals("kfatmr", caverphone.caverphone("enoughATM"));
        assertEquals("kfatmr", caverphone.caverphone("trouGhATM"));
        assertEquals("knATn", caverphone.caverphone("gnATn"));

        assertEquals("ma", caverphone.caverphone("mba"));
        assertEquals("ktk", caverphone.caverphone("cqtk"));
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
        assertEquals("A3", caverphone.caverphone("apple"));
        assertEquals("333", caverphone.caverphone("orange"));
        assertEquals("y", caverphone.caverphone("j"));
        assertEquals("A3", caverphone.caverphone("y3"));
        assertEquals("A3", caverphone.caverphone("y"));
        assertEquals("333", caverphone.caverphone("y"));
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
        assertEquals("33", caverphone.caverphone("33"));
        assertEquals("13221", caverphone.caverphone("r3"));
        assertEquals("2", caverphone.caverphone("l3"));
        assertEquals("A", caverphone.caverphone("h"));
        assertEquals("2", caverphone.caverphone("r"));
        assertEquals("2", caverphone.caverphone("l"));

        assertEquals("11111111111111111111", caverphone.caverphone("abcdefghijklmnopqrstuvwxyz"));
        assertEquals("kw", caverphone.caverphone("cbwr"));
    }

    @Test
    public void testEncode() {
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("1111111111", caverphone.encode("   "));

        assertEquals("kfatmr", caverphone.encode("coughatm"));
        assertEquals("kfatmr", caverphone.encode("rougHATM"));
        assertEquals("kfatmr", caverphone.encode("touGhatm"));
        assertEquals("kfatmr", caverphone.encode("enoughATM"));
        assertEquals("kfatmr", caverphone.encode("trouGhATM"));
        assertEquals("knATn", caverphone.encode("gnATn"));

        assertEquals("ma", caverphone.encode("mba"));
        assertEquals("ktk", caverphone.encode("cqtk"));
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
        assertEquals("A3", caverphone.encode("apple"));
        assertEquals("333", caverphone.encode("orange"));
        assertEquals("y", caverphone.encode("j"));
        assertEquals("A3", caverphone.encode("y3"));
        assertEquals("A3", caverphone.encode("y"));
        assertEquals("333", caverphone.encode("y"));
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
        assertEquals("33", caverphone.encode("33"));
        assertEquals("13221", caverphone.encode("r3"));
        assertEquals("2", caverphone.encode("l3"));
        assertEquals("A", caverphone.encode("h"));
        assertEquals("2", caverphone.encode("r"));
        assertEquals("2", caverphone.encode("l"));

        assertEquals("11111111111111111111", caverphone.encode("abcdefghijklmnopqrstuvwxyz"));
        assertEquals("kw", caverphone.encode("cbwr"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual(null, null));
        assertTrue(caverphone.isCaverphoneEqual("", ""));
        assertTrue(caverphone.isCaverphoneEqual("   ", "   "));
        assertTrue(caverphone.isCaverphoneEqual("hello", "HELLO"));
        assertFalse(caverphone.isCaverphoneEqual("hello", "world"));
    }
}
