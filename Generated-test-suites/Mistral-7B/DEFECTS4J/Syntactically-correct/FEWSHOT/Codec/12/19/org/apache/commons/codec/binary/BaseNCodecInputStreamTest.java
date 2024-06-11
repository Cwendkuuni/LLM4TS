package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

/**
 * Test cases for BaseNCodecInputStream class
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test constructor.
     *
     * @throws IOException
     *             if an I/O error occurs while creating test input stream
     */
    @Test
    public void testConstructor() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream(baos.toByteArray());
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        assertNotNull(inputStream);
    }

    /**
     * Test reading a single byte.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @Test
    public void testReadSingleByte() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(baos.toByteArray());
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        byte expectedByte = (byte) 0x1A; // arbitrary byte value
        byte readByte;

        baos.write(expectedByte);
        baos.flush();

        readByte = inputStream.read();
        assertEquals(expectedByte, readByte);
        assertEquals(expectedByte & 0xFF, readByte);

        // check if EOF is reached
        inputStream.mark(0);
        assertEquals(expectedByte, inputStream.read()); // read again to check that it's not EOF yet
        inputStream.reset();
        assertEquals(BaseNCodecInputStream.EOF, inputStream.read()); // check if EOF is reached
    }

    /**
     * Test reading multiple bytes.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @Test
    public void testReadMultipleBytes() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(baos.toByteArray());
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        byte[] expectedBytes = new byte[] { (byte) 0x1A, (byte) 0x2B, (byte) 0x3C }; // arbitrary bytes
        byte[] readBytes = new byte[expectedBytes.length];

        baos.write(expectedBytes);
        baos.flush();

        int readLen = inputStream.read(readBytes, 0, expectedBytes.length);
        assertEquals(expectedBytes.length, readLen);

        for (int i = 0; i < expectedBytes.length; i++) {
            assertEquals(expectedBytes[i], readBytes[i]);
            assertEquals(expectedBytes[i] & 0xFF, readBytes[i]);
        }
    }

    /**
     * Test mark and reset.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @Test
    public void testMarkAndReset() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(baos.toByteArray());
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        byte[] expectedBytes = new byte[] { (byte) 0x1A, (byte) 0x2B, (byte) 0x3C }; // arbitrary bytes
        byte[] readBytes = new byte[expectedBytes.length];

        baos.write(expectedBytes);
        baos.flush();

        inputStream.mark(0);
        int readLen = inputStream.read(readBytes, 0, expectedBytes.length);
        assertEquals(expectedBytes.length, readLen);

        for (int i = 0; i < expectedBytes.length; i++) {
            assertEquals(expectedBytes[i], readBytes[i]);
            assertEquals(expectedBytes[i] & 0xFF, readBytes[i]);
        }

        inputStream.reset();
        readLen = inputStream.read(readBytes, 0, expectedBytes.length);
        assertEquals(expectedBytes.length, readLen);

        for (int i = 0; i < expectedBytes.length; i++) {
            assertEquals(expectedBytes[i], readBytes[i]);
            assertEquals(expectedBytes[i] & 0xFF, readBytes[i]);
        }

        // test mark limit
        inputStream.mark(1);
        readLen = inputStream.read(readBytes, 0, expectedBytes.length);
        assertEquals(expectedBytes.length, readLen);

        for (int i = 0; i < expectedBytes.length; i++) {
            assertEquals(expectedBytes[i], readBytes[i]);
            assertEquals(expectedBytes[i] & 0xFF, readBytes[i]);
        }

        inputStream.reset();
        readLen = inputStream.read(readBytes, 0, expectedBytes.length + 1);
        assertEquals(-1, readLen); // mark limit exceeded
    }

    /**
     * Test skipping bytes.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @Test
    public void testSkipBytes() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(baos.toByteArray());
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        byte[] expectedBytes = new byte[] { (byte) 0x1A, (byte) 0x2B, (byte) 0x3C }; // arbitrary bytes
        long bytesToSkip = 3;

        baos.write(expectedBytes);
        baos.flush();

        inputStream.skip(bytesToSkip);
        int readLen = inputStream.read(expectedBytes, 0, expectedBytes.length);
        assertEquals(expectedBytes.length, readLen);

        for (int i = 0; i < expectedBytes.length; i++) {
            assertEquals(expectedBytes[i], readBytes[i]);
            assertEquals(expectedBytes[i] & 0xFF, readBytes[i]);
        }
    }

    /**
     * Test the case when there is no data available.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @Test
    public void testNoDataAvailable() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(baos.toByteArray());
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        // check if no data is available initially
        assertEquals(0, inputStream.read(new byte[1][], 0, 1));
        assertEquals(BaseNCodecInputStream.EOF, inputStream.read());
    }

    /**
     * Test the case when a large amount of data is decoded (non-baseN).
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @Test
    public void testLargeDecodedData() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(baos.toByteArray());
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        byte[] data = new byte[1024 * 1024]; // 1 MiB
        int readLen;

        baos.write(data);
        baos.flush();

        readLen = inputStream.read(new byte[data.length][], 0, data.length);
        assertTrue(readLen > 0); // non-zero bytes read

        // check if InputStreamReader interop works properly
        InputStreamReader reader = new InputStreamReader(inputStream);
        char[] buffer = new char[data.length * 2]; // assuming each byte becomes at least 2 chars in UTF-8
        int bytesRead = reader.read(buffer, 0, buffer.length);
        assertTrue(bytesRead > 0); // non-zero bytes read
    }

    /**
     * Test the case when a large amount of data is encoded.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @Test
    public void testLargeEncodedData() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(baos.toByteArray());
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, true);

        byte[] data = new byte[1024 * 1024]; // 1 MiB
        int readLen;

        baos.write(data);
        baos.flush();

        readLen = inputStream.read(new byte[data.length][], 0, data.length);
        assertTrue(readLen > 0); // non-zero bytes read

        // check if InputStreamReader interop works properly
        InputStreamReader reader = new InputStreamReader(inputStream);
        char[] buffer = new char[data.length * 2]; // assuming each byte becomes at least 2 chars in UTF-8
        int bytesRead = reader.read(buffer, 0, buffer.length);
        assertTrue(bytesRead > 0); // non-zero bytes read
    }

    /**
     * Test the case when the underlying input stream is closed.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @Test(expected = IOException.class)
    public void testClosedInputStream() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(baos.toByteArray());
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        input.close();
        inputStream.read();
    }
}