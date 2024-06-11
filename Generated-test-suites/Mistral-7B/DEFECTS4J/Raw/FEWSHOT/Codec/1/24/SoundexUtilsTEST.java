package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("FOO", SoundexUtils.clean("foo"));
        assertEquals("BAR", SoundexUtils.clean("bar"));
        assertEquals("THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG", SoundexUtils.clean("The quick brown fox jumps over the lazy dog"));
        assertNull(SoundexUtils.clean(null));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Hex();
        assertEquals(0, SoundexUtils.difference(encoder, "foo", "bar"));
        assertEquals(2, SoundexUtils.difference(encoder, "foo", "foo"));
        assertEquals(4, SoundexUtils.difference(encoder, "foo", "food"));
        assertEquals(4, SoundexUtils.difference(encoder, "food", "foo"));
        assertEquals(0, SoundexUtils.difference(encoder, "foo", "zoo"));
        assertEquals(3, SoundexUtils.difference(encoder, "apple", "appetizer"));
        assertEquals(2, SoundexUtils.differenceEncoded("KJ3L1X1W1", "KJ3L1X1V1"));
        assertEquals(4, SoundexUtils.differenceEncoded("KJ3L1X1W1", "KJ3L1Z1W1"));
    }
}