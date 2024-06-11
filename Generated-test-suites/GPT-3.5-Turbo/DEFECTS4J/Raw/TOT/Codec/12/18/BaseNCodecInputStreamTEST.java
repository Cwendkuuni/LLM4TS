import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class BaseNCodecInputStreamTest {

    private BaseNCodecInputStream baseNCodecInputStream;

    private byte[] testData;
    private byte[] encodedData;

    @Before
    public void setup() {
        testData = new byte[]{1, 2, 3, 4, 5};
        encodedData = getEncodedData(testData);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(encodedData);
        baseNCodecInputStream = new BaseNCodecInputStream(byteArrayInputStream, new BaseNCodec(), false);
    }

    @Test
    public void testRead_ReturnsNextByte_WhenDataAvailable() throws IOException {
        int result = baseNCodecInputStream.read();
        assertEquals(testData[0], (byte) result);
    }

    @Test
    public void testRead_ReturnsEOF_WhenNoDataAvailable() throws IOException {
        baseNCodecInputStream.read(); // Read all available bytes

        int result = baseNCodecInputStream.read();
        assertEquals(-1, result);
    }

    @Test
    public void testRead_ReadsMultipleBytes_WhenAvailable() throws IOException {
        byte[] result = new byte[2];
        int bytesRead = baseNCodecInputStream.read(result, 0, 2);

        assertEquals(2, bytesRead);
        assertEquals(testData[0], result[0]);
        assertEquals(testData[1], result[1]);
    }

    @Test
    public void testRead_ReturnsZeroBytesRead_WhenNoDataAvailable() throws IOException {
        baseNCodecInputStream.read(new byte[5], 0, 5); // Read all available bytes

        int result = baseNCodecInputStream.read(new byte[5], 0, 5);
        assertEquals(0, result);
    }

    @Test(expected = NullPointerException.class)
    public void testRead_ThrowsNullPointerException_WhenByteArrayNull() throws IOException {
        baseNCodecInputStream.read(null, 0, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_ThrowsIndexOutOfBoundsException_WhenOffsetInvalid() throws IOException {
        baseNCodecInputStream.read(new byte[5], -1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_ThrowsIndexOutOfBoundsException_WhenLenInvalid() throws IOException {
        baseNCodecInputStream.read(new byte[5], 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_ThrowsIndexOutOfBoundsException_WhenBufferSizeInvalid() throws IOException {
        baseNCodecInputStream.read(new byte[5], 6, 2);
    }

    private byte[] getEncodedData(byte[] data) {
        return BaseNCodec.encodeBase64(data);
    }

}