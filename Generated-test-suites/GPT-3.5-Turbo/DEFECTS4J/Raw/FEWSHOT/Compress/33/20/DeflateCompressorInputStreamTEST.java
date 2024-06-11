
package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.apache.commons.compress.compressors.CompressorInputStream;

import org.junit.Test;

public class DeflateCompressorInputStreamTest {
    
    @Test
    public void testRead() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        int result = -1;
        int expected = 1;

        result = deflateInputStream.read();
        assertEquals(expected, result);
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        int result = -1;

        result = deflateInputStream.read(buffer, offset, length);
        assertEquals(length, result);
        assertEquals(data[0], buffer[0]);
        assertEquals(data[1], buffer[1]);
        assertEquals(data[2], buffer[2]);
        assertEquals(data[3], buffer[3]);
        assertEquals(data[4], buffer[4]);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        long n = 3;
        long result = -1;

        result = deflateInputStream.skip(n);
        assertEquals(n, result);
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        int expected = 5;
        int result = -1;

        result = deflateInputStream.available();
        assertEquals(expected, result);
    }

    @Test
    public void testClose() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        deflateInputStream.close();
    }
}
