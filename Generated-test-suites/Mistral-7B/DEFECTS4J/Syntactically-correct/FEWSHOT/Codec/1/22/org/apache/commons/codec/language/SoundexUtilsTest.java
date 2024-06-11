package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("", SoundexUtils.clean(null));
        assertEquals("", SoundexUtils.clean(""));
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", SoundexUtils.clean("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertEquals("abcdefghijklmnopqrstuvwxyz", SoundexUtils.clean("abcdefghijklmnopqrstuvwxyz"));
        assertEquals("AbCdEfGhIjKlMnOpQrStUvWxYz", SoundexUtils.clean("AbCdEfGhIjKlMnOpQrStUvWxYz"));
        assertEquals("abcd efgh ijkl mnop qrst uvwxyz", SoundexUtils.clean("abcd efgh ijkl mnop qrst uvwxyz"));
        assertEquals("A B C D E F G H I J K L M N O P Q R S T U V W X Y Z", SoundexUtils.clean("A B C D E F G H I J K L M N O P Q R S T U V W X Y Z"));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        assertEquals(0, SoundexUtils.difference(encoder, null, null));
        assertEquals(0, SoundexUtils.difference(encoder, null, ""));
        assertEquals(0, SoundexUtils.difference(encoder, "", ""));
        assertEquals(0, SoundexUtils.difference(encoder, "", null));
        assertEquals(0, SoundexUtils.difference(encoder, "", "foo"));
        assertEquals(0, SoundexUtils.difference(encoder, "foo", ""));
        assertEquals(4, SoundexUtils.difference(encoder, "FOO", "BARFOO"));
        assertEquals(4, SoundexUtils.difference(encoder, "BARFOO", "FOO"));
        assertEquals(3, SoundexUtils.difference(encoder, "FOO", "FOOBAR"));
        assertEquals(3, SoundexUtils.difference(encoder, "FOOBAR", "FOO"));
        assertEquals(3, SoundexUtils.difference(encoder, "FOO", "FOOBA"));
        assertEquals(3, SoundexUtils.difference(encoder, "FOOBA", "FOO"));
        assertEquals(3, SoundexUtils.difference(encoder, "FOO", "FOOBARBA"));
        assertEquals(3, SoundexUtils.difference(encoder, "FOOBARBA", "FOO"));
        String s1 = "apple";
        String s2 = "apples";
        String s3 = "applesauce";
        String s4 = "Apricot";
        String s5 = "Apricotts";
        assertEquals(3, SoundexUtils.difference(encoder, s1, s2));
        assertEquals(3, SoundexUtils.difference(encoder, s2, s1));
        assertEquals(2, SoundexUtils.difference(encoder, s1, s3));
        assertEquals(2, SoundexUtils.difference(encoder, s3, s1));
        assertEquals(2, SoundexUtils.difference(encoder, s1, s4));
        assertEquals(2, SoundexUtils.difference(encoder, s4, s1));
        assertEquals(4, SoundexUtils.difference(encoder, s4, s5));
        assertEquals(4, SoundexUtils.difference(encoder, s5, s4));
        assertEquals(4, SoundexUtils.difference(encoder, s4, "Apricott"));
        assertEquals(4, SoundexUtils.difference(encoder, "Apricott", s4));
        assertEquals(3, SoundexUtils.difference(encoder, s1, "Apricot"));
        assertEquals(3, SoundexUtils.difference(encoder, "Apricot", s1));
        String s6 = "Orange";
        String s7 = "Oranges";
        String s8 = "OrangeJuice";
        String s9 = "Orangutang";
        assertEquals(3, SoundexUtils.difference(encoder, s6, s7));
        assertEquals(3, SoundexUtils.difference(encoder, s7, s6));
        assertEquals(2, SoundexUtils.difference(encoder, s6, s8));
        assertEquals(2, SoundexUtils.difference(encoder, s8, s6));
        assertEquals(3, SoundexUtils.difference(encoder, s6, s9));
        assertEquals(3, SoundexUtils.difference(encoder, s9, s6));
        assertEquals(4, SoundexUtils.difference(encoder, s9, "Orangutan"));
        assertEquals(4, SoundexUtils.difference(encoder, "Orangutan", s9));
        assertEquals(3, SoundexUtils.difference(encoder, s6, "Orangutan"));
        assertEquals(3, SoundexUtils.difference(encoder, "Orangutan", s6));
    }

    @Test
    public void testDifferenceEncoded() {
        String s1 = "apple";
        String s2 = "apples";
        String s3 = "applesauce";
        String s4 = "Apricot";
        String s5 = "Apricotts";
        String es1 = new org.apache.commons.codec.language.Soundex().encode(s1);
        String es2 = new org.apache.commons.codec.language.Soundex().encode(s2);
        String es3 = new org.apache.commons.codec.language.Soundex().encode(s3);
        String es4 = new org.apache.commons.codec.language.Soundex().encode(s4);
        String es5 = new org.apache.commons.codec.language.Soundex().encode(s5);
        assertEquals(3, SoundexUtils.differenceEncoded(es1, es2));
        assertEquals(3, SoundexUtils.differenceEncoded(es2, es1));
        assertEquals(2, SoundexUtils.differenceEncoded(es1, es3));
        assertEquals(2, SoundexUtils.differenceEncoded(es3, es1));
        assertEquals(2, SoundexUtils.differenceEncoded(es1, es4));
        assertEquals(2, SoundexUtils.differenceEncoded(es4, es1));
        assertEquals(4, SoundexUtils.differenceEncoded(es4, es5));
        assertEquals(4, SoundexUtils.differenceEncoded(es5, es4));
        assertEquals(4, SoundexUtils.differenceEncoded(es4, "Apricott"));
        assertEquals(4, SoundexUtils.differenceEncoded("Apricott", es4));
        assertEquals(3, SoundexUtils.differenceEncoded(es1, "Apricot"));
        assertEquals(3, SoundexUtils.differenceEncoded("Apricot", es1));
        String es6 = new org.apache.commons.codec.language.Soundex().encode(s6);
        String es7 = new org.apache.commons.codec.language.Soundex().encode(s7);
        String es8 = new org.apache.commons.codec.language.Soundex().encode(s8);
        String es9 = new org.apache.commons.codec.language.Soundex().encode(s9);
        assertEquals(3, SoundexUtils.differenceEncoded(es6, es7));
        assertEquals(3, SoundexUtils.differenceEncoded(es7, es6));
        assertEquals(2, SoundexUtils.differenceEncoded(es6, es8));
        assertEquals(2, SoundexUtils.differenceEncoded(es8, es6));
        assertEquals(3, SoundexUtils.differenceEncoded(es6, es9));
        assertEquals(3, SoundexUtils.differenceEncoded(es9, es6));
        assertEquals(4, SoundexUtils.differenceEncoded(es9, "Orangutan"));
        assertEquals(4, SoundexUtils.differenceEncoded("Orangutan", es9));
        assertEquals(3, SoundexUtils.differenceEncoded(es6, "Orangutan"));
        assertEquals(3, SoundexUtils.differenceEncoded("Orangutan", es6));
    }
}