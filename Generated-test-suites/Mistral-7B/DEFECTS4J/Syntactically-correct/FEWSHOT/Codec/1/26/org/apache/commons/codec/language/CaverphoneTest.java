package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneNull() {
        Caverphone caverphone = new Caverphone();
        assertNull(caverphone.encode(null));
    }

    @Test
    public void testEmptyString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.encode(""));
    }

    @Test
    public void testLowerCase() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A1A1A1A1A1A1A1A1A1A1", caverphone.encode("HeLLo WORld"));
    }

    @Test
    public void testRemoveOtherCharacters() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A1A1A1A1A1A1A1A1A1A1", caverphone.encode("HeLLo WORld123@#$%"));
    }

    @Test
    public void testRemoveFinalE() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A1A1A1A1A1A1A1A1A1A1", caverphone.encode("HeLLo WORld"));
        assertEquals("A1A1A1A1A1A1A1A1A1A1", caverphone.encode("HeLLo WORlde"));
    }

    @Test
    public void testReplaceStart() {
        Caverphone caverphone = new Caverphone();
        assertEquals("cou2f1A1A1A1A1A1A1A1A1A1", caverphone.encode("Cough"));
        assertEquals("rou2f1A1A1A1A1A1A1A1A1A1", caverphone.encode("Rough"));
        assertEquals("tou2f1A1A1A1A1A1A1A1A1A1", caverphone.encode("Tough"));
        assertEquals("enou2f1A1A1A1A1A1A1A1A1A1", caverphone.encode("Enough"));
        assertEquals("trou2f1A1A1A1A1A1A1A1A1A1", caverphone.encode("Trough")); // 2.0 only

        assertEquals("2n1A1A1A1A1A1A1A1A1A1", caverphone.encode("gn"));
        assertEquals("m21A1A1A1A1A1A1A1A1A1", caverphone.encode("mb"));
    }

    @Test
    public void testReplace() {
        Caverphone caverphone = new Caverphone();
        assertEquals("2q1A1A1A1A1A1A1A1A1A1", caverphone.encode("cq"));
        assertEquals("si1A1A1A1A1A1A1A1A1A1", caverphone.encode("ci"));
        assertEquals("se1A1A1A1A1A1A1A1A1A1", caverphone.encode("ce"));
        assertEquals("sy1A1A1A1A1A1A1A1A1A1", caverphone.encode("cy"));
        assertEquals("2ch1A1A1A1A1A1A1A1A1A1", caverphone.encode("tch"));
        assertEquals("k1A1A1A1A1A1A1A1A1A1", caverphone.encode("c"));
        assertEquals("k1A1A1A1A1A1A1A1A1A1", caverphone.encode("q"));
        assertEquals("k1A1A1A1A1A1A1A1A1A1", caverphone.encode("x"));
        assertEquals("f1A1A1A1A1A1A1A1A1A1", caverphone.encode("v"));
        assertEquals("2g1A1A1A1A1A1A1A1A1A1", caverphone.encode("dg"));
        assertEquals("sio1A1A1A1A1A1A1A1A1A1", caverphone.encode("tio"));
        assertEquals("sia1A1A1A1A1A1A1A1A1A1", caverphone.encode("tia"));
        assertEquals("t1A1A1A1A1A1A1A1A1A1", caverphone.encode("d"));
        assertEquals("fh1A1A1A1A1A1A1A1A1A1", caverphone.encode("ph"));
        assertEquals("p1A1A1A1A1A1A1A1A1A1", caverphone.encode("b"));
        assertEquals("s21A1A1A1A1A1A1A1A1A1", caverphone.encode("sh"));
        assertEquals("s1A1A1A1A1A1A1A1A1A1", caverphone.encode("z"));

        assertEquals("A1A1A1A1A1A1A1A1A1A1", caverphone.encode("")); // 2.0 only
        assertEquals("Y31A1A1A1A1A1A1A1A1A1", caverphone.encode("why")); // 1.0 only
        assertEquals("Why1A1A1A1A1A1A1A1A1A1", caverphone.encode("Why")); // 1.0 only
        assertEquals("W31A1A1A1A1A1A1A1A1A1", caverphone.encode("wy")); // 1.0 only
        assertEquals("Wy1A1A1A1A1A1A1A1A1A1", caverphone.encode("Wy")); // 1.0 only
        assertEquals("3kh31A1A1A1A1A1A1A1A1A1", caverphone.encode("3gh3"));
        assertEquals("221A1A1A1A1A1A1A1A1A1", caverphone.encode("gh"));
        assertEquals("k1A1A1A1A1A1A1A1A1A1", caverphone.encode("g"));

        assertEquals("S1A1A1A1A1A1A1A1", caverphone.encode("s+"));
        assertEquals("T1A1A1A1A1A1A1A1", caverphone.encode("t+"));
        assertEquals("P1A1A1A1A1A1A1A1", caverphone.encode("p+"));
        assertEquals("K1A1A1A1A1A1A1A1", caverphone.encode("k+"));
        assertEquals("F1A1A1A1A1A1A1A1", caverphone.encode("f+"));
        assertEquals("M1A1A1A1A1A1A1A1", caverphone.encode("m+"));
        assertEquals("N1A1A1A1A1A1A1A1", caverphone.encode("n+"));
        assertEquals("W31A1A1A1A1A1A1A1A1A1", caverphone.encode("w3"));
        //assertEquals("Wy1A1A1A1A1A1A1A1A1A1", caverphone.encode("wy")); // 1.0 only
        assertEquals("Wh31A1A1A1A1A1A1A1A1A1", caverphone.encode("wh3"));
        assertEquals("31A1A1A1A1A1A1A1A1A1", caverphone.encode("w"));
        assertEquals("A1A1A1A1A1A1A1A1", caverphone.encode("h"));
        assertEquals("21A1A1A1A1A1A1A1A1A1", caverphone.encode("h"));
        assertEquals("R31A1A1A1A1A1A1A1A1A1", caverphone.encode("r3"));
        assertEquals("31A1A1A1A1A1A1A1A1A1", caverphone.encode("r"));
        assertEquals("L31A1A1A1A1A1A1A1A1A1", caverphone.encode("l3"));
        assertEquals("31A1A1A1A1A1A1A1A1A1", caverphone.encode("l"));
        //assertEquals("Ly1A1A1A1A1A1A1A1A1A1", caverphone.encode("ly")); // 1.0 only
        //assertEquals("j1A1A1A1A1A1A1A1A1A1", caverphone.encode("j")); // 1.0 only
        //assertEquals("y31A1A1A1A1A1A1A1A1A1", caverphone.encode("y3")); // 1.0 only
        //assertEquals("Y31A1A1A1A1A1A1A1A1A1", caverphone.encode("Y3")); // 1.0 only
        //assertEquals("y1A1A1A1A1A1A1A1A1A1", caverphone.encode("y")); // 1.0 only

        assertEquals("", caverphone.encode("")); // 2.0 only
        assertEquals("A", caverphone.encode("3")); // 2.0 only
    }

    @Test
    public void testRemove() {
        Caverphone caverphone = new Caverphone();
        assertEquals("", caverphone.encode("2"));
        assertEquals("A", caverphone.encode("3$")); // 2.0 only
        assertEquals("", caverphone.encode("3"));
    }

    @Test
    public void testAddOnes() {
        Caverphone caverphone = new Caverphone();
        assertEquals("11111111111111111111", caverphone.encode("abcdefghijk"));
    }

    @Test
    public void testFirstSix() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.encode("abcdefghijk"));
    }

    @Test
    public void testEncodeEmptyString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.encode(""));
    }

    @Test
    public void testEncodeNull() {
        Caverphone caverphone = new Caverphone();
        assertNull(caverphone.encode(null));
    }

    @Test
    public void testEncodeObject() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.encode(new String("abcdefghijk")));
    }

    @Test
    public void testIsCaverphoneEqualNull() {
        Caverphone caverphone = new Caverphone();
        assertNull(caverphone.isCaverphoneEqual(null, "abcdefghijk"));
    }

    @Test
    public void testIsCaverphoneEqualDifferentStrings() {
        Caverphone caverphone = new Caverphone();
        assertFalse(caverphone.isCaverphoneEqual("abcdefghijk", "lmnopqrstu"));
    }

    @Test
    public void testIsCaverphoneEqualSameStrings() {
        Caverphone caverphone = new Caverphone();
        assertTrue(caverphone.isCaverphoneEqual("abcdefghijk", "abcdefghijk"));
    }
}