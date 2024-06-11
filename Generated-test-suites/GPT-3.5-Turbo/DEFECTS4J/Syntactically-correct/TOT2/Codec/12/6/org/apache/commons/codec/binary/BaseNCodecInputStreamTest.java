import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Base16;

public class BaseNCodecInputStreamTest {

    @Test
    public void testReadWithBase64() throws IOException {
        byte[] testData = {65, 66, 67}; // ABC
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(testData));
        BaseNCodecInputStream base64InputStream = new BaseNCodecInputStream(inputStream, new Base64(), false);

        assertEquals(65, base64InputStream.read());
        assertEquals(66, base64InputStream.read());
        assertEquals(67, base64InputStream.read());
        assertEquals(-1, base64InputStream.read()); // EOF
    }

    @Test
    public void testReadWithBase32() throws IOException {
        byte[] testData = {49, 50, 51}; // 123
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base32.encodeBase32(testData));
        BaseNCodecInputStream base32InputStream = new BaseNCodecInputStream(inputStream, new Base32(), false);

        assertEquals(49, base32InputStream.read());
        assertEquals(50, base32InputStream.read());
        assertEquals(51, base32InputStream.read());
        assertEquals(-1, base32InputStream.read()); // EOF
    }

    @Test
    public void testReadWithBase16() throws IOException {
        byte[] testData = {97, 98, 99}; // abc
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base16.encodeBase16(testData));
        BaseNCodecInputStream base16InputStream = new BaseNCodecInputStream(inputStream, new Base16(), false);

        assertEquals(97, base16InputStream.read());
        assertEquals(98, base16InputStream.read());
        assertEquals(99, base16InputStream.read());
        assertEquals(-1, base16InputStream.read()); // EOF
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] testData = {65, 66, 67, 68}; // ABCD
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(testData));
        BaseNCodecInputStream base64InputStream = new BaseNCodecInputStream(inputStream, new Base64(), false);
        byte[] result = new byte[4];

        assertEquals(4, base64InputStream.read(result, 0, 4));
        assertArrayEquals(new byte[]{65, 66, 67, 68}, result);

        assertEquals(-1, base64InputStream.read(result, 0, 4)); // EOF
    }

    // Write additional test cases for edge cases and exceptions as needed

}