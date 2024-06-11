package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testNewInstanceWithInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{-37, 78, 0, 0, 0, 0, 0, 0, 0});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        assertTrue(deflateCompressorInputStream instanceof DeflateCompressorInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithNullInputStream() {
        new DeflateCompressorInputStream(null);
    }

    @Test
    public void testReadSingleByte() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x78});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        int ret = deflateCompressorInputStream.read();
        assertEquals((int) 0x78, ret);
    }

    @Test
    public void testReadMultipleBytes() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x78, 0x73, 0x73, 0x73});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[4];
        int len = deflateCompressorInputStream.read(buffer, 0, buffer.length);
        assertEquals(len, buffer.length);
        for (int i = 0; i < buffer.length; i++) {
            assertEquals((int) buffer[i], (int) (0x78 + i));
        }
    }

    @Test
    public void testSkip() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x78, 0x73, 0x73, 0x73});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        long skipped = deflateCompressorInputStream.skip(3L);
        assertEquals(3L, skipped);
    }

    @Test
    public void testAvailable() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x78, 0x73, 0x73, 0x73});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        long available = deflateCompressorInputStream.available();
        assertEquals(3L, available);
    }

    @Test
    public void testClose() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x78, 0x73, 0x73, 0x73});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        deflateCompressorInputStream.close();
        assertNull(deflateCompressorInputStream.in);
    }

    @Test
    public void testNewInstanceWithInputStreamAndDeflateParameters() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x78, 0x0, 0x14, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1});
        DeflateParameters deflateParameters = new DeflateParameters();
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, deflateParameters);
        assertTrue(deflateCompressorInputStream.in instanceof InflaterInputStream);
    }

    @Test
    public void testCheckZlibSignature() throws IOException {
        byte[] zlibSignature = new byte[]{0x78, 0x5, 0x0, 0x1, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1};
        byte[] deflatedData = new byte[]{0x78, 0x73, 0x73, 0x73};

        ByteArrayInputStream zlibInputStream = new ByteArrayInputStream(zlibSignature);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(deflatedData));

        boolean result = deflateCompressorInputStream.checkZlibSignature(zlibInputStream);
        assertTrue(result);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Deflater deflater = new Deflater();
        deflater.setInput(deflatedData);
        deflater.finish();
        byte[] compressedData = new byte[deflater.getTotalIn()];
        deflater.deflate(compressedData, 0, deflater.getTotalIn());
        ByteArrayInputStream compressedInputStream = new ByteArrayInputStream(compressedData);

        result = deflateCompressorInputStream.checkZlibSignature(compressedInputStream);
        assertTrue(result);

        ByteArrayInputStream invalidZlibInputStream = new ByteArrayInputStream(new byte[]{0x78, 0x5, 0x0, (byte) 0x7F, 0x1, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1});
        result = deflateCompressorInputStream.checkZlibSignature(invalidZlibInputStream);
        assertFalse(result);

        ByteArrayInputStream noZlibInputStream = new ByteArrayInputStream(new byte[]{0x78, 0x73, 0x73, 0x73});
        result = deflateCompressorInputStream.checkZlibSignature(noZlibInputStream);
        assertFalse(result);
    }

    @Test(expected = IOException.class)
    public void testNewInstanceWithInvalidInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{-1});
        new DeflateCompressorInputStream(inputStream);
    }
}