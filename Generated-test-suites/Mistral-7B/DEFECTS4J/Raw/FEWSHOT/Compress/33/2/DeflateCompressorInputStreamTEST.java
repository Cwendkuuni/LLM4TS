package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.DeflateParameters;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test cases for DeflateCompressorInputStream.
 */
public class DeflateCompressorInputStreamTest {

    @Test
    public void testNewInstanceWithNullInputStream() {
        assertNull(new DeflateCompressorInputStream(null));
    }

    @Test
    public void testNewInstance() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'N', 'o', 't', ' ', 'D', 'e', 'f', 'l', 'a', 't', 'e', '\r', '\n'});
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        assertTrue(compressorInputStream instanceof DeflateCompressorInputStream);
        assertFalse(compressorInputStream instanceof InflaterInputStream);
        compressorInputStream.close();
    }

    @Test
    public void testNewInstanceWithDeflateParameter() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'N', 'o', 't', ' ', 'D', 'e', 'f', 'l', 'a', 't', 'e', '\r', '\n'});
        DeflateParameters parameters = new DeflateParameters();
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream, parameters);
        assertTrue(compressorInputStream instanceof DeflateCompressorInputStream);
        assertTrue(compressorInputStream instanceof InflaterInputStream);
        compressorInputStream.close();
    }

    @Test
    public void testNewInstanceWithZlibHeader() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'G', 'P', 'G', 'Z', 'G', 'P', 'G', 'Z', 0x19, 0x2a, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 'D', 'e', 'f', 'l', 'a', 't', 'e', '\r', '\n'});
        DeflateParameters parameters = new DeflateParameters();
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream, parameters);
        assertTrue(compressorInputStream instanceof DeflateCompressorInputStream);
        assertTrue(compressorInputStream instanceof InflaterInputStream);
        compressorInputStream.close();
    }

    @Test
    public void testNewInstanceWithoutZlibHeader() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'D', 'e', 'f', 'l', 'a', 't', 'e', '\r', '\n'});
        DeflateParameters parameters = new DeflateParameters();
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream, parameters);
        assertTrue(compressorInputStream instanceof DeflateCompressorInputStream);
        assertFalse(compressorInputStream instanceof InflaterInputStream);
        compressorInputStream.close();
    }

    @Test
    public void testRead() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'D', 'e', 'f', 'l', 'a', 't', 'e', '\r', '\n'});
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[1];
        int read = compressorInputStream.read(buffer);
        assertEquals(-1, read);
        compressorInputStream.close();

        inputStream = new ByteArrayInputStream(new byte[]{'D', 'e', 'f', 'l', 'a', 't', 'e'});
        compressorInputStream = new DeflateCompressorInputStream(inputStream);
        read = compressorInputStream.read(buffer);
        assertEquals((byte) 'd', buffer[0]);
        read = compressorInputStream.read(buffer);
        assertEquals((byte) 'e', buffer[0]);
        assertEquals(3, compressorInputStream.available());
        compressorInputStream.close();

        inputStream = new ByteArrayInputStream(new byte[]{'D', 'e', 'f', 'l', 'a', 't', 'e', '\r', '\n'});
        compressorInputStream = new DeflateCompressorInputStream(inputStream);
        read = compressorInputStream.read();
        assertEquals((byte) 'D', read);
        compressorInputStream.close();
    }

    @Test
    public void testReadWithEmptyBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'D', 'e', 'f', 'l', 'a', 't', 'e', '\r', '\n'});
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[0];
        int read = compressorInputStream.read(buffer);
        assertEquals(-1, read);
        compressorInputStream.close();
    }

    @Test
    public void testReadWithLargerBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'D', 'e', 'f', 'l', 'a', 't', 'e', '\r', '\n'});
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[3];
        int read = compressorInputStream.read(buffer);
        assertEquals(3, read);
        assertEquals((byte) 'D', buffer[0]);
        assertEquals((byte) 'e', buffer[1]);
        assertEquals((byte) 'f', buffer[2]);
        compressorInputStream.close();
    }

    @Test
    public void testReadWithPartiallyFilledBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'D', 'e', 'f', 'l', 'a', 't', 'e', '\r', '\n'});
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[3];
        int read = compressorInputStream.read(buffer);
        assertEquals(3, read);
        assertEquals((byte) 'D', buffer[0]);
        assertEquals((byte) 'e', buffer[1]);
        assertEquals((byte) 'f', buffer[2]);
        read = compressorInputStream.read(buffer);
        assertEquals(2, read);
        assertEquals((byte) 'l', buffer[0]);
        assertEquals((byte) 'a', buffer[1]);
        compressorInputStream.close();
    }

    @Test
    public void testSkip() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'D', 'e', 'f', 'l', 'a', 't', 'e', '\r', '\n'});
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        long skipped = compressorInputStream.skip(5);
        assertEquals(3, skipped);
        compressorInputStream.close();

        inputStream = new ByteArrayInputStream(new byte[]{'D', 'e', 'f', 'l', 'a', 't', 'e', '\r', '\n'});
        compressorInputStream = new DeflateCompressorInputStream(inputStream);
        skipped = compressorInputStream.skip(11);
        assertEquals(-1, skipped);
        compressorInputStream.close();
    }

    @Test
    public void testAvailable() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'D', 'e', 'f', 'l', 'a', 't', 'e', '\r', '\n'});
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        assertEquals(0, compressorInputStream.available());
        compressorInputStream.read();
        assertEquals(8, compressorInputStream.available());
        compressorInputStream.close();

        inputStream = new ByteArrayInputStream(new byte[]{'D', 'e', 'f', 'l', 'a', 't', 'e', '\r', '\n'});
        compressorInputStream = new DeflateCompressorInputStream(inputStream);
        assertEquals(9, compressorInputStream.available());
        compressorInputStream.read(new byte[3]);
        assertEquals(6, compressorInputStream.available());
        compressorInputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'D', 'e', 'f', 'l', 'a', 't', 'e', '\r', '\n'});
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        compressorInputStream.close();
        inputStream.close();
    }
}