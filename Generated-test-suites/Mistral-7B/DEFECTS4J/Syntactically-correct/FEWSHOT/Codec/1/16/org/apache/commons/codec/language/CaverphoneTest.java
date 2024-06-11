package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEncodedString() {
        Caverphone caverphone = new Caverphone();

        assertEquals("11222333333333333333333311", caverphone.caverphone("apache"));
        assertEquals("222233333333333333333322", caverphone.caverphone("commons"));
        assertEquals("111112222333333333331111", caverphone.caverphone("codec"));
        assertEquals("11111111122223333333331111", caverphone.caverphone("language"));

        assertEquals("11111111111111111111111111", caverphone.caverphone(""));
        assertEquals("11111111111111111111111111", caverphone.caverphone(null));

        // Test with special characters
        assertEquals("11111111232333333533333333333333311", caverphone.caverphone("Ap@che"));
    }

    @Test(expected = EncoderException.class)
    public void testNullString() {
        Caverphone caverphone = new Caverphone();
        caverphone.caverphone(null);
    }

    @Test
    public void testEqualsCaverphone() {
        Caverphone caverphone = new Caverphone();

        assertTrue(caverphone.isCaverphoneEqual("apache", "Apache"));
        assertTrue(caverphone.isCaverphoneEqual("commons", "Commons"));
        assertTrue(caverphone.isCaverphoneEqual("codec", "Codec"));
        assertTrue(caverphone.isCaverphoneEqual("language", "Language"));
        assertTrue(caverphone.isCaverphoneEqual("", ""));
        assertTrue(caverphone.isCaverphoneEqual(null, null));

        assertFalse(caverphone.isCaverphoneEqual("apache", "commons"));
        assertFalse(caverphone.isCaverphoneEqual("commons", "codec"));
        assertFalse(caverphone.isCaverphoneEqual("codec", "language"));
        assertFalse(caverphone.isCaverphoneEqual("", "commons"));
        assertFalse(caverphone.isCaverphoneEqual("null", null));
        assertFalse(caverphone.isCaverphoneEqual("Ap@che", "Apache"));

        String str1 = "Apache";
        String str2 = new String(new byte[0]);
        assertFalse(caverphone.isCaverphoneEqual(str1, str2));

        String str3 = new String(new byte[0]);
        str3 = str3.intern();
        assertFalse(caverphone.isCaverphoneEqual(str1, str3));
    }

    @Test
    public void testEncodeObject() {
        Caverphone caverphone = new Caverphone();

        assertSame("11222333333333333333333311", caverphone.encode(new String("apache")));
        assertSame("222233333333333333333322", caverphone.encode(new String("commons")));
        assertSame("111112222333333333331111", caverphone.encode(new String("codec")));
        assertSame("11111111122223333333331111", caverphone.encode(new String("language")));

        assertNull(caverphone.encode(null));
    }
}