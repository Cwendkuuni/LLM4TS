import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import static org.junit.Assert.assertEquals;

public class ChecksumCalculatingInputStreamTest {
    private ChecksumCalculatingInputStream inputStream;
    private byte[] testData;

    @Before
    public void setUp() {
        testData = "Test data".getBytes();
        Checksum checksum = new CRC32();
        InputStream in = new ByteArrayInputStream(testData);
        inputStream = new ChecksumCalculatingInputStream(checksum, in);
    }

    @Test
    public void read_singleByte() throws IOException {
        int expectedValue = testData[0] & 0xFF;
        int actualValue = inputStream.read();
        assertEquals(expectedValue, actualValue);
        assertEquals(expectedValue, inputStream.getValue());
    }

    @Test
    public void read_byteArray() throws IOException {
        byte[] actualBytes = new byte[testData.length];
        int bytesRead = inputStream.read(actualBytes);
        assertEquals(testData.length, bytesRead);
        assertEquals(0, inputStream.available());
        assertEquals(testData.length, inputStream.getValue());
        assertArraysEqual(testData, actualBytes);
    }

    @Test
    public void read_byteArrayWithOffsetAndLength() throws IOException {
        int offset = 2;
        int length = 4;
        byte[] actualBytes = new byte[length];
        int bytesRead = inputStream.read(actualBytes, offset, length);
        assertEquals(length, bytesRead);
        assertEquals(0, inputStream.available());
        assertEquals(inputStream.getValue(), checksumValueFromBytes(testData, offset, length));
        assertArraysEqual(expectedBytesFromOffsetAndLength(testData, offset, length), actualBytes);
    }

    @Test
    public void skip() throws IOException {
        long skippedBytes = inputStream.skip(testData.length);
        assertEquals(testData.length, skippedBytes);
        assertEquals(-1, inputStream.read());
        assertEquals(testData.length, inputStream.getValue());
    }

    private void assertArraysEqual(byte[] expected, byte[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    private long checksumValueFromBytes(byte[] bytes, int offset, int length) {
        CRC32 crc32 = new CRC32();
        crc32.update(bytes, offset, length);
        return crc32.getValue();
    }

    private byte[] expectedBytesFromOffsetAndLength(byte[] bytes, int offset, int length) {
        byte[] expectedBytes = new byte[length];
        System.arraycopy(bytes, offset, expectedBytes, 0, length);
        return expectedBytes;
    }
}