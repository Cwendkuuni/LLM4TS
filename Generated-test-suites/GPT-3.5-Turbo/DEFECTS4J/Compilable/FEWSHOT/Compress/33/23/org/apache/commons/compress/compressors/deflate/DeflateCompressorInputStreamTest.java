package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class DeflateCompressorInputStreamTest {

    private DeflateCompressorInputStream deflateCompressorInputStream;
    private ByteArrayInputStream byteArrayInputStream;

    @Before
    public void setUp() {
        byte[] data = new byte[100];
        byteArrayInputStream = new ByteArrayInputStream(data);
        deflateCompressorInputStream = new DeflateCompressorInputStream(byteArrayInputStream);
    }

    @Test
    public void testRead() throws IOException {
        int result = deflateCompressorInputStream.read();
        assertTrue(result >= 0);
    }

    @Test
    public void testReadWithByteArray() throws IOException {
        byte[] buffer = new byte[10];
        int result = deflateCompressorInputStream.read(buffer);
        assertTrue(result >= 0);
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 5;
        int result = deflateCompressorInputStream.read(buffer, offset, length);
        assertTrue(result >= 0);
    }

    @Test
    public void testSkip() throws IOException {
        long result = deflateCompressorInputStream.skip(5);
        assertTrue(result >= 0);
    }

    @Test
    public void testAvailable() throws IOException {
        int result = deflateCompressorInputStream.available();
        assertTrue(result >= 0);
    }

    @Test
    public void testClose() throws IOException {
        deflateCompressorInputStream.close();
        // Check that the stream is closed
        assertTrue(byteArrayInputStream.available() == 0);
    }
}