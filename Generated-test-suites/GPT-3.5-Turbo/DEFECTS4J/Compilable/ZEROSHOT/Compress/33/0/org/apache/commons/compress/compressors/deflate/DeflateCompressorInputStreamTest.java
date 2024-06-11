package org.apache.commons.compress.compressors.deflate;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

public class DeflateCompressorInputStreamTest {

    private InputStream inputStream;

    @Before
    public void setUp() {
        // Create sample compressed data
        byte[] data = new byte[]{31, -117, 8, 0, 0, 0, 0, 0, 0, 0, 99, 96, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -62, -58, 62, 96, 70, 0, 0, 0};

        inputStream = new ByteArrayInputStream(data);
    }

    @Test
    public void testRead() throws IOException {
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        int b1 = deflateInputStream.read();
        assertEquals(31, b1);

        int b2 = deflateInputStream.read();
        assertEquals(-117, b2);
    }

    @Test
    public void testReadByteArrayIntInt() throws IOException {
        byte[] buf = new byte[10];

        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        int readBytes = deflateInputStream.read(buf, 0, buf.length);
        assertEquals(10, readBytes);

        assertArrayEquals(new byte[]{31, -117, 8, 0, 0, 0, 0, 0, 0, 0}, buf);
    }

    @Test
    public void testSkip() throws IOException {
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        long skipped = deflateInputStream.skip(5);
        assertEquals(5, skipped);

        int b = deflateInputStream.read();
        assertEquals(0, b);
    }

    @Test
    public void testAvailable() throws IOException {
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        int available = deflateInputStream.available();
        assertEquals(94, available);

        deflateInputStream.read(new byte[50]);
        available = deflateInputStream.available();
        assertEquals(44, available);
    }

    @Test
    public void testClose() throws IOException {
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        deflateInputStream.close();
        
        int b = deflateInputStream.read();
        assertEquals(-1, b);
    }
}