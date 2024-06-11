import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Utf8Test {
    @Test
    public void testEncodedLength() {
        String str1 = "hello";
        int expected1 = 5;
        assertEquals(expected1, Utf8.encodedLength(str1));

        String str2 = "";
        int expected2 = 0;
        assertEquals(expected2, Utf8.encodedLength(str2));

        String str3 = "φίλος";
        int expected3 = 11;
        assertEquals(expected3, Utf8.encodedLength(str3));

        String str4 = "😊";
        int expected4 = 4;
        assertEquals(expected4, Utf8.encodedLength(str4));
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes1 = {0x68, 0x65, 0x6c, 0x6c, 0x6f};
        assertTrue(Utf8.isWellFormed(bytes1));

        byte[] bytes2 = {0x68, (byte) 0x80, 0x65, 0x6c, 0x6c, 0x6f};
        assertFalse(Utf8.isWellFormed(bytes2));

        byte[] bytes3 = {(byte) 0xef, (byte) 0xbb, (byte) 0xbf, 0x68, 0x65, 0x6c, 0x6c, 0x6f};
        assertTrue(Utf8.isWellFormed(bytes3));
    }
}