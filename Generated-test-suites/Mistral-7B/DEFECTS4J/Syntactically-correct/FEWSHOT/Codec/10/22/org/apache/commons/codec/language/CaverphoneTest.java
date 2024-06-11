package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEmptyString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(""));
    }

    @Test
    public void testCaverphoneSingleLetter() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A", caverphone.caverphone("a"));
        assertEquals("A", caverphone.caverphone("A"));
    }

    @Test
    public void testCaverphoneMultipleLetters() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A2A", caverphone.caverphone("ab"));
        assertEquals("A2F", caverphone.caverphone("abcd"));
        assertEquals("F", caverphone.caverphone("e"));
        assertEquals("A2F3", caverphone.caverphone("abcdef"));
        assertEquals("A2F3S", caverphone.caverphone("abcdefg"));
        assertEquals("A2F3S2", caverphone.caverphone("abcdefgh"));
        assertEquals("A2F3S2T", caverphone.caverphone("abcdefghij"));
        assertEquals("A2F3S2T2", caverphone.caverphone("abcdefghijkl"));
        assertEquals("A2F3S2T2K", caverphone.caverphone("abcdefghijklm"));
    }

    @Test
    public void testCaverphoneSpecialCases() {
        Caverphone caverphone = new Caverphone();
        assertEquals("Cou2f", caverphone.caverphone("cough"));
        assertEquals("Rou2f", caverphone.caverphone("rough"));
        assertEquals("Tou2f", caverphone.caverphone("tough"));
        assertEquals("Enou2f", caverphone.caverphone("enough"));
        assertEquals("Trou2f", caverphone.caverphone("trough"));
        assertEquals("M2", caverphone.caverphone("mb"));
        assertEquals("K", caverphone.caverphone("cq"));
        assertEquals("S", caverphone.caverphone("ci"));
        assertEquals("S", caverphone.caverphone("ce"));
        assertEquals("Sy", caverphone.caverphone("cy"));
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
        assertEquals("A", caverphone.caverphone("h"));
        assertEquals("2", caverphone.caverphone("h"));
        assertEquals("R3", caverphone.caverphone("r3"));
        assertEquals("3", caverphone.caverphone("r$"));
        assertEquals("L3", caverphone.caverphone("l3"));
        assertEquals("3", caverphone.caverphone("l$"));
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneNonString() {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(123);
    }

    @Test
    public void testCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        String code1 = caverphone.caverphone("abcdefghijklm");
        String code2 = caverphone.caverphone("abcdefghijklm");
        assertEquals(code1, code2);
    }

    @Test
    public void testCaverphoneNotEqual() {
        Caverphone caverphone = new Caverphone();
        String code1 = caverphone.caverphone("abcdefghijklm");
        String code2 = caverphone.caverphone("abcdefghijklmn");
        assertFalse(caverphone.isCaverphoneEqual("abcdefghijklm", "abcdefghijklmn"));
    }

}