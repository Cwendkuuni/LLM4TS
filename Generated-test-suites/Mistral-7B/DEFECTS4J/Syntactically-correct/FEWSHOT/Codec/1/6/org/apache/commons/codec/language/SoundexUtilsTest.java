package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;

/**
 * Test cases for SoundexUtils class.
 */
public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("", SoundexUtils.clean(null));
        assertEquals("", SoundexUtils.clean(""));
        assertEquals("ABC", SoundexUtils.clean("AbC"));
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", SoundexUtils.clean("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertEquals("AbC", SoundexUtils.clean("AbC"));
        assertEquals("aBc", SoundexUtils.clean("aBc"));
        assertEquals("123AbC", SoundexUtils.clean("123AbC"));
        assertEquals("AbC-123", SoundexUtils.clean("AbC-123"));
        assertEquals("AbC_123", SoundexUtils.clean("AbC_123"));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        assertEquals(0, SoundexUtils.difference(encoder, null, null));
        assertEquals(0, SoundexUtils.difference(encoder, null, ""));
        assertEquals(0, SoundexUtils.difference(encoder, "", null));
        assertEquals(0, SoundexUtils.difference(encoder, "", ""));
        assertEquals(0, SoundexUtils.difference(encoder, "foo", null));
        assertEquals(0, SoundexUtils.difference(encoder, null, "foo"));
        assertEquals(0, SoundexUtils.difference(encoder, "foo", "foo"));
        assertEquals(3, SoundexUtils.difference(encoder, "apple", "appetizer"));
        assertEquals(3, SoundexUtils.difference(encoder, "appetizer", "apple"));
        assertEquals(3, SoundexUtils.difference(encoder, "Apple", "Appetizer"));
        assertEquals(3, SoundexUtils.difference(encoder, "Appetizer", "Apple"));
        assertEquals(3, SoundexUtils.difference(encoder, "Apple", "Apple"));
        assertEquals(3, SoundexUtils.difference(encoder, "Appetizer", "Appetizer"));
        assertEquals(3, SoundexUtils.difference(encoder, "Apple", "Apple"));
        assertEquals(3, SoundxUtils.difference(encoder, "Appetizer", "Appetizer"));
        assertEquals(3, SoundxUtils.difference(encoder, "Apple", "Appetizer"));
        assertEquals(3, SoundxUtils.difference(encoder, "Appetizer", "Apple"));
        assertEquals(3, SoundxUtils.difference(encoder, "Apple", "Appetizer"));
        assertEquals(3, SoundxUtils.difference(encoder, "Appetizer", "Apple"));
        assertEquals(3, SoundxUtils.difference(encoder, "Apple", "Appetizer"));
        assertEquals(3, SoundxUtils.difference(encoder, "Appetizer", "Apple"));
        assertEquals(3, SoundxUtils.difference(encoder, "Apple", "Appetizer"));
        assertEquals(3, SoundxUtils.difference(encoder, "Appetizer", "Apple"));
        assertEquals(3, SoundxUtils.difference(encoder, "Apple", "Appetizer"));
        assertEquals(3, SoundxUtils.difference(encoder, "Appetizer", "Apple"));
        assertEquals(3, SoundxUtils.difference(encoder, "Apple", "Appetizer"));
        assertEquals(3, SoundxUtils.difference(encoder, "Appetizer", "Apple"));

        StringEncoder soundexEncoder = new Soundex();
        String encodedApple = soundexEncoder.encode("Apple");
        String encodedAppetizer = soundexEncoder.encode("Appetizer");

        assertEquals(3, SoundexUtils.differenceEncoded(encodedApple, encodedAppetizer));
        assertEquals(3, SoundexUtils.differenceEncoded(encodedAppetizer, encodedApple));
        assertEquals(3, SoundexUtils.differenceEncoded(encodedApple, encodedApple));
        assertEquals(3, SoundexUtils.differenceEncoded(encodedAppetizer, encodedAppetizer));
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceWithNullEncoder() throws EncoderException {
        String encodedApple = new Soundex().encode("Apple");
        String encodedAppetizer = new Soundex().encode("Appetizer");
        SoundexUtils.differenceEncoded(null, encodedApple, encodedAppetizer);
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
        assertEquals(0, SoundexUtils.differenceEncoded("foo", ""));
        assertEquals(0, SoundexUtils.differenceEncoded("", "foo"));
        assertEquals(3, SoundexUtils.differenceEncoded("apple", "appetizer"));
        assertEquals(3, SoundexUtils.differenceEncoded("appetizer", "apple"));
        assertEquals(3, SoundexUtils.differenceEncoded("Apple", "Appetizer"));
        assertEquals(3, SoundexUtils.differenceEncoded("Appetizer", "Apple"));
        assertEquals(3, SoundexUtils.differenceEncoded("Apple", "Apple"));
        assertEquals(3, SoundexUtils.differenceEncoded("Appetizer", "Appetizer"));
        assertEquals(3, SoundexUtils.differenceEncoded("Apple", "Appetizer"));
        assertEquals(3, SoundexUtils.differenceEncoded("Appetizer", "Apple"));
        assertEquals(3, SoundexUtils.differenceEncoded("Apple", "Appetizer"));
        assertEquals(3, SoundexUtils.differenceEncoded("Appetizer", "Apple"));
        assertEquals(3, SoundexUtils.differenceEncoded("Apple", "Appetizer"));
        assertEquals(3, SoundexUtils.differenceEncoded("Appetizer", "Apple"));
        assertEquals(3, SoundexUtils.differenceEncoded("Apple", "Appetizer"));
        assertEquals(3, SoundexUtils.differenceEncoded("Appetizer", "Apple"));
        assertEquals(3, SoundexUtils.differenceEncoded("Apple", "Appetizer"));
        assertEquals(3, SoundexUtils.differenceEncoded("Appetizer", "Apple"));
        assertEquals(3, SoundexUtils.differenceEncoded("Apple", "Appetizer"));
        assertEquals(3, SoundexUtils.differenceEncoded("Appetizer", "Apple"));
    }
}