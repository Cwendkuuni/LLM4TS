package org.apache.commons.compress.compressors.deflate;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testDeflateCompressorInputStream_WithDefaultConstructor() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[10];
        int length;

        // Test reading data
        length = testStream.read(buffer, 0, buffer.length);
        Assert.assertNotEquals(-1, length);

        // Test reading data with offset
        length = testStream.read(buffer, 5, buffer.length - 5);
        Assert.assertNotEquals(-1, length);

        // Test reading data with custom buffer size
        length = testStream.read(new byte[1], 0, 1);

        // Test skipping data
        testStream.skip(10);

        // Test available data
        Assert.assertEquals(0, testStream.available());

        // Test closing the stream
        testStream.close();

        // Test reading data after closing the stream
        length = testStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(-1, length);
    }

    @Test
    public void testDeflateCompressorInputStream_WithCustomConstructor() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        DeflateParameters deflateParameters = new DeflateParameters();
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(inputStream, deflateParameters);

        byte[] buffer = new byte[10];
        int length;

        // Test reading data
        length = testStream.read(buffer, 0, buffer.length);
        Assert.assertNotEquals(-1, length);

        // Test reading data with offset
        length = testStream.read(buffer, 5, buffer.length - 5);
        Assert.assertNotEquals(-1, length);

        // Test reading data with custom buffer size
        length = testStream.read(new byte[1], 0, 1);

        // Test skipping data
        testStream.skip(10);

        // Test available data
        Assert.assertEquals(0, testStream.available());

        // Test closing the stream
        testStream.close();

        // Test reading data after closing the stream
        length = testStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(-1, length);
    }

    @Test(expected = IOException.class)
    public void testDeflateCompressorInputStream_WithInvalidConstructor() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(inputStream, null);
    }

    @Test(expected = NullPointerException.class)
    public void testDeflateCompressorInputStream_WithNullConstructorParameters() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(inputStream, null);
    }
}