package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEncodesEmptyString() {
        Caverphone caverphone = new Caverphone();
        String expected = "1111111111";
        String result = caverphone.caverphone("");
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneEncodesSingleCharacter() {
        Caverphone caverphone = new Caverphone();
        String expected = "2";
        String result = caverphone.caverphone("a");
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneEncodesMultipleCharacters() {
        Caverphone caverphone = new Caverphone();
        String text = "the quick brown fox jumps over the lazy dog";
        String expected = "t3h3 q3u3i3c3 k3i3k3 3r3o3f v3j3u3m3s 3o3v3e3r3 t3h3e3 l3a3z3y3 d3o3g";
        String result = caverphone.caverphone(text);
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesSpecialCases() {
        Caverphone caverphone = new Caverphone();
        String text = "caverphone";
        String expected = "c2v3r2p3h3o3n3";
        String result = caverphone.caverphone(text);
        assertEquals(expected, result);

        text = "Caverphone";
        expected = "C2V3R2P3H3O3N3";
        result = caverphone.caverphone(text);
        assertEquals(expected, result);

        text = "Caverphone!";
        expected = "C2V3R2P3H3O3N3!";
        result = caverphone.caverphone(text);
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesStartOptions() {
        Caverphone caverphone = new Caverphone();
        String text = "coughenough";
        String expected = "cou2f enou2f";
        String result = caverphone.caverphone(text);
        assertEquals(expected, result);

        text = "enough cough";
        expected = "enou2f cou2f";
        result = caverphone.caverphone(text);
        assertEquals(expected, result);

        text = "trough enough";
        expected = "trou2f enou2f"; // Note this is different from the 1.0 spec
        result = caverphone.caverphone(text);
        assertEquals(expected, result);

        text = "enough trough";
        expected = "enou2f trou2f"; // Note this is different from the 1.0 spec
        result = caverphone.caverphone(text);
        assertEquals(expected, result);

        text = "tough enough";
        expected = "tou2f enou2f";
        result = caverphone.caverphone(text);
        assertEquals(expected, result);

        text = "rough enough";
        expected = "rou2f enou2f";
        result = caverphone.caverphone(text);
        assertEquals(expected, result);

        text = "coughenough enough";
        expected = "cou2f enou2f enou2f";
        result = caverphone.caverphone(text);
        assertEquals(expected, result);

        text = "enough coughenough";
        expected = "enou2f cou2f cou2f";
        result = caverphone.caverphone(text);
        assertEquals(expected, result);

        text = "enough enough coughenough";
        expected = "enou2f enou2f cou2f";
        result = caverphone.caverphone(text);
        assertEquals(expected, result);

        text = "coughenough enough enough";
        expected = "cou2f enou2f enou2f";
        result = caverphone.caverphone(text);
        assertEquals(expected, result);

        text = "enough enough coughenough";
        expected = "enou2f enou2f cou2f";
        result = caverphone.caverphone(text);
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesReplacements() {
        Caverphone caverphone = new Caverphone();
        String text = "this is a test";
        String expected = "th3s3 i3 s3 a3 t3st";
        String result = caverphone.caverphone(text);
        assertEquals(expected, result);

        text = "this is a test with j and y";
        expected = "th3s3 i3 s3 a3 t3st w3 j3 y3";
        result = caverphone.caverphone(text);
        assertEquals(expected, result);

        text = "this is a test with j, y, y, and j";
        expected = "th3s3 i3 s3 a3 t3st w3 j3 y3 y3 j3";
        result = caverphone.caverphone(text);
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesRemovals() {
        Caverphone caverphone = new Caverphone();
        String text = "this is a test";
        String expected = "th3s";
        String result = caverphone.caverphone(text).substring(0, 10);
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesShorterStrings() {
        Caverphone caverphone = new Caverphone();
        String text = "test";
        String expected = "t3st";
        String result = caverphone.caverphone(text).substring(0, 10);
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesLongerStrings() {
        Caverphone caverphone = new Caverphone();
        String text = "the quick brown fox jumps over the lazy dog";
        String expected = "t3h3 q3u3i3c3 k3i3k3 3r3o3f v3j3u3m3s 3o3v3e3r3 t3h3e3 l3a3z3y3 d3o3g";
        String result = caverphone.caverphone(text).substring(0, 10);
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesRepeatedCharacters() {
        Caverphone caverphone = new Caverphone();
        String text = "aaa";
        String expected = "3";
        String result = caverphone.caverphone(text).substring(0, 10);
        assertEquals(expected, result);

        text = "aaaaaa";
        expected = "33333";
        result = caverphone.caverphone(text).substring(0, 10);
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesSameStringTwice() {
        Caverphone caverphone = new Caverphone();
        String text = "the quick brown fox jumps over the lazy dog";
        String result1 = caverphone.caverphone(text);
        String result2 = caverphone.caverphone(text);
        assertEquals(result1, result2);
    }

    @Test
    public void testCaverphoneHandlesDifferentStrings() {
        Caverphone caverphone = new Caverphone();
        String text1 = "the quick brown fox jumps over the lazy dog";
        String text2 = "the quick red fox jumps over the sleepy cat";
        String result1 = caverphone.caverphone(text1);
        String result2 = caverphone.caverphone(text2);
        assertNotEquals(result1, result2);
    }
}