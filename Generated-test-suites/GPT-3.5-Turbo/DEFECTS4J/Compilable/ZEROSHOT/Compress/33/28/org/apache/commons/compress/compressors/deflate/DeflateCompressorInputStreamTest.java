package org.apache.commons.compress.compressors.deflate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class DeflateCompressorInputStreamTest {

    private DeflateCompressorInputStream deflateInputStream;

    @Before
    public void setUp() {
        byte[] compressedData = {120, -100, -38, 126, 0, 1, 2, 3, 4, 5};
        InputStream inputStream = new ByteArrayInputStream(compressedData);
        deflateInputStream = new DeflateCompressorInputStream(inputStream);
    }

    @Test
    public void testRead() throws IOException {
        int bytesRead = deflateInputStream.read();
        Assert.assertEquals(120, bytesRead);
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] buffer = new byte[5];
        int bytesRead = deflateInputStream.read(buffer);
        Assert.assertEquals(5, bytesRead);
    }

    @Test
    public void testSkip() throws IOException {
        long skippedBytes = deflateInputStream.skip(5);
        Assert.assertEquals(5, skippedBytes);
    }

    @Test
    public void testAvailable() throws IOException {
        int availableBytes = deflateInputStream.available();
        Assert.assertEquals(1024, availableBytes);
    }

    @Test
    public void testClose() throws IOException {
        deflateInputStream.close();
        // Ensure that the stream is closed and no further operations are possible
        try {
            int bytesRead = deflateInputStream.read();
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected IOException
        }
    }
}