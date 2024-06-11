package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.*;

import org.junit.Test;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testNewInstanceWithInputStream() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("deflate compression test".getBytes());
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        assertNotNull(deflateCompressorInputStream);
    }

    @Test(expected = NullPointerException.class)
    public void testNewInstanceWithNullInputStream() {
        new DeflateCompressorInputStream(null);
    }

    @Test
    public void testNewInstanceWithInputStreamAndParameters() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("deflate compression test".getBytes());
        DeflateParameters deflateParameters = new DeflateParameters();
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, deflateParameters);
        assertNotNull(deflateCompressorInputStream);
    }

    @Test(expected = NullPointerException.class)
    public void testNewInstanceWithNullInputStreamAndParameters() {
        new DeflateCompressorInputStream(null, new DeflateParameters());
    }

    @Test
    public void testRead() throws Exception {
        String testData = "deflate compression test";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[1];
        int read = 0;
        while ((read = deflateCompressorInputStream.read()) != -1) {
            assertEquals((byte) testData.charAt(read - deflateCompressorInputStream.getByteOffset()), (byte) testData.charAt(read));
            buffer[0] = (byte) testData.charAt(read + deflateCompressorInputStream.getByteOffset());
            assertEquals(buffer[0], (byte) testData.charAt(read + deflateCompressorInputStream.getByteOffset()));
        }
    }

    @Test
    public void testReadByteArray() throws Exception {
        String testData = "deflate compression test";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[testData.length()];
        int length = deflateCompressorInputStream.read(buffer, 0, buffer.length);
        assertEquals(testData.length(), length);

        for (int i = 0; i < length; i++) {
            assertEquals((byte) testData.charAt(i), buffer[i]);
        }
    }

    @Test
    public void testSkip() throws Exception {
        String testData = "deflate compression test";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        long skipped = deflateCompressorInputStream.skip(testData.length());
        assertEquals(testData.length(), skipped);
    }

    @Test
    public void testAvailable() throws Exception {
        String testData = "deflate compression test";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        int available = deflateCompressorInputStream.available();
        assertEquals(testData.length(), available);
    }

    @Test
    public void testClose() throws Exception {
        String testData = "deflate compression test";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        deflateCompressorInputStream.close();
        assertThrows(IOException.class, () -> deflateCompressorInputStream.read());
    }
}