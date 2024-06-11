package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.apache.commons.codec.EncoderException;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEmpty() {
        assertEquals("1111111111", new Caverphone().caverphone(""));
    }

    @Test
    public void testCaverphoneSingleLetter() {
        assertEquals("1", new Caverphone().caverphone("a"));
    }

    @Test
    public void testCaverphoneMultipleLetters() {
        assertEquals("222222222222", new Caverphone().caverphone("apple"));
    }

    @Test
    public void testCaverphoneStartOptions() {
        assertEquals("cou2f", new Caverphone().caverphone("cough"));
        assertEquals("cou2f", new Caverphone().caverphone("cougH")); // H is uppercase
    }

    @Test
    public void testCaverphoneHandlesReplacements() {
        assertEquals("k2syi2k3syi2k3syi", new Caverphone().caverphone("catcyclescat"));
    }

    @Test
    public void testCaverphoneHandlesRemovals() {
        assertEquals("2222222222", new Caverphone().caverphone("apples"));
    }

    @Test
    public void testCaverphoneHandlesDoubles() {
        assertEquals("A222222222222A", new Caverphone().caverphone("AppleApple"));
    }

    @Test
    public void testCaverphoneHandlesLongStrings() {
        String longString = "The quick brown fox jumps over the lazy dog The quick brown fox jumps over the lazy dog";
        assertEquals("T3q2qb3r3o2x2f3o3x3j2p3m2p3l3a3z3y3z3a3z3d3o3g3 T3q2qb3r3o2x2f3o3x3j2p3m2p3l3a3z3y3z3a3z3d3o3g3", new Caverphone().caverphone(longString));
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneInvalidInput() {
        new Caverphone().caverphone(null);
    }

    @Test
    public void testCaverphoneEquals() {
        String str1 = "The quick brown fox";
        String str2 = "The quick brown fox";
        assertTrue(new Caverphone().isCaverphoneEqual(str1, str2));

        String str3 = "The quick brown fox";
        String str4 = "The quick white fox";
        assertFalse(new Caverphone().isCaverphoneEqual(str3, str4));
    }
}