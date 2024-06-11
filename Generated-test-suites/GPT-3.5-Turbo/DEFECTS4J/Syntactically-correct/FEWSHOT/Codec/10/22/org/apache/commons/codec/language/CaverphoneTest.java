package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CaverphoneTest {
    @Test
    public void testEncode() {
        Caverphone encoder = new Caverphone();
        assertEquals("1111111111", encoder.encode(""));
        assertEquals("32111111", encoder.encode("abc"));
        assertEquals("3111", encoder.encode("def"));
        assertEquals("AZ128", encoder.encode("ghi"));
        assertEquals("AZ228", encoder.encode("jkl"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone encoder = new Caverphone();
        assertEquals(true, encoder.isCaverphoneEqual("", ""));
        assertEquals(false, encoder.isCaverphoneEqual("abc", "def"));
        assertEquals(true, encoder.isCaverphoneEqual("ghi", "ghi"));
        assertEquals(false, encoder.isCaverphoneEqual("jkl", "mno"));
    }
}