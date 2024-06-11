
package com.google.common.base;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        assertEquals(3, Utf8.encodedLength("abc"));
        assertEquals(4, Utf8.encodedLength("中国"));
        assertEquals(1, Utf8.encodedLength("a"));
        assertEquals(2, Utf8.encodedLength("ä"));
        assertEquals(2, Utf8.encodedLength("€"));
        assertEquals(3, Utf8.encodedLength("€€€"));
        assertEquals(4, Utf8.encodedLength("𐍈𠁴"));
        assertEquals(4, Utf8.encodedLength("𝄞𝄞𝄞"));
        assertEquals(5, Utf8.encodedLength("€𐍈𝄞"));
        assertEquals(6, Utf8.encodedLength("€€€𐍈𝄞"));
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes1 = { 97, 98, 99 };
        assertEquals(true, Utf8.isWellFormed(bytes1));

        byte[] bytes2 = { -28, -72, -83, -26, -106, -121 };
        assertEquals(true, Utf8.isWellFormed(bytes2));

        byte[] bytes3 = { 97, -62, -80 };
        assertEquals(false, Utf8.isWellFormed(bytes3));

        byte[] bytes4 = { -28, -72, -83, 97, 98, 99 };
        assertEquals(false, Utf8.isWellFormed(bytes4));

        byte[] bytes5 = { -28, -72, -83, -26, -106 };
        assertEquals(false, Utf8.isWellFormed(bytes5));

        byte[] bytes6 = { -28, -72, -83, -26, -106, -121, 97, 98, 99 };
        assertEquals(false, Utf8.isWellFormed(bytes6));
    }

    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        byte[] bytes1 = { 97, 98, 99 };
        assertEquals(true, Utf8.isWellFormed(bytes1, 0, 3));

        byte[] bytes2 = { -28, -72, -83, -26, -106, -121 };
        assertEquals(true, Utf8.isWellFormed(bytes2, 0, 6));

        byte[] bytes3 = { 97, -62, -80 };
        assertEquals(false, Utf8.isWellFormed(bytes3, 0, 3));

        byte[] bytes4 = { -28, -72, -83, 97, 98, 99 };
        assertEquals(false, Utf8.isWellFormed(bytes4, 0, 6));

        byte[] bytes5 = { -28, -72, -83, -26, -106 };
        assertEquals(false, Utf8.isWellFormed(bytes5, 0, 5));

        byte[] bytes6 = { -28, -72, -83, -26, -106, -121, 97, 98, 99 };
        assertEquals(false, Utf8.isWellFormed(bytes6, 0, 9));
    }

}
