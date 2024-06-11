
package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class SoundexUtilsTest {

    public class DummyEncoder implements StringEncoder {

        @Override
        public String encode(String arg0) throws EncoderException {
            // Dummy implementation for testing
            return arg0;
        }
    }

    @Test
    public void testClean() {
        // Test empty string
        assertEquals("", SoundexUtils.clean(""));

        // Test null
        assertNull(SoundexUtils.clean(null));

        // Test lowercase string
        assertEquals("ABC", SoundexUtils.clean("abc"));

        // Test string with non-letter characters
        assertEquals("ABC", SoundexUtils.clean("AB1C"));

        // Test string with non-letter characters and uppercase
        assertEquals("ABC", SoundexUtils.clean("A!!B1C"));

        // Test string with spaces
        assertEquals("ABC", SoundexUtils.clean("A B C"));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new DummyEncoder();

        // Test equal encoded strings
        assertEquals(4, SoundexUtils.difference(encoder, "ABCD", "ABCD"));

        // Test different encoded strings
        assertEquals(1, SoundexUtils.difference(encoder, "ABCD", "ACDE"));

        // Test one of the encoded strings is null
        assertEquals(0, SoundexUtils.difference(encoder, "ABCD", null));

        // Test both encoded strings are null
        assertEquals(0, SoundexUtils.difference(encoder, null, null));
    }

    @Test
    public void testDifferenceEncoded() {
        // Test equal encoded strings
        assertEquals(4, SoundexUtils.differenceEncoded("ABCD", "ABCD"));

        // Test different encoded strings
        assertEquals(2, SoundexUtils.differenceEncoded("ABCD", "ACDE"));

        // Test one of the encoded strings is null
        assertEquals(0, SoundexUtils.differenceEncoded("ABCD", null));

        // Test both encoded strings are null
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
    }
}
