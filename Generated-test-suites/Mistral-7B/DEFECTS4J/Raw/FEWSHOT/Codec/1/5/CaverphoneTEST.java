package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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
    }

    @Test
    public void testCaverphoneMultipleLetters() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A22F22F22F", caverphone.caverphone("CavershamProject"));
        assertEquals("K2K3S2S2S2S2F", caverphone.caverphone("TheQuickBrownFoxJumpsOverTheLazyDog"));
    }

    @Test
    public void testCaverphoneWithSpecialChars() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A33S2S2S2S2F", caverphone.caverphone("The_Quick_Brown_Fox_Jumps_Over_The_Lazy_Dog"));
    }

    @Test
    public void testCaverphoneCaseInsensitive() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A22F22F22F", caverphone.caverphone("CavershamProject"));
        assertEquals("A22F22F22F", caverphone.caverphone("cavershamproject"));
    }

    @Test
    public void testCaverphoneWithStartOptions() {
        Caverphone caverphone = new Caverphone();
        assertEquals("cou2f", caverphone.caverphone("Cough"));
        assertEquals("enou2f", caverphone.caverphone("Enough"));
        assertEquals("tio2f", caverphone.caverphone("Tio"));
        assertEquals("sio2f", caverphone.caverphone("Sia"));
        assertEquals("tou2f", caverphone.caverphone("Tough"));
        assertEquals("rou2f", caverphone.caverphone("Rough"));
        assertEquals("cou2f", caverphone.caverphone("Coughing"));
        assertEquals("rou2f", caverphone.caverphone("Roughing"));
    }

    @Test
    public void testCaverphoneWithReplacements() {
        Caverphone caverphone = new Caverphone();
        assertEquals("3q3q", caverphone.caverphone("Ciq"));
        assertEquals("si3si3", caverphone.caverphone("CiSi"));
        assertEquals("se3se3", caverphone.caverphone("CeSi"));
        assertEquals("sy3sy3", caverphone.caverphone("CySi"));
        assertEquals("fh2ch2", caverphone.caverphone("Phch"));
        assertEquals("k3k3", caverphone.caverphone("C"));
        assertEquals("k3k3", caverphone.caverphone("Q"));
        assertEquals("k3k3", caverphone.caverphone("X"));
        assertEquals("f2f2", caverphone.caverphone("Vv"));
        assertEquals("2g2g", caverphone.caverphone("Dg"));
        assertEquals("sio2f", caverphone.caverphone("Tio"));
        assertEquals("sia2f", caverphone.caverphone("Tia"));
        assertEquals("t2t2", caverphone.caverphone("Dd"));
        assertEquals("fh2ph2", caverphone.caverphone("Php"));
        assertEquals("p3p3", caverphone.caverphone("Bb"));
        assertEquals("s2s2s2s2", caverphone.caverphone("Shshshshsh"));
        assertEquals("s3s3", caverphone.caverphone("Zz"));
        assertEquals("A22A22", caverphone.caverphone("Hh"));
        assertEquals("A", caverphone.caverphone("e"));
        assertEquals("3", caverphone.caverphone("aeiou"));
        assertEquals("y3", caverphone.caverphone("y"));
        assertEquals("Y3", caverphone.caverphone("Y"));
        assertEquals("A", caverphone.caverphone("Y3"));
        assertEquals("3", caverphone.caverphone("y"));
        assertEquals("3kh3", caverphone.caverphone("3gh3"));
        assertEquals("22", caverphone.caverphone("gh"));
        assertEquals("k3", caverphone.caverphone("g"));
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
        assertEquals("A", caverphone.caverphone("h"));
        assertEquals("2", caverphone.caverphone("h"));
        assertEquals("R3", caverphone.caverphone("r3"));
        assertEquals("3", caverphone.caverphone("r"));
        assertEquals("L3", caverphone.caverphone("l3"));
        assertEquals("3", caverphone.caverphone("l"));
    }

    @Test
    public void testCaverphoneWithRemovals() {
        Caverphone caverphone = new Caverphone();
        assertEquals("", caverphone.caverphone("2"));
        assertEquals("A", caverphone.caverphone("3$"));       // 2.0 only
        assertEquals("", caverphone.caverphone("3"));
    }

    @Test
    public void testCaverphoneWithPutTenOnEnd() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A22F22F22F1111111111", caverphone.caverphone("CavershamProject"));
        assertEquals("K2K3S2S2S2S2F1111111111", caverphone.caverphone("TheQuickBrownFoxJumpsOverTheLazyDog"));
    }

    @Test
    public void testCaverphoneWithTruncateSix() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A22F22F", caverphone.caverphone("CavershamProject"));
        assertEquals("K2K3S2S2S2S2", caverphone.caverphone("TheQuickBrownFoxJumpsOverTheLazyDog"));
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneWithNonStringParameter() {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(123);
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        String str1 = "TheQuickBrownFoxJumpsOverTheLazyDog";
        String str2 = "TheQu1ckBr0wnF0xJumpsOv3rTh3L@zYD0g";
        assertEquals(true, caverphone.isCaverphoneEqual(str1, str2));
    }

    @Test
    public void testIsCaverphoneNotEqual() {
        Caverphone caverphone = new Caverphone();
        String str1 = "TheQuickBrownFoxJumpsOverTheLazyDog";
        String str2 = "TheQuickBrownFoxJumpsOverTheLazyCat";
        assertEquals(false, caverphone.isCaverphoneEqual(str1, str2));
    }
}