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
        assertEquals("k", caverphone.caverphone("a"));
        assertEquals("k", caverphone.caverphone("A"));
        assertEquals("k", caverphone.caverphone("e"));
        // ... test all single letters
    }

    @Test
    public void testCaverphoneMultipleLetters() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A23T2F2S2F2S2F2S2F", caverphone.caverphone("The quick brown fox jumps over the lazy dog"));
        assertEquals("A23T2F2S2F2S2F2S2F2S2F2S2F2S2F2S2F2S2F2S2F", caverphone.caverphone("The quick brown fox jumps over the lazy dog The quick brown fox jumps over the lazy dog"));
        // ... test all combinations of letters
    }

    @Test
    public void testCaverphoneSpecialChars() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A", caverphone.caverphone(""));
        assertEquals("A", caverphone.caverphone(" "));
        assertEquals("A", caverphone.caverphone("."));
        assertEquals("A3", caverphone.caverphone(".,;:!?"));
        // ... test all special characters
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneNonString() {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(new Object());
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        String str1 = "The quick brown fox jumps over the lazy dog";
        String str2 = "the quick brown fox jumps over the lazy dog";
        assertEquals(true, caverphone.isCaverphoneEqual(str1, str2));
        String str3 = "The quick brown fox jumps over the quick dog";
        assertEquals(false, caverphone.isCaverphoneEqual(str1, str3));
    }
}