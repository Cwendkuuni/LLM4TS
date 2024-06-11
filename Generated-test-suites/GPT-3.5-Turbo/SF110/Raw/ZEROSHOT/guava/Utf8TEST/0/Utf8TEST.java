
package com.google.common.base;

import org.junit.Assert;
import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Test when sequence is empty
        int length = Utf8.encodedLength("");
        Assert.assertEquals(0, length);

        // Test when sequence contains only ASCII characters
        length = Utf8.encodedLength("abcdefg");
        Assert.assertEquals(7, length);

        // Test when sequence contains non-ASCII characters
        length = Utf8.encodedLength("世界");
        Assert.assertEquals(6, length);

        // Test when sequence contains surrogate pairs
        length = Utf8.encodedLength("\uD83D\uDE00\uD83D\uDE01");
        Assert.assertEquals(8, length);

        // Test when sequence contains mix of ASCII and non-ASCII characters
        length = Utf8.encodedLength("hello 世界");
        Assert.assertEquals(11, length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthException() {
        // Test when encoded length does not fit in int
        Utf8.encodedLength(new String(new char[Integer.MAX_VALUE / 2]));
    }

    @Test
    public void testIsWellFormed() {
        // Test when bytes is empty
        boolean wellFormed = Utf8.isWellFormed(new byte[]{});
        Assert.assertTrue(wellFormed);

        // Test when bytes contains only positive values
        wellFormed = Utf8.isWellFormed(new byte[]{1, 2, 3, 4, 5});
        Assert.assertTrue(wellFormed);

        // Test when bytes contains negative values that are well-formed
        wellFormed = Utf8.isWellFormed(new byte[]{-30, -128, -128});
        Assert.assertTrue(wellFormed);

        // Test when bytes contains negative values that are not well-formed
        wellFormed = Utf8.isWellFormed(new byte[]{-30, -128});
        Assert.assertFalse(wellFormed);
        wellFormed = Utf8.isWellFormed(new byte[]{-30, -128, 127});
        Assert.assertFalse(wellFormed);

        // Test when bytes contains mix of well-formed and not well-formed values
        wellFormed = Utf8.isWellFormed(new byte[]{-30, -128, 127, -30, -128, -128});
        Assert.assertFalse(wellFormed);
        wellFormed = Utf8.isWellFormed(new byte[]{1, -30, -128, 127, 2, -30, -128, -128, 3});
        Assert.assertFalse(wellFormed);
    }

    @Test
    public void testIsWellFormedOffLen() {
        byte[] bytes = new byte[]{1, -30, -128, 127, 2, -30, -128, -128, 3};

        // Test when off and len cover the entire bytes array
        boolean wellFormed = Utf8.isWellFormed(bytes, 0, bytes.length);
        Assert.assertFalse(wellFormed);

        // Test when off and len cover only part of the bytes array
        wellFormed = Utf8.isWellFormed(bytes, 1, 6);
        Assert.assertFalse(wellFormed);
    }
}
