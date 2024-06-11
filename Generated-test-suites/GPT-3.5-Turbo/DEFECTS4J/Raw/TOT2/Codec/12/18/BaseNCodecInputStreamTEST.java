import org.junit.Test;
import org.junit.Assert;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class BaseNCodecInputStreamTest {

    // Test read method
    @Test
    public void testRead() throws IOException {
        byte[] testData = {65, 66, 67}; // ASCII values for A, B, C
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new BaseNCodec(), true);

        int result = baseNCodecInputStream.read();

        Assert.assertEquals(65, result); // Expected result is 65 which is ASCII value for 'A'
    }

    // Test read method for EOF
    @Test
    public void testReadEOF() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{});
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new BaseNCodec(), true);

        int result = baseNCodecInputStream.read();

        Assert.assertEquals(-1, result); // Expected result is -1 for EOF
    }

    // Test read(byte[] b, int offset, int len) method
    @Test
    public void testReadByteArray() throws IOException {
        byte[] testData = {65, 66, 67}; // ASCII values for A, B, C
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new BaseNCodec(), true);

        byte[] buffer = new byte[3];
        int readLen = baseNCodecInputStream.read(buffer, 0, 3);

        Assert.assertEquals(3, readLen); // Expected result is 3 bytes read
        Assert.assertArrayEquals(testData, buffer); // Expected result is the same input data
    }

    // Test read(byte[] b, int offset, int len) for null byte array
    @Test(expected = NullPointerException.class)
    public void testReadNullByteArray() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new BaseNCodec(), true);

        baseNCodecInputStream.read(null, 0, 0);
    }

    // Test read(byte[] b, int offset, int len) for invalid offset
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidOffset() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new BaseNCodec(), true);

        baseNCodecInputStream.read(new byte[1], -1, 0);
    }

    // Test read(byte[] b, int offset, int len) for invalid len
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidLen() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new BaseNCodec(), true);

        baseNCodecInputStream.read(new byte[1], 0, -1);
    }

    // Test markSupported method
    @Test
    public void testMarkSupported() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new BaseNCodec(), true);

        boolean result = baseNCodecInputStream.markSupported();

        Assert.assertFalse(result); // Expected result is false
    }

    // Test available method
    @Test
    public void testAvailable() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new BaseNCodec(), true);

        int result = baseNCodecInputStream.available();

        Assert.assertEquals(0, result); // Expected result is 0
    }
}