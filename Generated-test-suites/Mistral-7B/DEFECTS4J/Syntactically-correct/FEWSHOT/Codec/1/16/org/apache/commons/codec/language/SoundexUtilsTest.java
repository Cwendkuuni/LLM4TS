package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("Foo", SoundexUtils.clean("foo"));
        assertEquals("Foo Bar Baz", SoundexUtils.clean("Foo Bar Baz"));
        assertEquals("", SoundexUtils.clean(""));
        assertNull(SoundexUtils.clean(null));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = org.apache.commons.codec.net.URLCodec.INSTANCE;
        assertEquals(3, SoundexUtils.difference(encoder, "Foo", "Foo Bar"));
        assertEquals(4, SoundexUtils.difference(encoder, "Foo", "Foo Foobar"));
        assertEquals(2, SoundexUtils.difference(encoder, "Foo", "Foo1"));
        assertEquals(2, SoundexUtils.difference(encoder, "Foo", "Foo, Bar"));
        assertEquals(1, SoundexUtils.difference(encoder, "Foo", "Foo"));
        assertEquals(0, SoundexUtils.difference(encoder, "Foo", "1"));
        assertEquals(0, SoundexUtils.difference(encoder, "Foo", null));
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "F1234";
        String es2 = "F4123";
        assertEquals(3, SoundexUtils.differenceEncoded(es1, es2));
        String es3 = "F234";
        assertEquals(3, SoundexUtils.differenceEncoded(es1, es3));
        String es4 = "F000";
        assertEquals(0, SoundexUtils.differenceEncoded(es1, es4));
    }
}