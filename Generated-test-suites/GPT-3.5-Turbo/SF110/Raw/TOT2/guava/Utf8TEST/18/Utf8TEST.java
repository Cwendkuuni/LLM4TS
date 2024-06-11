package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        assertEquals(5, Utf8.encodedLength("hello"));
        assertEquals(9, Utf8.encodedLength("¡Hola!"));
        assertEquals(11, Utf8.encodedLength("你好"));
        assertEquals(7, Utf8.encodedLength("😊"));
    }

    @Test
    public void testEncodedLengthGeneral() {
        assertEquals(2, Utf8.encodedLengthGeneral("hi", 0));
        assertEquals(4, Utf8.encodedLengthGeneral("¡Hola!", 1));
        assertEquals(3, Utf8.encodedLengthGeneral("你好", 0));
        assertEquals(4, Utf8.encodedLengthGeneral("Hello 👋", 2));
    }

    @Test
    public void testIsWellFormed() {
        byte[] validBytes = {65, 66, 67};
        assertTrue(Utf8.isWellFormed(validBytes));

        byte[] invalidBytes = {-65, 66, 67};
        assertFalse(Utf8.isWellFormed(invalidBytes));
    }

    @Test
    public void testIsWellFormedWithParameters() {
        byte[] bytes = {65, -65, 66, 67};
        assertTrue(Utf8.isWellFormed(bytes, 1, 3));

        byte[] bytes2 = {65, -65, 66, 67};
        assertFalse(Utf8.isWellFormed(bytes2, 0, 4));
    }

}