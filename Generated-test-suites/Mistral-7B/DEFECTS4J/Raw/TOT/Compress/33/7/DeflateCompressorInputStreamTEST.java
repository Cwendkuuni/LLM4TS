import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * Test for DeflateCompressorInputStream class.
 */
public class DeflateCompressorInputStreamTest {

    private InputStream inputStream;
    private ByteArrayOutputStream outputStream;
    private DeflateCompressorInputStream testStream;

    @Before
    public void setUp() throws IOException {
        inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x04}); // zlib header
        outputStream = new ByteArrayOutputStream();
        testStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
    }

    @After
    public void tearDown() throws IOException {
        testStream.close();
        IOUtils.closeQuietly(outputStream);
        IOUtils.closeQuietly(inputStream);
    }

    /**
     * Test for reading single byte from DeflateCompressorInputStream.
     *
     * @throws IOException
     */
    @Test
    public void testReadSingleByte() throws IOException {
        int readByte = testStream.read();
        assertEquals((int) 0x78, readByte);
    }

    /**
     * Test for reading byte array from DeflateCompressorInputStream.
     *
     * @throws IOException
     */
    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[10];
        int readLength = testStream.read(buffer);
        assertNotNull(readLength);
        assertEquals(readLength, 10);
        for (int i = 0; i < readLength; i++) {
            assertEquals(buffer[i], (byte) (0x78 + i));
        }
    }

    /**
     * Test for skipping bytes from DeflateCompressorInputStream.
     *
     * @throws IOException
     */
    @Test
    public void testSkipBytes() throws IOException {
        long skipLength = 5;
        long skippedBytes = testStream.skip(skipLength);
        assertEquals(skippedBytes, skipLength);
        byte[] buffer = new byte[10];
        int readLength = testStream.read(buffer);
        assertArrayEquals(new byte[]{(byte) 0x7d, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0x78, (byte) 0x78, (byte) 0x78, (byte) 0x78}, buffer);
    }

    /**
     * Test for testing available bytes in DeflateCompressorInputStream.
     *
     * @throws IOException
     */
    @Test
    public void testAvailableBytes() throws IOException {
        byte[] buffer = new byte[12];
        int readLength = testStream.read(buffer);
        assertEquals(readLength, 10);
        long availableBytes = testStream.available();
        assertEquals(availableBytes, 2);
    }

    /**
     * Test for closing DeflateCompressorInputStream.
     *
     * @throws IOException
     */
    @Test
    public void testCloseStream() throws IOException {
        testStream.close();
        assertFalse(testStream.isOpen());
    }

    /**
     * Test for reading invalid zlib header.
     *
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testInvalidZlibHeader() throws IOException {
        inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x05}); // invalid zlib header (last byte should be 0x04)
        testStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
        testStream.read();
    }
}