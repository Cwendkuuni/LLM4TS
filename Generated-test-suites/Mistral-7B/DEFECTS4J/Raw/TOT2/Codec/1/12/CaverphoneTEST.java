package org.junit.test;

import org.apache.commons.codec.language.Caverphone;
import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphoneWithEmptyString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("");
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphoneWithNullString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone(null);
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphoneWithLowerCaseString() {
        Caverphone caverphone = new Caverphone();
        String txt = "hello";
        String result = caverphone.caverphone(txt);
        String expected = "h3l3l3o";
        assertEquals(expected, result);
    }

    @Test
    public void testEncodeWithString() {
        Caverphone caverphone = new Caverphone();
        String txt = "test";
        String result = (String) caverphone.encode(txt);
        String expected = caverphone.caverphone(txt);
        assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeWithNonString() {
        Caverphone caverphone = new Caverphone();
        Integer num = 123;
        caverphone.encode(num);
    }

    @Test
    public void testEncodeWithLowerCaseString() {
        Caverphone caverphone = new Caverphone();
        String txt = "hello";
        String result = caverphone.encode(txt);
        String expected = caverphone.caverphone(txt);
        assertEquals(expected, result);
    }

    @Test
    public void testIsCaverphoneEqualWithSameStrings() {
        Caverphone caverphone = new Caverphone();
        String str1 = "test";
        String str2 = "test";
        boolean result = caverphone.isCaverphoneEqual(str1, str2);
        assertTrue(result);
    }

    @Test
    public void testIsCaverphoneEqualWithDifferentStrings() {
        Caverphone caverphone = new Caverphone();
        String str1 = "test";
        String str2 = "hello";
        boolean result = caverphone.isCaverphoneEqual(str1, str2);
        assertFalse(result);
    }
}

//