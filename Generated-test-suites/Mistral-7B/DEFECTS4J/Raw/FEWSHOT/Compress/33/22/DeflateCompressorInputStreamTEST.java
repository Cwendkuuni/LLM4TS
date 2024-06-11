package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.DeflateParameters;
import org.apache.commons.compress.utils.IOUtils;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testDeflateCompressorInputStream() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0x4E, (byte) 0x65, (byte) 0x6C, (byte) 0x63, (byte) 0x6F, (byte) 0x6D, (byte) 0x65, (byte) 0x06, (byte) 0x08}); // zlib header
        DeflateCompressorInputStream inputStreamUnderTest = new DeflateCompressorInputStream(inputStream, new DeflateParameters());

        byte[] expectedBytes = new byte[]{(byte) 0x7A, (byte) 0x42, (byte) 0x50, (byte) 0x4D, (byte) 0x4C, (byte) 0x4B, (byte) 0x05, (byte) 0x06, (byte) 0x08, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x0F, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF}; // decompressed data

        byte[] actualBytes = new byte[expectedBytes.length];
        int read;
        do {
            read = inputStreamUnderTest.read(actualBytes, 0, actualBytes.length);
            if (read > 0) {
                System.arraycopy(actualBytes, 0, expectedBytes, 0, read);
            }
        } while (read > 0);

        assertEquals(expectedBytes, IOUtils.toByteArray(new ByteArrayInputStream(actualBytes)));
    }

    @Test
    public void testDeflateCompressorInputStreamNoZlibHeader() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08});
        DeflateCompressorInputStream inputStreamUnderTest = new DeflateCompressorInputStream(inputStream, new DeflateParameters());

        byte[] expectedBytes = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};

        byte[] actualBytes = new byte[expectedBytes.length];
        int read;
        do {
            read = inputStreamUnderTest.read(actualBytes, 0, actualBytes.length);
            if (read > 0) {
                System.arraycopy(actualBytes, 0, expectedBytes, 0, read);
            }
        } while (read > 0);

        assertEquals(expectedBytes, IOUtils.toByteArray(new ByteArrayInputStream(actualBytes)));
    }

    @Test
    public void testRead() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01});
        DeflateCompressorInputStream inputStreamUnderTest = new DeflateCompressorInputStream(inputStream, new DeflateParameters());

        byte[] buffer = new byte[1];
        int result = inputStreamUnderTest.read();

        assertEquals(-1, result);
        assertTrue(inputStreamUnderTest.available() == 0);
        inputStreamUnderTest.close();
    }

    @Test
    public void testReadWithByteArray() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02});
        DeflateCompressorInputStream inputStreamUnderTest = new DeflateCompressorInputStream(inputStream, new DeflateParameters());

        byte[] buffer = new byte[2];
        int result = inputStreamUnderTest.read(buffer, 0, buffer.length);

        assertEquals(2, result);
        assertEquals((byte) 0x01, buffer[0]);
        assertEquals((byte) 0x02, buffer[1]);
        inputStreamUnderTest.close();
    }

    @Test
    public void testSkip() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02});
        DeflateCompressorInputStream inputStreamUnderTest = new DeflateCompressorInputStream(inputStream, new DeflateParameters());

        long result = inputStreamUnderTest.skip(3);

        assertEquals(-1, result);
        assertTrue(inputStreamUnderTest.available() == 2);
        inputStreamUnderTest.close();
    }

    @Test
    public void testAvailable() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        DeflateCompressorInputStream inputStreamUnderTest = new DeflateCompressorInputStream(inputStream, new DeflateParameters());

        int result = inputStreamUnderTest.available();

        assertEquals(3, result);
        inputStreamUnderTest.close();
    }

    @Test
    public void testClose() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        DeflateCompressorInputStream inputStreamUnderTest = new DeflateCompressorInputStream(inputStream, new DeflateParameters());

        inputStreamUnderTest.close();
        assertFalse(inputStreamUnderTest.isOpen());
        assertNull(inputStreamUnderTest.in);
    }
}