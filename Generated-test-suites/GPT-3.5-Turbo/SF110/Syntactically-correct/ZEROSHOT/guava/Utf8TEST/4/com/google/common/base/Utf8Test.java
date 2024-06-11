import org.junit.Assert;
import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Test empty CharSequence
        String emptyString = "";
        int result = Utf8.encodedLength(emptyString);
        Assert.assertEquals(0, result);

        // Test ASCII characters only
        String asciiString = "abcdefg12345";
        result = Utf8.encodedLength(asciiString);
        Assert.assertEquals(asciiString.length(), result);

        // Test non-ASCII characters
        String nonAsciiString = "你好，世界！";
        result = Utf8.encodedLength(nonAsciiString);
        Assert.assertEquals(nonAsciiString.getBytes().length, result);
    }

    @Test
    public void testIsWellFormed() {
        // Test empty byte array
        byte[] emptyArray = new byte[0];
        boolean result = Utf8.isWellFormed(emptyArray);
        Assert.assertTrue(result);

        // Test well-formed byte array
        byte[] wellFormedArray = new byte[]{0x41, 0x42, (byte)0xC2, (byte)0xA9};
        result = Utf8.isWellFormed(wellFormedArray);
        Assert.assertTrue(result);

        // Test malformed byte array
        byte[] malformedArray = new byte[]{0x41, 0x42, (byte)0xC2, (byte)0x80};
        result = Utf8.isWellFormed(malformedArray);
        Assert.assertFalse(result);
    }
}