package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("Foo", SoundexUtils.clean("Foo"));
        assertEquals("Bar", SoundexUtils.clean("bar"));
        assertEquals("BaR", SoundexUtils.clean("bAR"));
        assertEquals("", SoundexUtils.clean(""));
        assertEquals("123", SoundexUtils.clean("123"));
        assertEquals("Foo Bar", SoundexUtils.clean("Foo Bar"));
    }

    @Test
    public void testDifference() throws EncoderException {
        SoundexEncoder soundexEncoder = new SoundexEncoder();
        assertEquals(4, SoundexUtils.difference(soundexEncoder, "Foo", "Foo"));
        assertEquals(3, SoundexUtils.difference(soundexEncoder, "Foo", "Bar"));
        assertEquals(3, SoundexUtils.difference(soundexEncoder, "Bar", "Foo"));
        assertEquals(2, SoundexUtils.difference(soundexEncoder, "Foo", "Baz"));
        assertEquals(2, SoundexUtils.difference(soundexEncoder, "Baz", "Foo"));
        assertEquals(0, SoundexUtils.difference(soundexEncoder, "", ""));
        assertEquals(0, SoundexUtils.difference(soundexEncoder, null, ""));
        assertEquals(0, SoundexUtils.difference(soundexEncoder, null, null));
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(4, SoundexUtils.differenceEncoded("122321", "122321"));
        assertEquals(3, SoundexUtils.differenceEncoded("122321", "210323"));
        assertEquals(3, SoundexUtils.differenceEncoded("210323", "122321"));
        assertEquals(3, SoundexUtils.differenceEncoded("122321", "132321"));
        assertEquals(3, SoundexUtils.differenceEncoded("132321", "122321"));
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
        assertEquals(0, SoundexUtils.differenceEncoded(null, ""));
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
    }
}