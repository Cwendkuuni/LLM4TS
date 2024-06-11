package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.*;

import org.junit.Test;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testDeflateCompressorInputStream() {
        InputStream inputStream = null;
        DeflateCompressorInputStream deflateCompressorInputStream = null;

        // Test null inputStream
        try {
            deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
            fail("Expected IllegalArgumentException for null inputStream");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test valid inputStream
        inputStream = new MockInputStream();
        deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
        assertNotNull(deflateCompressorInputStream);

        // Test close method
        deflateCompressorInputStream.close();
        assertFalse(deflateCompressorInputStream.isOpen());
    }

    @Test
    public void testReadMethod() throws IOException {
        InputStream inputStream = new MockInputStream();
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());

        byte[] b = new byte[1];
        int read = deflateCompressorInputStream.read();
        assertEquals(-1, read);

        b[0] = (byte) 0x01;
        inputStream.read(b, 0, 1);
        read = deflateCompressorInputStream.read();
        assertEquals(0x01, read);

        deflateCompressorInputStream.close();
    }

    @Test
    public void testReadByteArrayMethod() throws IOException {
        InputStream inputStream = new MockInputStream();
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());

        byte[] expected = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        byte[] buffer = new byte[3];

        inputStream.read(buffer, 0, 3);
        deflateCompressorInputStream.read(buffer, 0, 3);
        assertArrayEquals(expected, buffer);

        deflateCompressorInputStream.close();
    }

    @Test
    public void testSkipMethod() throws IOException {
        InputStream inputStream = new MockInputStream();
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());

        long expected = 10L;
        long skipped = deflateCompressorInputStream.skip(expected);
        assertEquals(expected, skipped);

        deflateCompressorInputStream.close();
    }

    @Test
    public void testAvailableMethod() throws IOException {
        InputStream inputStream = new MockInputStream();
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());

        long available = deflateCompressorInputStream.available();
        assertEquals(-1, available);

        inputStream.read(new byte[1][], 0, 1);
        available = deflateCompressorInputStream.available();
        assertEquals(0, available);

        deflateCompressorInputStream.close();
    }

    @Test
    public void testCloseMethod() throws IOException {
        InputStream inputStream = new MockInputStream();
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());

        boolean closed = deflateCompressorInputStream.isOpen();
        deflateCompressorInputStream.close();
        assertFalse(closed);

        try {
            deflateCompressorInputStream.read();
            fail("Expected IOException after close method call");
        } catch (IOException e) {
            // Expected
        }
    }
}

class MockInputStream extends InputStream {
    private int bytesRead = 0;
    private byte[] buffer;

    @Override
    public int read() throws IOException {
        if (buffer == null || bytesRead >= buffer.length) {
            return -1;
        }
        return buffer[bytesRead++];
    }

    @Override
    public int read(byte[] bytes, int offset, int length) throws IOException {
        if (buffer == null) {
            return -1;
        }
        if ((offset < 0) || (length < 0) || ((offset + length) > buffer.length)) {
            throw new IndexOutOfBoundsException();
        }

        int bytesToRead = Math.min(length, buffer.length - offset);
        System.arraycopy(buffer, offset, bytes, offset, bytesToRead);
        bytesRead += bytesToRead;

        return bytesToRead;
    }

    @Override
    public void close() throws IOException {
        // Empty
    }

    @Override
    public synchronized void mark(int readlimit) {
        // Empty
    }

    @Override
    public synchronized void reset() throws IOException {
        // Empty
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }
}