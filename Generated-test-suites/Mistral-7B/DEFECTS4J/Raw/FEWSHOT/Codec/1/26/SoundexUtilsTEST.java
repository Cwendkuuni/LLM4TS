package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;

import java.util.Locale;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("ABC", SoundexUtils.clean("AbCdEf"));
        assertEquals("ABC", SoundexUtils.clean("AbCdEf").toUpperCase());
        assertEquals("A", SoundexUtils.clean(""));
        assertEquals("A", SoundexUtils.clean(null));
        assertEquals("ABCDE", SoundexUtils.clean("AbCdE"));
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", SoundexUtils.clean("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.net.Md5Encoder();
        assertEquals(0, SoundexUtils.difference(encoder, "Apple", "Orange"));
        assertEquals(3, SoundexUtils.difference(encoder, "Apple", "Apricot"));
        assertEquals(1, SoundexUtils.difference(encoder, "Apple", "Apple"));
        assertEquals(3, SoundexUtils.difference(encoder, "Apple", "Apricot"));
        assertEquals(4, SoundexUtils.difference(encoder, "Apple", "Apricot"));
        assertThrows(EncoderException.class, () -> SoundexUtils.difference(encoder, null, "Apple"));
        assertThrows(EncoderException.class, () -> SoundexUtils.difference(encoder, "Apple", null));
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "K1N2R2T2";
        String es2 = "K1N2S2T2";
        assertEquals(3, SoundexUtils.differenceEncoded(es1, es2));
        assertEquals(4, SoundexUtils.differenceEncoded("K1N2R2T2", "K1N2R2T2"));
        assertEquals(0, SoundexUtils.differenceEncoded(null, "K1N2R2T2"));
        assertEquals(0, SoundexUtils.differenceEncoded("K1N2R2T2", null));
    }

    @Test
    public void testCleanAllMethods() {
        StringEncoder encoder = new org.apache.commons.codec.net.Md5Encoder();
        assertEquals(0, SoundexUtils.difference(encoder, null, null));
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
    }
}