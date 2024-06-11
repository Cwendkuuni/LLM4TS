import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.DeflateParameters;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link DeflateCompressorInputStream}.
 */
public class DeflateCompressorInputStreamTest {

    private InputStream inputStream;
    private DeflateCompressorInputStream deflateInputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        String testData = "This is a test for DeflateCompressorInputStream.";
        byte[] testBytes = testData.getBytes();
        inputStream = new ByteArrayInputStream(testBytes);
        outputStream = new ByteArrayOutputStream();
        DeflateParameters parameters = new DeflateParameters();
        deflateInputStream = new DeflateCompressorInputStream(inputStream, parameters);
    }

    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(deflateInputStream);
        IOUtils.closeQuietly(outputStream);
    }

    /**
     * Test reading single byte from DeflateCompressorInputStream.
     */
    @Test
    public void testRead() throws IOException {
        int readByte = deflateInputStream.read();
        assertNotNull(readByte);
        assertEquals(testData.charAt(0), (char) readByte);
    }

    /**
     * Test reading multiple bytes from DeflateCompressorInputStream.
     */
    @Test
    public void testReadMultipleBytes() throws IOException {
        byte[] buffer = new byte[10];
        int readBytes = deflateInputStream.read(buffer, 0, buffer.length);
        assertNotNull(readBytes);
        assertEquals(testData.length(), readBytes);
        for (int i = 0; i < readBytes; i++) {
            assertEquals(testData.charAt(i), (char) buffer[i]);
        }
    }

    /**
     * Test skipping bytes in DeflateCompressorInputStream.
     */
    @Test
    public void testSkip() throws IOException {
        long skippedBytes = deflateInputStream.skip(5);
        assertEquals(5, skippedBytes);
    }

    /**
     * Test available bytes in DeflateCompressorInputStream.
     */
    @Test
    public void testAvailable() throws IOException {
        long availableBytes = deflateInputStream.available();
        assertEquals(testData.length(), availableBytes);
    }

    /**
     * Test closing DeflateCompressorInputStream.
     */
    @Test
    public void testClose() throws IOException {
        byte[] buffer = new byte[10];
        int readBytes = deflateInputStream.read(buffer, 0, buffer.length);
        assertNotNull(readBytes);
        deflateInputStream.close();
        int readBytesAfterClose = deflateInputStream.read(buffer, 0, buffer.length);
        assertEquals(-1, readBytesAfterClose);
    }

    /**
     * Test reading zlib/deflate-compressed data without zlib header.
     */
    @Test
    public void testReadZlibDecompressedDataWithoutZlibHeader() throws IOException {
        DeflateParameters parameters = new DeflateParameters();
        parameters.setWithZlibHeader(false);
        deflateInputStream = new DeflateCompressorInputStream(inputStream, parameters);
        int readByte = deflateInputStream.read();
        assertNotNull(readByte);
        assertEquals(testData.charAt(0), (char) readByte);
    }

    /**
     * Test reading invalid compressed data.
     */
    @Test(expected = IOException.class)
    public void testReadInvalidCompressedData() throws IOException {
        inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        deflateInputStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
        int readByte = deflateInputStream.read();
    }
}