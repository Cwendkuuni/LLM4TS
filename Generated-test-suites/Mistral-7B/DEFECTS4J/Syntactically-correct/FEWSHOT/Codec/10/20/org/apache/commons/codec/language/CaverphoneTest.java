package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CaverphoneTest {
    @Test
    public void testCaverphone1() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A1F1K1L1K1F1S1T1", caverphone.caverphone("The quick brown fox jumps over the lazy dog"));
    }

    @Test
    public void testCaverphone2() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A1F1K1L1K1F1S1T1", caverphone.caverphone("The quick brown fox jumps over the lazy dog."));
    }

    @Test
    public void testCaverphone3() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(""));
    }

    @Test
    public void testCaverphone4() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A1F1K1L1K1F1S1T1P1P1K1F1K1", caverphone.caverphone("The quick brown fox jumps over the lazy dog, and the quick brown fox jumps over the lazy dog."));
    }

    @Test
    public void testCaverphone5() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A2F2K2L2K2F2S2T2S2T2P2P2P2K1F1K1", caverphone.caverphone("The quick brown fox jumps over the quick brown fox."));
    }

    @Test
    public void testCaverphone6() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A1F1K1L1K1F1S1T1P1K1F1K1", caverphone.caverphone("The quick brown fox jumps."));
    }

    @Test
    public void testCaverphone7() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A1F1K1L1K1F1S1T1T1P1P1K1F1K1", caverphone.caverphone("The quick brown fox jumps over."));
    }

    @Test
    public void testCaverphone8() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A1F1K1L1K1F1S1T1T1P1P1K1F1K11111111", caverphone.caverphone("The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog."));
    }

    @Test (expected = EncoderException.class)
    public void testCaverphoneFail() {
        Caverphone caverphone = new Caverphone();
        caverphone.caverphone(123);
    }
}