package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.DecoderResult;
import org.apache.commons.codec.StringDecoder;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("", SoundexUtils.clean(""));
        assertEquals("Foo", SoundexUtils.clean("foo"));
        assertEquals("BaR", SoundexUtils.clean("BAR"));
        assertEquals("Jones", SoundexUtils.clean("jones"));
        assertEquals("McDonald", SoundexUtils.clean("McDONALD"));
        assertEquals("Macdonald", SoundexUtils.clean("MACDONALD"));
        assertEquals("Miller", SoundexUtils.clean("MILLER"));
        assertEquals("macdonald", SoundexUtils.clean("macdonald"));
        assertEquals("miller", SoundexUtils.clean("miller"));
    }

    @Test(expected = NullPointerException.class)
    public void testCleanNullInput() {
        SoundexUtils.clean(null);
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new SoundexEncoder();
        int difference = SoundexUtils.difference(encoder, "foo", "bar");
        assertEquals(2, difference);

        difference = SoundexUtils.difference(encoder, "foo", "foo");
        assertEquals(4, difference);

        difference = SoundexUtils.difference(encoder, "foo", "jones");
        assertEquals(2, difference);

        difference = SoundexUtils.difference(encoder, "foo", "FOO");
        assertEquals(4, difference);

        difference = SoundexUtils.difference(encoder, "foo", "FOO");
        assertEquals(4, difference);
    }

    @Test
    public void testDifferenceEncoded() {
        String encodedFoo = new SoundexEncoder().encode("foo");
        String encodedBar = new SoundexEncoder().encode("bar");
        int difference = SoundexUtils.differenceEncoded(encodedFoo, encodedBar);
        assertEquals(2, difference);

        String encodedFooFoo = new SoundexEncoder().encode("foo");
        String encodedFooFoo = new SoundexEncoder().encode("foo");
        int differenceFooFoo = SoundexUtils.differenceEncoded(encodedFooFoo, encodedFooFoo);
        assertEquals(4, differenceFooFoo);

        String encodedJones = new SoundexEncoder().encode("jones");
        String encodedFoo = new SoundexEncoder().encode("foo");
        int differenceFooJones = SoundexUtils.differenceEncoded(encodedFoo, encodedJones);
        assertEquals(2, differenceFooJones);

        String encodedFoo = new SoundexEncoder().encode("foo");
        String encodedFoo = new SoundexEncoder().encode("foo");
        int differenceFooFoo = SoundexUtils.differenceEncoded(encodedFoo, encodedFoo);
        assertEquals(4, differenceFooFoo);

        String encodedFoo = new SoundexEncoder().encode("foo");
        String encodedFooUpper = new SoundexEncoder().encode("FOO");
        int differenceFooFooUpper = SoundexUtils.differenceEncoded(encodedFoo, encodedFooUpper);
        assertEquals(4, differenceFooFooUpper);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceEncodedWithNullInput() throws EncoderException {
        String encodedFoo = new SoundexEncoder().encode("foo");
        String encodedNull = null;
        SoundexUtils.differenceEncoded(encodedFoo, encodedNull);
    }

    @Test
    public void testDifferenceEncodedWithEmptyEncodedInputs() {
        String encodedEmpty = "";
        int difference = SoundexUtils.differenceEncoded(encodedEmpty, encodedEmpty);
        assertEquals(4, difference);
    }

    @Test
    public void testDifferenceEncodedWithDifferentLengths() {
        String encodedFoo = new SoundexEncoder().encode("foo");
        String encodedBar = new SoundexEncoder().encode("bar");
        String encodedJones = new SoundexEncoder().encode("jones");

        int differenceFooBar = SoundexUtils.differenceEncoded(encodedFoo, encodedBar);
        assertEquals(2, differenceFooBar);

        int differenceFooJones = SoundexUtils.differenceEncoded(encodedFoo, encodedJones);
        assertEquals(2, differenceFooJones);

        int differenceBarJones = SoundexUtils.differenceEncoded(encodedBar, encodedJones);
        assertEquals(3, differenceBarJones);
    }

    @Test
    public void testDecodeSoundexCode() throws DecoderException, EncoderException {
        StringEncoder encoder = new SoundexEncoder();
        String decoder = new SoundexDecoder();
        String encodedFoo = encoder.encode("foo");
        DecoderResult decodedFoo = decoder.decode(encodedFoo);
        assertEquals("foo", decodedFoo.getDecodedString());

        String encodedJones = encoder.encode("jones");
        DecoderResult decodedJones = decoder.decode(encodedJones);
        assertEquals("jones", decodedJones.getDecodedString());

        String encodedNull = null;
        try {
            decoder.decode(encodedNull);
            assertNull(false);
        } catch (DecoderException e) {
            //Expected
        }
    }
}