package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.DecoderResult;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", SoundexUtils.clean("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertEquals("abcedfghijklmopqrstuvwxyz", SoundexUtils.clean("abcedfghijklmopqrstuvwxyz"));
        assertEquals("AbCdEfGhIjKlMnOpQrStUvWxYz", SoundexUtils.clean("AbCdEfGhIjKlMnOpQrStUvWxYz"));
        assertEquals("", SoundexUtils.clean(""));
        assertEquals("12345", SoundexUtils.clean("12345"));
        assertEquals("A b c", SoundexUtils.clean("A b c"));
        assertEquals("A b c d", SoundexUtils.clean("A b c d"));
        assertEquals("A1 b2 c3 d4", SoundexUtils.clean("A1 b2 c3 d4"));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "Apple";
        String s2 = "Orange";
        int diff1 = SoundexUtils.difference(encoder, s1, s1);
        int diff2 = SoundexUtils.difference(encoder, s1, s2);
        int diff3 = SoundexUtils.difference(encoder, s2, s1);
        int diff4 = SoundexUtils.difference(encoder, s2, s2);

        assertEquals(4, diff1);
        assertEquals(0, diff2);
        assertEquals(0, diff3);
        assertEquals(4, diff4);

        s1 = "Alfred";
        s2 = "Alfonso";
        int diff5 = SoundexUtils.difference(encoder, s1, s1);
        int diff6 = SoundexUtils.difference(encoder, s1, s2);
        int diff7 = SoundexUtils.difference(encoder, s2, s1);
        int diff8 = SoundexUtils.difference(encoder, s2, s2);

        assertEquals(4, diff5);
        assertEquals(3, diff6);
        assertEquals(3, diff7);
        assertEquals(4, diff8);

        s1 = "Algerian";
        s2 = "Algerian";
        int diff9 = SoundexUtils.difference(encoder, s1, s1);
        int diff10 = SoundexUtils.difference(encoder, s1, s2);

        assertEquals(4, diff9);
        assertEquals(4, diff10);

        String s3 = "AppleApple";
        String s4 = "OrangeApple";
        int diff11 = SoundexUtils.difference(encoder, s3, s3);
        int diff12 = SoundexUtils.difference(encoder, s3, s4);
        int diff13 = SoundexUtils.difference(encoder, s4, s3);
        int diff14 = SoundexUtils.difference(encoder, s4, s4);

        assertEquals(4, diff11);
        assertEquals(0, diff12);
        assertEquals(0, diff13);
        assertEquals(4, diff14);

        s1 = "Apple";
        s2 = "Banana";
        int diff15 = SoundexUtils.difference(encoder, s1, s1);
        int diff16 = SoundexUtils.difference(encoder, s1, s2);
        int diff17 = SoundexUtils.difference(encoder, s2, s1);
        int diff18 = SoundexUtils.difference(encoder, s2, s2);

        assertEquals(4, diff15);
        assertEquals(0, diff16);
        assertEquals(0, diff17);
        assertEquals(3, diff18);

        String s5 = "Algebra";
        String s6 = "Algebraic";
        int diff19 = SoundexUtils.difference(encoder, s5, s5);
        int diff20 = SoundexUtils.difference(encoder, s5, s6);
        int diff21 = SoundexUtils.difference(encoder, s6, s5);
        int diff22 = SoundexUtils.difference(encoder, s6, s6);

        assertEquals(4, diff19);
        assertEquals(3, diff20);
        assertEquals(3, diff21);
        assertEquals(4, diff22);

        String s7 = "Algorithms";
        String s8 = "Algorithms";
        int diff23 = SoundexUtils.difference(encoder, s7, s7);
        int diff24 = SoundexUtils.difference(encoder, s7, s8);

        assertEquals(4, diff23);
        assertEquals(4, diff24);

        String s9 = "Algorithms";
        String s10 = "DataProcessing";
        int diff25 = SoundexUtils.difference(encoder, s9, s9);
        int diff26 = SoundexUtils.difference(encoder, s9, s10);
        int diff27 = SoundexUtils.difference(encoder, s10, s9);
        int diff28 = SoundexUtils.difference(encoder, s10, s10);

        assertEquals(4, diff25);
        assertEquals(0, diff26);
        assertEquals(0, diff27);
        assertEquals(3, diff28);

        s1 = "Algorithms";
        s2 = "DataProcessing";
        int diff29 = SoundexUtils.difference(encoder, s1, s1);
        int diff30 = SoundexUtils.difference(encoder, s1, s2);
        int diff31 = SoundexUtils.difference(encoder, s2, s1);
        int diff32 = SoundexUtils.difference(encoder, s2, s2);

        assertEquals(4, diff29);
        assertEquals(0, diff30);
        assertEquals(0, diff31);
        assertEquals(3, diff32);

        String s11 = "Algorithms";
        String s12 = "DataProcessingAlgorithms";
        int diff33 = SoundexUtils.difference(encoder, s11, s11);
        int diff34 = SoundexUtils.difference(encoder, s11, s12);
        int diff35 = SoundexUtils.difference(encoder, s12, s11);
        int diff36 = SoundexUtils.difference(encoder, s12, s12);

        assertEquals(4, diff33);
        assertEquals(1, diff34);
        assertEquals(1, diff35);
        assertEquals(3, diff36);

        s1 = "Algorithms";
        s2 = "DataProcessingAlgorithms";
        int diff37 = SoundexUtils.difference(encoder, s1, s1);
        int diff38 = SoundexUtils.difference(encoder, s1, s2);
        int diff39 = SoundexUtils.difference(encoder, s2, s1);
        int diff40 = SoundexUtils.difference(encoder, s2, s2);

        assertEquals(4, diff37);
        assertEquals(1, diff38);
        assertEquals(1, diff39);
        assertEquals(3, diff40);

        String s13 = "Algorithms";
        String s14 = "DataProcessingAlgorithmsDataProcessing";
        int diff41 = SoundexUtils.difference(encoder, s13, s13);
        int diff42 = SoundexUtils.difference(encoder, s13, s14);
        int diff43 = SoundexUtils.difference(encoder, s14, s13);
        int diff44 = SoundexUtils.difference(encoder, s14, s14);

        assertEquals(4, diff41);
        assertEquals(1, diff42);
        assertEquals(1, diff43);
        assertEquals(3, diff44);

        s1 = "Algorithms";
        s2 = "DataProcessingAlgorithmsDataProcessing";
        int diff45 = SoundexUtils.difference(encoder, s1, s1);
        int diff46 = SoundexUtils.difference(encoder, s1, s2);
        int diff47 = SoundexUtils.difference(encoder, s2, s1);
        int diff48 = SoundexUtils.difference(encoder, s2, s2);

        assertEquals(4, diff45);
        assertEquals(1, diff46);
        assertEquals(1, diff47);
        assertEquals(3, diff48);
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = Soundex.encode("Apple");
        String es2 = Soundex.encode("Orange");
        String es3 = Soundex.encode("Alfred");
        String es4 = Soundex.encode("Alfonso");
        String es5 = Soundex.encode("Algerian");
        String es6 = Soundex.encode("Algebra");
        String es7 = Soundex.encode("Algorithms");
        String es8 = Soundex.encode("DataProcessing");
        String es9 = Soundex.encode("Algorithms");
        String es10 = Soundex.encode("DataProcessing");
        String es11 = Soundex.encode("Algorithms");
        String es12 = Soundex.encode("DataProcessingAlgorithms");
        String es13 = Soundex.encode("Algorithms");
        String es14 = Soundex.encode("DataProcessingAlgorithmsDataProcessing");

        assertEquals(4, SoundexUtils.differenceEncoded(es1, es1).length());
        assertEquals(0, SoundexUtils.differenceEncoded(es1, es2).length());
        assertEquals(0, SoundexUtils.differenceEncoded(es2, es1).length());
        assertEquals(4, SoundexUtils.differenceEncoded(es2, es2).length());

        assertEquals(3, SoundexUtils.differenceEncoded(es3, es3).length());
        assertEquals(3, SoundexUtils.differenceEncoded(es3, es4).length());
        assertEquals(3, SoundexUtils.differenceEncoded(es4, es3).length());
        assertEquals(4, SoundexUtils.differenceEncoded(es4, es4).length());

        assertEquals(4, SoundexUtils.differenceEncoded(es5, es5).length());
        assertEquals(3, SoundexUtils.differenceEncoded(es5, es6).length());
        assertEquals(3, SoundexUtils.differenceEncoded(es6, es5).length());
        assertEquals(4, SoundexUtils.differenceEncoded(es6, es6).length());

        assertEquals(4, SoundexUtils.differenceEncoded(es7, es7).length());
        assertEquals(4, SoundexUtils.differenceEncoded(es7, es8).length());
        assertEquals(4, SoundexUtils.differenceEncoded(es8, es7).length());
        assertEquals(3, SoundexUtils.differenceEncoded(es8, es8).length());

        assertEquals(4, SoundexUtils.differenceEncoded(es9, es9).length());
        assertEquals(0, SoundexUtils.differenceEncoded(es9, es10).length());
        assertEquals(0, SoundexUtils.differenceEncoded(es10, es9).length());
        assertEquals(3, SoundexUtils.differenceEncoded(es10, es10).length());

        assertEquals(4, SoundexUtils.differenceEncoded(es11, es11).length());
        assertEquals(1, SoundexUtils.differenceEncoded(es11, es12).length());
        assertEquals(1, SoundexUtils.differenceEncoded(es12, es11).length());
        assertEquals(3, SoundexUtils.differenceEncoded(es12, es12).length());

        assertEquals(4, SoundexUtils.differenceEncoded(es13, es13).length());
        assertEquals(1, SoundexUtils.differenceEncoded(es13, es14).length());
        assertEquals(1, SoundexUtils.differenceEncoded(es14, es13).length());
        assertEquals(3, SoundexUtils.differenceEncoded(es14, es14).length());
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceEncodedWithNull() throws EncoderException {
        String es1 = null;
        String es2 = Soundex.encode("Orange");
        SoundexUtils.differenceEncoded(es1, es2);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceEncodedWithNull2() throws EncoderException {
        String es1 = Soundex.encode("Algorithms");
        String es2 = null;
        SoundexUtils.differenceEncoded(es1, es2);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceEncodedWithNull3() throws EncoderException {
        String es1 = null;
        String es2 = null;
        StringEncoder encoder = new Soundex();
        SoundexUtils.differenceEncoded(es1, es2, encoder);
    }

    @Test
    public void testDifferenceEncodedWithRefinedSoundex() throws EncoderException, DecoderException {
        String encoderName = "RefinedSoundex";
        StringEncoder encoder = new RefinedSoundex();
        String s1 = "Algorithms";
        String s2 = "DataProcessingAlgorithms";
        String es1 = encoder.encode(s1);
        String es2 = encoder.encode(s2);
        String ds1 = new StringDecoder().decode(es1, encoder, null, null);
        String ds2 = new StringDecoder().decode(es2, encoder, null, null);
        int diff = SoundexUtils.differenceEncoded(es1, es2);
        int expected = ds1.length() < ds2.length() ? ds1.length() : ds2.length();
        for (int i = 0; i < Math.min(ds1.length(), ds2.length()); i++) {
            if (ds1.charAt(i) != ds2.charAt(i)) {
                diff--;
                break;
            }
        }
        assertEquals(expected, diff);
    }

}