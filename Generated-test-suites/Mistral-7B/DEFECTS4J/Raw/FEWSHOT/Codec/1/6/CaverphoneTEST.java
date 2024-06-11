package org.apache.commons.codec.language;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEmpty() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("");
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphoneNull() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone(null);
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphoneLowerCase() {
        Caverphone caverphone = new Caverphone();
        String input = "Hello, World!";
        String result = caverphone.caverphone(input);

        String expected = "H2l2o2 W2r2l2d2!";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneRemoveNonAlphabets() {
        Caverphone caverphone = new Caverphone();
        String input = "12Aple3Banana4Cherry5";
        String result = caverphone.caverphone(input);

        String expected = "ApLe BaNaNa ChErRy";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneRemoveFinalE() {
        Caverphone caverphone = new Caverphone();
        String input = "Apple";
        String result = caverphone.caverphone(input);

        String expected = "Apple";
        assertEquals(expected, result);

        input = "Apples";
        result = caverphone.caverphone(input);

        expected = "AppleS";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandleStartOptions() {
        Caverphone caverphone = new Caverphone();
        String input = "CoughPotato";
        String result = caverphone.caverphone(input);

        String expected = "cou2fPotato";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandleReplacements() {
        Caverphone caverphone = new Caverphone();
        String input = "CucumberTchin";
        String result = caverphone.caverphone(input);

        String expected = "Cukum2er T2ch2in";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandleRemovals() {
        Caverphone caverphone = new Caverphone();
        String input = "1232qwerty34uiop123";
        String result = caverphone.caverphone(input);

        String expected = "qwertyuiop";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandleRemovalsWithThreeCharacter() {
        Caverphone caverphone = new Caverphone();
        String input = "123abc3def4ghi123";
        String result = caverphone.caverphone(input);

        String expected = "abcdefghi";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandleRemovalsWithTwoCharacter() {
        Caverphone caverphone = new Caverphone();
        String input = "123ab3cd4ef5gh123";
        String result = caverphone.caverphone(input);

        String expected = "abcdefgh";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandleRemovalsWithOneCharacter() {
        Caverphone caverphone = new Caverphone();
        String input = "123a3b3c3d3e3f3g3h3i123";
        String result = caverphone.caverphone(input);

        String expected = "abcdefghi";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandleRemovalsWithNoCharacter() {
        Caverphone caverphone = new Caverphone();
        String input = "12333333333333333123";
        String result = caverphone.caverphone(input);

        String expected = "";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandleRemovalsWithEmptyString() {
        Caverphone caverphone = new Caverphone();
        String input = "123";
        String result = caverphone.caverphone(input);

        String expected = "";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandleRemovalsWithEmptyStringAtEnd() {
        Caverphone caverphone = new Caverphone();
        String input = "123";
        String result = caverphone.caverphone(input + "");

        String expected = "1111111111";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandleRemovalsWithEmptyStringAtBeginning() {
        Caverphone caverphone = new Caverphone();
        String input = "" + "123";
        String result = caverphone.caverphone(input);

        String expected = "1111111111";
        assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneNonStringParameter() {
        Caverphone caverphone = new Caverphone();
        int num = 123;
        caverphone.encode(num);
    }

    @Test
    public void testCaverphoneEncode() {
        Caverphone caverphone = new Caverphone();
        String input = "HelloWorld!";
        String result = caverphone.encode(input);

        String expected = "H2l2o2 W2r2l2d2!";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        String str1 = "Apple";
        String str2 = "Apple";
        boolean result = caverphone.isCaverphoneEqual(str1, str2);

        boolean expected = true;
        assertEquals(expected, result);

        str1 = "Apple";
        str2 = "AppleS";
        result = caverphone.isCaverphoneEqual(str1, str2);

        expected = false;
        assertEquals(expected, result);

        str1 = "Apple";
        str2 = "Orange";
        result = caverphone.isCaverphoneEqual(str1, str2);

        expected = false;
        assertEquals(expected, result);
    }
}