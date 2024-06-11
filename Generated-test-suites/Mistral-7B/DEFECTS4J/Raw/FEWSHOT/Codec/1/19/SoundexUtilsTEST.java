package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("Foo", SoundexUtils.clean("Foo"));
        assertEquals("BaR", SoundexUtils.clean("BaR"));
        assertEquals("ThIs Is A tEsT", SoundexUtils.clean("ThIs Is A tEsT"));
        assertNull(SoundexUtils.clean(null));
        assertNull(SoundexUtils.clean(""));
    }

    @Test
    public void testDifferenceEncodedNull() {
        assertNull(SoundexUtils.differenceEncoded(null, null));
    }

    @Test
    public void testDifferenceEncodedEmpty() {
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
    }

    @Test
    public void testDifferenceEncodedSame() {
        StringEncoder encoder = new org.apache.commons.codec.net.URL3CodeEncoder();
        assertEquals(4, SoundexUtils.differenceEncoded(encoder.encode("foo"), encoder.encode("foo")));
        assertEquals(4, SoundexUtils.differenceEncoded(encoder.encode("FOO"), encoder.encode("foo")));
    }

    @Test
    public void testDifferenceEncodedDifferent() {
        StringEncoder encoder = new org.apache.commons.codec.net.URL3CodeEncoder();
        assertEquals(0, SoundexUtils.differenceEncoded(encoder.encode("foo"), encoder.encode("bar")));
        assertEquals(1, SoundexUtils.differenceEncoded(encoder.encode("foo"), encoder.encode("barb")));
        assertEquals(1, SoundexUtils.differenceEncoded(encoder.encode("foo"), encoder.encode("barr")));
        assertEquals(1, SoundexUtils.differenceEncoded(encoder.encode("foo"), encoder.encode("barr--")));
        assertEquals(2, SoundexUtils.differenceEncoded(encoder.encode("foo"), encoder.encode("foobar")));
        assertEquals(3, SoundexUtils.differenceEncoded(encoder.encode("foo"), encoder.encode("foobarb")));
        assertEquals(3, SoundexUtils.differenceEncoded(encoder.encode("foo"), encoder.encode("foobarr")));
        assertEquals(3, SoundexUtils.differenceEncoded(encoder.encode("foo"), encoder.encode("foobarr--")));
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceEncodedError() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.net.URL3CodeEncoder();
        SoundexUtils.differenceEncoded(encoder.encode("foo"), null);
    }
}