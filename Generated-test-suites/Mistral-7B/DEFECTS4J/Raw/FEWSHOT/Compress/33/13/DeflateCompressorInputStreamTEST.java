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
import java.util.zip.Inflater;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testNewInstanceWithInputStream() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{-37, 78, 123, 9, 32, 125, 2, 255, -1});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        assertNotNull(deflateCompressorInputStream);
    }

    @Test
    public void testNewInstanceWithInputStreamAndParameters() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{-37, 78, 123, 9, 32, 125, 2, 255, -1});
        DeflateParameters parameters = new DeflateParameters();
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, parameters);
        assertNotNull(deflateCompressorInputStream);
    }

    @Test(expected = NullPointerException.class)
    public void testNewInstanceWithNullInputStream() {
        new DeflateCompressorInputStream(null);
    }

    @Test
    public void testReadMethod() throws IOException {
        byte[] inputData = new byte[]{-37, 78, 123, 9, 32, 125, 2, 255, -1};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        byte readByte = (byte) deflateCompressorInputStream.read();
        assertEquals((byte) 78, readByte);
    }

    @Test
    public void testReadMethodWithEndOfStream() throws IOException {
        byte[] inputData = new byte[]{-37, 78, 123, 9, 32, 125, 2, 255, -1};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        int read = deflateCompressorInputStream.read();
        assertEquals((int) -1, read);
        assertFalse(deflateCompressorInputStream.isOpen());
    }

    @Test
    public void testReadMethodWithByteArray() throws IOException {
        byte[] inputData = new byte[]{-37, 78, 123, 9, 32, 125, 2, 255, -1};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] outputData = new byte[inputData.length - 1];
        int bytesRead = deflateCompressorInputStream.read(outputData);
        assertEquals(inputData.length - 1, bytesRead);

        for (int i = 0; i < outputData.length; i++) {
            assertEquals(inputData[i + 1], outputData[i]);
        }
    }

    @Test
    public void testSkipMethod() throws IOException {
        byte[] inputData = new byte[]{-37, 78, 123, 9, 32, 125, 2, 255, -1};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        long skippedBytes = deflateCompressorInputStream.skip(3L);
        assertEquals(3L, skippedBytes);

        long skippedBytes2 = deflateCompressorInputStream.skip(inputData.length - 4);
        assertEquals(inputData.length - 4, skippedBytes2);

        assertFalse(deflateCompressorInputStream.isOpen());
    }

    @Test
    public void testAvailableMethod() throws IOException {
        byte[] inputData = new byte[]{-37, 78, 123, 9, 32, 125, 2, 255, -1};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        int available = deflateCompressorInputStream.available();
        assertEquals(inputData.length - 1, available);
    }

    @Test
    public void testCloseMethod() throws IOException {
        byte[] inputData = new byte[]{-37, 78, 123, 9, 32, 125, 2, 255, -1};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        deflateCompressorInputStream.close();
        assertFalse(deflateCompressorInputStream.isOpen());
    }

    @Test
    public void testCheckZlibSignature() {
        byte[] zlibSignature = new byte[]{-37, 78, 123};
        boolean isZlib = DeflateCompressorInputStream.isZlib(zlibSignature);
        assertTrue(isZlib);
    }

    @Test
    public void testCheckZlibSignatureWithWrongLength() {
        byte[] zlibSignature = new byte[]{-37, 78, 123, 125};
        boolean isZlib = DeflateCompressorInputStream.isZlib(zlibSignature);
        assertFalse(isZlib);
    }

    @Test
    public void testCheckZlibSignatureWithWrongFirstByte() {
        byte[] zlibSignature = new byte[]{-38, 78, 123};
        boolean isZlib = DeflateCompressorInputStream.isZlib(zlibSignature);
        assertFalse(isZlib);
    }
}