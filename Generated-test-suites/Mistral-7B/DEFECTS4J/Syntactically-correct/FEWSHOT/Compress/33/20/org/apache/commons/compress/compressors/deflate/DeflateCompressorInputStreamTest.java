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

import org.junit.Test;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testNewInstance() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{-37, 78, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        assertNotNull(deflateCompressorInputStream);
    }

    @Test
    public void testNewInstanceWithParameters() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{-37, 78, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        DeflateParameters parameters = new DeflateParameters();
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, parameters);
        assertNotNull(deflateCompressorInputStream);
    }

    @Test(expected = NullPointerException.class)
    public void testNewInstanceNullInputStream() {
        new DeflateCompressorInputStream(null);
    }

    @Test
    public void testRead() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{-37, 78, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[1];
        int read = deflateCompressorInputStream.read(buffer, 0, 1);
        assertEquals(1, read);
        assertEquals((byte) 1, buffer[0]);
    }

    @Test
    public void testReadZero() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{-37, 78, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[1];
        int read = deflateCompressorInputStream.read(buffer, 0, 0);
        assertEquals(-1, read);
    }

    @Test
    public void testReadAllBytes() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{-37, 78, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[12];
        int read = deflateCompressorInputStream.read(buffer, 0, 12);
        assertEquals(12, read);
        for (int i = 0; i < buffer.length; i++) {
            assertEquals(i + 1, buffer[i]);
        }
    }

    @Test
    public void testReadBeyondAvailable() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{-37, 78, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[12];
        int read = deflateCompressorInputStream.read(buffer, 0, 13);
        assertEquals(-1, read);
    }

    @Test
    public void testSkip() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{-37, 78, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        long skipped = deflateCompressorInputStream.skip(5);
        assertEquals(5, skipped);
    }

    @Test
    public void testAvailable() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{-37, 78, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        int available = deflateCompressorInputStream.available();
        assertEquals(12, available);
    }

    @Test
    public void testClose() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{-37, 78, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        deflateCompressorInputStream.close();
    }

    @Test
    public void testSignatureCheckForZlibHeader() {
        byte[] zlibSignature = new byte[]{-37, 78};
        byte[] deflateData = new byte[]{0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        byte[] data = new byte[zlibSignature.length + deflateData.length];
        System.arraycopy(zlibSignature, 0, data, 0, zlibSignature.length);
        System.arraycopy(deflateData, 0, data, zlibSignature.length, deflateData.length);
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        assertTrue(deflateCompressorInputStream.checkSignature(zlibSignature));
    }

    @Test
    public void testSignatureCheckForNoZlibHeader() {
        byte[] deflateData = new byte[]{0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        byte[] data = new byte[deflateData.length];
        System.arraycopy(deflateData, 0, data, 0, deflateData.length);
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        assertFalse(deflateCompressorInputStream.checkSignature(new byte[]{-37, 78}));
    }
}