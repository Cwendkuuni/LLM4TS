package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class CaverphoneTest {
    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("fodikkopfttfkkfddsfs", caverphone.caverphone("Photic"));
        assertEquals("h4131", caverphone.caverphone("Hello"));
        assertEquals("P2321", caverphone.caverphone("World"));
        assertEquals("W111111111", caverphone.caverphone("Why"));
        assertEquals("e111m11111", caverphone.caverphone("Example"));
        assertEquals("f4434f44h2fs", caverphone.caverphone("Framework"));
    }

    @Test
    public void testEncode() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("fodikkopfttfkkfddsfs", caverphone.encode("Photic"));
        assertEquals("h4131", caverphone.encode("Hello"));
        assertEquals("P2321", caverphone.encode("World"));
        assertEquals("W111111111", caverphone.encode("Why"));
        assertEquals("e111m11111", caverphone.encode("Example"));
        assertEquals("f4434f44h2fs", caverphone.encode("Framework"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertTrue(caverphone.isCaverphoneEqual(null, null));
        assertFalse(caverphone.isCaverphoneEqual(null, ""));
        assertFalse(caverphone.isCaverphoneEqual("", null));
        assertTrue(caverphone.isCaverphoneEqual("Hello", "Hello"));
        assertFalse(caverphone.isCaverphoneEqual("Hello", "World"));
    }
}