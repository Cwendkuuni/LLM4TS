package org.apache.commons.compress.compressors;

import org.apache.commons.compress.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for CompressorStreamFactory.
 */
public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_GZIP() throws IOException, CompressorException {
        String name = CompressorStreamFactory.GZIP;
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        CompressorInputStream compressorInputStream =
                new CompressorStreamFactory().createCompressorInputStream(name, inputStream);
        Assert.assertNotNull(compressorInputStream);
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStream_BZIP2() throws IOException, CompressorException {
        String name = CompressorStreamFactory.BZIP2;
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        CompressorInputStream compressorInputStream =
                new CompressorStreamFactory().createCompressorInputStream(name, inputStream);
        Assert.assertNotNull(compressorInputStream);
        compressorInputStream.close();
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_UnknownFormat() throws IOException, CompressorException {
        String name = "UNKNOWN";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        new CompressorStreamFactory().createCompressorInputStream(name, inputStream);
    }

    @Test
    public void testCreateCompressorInputStream_DetectFormat() throws IOException, CompressorException {
        InputStream inputStream = new ByteArrayInputStream(
                new byte[]{(byte) 0x42, (byte) 0x4A, (byte) 0x50, (byte) 0x42, (byte) 0x0A, (byte) 0x00,
                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x07, (byte) 0x40,
                        (byte) 0x07, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x0C, (byte) 0x40,
                        (byte) 0x05, (byte) 0x00, (byte) 0x01, (byte) 0x0C, (byte) 0x40, (byte) 0x09,
                        (byte) 0x07, (byte) 0x40, (byte) 0x08, (byte) 0x0E, (byte) 0x00, (byte) 0x02,
                        (byte) 0x0E, (byte) 0x00, (byte) 0x03, (byte) 0x0E, (byte) 0x00, (byte) 0x04,
                        (byte) 0x0E, (byte) 0x00, (byte) 0x05, (byte) 0x0E, (byte) 0x00, (byte) 0x06,
                        (byte) 0x0E, (byte) 0x00, (byte) 0x07, (byte) 0x0E, (byte) 0x00, (byte) 0x08,
                        (byte) 0x0E, (byte) 0x00, (byte) 0x09});
        CompressorInputStream compressorInputStream =
                new CompressorStreamFactory().createCompressorInputStream(inputStream);
        String detectedFormat = compressorInputStream.getFormatName();
        Assert.assertEquals(CompressorStreamFactory.GZIP, detectedFormat);
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStream_GZIP() throws IOException, CompressorException {
        String name = CompressorStreamFactory.GZIP;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream =
                new CompressorStreamFactory().createCompressorOutputStream(name, outputStream);
        compressorOutputStream.write("Test data".getBytes());
        compressorOutputStream.finish();
        compressorOutputStream.close();
        Assert.assertEquals("Test data", new String(IOUtils.toByteArray(new ByteArrayInputStream(outputStream.toByteArray()))));
    }

    @Test
    public void testCreateCompressorOutputStream_BZIP2() throws IOException, CompressorException {
        String name = CompressorStreamFactory.BZIP2;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream =
                new CompressorStreamFactory().createCompressorOutputStream(name, outputStream);
        compressorOutputStream.write("Test data".getBytes());
        compressorOutputStream.finish();
        compressorOutputStream.close();
        Assert.assertEquals("Test data", new String(IOUtils.toByteArray(new ByteArrayInputStream(outputStream.toByteArray()))));
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStream_UnknownFormat() throws IOException, CompressorException {
        String name = "UNKNOWN";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        new CompressorStreamFactory().createCompressorOutputStream(name, outputStream);
    }

    @Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory1 = new CompressorStreamFactory();
        Assert.assertFalse(factory1.getDecompressConcatenated());

        CompressorStreamFactory factory2 = new CompressorStreamFactory(true);
        Assert.assertTrue(factory2.getDecompressConcatenated());
    }
}