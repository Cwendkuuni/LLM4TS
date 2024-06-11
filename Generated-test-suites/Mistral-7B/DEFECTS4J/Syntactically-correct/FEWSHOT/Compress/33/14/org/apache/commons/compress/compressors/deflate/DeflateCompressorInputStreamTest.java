package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Inflater;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.DeflateParameters;
import org.junit.Test;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testDeflateCompressorInputStream() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x78, 0x5e, (byte) 0x01, 0x00, 0x00, 0x0c, 0x6c, 0x63, 0x61, 0x76, 0x65, 0x21, 0x01, 0x08, 0x00, 0x00, 0x0b, 0x1b, 0x05, 0x00, 0x17, 0x00, 0x05, 0x08, 0x00, 0x14, 0x00, 0x00, 0x0f, 0x04, 0x00, 0x1a, 0x41, 0x4d, 0x53, 0x2d, 0x5a, 0x53, 0x51, 0x55, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0x00, 0x01, 0x00, 0x1d, 0xcb, 0x31, 0x36, 0x38, 0x37, 0x33, 0x34, 0x32, 0x00 });
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        assertTrue(compressorInputStream.withZlibHeader());

        InputStream inputStreamNoZlibHeader = new ByteArrayInputStream(new byte[] { 0x78, 0x5e, (byte) 0x01, 0x00, 0x00, 0x0c, 0x6c, 0x63, 0x61, 0x76, 0x65, 0x21, 0x01, 0x08, 0x00, 0x00, 0x0b, 0x1b, 0x05, 0x00, 0x17, 0x00, 0x05, 0x08, 0x00, 0x14, 0x00, 0x00, 0x0f, 0x04, 0x00, 0x1a, 0x41, 0x4d, 0x53, 0x2d, 0x5a, 0x53, 0x51, 0x55, 0x00, 0x00, 0x00, 0x1d, 0xcb, 0x31, 0x36, 0x38, 0x37, 0x33, 0x34, 0x32, 0x05, 0x00, 0x2d, 0x64, 0x61, 0x73, 0x68, 0x00 });
        DeflateCompressorInputStream compressorInputStreamNoZlibHeader = new DeflateCompressorInputStream(inputStreamNoZlibHeader, new DeflateParameters());
        assertFalse(compressorInputStreamNoZlibHeader.withZlibHeader());
    }

    @Test
    public void testDeflateCompressorInputStreamWithRead() throws IOException {
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[10];
        int read = compressorInputStream.read(buffer, 0, buffer.length);
        assertSame(data, buffer);
        assertEquals(data.length, read);

        byte[] buffer2 = new byte[5];
        int read2 = compressorInputStream.read(buffer2, 0, buffer2.length);
        assertNotNull(read2);
        assertEquals(5, read2);
        assertArrayEquals(new byte[] { 0x06, 0x07, 0x08, 0x09, 0x0a }, buffer2);
    }

    @Test
    public void testDeflateCompressorInputStreamWithReadByte() throws IOException {
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[10];
        int read = compressorInputStream.read(buffer, 0, buffer.length);
        assertSame(data, buffer);
        assertEquals(data.length, read);

        byte b = compressorInputStream.read();
        assertEquals(0x01, b);

        b = compressorInputStream.read();
        assertEquals(0x02, b);

        b = compressorInputStream.read();
        assertEquals(-1, b);

        b = compressorInputStream.read();
        assertNull(b);
    }

    @Test
    public void testDeflateCompressorInputStreamWithReadAndClose() throws IOException {
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[10];
        int read = compressorInputStream.read(buffer, 0, buffer.length);
        assertSame(data, buffer);
        assertEquals(data.length, read);

        compressorInputStream.close();

        byte b = compressorInputStream.read();
        assertNull(b);
    }

    @Test
    public void testDeflateCompressorInputStreamWithAvailable() throws IOException {
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

        assertEquals(data.length, compressorInputStream.available());

        compressorInputStream.read();
        compressorInputStream.read();

        assertEquals(data.length - 2, compressorInputStream.available());
    }

    @Test
    public void testDeflateCompressorInputStreamWithSkip() throws IOException {
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

        long skipped = compressorInputStream.skip(5);
        assertEquals(5, skipped);

        long skipped2 = compressorInputStream.skip(data.length - 5);
        assertEquals(data.length - 5, skipped2);

        long skipped3 = compressorInputStream.skip(-5);
        assertEquals(0, skipped3);
    }

    @Test
    public void testDeflateCompressorInputStreamWithClose() throws IOException {
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

        compressorInputStream.close();

        byte[] buffer = new byte[10];
        int read = compressorInputStream.read(buffer, 0, buffer.length);
        assertNull(read);
    }

    @Test(expected = IOException.class)
    public void testDeflateCompressorInputStreamWithReadBeyondAvailable() throws IOException {
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[11];
        int read = compressorInputStream.read(buffer, 0, buffer.length);
        assertNull(read);

        byte b = compressorInputStream.read();
        assertEquals(0x01, b);

        compressorInputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[] { 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f }, buffer);

        byte b2 = compressorInputStream.read();
        assertNull(b2);

        compressorInputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[] {}, buffer);
    }

    @Test(expected = IOException.class)
    public void testDeflateCompressorInputStreamWithReadNegativeOffset() throws IOException {
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

        compressorInputStream.read(new byte[10], -1, 5);
    }

    @Test(expected = IOException.class)
    public void testDeflateCompressorInputStreamWithReadNegativeLength() throws IOException {
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

        compressorInputStream.read(new byte[10], 0, -5);
    }

    @Test(expected = IOException.class)
    public void testDeflateCompressorInputStreamWithReadNullBuffer() throws IOException {
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

        compressorInputStream.read(null, 0, 10);
    }

}