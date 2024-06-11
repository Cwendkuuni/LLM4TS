package org.apache.commons.codec.language;

import static org.junit.Assert.*;

import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();

        // Test empty string
        assertEquals("1111111111", caverphone.caverphone(""));

        // Test single character
        assertEquals("A", caverphone.caverphone("a"));

        // Test various strings
        assertEquals("2222222222", caverphone.caverphone("hello"));
        assertEquals("K2K2K2K2K2K2K2K2", caverphone.caverphone("CavershamProject"));
        assertEquals("F2S2S2S2S2S2S2S2", caverphone.caverphone("TheQuickBrownFoxJumpsOverTheLazyDog"));
        assertEquals("P2T2T2T2T2T2T2T2", caverphone.caverphone("TheQuickBrownFoxJumpsOverTheLazyDogTheQuickBrownFoxJumpsOverTheLazyDog"));

        // Test special cases
        assertEquals("A", caverphone.caverphone("a"));
        assertEquals("A", caverphone.caverphone("A"));
        assertEquals("1111111111", caverphone.caverphone(null));
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();

        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("2222222222", caverphone.encode("hello"));
        assertEquals("K2K2K2K2K2K2K2K2", caverphone.encode("CavershamProject"));
        assertEquals("F2S2S2S2S2S2S2S2", caverphone.encode("TheQuickBrownFoxJumpsOverTheLazyDog"));
        assertEquals("P2T2T2T2T2T2T2T2", caverphone.encode("TheQuickBrownFoxJumpsOverTheLazyDogTheQuickBrownFoxJumpsOverTheLazyDog"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        assertTrue(caverphone.isCaverphoneEqual("hello", "hello"));
        assertTrue(caverphone.isCaverphoneEqual("CavershamProject", "CavershamProject"));
        assertTrue(caverphone.isCaverphoneEqual("TheQuickBrownFoxJumpsOverTheLazyDog", "TheQuickBrownFoxJumpsOverTheLazyDog"));
        assertTrue(caverphone.isCaverphoneEqual("TheQuickBrownFoxJumpsOverTheLazyDogTheQuickBrownFoxJumpsOverTheLazyDog", "TheQuickBrownFoxJumpsOverTheLazyDogTheQuickBrownFoxJumpsOverTheLazyDog"));
        assertFalse(caverphone.isCaverphoneEqual("hello", "hello world"));
    }
}