package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
    public void testNewInstanceWithInputStream() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0xDA, (byte) 0x10, (byte) 0x00, (byte) 0x00});
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        assertNotNull(compressorInputStream);
    }

    @Test
    public void testNewInstanceWithInputStreamAndDeflateParameters() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0xDA, (byte) 0x10, (byte) 0x00, (byte) 0x00});
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
        assertNotNull(compressorInputStream);
    }

    @Test(expected = NullPointerException.class)
    public void testNewInstanceWithNullInputStream() {
        new DeflateCompressorInputStream(null);
    }

    @Test
    public void testRead() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x00, 0x01});
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[1];
        compressorInputStream.read(buffer, 0, 1);
        assertEquals((byte) 0x01, buffer[0]);
    }

    @Test
    public void testReadWithByteArray() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x00, 0x01, 0x02, 0x03});
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[4];
        compressorInputStream.read(buffer, 0, 4);
        assertEquals(new byte[]{0x01, 0x02, 0x03, 0x00}, buffer);
    }

    @Test
    public void testSkip() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x00, 0x01, 0x02, 0x03});
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        compressorInputStream.skip(2L);
        byte[] buffer = new byte[3];
        compressorInputStream.read(buffer, 0, 3);
        assertEquals(new byte[]{0x02, 0x03, 0x00}, buffer);
    }

    @Test
    public void testAvailable() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x00, 0x01, 0x02, 0x03});
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        assertEquals(3, compressorInputStream.available());
    }

    @Test
    public void testClose() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x00, 0x01, 0x02, 0x03});
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        compressorInputStream.close();
        assertNull(compressorInputStream.in);
    }

    @Test
    public void testSignatureWithZlibHeader() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0xDA, (byte) 0x10, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08});
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
        byte[] signature = new byte[16];
        compressorInputStream.read(signature, 0, 16);
        byte[] expectedSignature = new byte[]{(byte) 0x78, (byte) 0xDA, (byte) 0x10, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08};
        assertEquals(expectedSignature, signature);
    }

    @Test
    public void testSignatureWithoutZlibHeader() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08});
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters(false));
        byte[] signature = new byte[8];
        compressorInputStream.read(signature, 0, 8);
        assertEquals(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08}, signature);
    }
}