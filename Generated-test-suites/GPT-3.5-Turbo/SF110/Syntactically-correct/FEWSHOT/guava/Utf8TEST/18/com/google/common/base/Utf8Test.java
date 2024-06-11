import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Utf8Test {
    @Test
    public void testEncodedLength() {
        // test when sequence contains only ASCII characters
        assertEquals(0, Utf8.encodedLength(""));
        assertEquals(1, Utf8.encodedLength("a"));
        assertEquals(5, Utf8.encodedLength("hello"));

        // test when sequence contains non-ASCII characters
        assertEquals(2, Utf8.encodedLength("\u0080"));
        assertEquals(2, Utf8.encodedLength("\u07FF"));
        assertEquals(3, Utf8.encodedLength("\u0800"));
        assertEquals(3, Utf8.encodedLength("\uFFFF"));
        assertEquals(4, Utf8.encodedLength("\uD800\uDC00"));
        assertEquals(4, Utf8.encodedLength("\uDFFF\uDFFF"));
    }

    @Test
    public void testIsWellFormed() {
        // test when bytes are well-formed UTF-8
        assertEquals(true, Utf8.isWellFormed(new byte[] {})); // empty array
        assertEquals(true, Utf8.isWellFormed(new byte[] { 97 })); // single ASCII character
        assertEquals(true, Utf8.isWellFormed(new byte[] { -62, -128 })); // 2-byte encoded character
        assertEquals(true, Utf8.isWellFormed(new byte[] { -32, -96, -128 })); // 3-byte encoded character
        assertEquals(true, Utf8.isWellFormed(new byte[] { -16, -96, -128, -128 })); // 4-byte encoded character
        assertEquals(true, Utf8.isWellFormed(new byte[] { 97, -62, -128, -32, -96, -128, -16, -96, -128, -128 })); // mix of ASCII and encoded characters

        // test when bytes are not well-formed UTF-8
        assertEquals(false, Utf8.isWellFormed(new byte[] { -64 })); // invalid prefix
        assertEquals(false, Utf8.isWellFormed(new byte[] { -62, -80 })); // invalid continuation
        assertEquals(false, Utf8.isWellFormed(new byte[] { -32, -96, -80 })); // invalid continuation
        assertEquals(false, Utf8.isWellFormed(new byte[] { -16, -96, -80, -128 })); // invalid continuation
        assertEquals(false, Utf8.isWellFormed(new byte[] { -16, -320, -128, -128 })); // invalid continuation

        // test when bytes contain overlong encoded characters
        assertEquals(false, Utf8.isWellFormed(new byte[] { -64, -128 })); // overlong 2-byte encoding
        assertEquals(false, Utf8.isWellFormed(new byte[] { -32, -96, -128, -128 })); // overlong 3-byte encoding
        assertEquals(false, Utf8.isWellFormed(new byte[] { -16, -96, -128, -128, -128 })); // overlong 4-byte encoding

        // test when bytes contain invalid surrogate characters
        assertEquals(false, Utf8.isWellFormed(new byte[] { -16, -97, -128, -128 })); // invalid high surrogate
        assertEquals(false, Utf8.isWellFormed(new byte[] { -16, -90, -128, -128 })); // invalid low surrogate
    }
}