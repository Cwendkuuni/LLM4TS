package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("", SoundexUtils.clean(null));
        assertEquals("", SoundexUtils.clean(""));
        assertEquals("ABC", SoundexUtils.clean("AbC"));
        assertEquals("ABC", SoundexUtils.clean("AbC "));
        assertEquals("ABC", SoundexUtils.clean("ABC"));
        assertEquals("ABCDEFG", SoundexUtils.clean("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertEquals("ABC", SoundexUtils.clean("ABCdEfG"));
        assertEquals("ABC", SoundexUtils.clean("ABC dEfG"));
        assertEquals("ABC", SoundexUtils.clean("ABC dEf G"));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = null; // You may initialize encoder here if needed

        assertEquals(0, SoundexUtils.difference(encoder, null, null));
        assertEquals(0, SoundexUtils.difference(encoder, "", ""));
        assertEquals(0, SoundexUtils.difference(encoder, null, ""));
        assertEquals(0, SoundexUtils.difference(encoder, "", null));

        String s1 = "apple";
        String s2 = "appel";
        assertEquals(3, SoundexUtils.difference(encoder, s1, s2));

        s1 = "apple";
        s2 = "apfel";
        assertEquals(3, SoundexUtils.difference(encoder, s1, s2));

        s1 = "apple";
        s2 = "aple";
        assertEquals(3, SoundexUtils.difference(encoder, s1, s2));

        s1 = "apple";
        s2 = "apples";
        assertEquals(3, SoundexUtils.difference(encoder, s1, s2));

        s1 = "apple";
        s2 = "aple";
        assertEquals(3, SoundxUtils.difference(encoder, s1, s2));

        s1 = "barney";
        s2 = "bernie";
        assertEquals(2, SoundexUtils.difference(encoder, s1, s2));

        s1 = "bob";
        s2 = "bobby";
        assertEquals(2, SoundexUtils.difference(encoder, s1, s2));

        s1 = "cat";
        s2 = "dog";
        assertEquals(0, SoundexUtils.difference(encoder, s1, s2));

        s1 = "alpha";
        s2 = "alpha";
        assertEquals(4, SoundexUtils.difference(encoder, s1, s2));

        s1 = "alpha";
        s2 = "beta";
        assertEquals(0, SoundexUtils.difference(encoder, s1, s2));

        s1 = "alpha";
        s2 = "gamma";
        assertEquals(0, SoundexUtils.difference(encoder, s1, s2));

        s1 = "alpha";
        s2 = "delta";
        assertEquals(0, SoundexUtils.difference(encoder, s1, s2));

        String s3 = "alpha";
        String s4 = "alpha";
        assertEquals(4, SoundexUtils.difference(encoder, s3, s4));

        s3 = "alpha";
        s4 = "alpha beta";
        assertEquals(0, SoundexUtils.difference(encoder, s3, s4));

        s3 = "alpha beta";
        s4 = "alpha";
        assertEquals(0, SoundexUtils.difference(encoder, s3, s4));

        s3 = "alpha beta";
        s4 = "alpha beta";
        assertEquals(4, SoundexUtils.difference(encoder, s3, s4));
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = null; // You may initialize es1 here if needed
        String es2 = null; // You may initialize es2 here if needed

        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
        assertEquals(0, SoundexUtils.differenceEncoded(null, ""));
        assertEquals(0, SoundexUtils.differenceEncoded("", null));

        String s1 = "apple";
        String s2 = "appel";
        String es1_expected = "1223";
        String es2_expected = "1223";
        assertEquals(3, SoundexUtils.differenceEncoded(es1_expected, es2_expected));

        s1 = "apple";
        s2 = "apfel";
        String es1_actual = SoundexUtils.encodeSoundex(s1);
        String es2_actual = SoundexUtils.encodeSoundex(s2);
        assertEquals(3, SoundexUtils.differenceEncoded(es1_actual, es2_actual));

        s1 = "apple";
        s2 = "aple";
        es1_expected = "1223";
        es2_expected = "1223";
        assertEquals(3, SoundexUtils.differenceEncoded(es1_expected, es2_expected));

        s1 = "apple";
        s2 = "apples";
        es1_expected = "1223";
        es2_expected = "12233";
        assertEquals(3, SoundexUtils.differenceEncoded(es1_expected, es2_expected));

        es1 = es1_actual;
        es2 = es2_actual;
        assertEquals(3, SoundexUtils.differenceEncoded(es1, es2));
    }
}