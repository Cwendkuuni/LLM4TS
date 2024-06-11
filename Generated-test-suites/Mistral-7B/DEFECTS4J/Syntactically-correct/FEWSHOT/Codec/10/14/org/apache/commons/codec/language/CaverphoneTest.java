package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEmpty() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(""));
    }

    @Test
    public void testCaverphoneSingleLetter() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A", caverphone.caverphone("a"));
    }

    @Test
    public void testCaverphoneMultipleLetters() {
        Caverphone caverphone = new Caverphone();
        assertEquals("AAA222T", caverphone.caverphone("Apple"));
        assertEquals("K2F2F2S2S2S", caverphone.caverphone("Kiwi"));
        assertEquals("F1F1F1F1F1F1F1F1F2", caverphone.caverphone("Java"));
        assertEquals("2T3N2T2T2T2T2T2T", caverphone.caverphone("Orange"));
        assertEquals("2K2F2S2S2F2F2F2", caverphone.caverphone("Pineapple"));
        assertEquals("K2K2K2K2F2F2F2F2F2", caverphone.caverphone("Watermelon"));
        assertEquals("M2M2M2M2M2M2M2M2M2M2", caverphone.caverphone("Blueberry"));
    }

    @Test
    public void testCaverphoneStartOptions() {
        Caverphone caverphone = new Caverphone();
        assertEquals("cou2f", caverphone.caverphone("cough"));
        assertEquals("cou2f", caverphone.caverphone("Cough"));
        assertEquals("rou2f", caverphone.caverphone("rough"));
        assertEquals("rou2f", caverphone.caverphone("Rough"));
        assertEquals("tou2f", caverphone.caverphone("tough"));
        assertEquals("tou2f", caverphone.caverphone("Tough"));
        assertEquals("enou2f", caverphone.caverphone("enough"));
        assertEquals("enou2f", caverphone.caverphone("Enough"));
        assertEquals("trou2f", caverphone.caverphone("trough"));
        assertEquals("trou2f", caverphone.caverphone("Trough"));
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
        assertEquals("A", caverphone.caverphone("^h"));
        assertEquals("2", caverphone.caverphone("h"));
        assertEquals("R3", caverphone.caverphone("r3"));
        assertEquals("3", caverphone.caverphone("r"));
        assertEquals("L3", caverphone.caverphone("l3"));
        assertEquals("3", caverphone.caverphone("l"));
    }

    @Test
    public void testCaverphoneRemovals() {
        Caverphone caverphone = new Caverphone();
        assertEquals("111111", caverphone.caverphone("2"));
        assertEquals("A", caverphone.caverphone("3$"));
        assertEquals("", caverphone.caverphone("3"));
    }

    @Test
    public void testCaverphoneEquals() {
        Caverphone caverphone = new Caverphone();
        String codeApple = caverphone.caverphone("Apple");
        String codeApple2 = caverphone.caverphone("Apple");
        String codeOrange = caverphone.caverphone("Orange");
        assertTrue(caverphone.isCaverphoneEqual("Apple", "Apple"));
        assertTrue(caverphone.isCaverphoneEqual("Apple", "Apple"));
        assertFalse(caverphone.isCaverphoneEqual("Apple", "Orange"));
        assertFalse(caverphone.isCaverphoneEqual("Apple", "Orange"));
    }
}