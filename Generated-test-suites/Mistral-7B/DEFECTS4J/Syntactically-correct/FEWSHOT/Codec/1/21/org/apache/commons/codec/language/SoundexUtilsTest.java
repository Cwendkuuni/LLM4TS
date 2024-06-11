package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertNull;
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
        assertEquals("FOO", SoundexUtils.clean("foo"));
        assertEquals("FOO BAR", SoundexUtils.clean("foo bar"));
        assertEquals("FOO BA R", SoundexUtils.clean("foo ba r"));
        assertEquals("FOO BA R MILLER", SoundexUtils.clean("foo ba r miller"));
        assertEquals("FOO BA R MILLER JONES", SoundexUtils.clean("foo ba r miller jones"));
        assertEquals("FOO BA R MILLER JONES DOE", SoundexUtils.clean("foo ba r miller jones doe"));
        assertEquals("FOO BA R MILLER JONES S MITH", SoundexUtils.clean("foo ba r miller jones s mitch"));
        assertEquals("FOO BA R MILLER JONES S MITH JOHN", SoundexUtils.clean("foo ba r miller jones s mitch john"));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        assertEquals(0, SoundexUtils.difference(encoder, null, null));
        assertEquals(0, SoundexUtils.difference(encoder, null, ""));
        assertEquals(0, SoundexUtils.difference(encoder, "", null));
        assertEquals(0, SoundexUtils.difference(encoder, "", ""));

        String s1 = "apple";
        String s2 = "apple";
        assertEquals(4, SoundexUtils.difference(encoder, s1, s2));

        s1 = "Apple";
        s2 = "apple";
        assertEquals(3, SoundexUtils.difference(encoder, s1, s2));

        s1 = "Apple";
        s2 = "Apple";
        assertEquals(4, SoundexUtils.difference(encoder, s1, s2));

        s1 = "Apple";
        s2 = "orange";
        assertEquals(0, SoundexUtils.difference(encoder, s1, s2));

        s1 = "Apple";
        s2 = "pineapple";
        assertEquals(3, SoundexUtils.difference(encoder, s1, s2));

        s1 = "Apple";
        s2 = "pineapples";
        assertEquals(3, SoundexUtils.difference(encoder, s1, s2));

        s1 = "Apple";
        s2 = "pineapples";
        assertEquals(3, SoundexUtils.difference(encoder, s1, s2));

        s1 = "Apple";
        s2 = "Pineapple";
        assertEquals(3, SoundexUtils.difference(encoder, s1, s2));

        s1 = "Apple";
        s2 = "Pineapples";
        assertEquals(3, SoundexUtils.difference(encoder, s1, s2));

        s1 = "Apple";
        s2 = "Pineapples";
        assertEquals(3, SoundexUtils.difference(encoder, s1, s2));

        String s3 = "abcdefghijklmnopqrstuvwxyz";
        String s4 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        assertEquals(3, SoundexUtils.difference(encoder, s3, s4));

        String s5 = "Apple";
        String s6 = "Apple";
        String s7 = SoundexUtils.clean(s5);
        String s8 = SoundexUtils.clean(s6);
        assertEquals(4, SoundexUtils.difference(encoder, s7, s8));

        String s9 = "Apple";
        String s10 = "Apple";
        String s11 = SoundexUtils.clean(s9);
        String s12 = SoundexUtils.clean(s10);
        String es1 = encoder.encode(s11);
        String es2 = encoder.encode(s12);
        assertEquals(4, SoundexUtils.differenceEncoded(es1, es2));

        String s13 = "Apple";
        String s14 = "Apple";
        String s15 = SoundexUtils.clean(s13);
        String s16 = SoundexUtils.clean(s14);
        String es3 = encoder.encode(s15);
        String es4 = encoder.encode(s16);
        assertEquals(4, SoundexUtils.differenceEncoded(es3, es4));

        String s17 = "Apple";
        String s18 = "Apple";
        String s19 = SoundexUtils.clean(s17);
        String s20 = SoundexUtils.clean(s18);
        assertEquals(4, SoundexUtils.difference(encoder, s19, s20));

        String s21 = "Apple";
        String s22 = "apple";
        String s23 = SoundexUtils.clean(s21);
        String s24 = SoundexUtils.clean(s22);
        assertEquals(3, SoundexUtils.difference(encoder, s23, s24));

        String s25 = "Apple";
        String s26 = "Apple";
        String s27 = SoundexUtils.clean(s25);
        String s28 = SoundexUtils.clean(s26);
        assertEquals(4, SoundexUtils.difference(encoder, s27, s28));

        String s29 = "Apple";
        String s30 = "APPLE";
        String s31 = SoundexUtils.clean(s29);
        String s32 = SoundexUtils.clean(s30);
        assertEquals(3, SoundexUtils.difference(encoder, s31, s32));

        String s33 = "Apple";
        String s34 = "Apple";
        String s35 = SoundexUtils.clean(s33);
        String s36 = SoundexUtils.clean(s34);
        String es5 = encoder.encode(s35);
        String es6 = encoder.encode(s36);
        assertEquals(4, SoundexUtils.differenceEncoded(es5, es6));

        String s37 = "Apple";
        String s38 = "Apple";
        String s39 = SoundexUtils.clean(s37);
        String s40 = SoundexUtils.clean(s38);
        String es7 = encoder.encode(s39);
        String es8 = encoder.encode(s40);
        assertEquals(4, SoundexUtils.differenceEncoded(es7, es8));
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = null;
        String es2 = null;
        assertEquals(0, SoundexUtils.differenceEncoded(es1, es2));

        es1 = "";
        es2 = "";
        assertEquals(0, SoundexUtils.differenceEncoded(es1, es2));

        es1 = "";
        es2 = "Apple";
        assertEquals(0, SoundexUtils.differenceEncoded(es1, es2));

        es1 = "Apple";
        es2 = "";
        assertEquals(0, SoundexUtils.differenceEncoded(es1, es2));

        es1 = "Apple";
        es2 = "Apple";
        assertEquals(4, SoundexUtils.differenceEncoded(es1, es2));

        es1 = "Apple";
        es2 = "orange";
        assertEquals(0, SoundexUtils.differenceEncoded(es1, es2));

        es1 = "Apple";
        es2 = "pineapple";
        assertEquals(3, SoundexUtils.differenceEncoded(es1, es2));

        es1 = "Apple";
        es2 = "pineapples";
        assertEquals(3, SoundexUtils.differenceEncoded(es1, es2));

        es1 = "Apple";
        es2 = "Pineapple";
        assertEquals(3, SoundexUtils.differenceEncoded(es1, es2));

        es1 = "Apple";
        es2 = "Pineapples";
        assertEquals(3, SoundexUtils.differenceEncoded(es1, es2));

        es1 = "Apple";
        es2 = "Pineapples";
        assertEquals(3, SoundexUtils.differenceEncoded(es1, es2));

        String es3 = "p1p1p1";
        String es4 = "p1p1p1";
        assertEquals(4, SoundexUtils.differenceEncoded(es3, es4));

        es1 = "Apple";
        es2 = "Apple";
        String s1 = SoundexUtils.clean(es1);
        String s2 = SoundexUtils.clean(es2);
        String es5 = encoder.encode(s1);
        String es6 = encoder.encode(s2);
        assertEquals(4, SoundexUtils.differenceEncoded(es5, es6));

        String es7 = "Apple";
        String es8 = "Apple";
        String s3 = SoundexUtils.clean(es7);
        String s4 = SoundexUtils.clean(es8);
        assertEquals(4, SoundexUtils.differenceEncoded(encoder.encode(s3), encoder.encode(s4)));

        String es9 = "Apple";
        String es10 = "Apple";
        String s5 = SoundexUtils.clean(es9);
        String s6 = SoundexUtils.clean(es10);
        assertEquals(4, SoundexUtils.differenceEncoded(encoder.encode(s5), encoder.encode(s6)));

        String es11 = "Apple";
        String es12 = "Apple";
        String s7 = SoundexUtils.clean(es11);
        String s8 = SoundexUtils.clean(es12);
        assertEquals(4, SoundexUtils.differenceEncoded(encoder.encode(s7), encoder.encode(s8)));

        String es13 = "Apple";
        String es14 = "apple";
        String s9 = SoundexUtils.clean(es13);
        String s10 = SoundexUtils.clean(es14);
        assertEquals(3, SoundexUtils.differenceEncoded(encoder.encode(s9), encoder.encode(s10)));

        String es15 = "Apple";
        String es16 = "Apple";
        String s11 = SoundexUtils.clean(es15);
        String s12 = SoundexUtils.clean(es16);
        assertEquals(4, SoundexUtils.differenceEncoded(encoder.encode(s11), encoder.encode(s12)));

        String es17 = "Apple";
        String es18 = "Apple";
        String s13 = SoundexUtils.clean(es17);
        String s14 = SoundexUtils.clean(es18);
        assertEquals(4, SoundexUtils.differenceEncoded(encoder.encode(s13), encoder.encode(s14)));

        String es19 = "Apple";
        String es20 = "APPLE";
        String s15 = SoundexUtils.clean(es19);
        String s16 = SoundexUtils.clean(es20);
        assertEquals(3, SoundexUtils.differenceEncoded(encoder.encode(s15), encoder.encode(s16)));

        String es21 = "Apple";
        String es22 = "Apple";
        String s17 = SoundexUtils.clean(es21);
        String s18 = SoundexUtils.clean(es22);
        assertEquals(4, SoundexUtils.differenceEncoded(encoder.encode(s17), encoder.encode(s18)));

        String es23 = "Apple";
        String es24 = "Apple";
        String s19 = SoundexUtils.clean(es23);
        String s20 = SoundexUtils.clean(es24);
        assertEquals(4, SoundexUtils.differenceEncoded(encoder.encode(s19), encoder.encode(s20)));

        String es25 = "Apple";
        String es26 = "Apple";
        String s21 = SoundexUtils.clean(es25);
        String s22 = SoundexUtils.clean(es26);
        assertEquals(4, SoundexUtils.differenceEncoded(encoder.encode(s21), encoder.encode(s22)));
    }

}