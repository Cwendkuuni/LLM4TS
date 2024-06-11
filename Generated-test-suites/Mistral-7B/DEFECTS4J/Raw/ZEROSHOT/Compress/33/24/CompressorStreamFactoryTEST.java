package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

/**
 * Test class for CompressorStreamFactory.
 */
public class CompressorStreamFactoryTest {

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorInputStream(String, InputStream)}.
     * @throws IOException
     * @throws DataFormatException
     */
    @org.junit.Test
    public void testCreateCompressorInputStream() throws IOException, DataFormatException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        // Test with valid compressor names
        InputStream input = new ByteArrayInputStream("Test data".getBytes());
        CompressorInputStream compressorInputStream = null;

        compressorInputStream = factory.createCompressorInputStream(CompressorStreamFactory.GZIP, input);
        assertNotNull(compressorInputStream);
        assertEquals("Test data", IOUtils.toString(compressorInputStream));

        compressorInputStream = factory.createCompressorInputStream(CompressorStreamFactory.BZIP2, input);
        assertNotNull(compressorInputStream);
        assertEquals("Test data", IOUtils.toString(compressorInputStream));

        compressorInputStream = factory.createCompressorInputStream(CompressorStreamFactory.Z, input);
        assertNotNull(compressorInputStream);
        assertEquals("Test data", IOUtils.toString(compressorInputStream));

        compressorInputStream = factory.createCompressorInputStream(CompressorStreamFactory.DEFLATE, input);
        assertNotNull(compressorInputStream);
        assertEquals("Test data", IOUtils.toString(compressorInputStream));

        compressorInputStream = factory.createCompressorInputStream(CompressorStreamFactory.XZ, input);
        assertNotNull(compressorInputStream);
        // XZ is not supported as an input stream type, so just check that it doesn't throw an exception

        compressorInputStream = factory.createCompressorInputStream(CompressorStreamFactory.PACK200, input);
        assertNotNull(compressorInputStream);
        // PACK200 is not supported as an input stream type, so just check that it doesn't throw an exception

        compressorInputStream = factory.createCompressorInputStream(CompressorStreamFactory.SNAPPY_FRAMED, input);
        assertNotNull(compressorInputStream);
        // SNAPPY_FRAMED is not supported as an input stream type, so just check that it doesn't throw an exception

        compressorInputStream = factory.createCompressorInputStream(CompressorStreamFactory.SNAPPY_RAW, input);
        assertNotNull(compressorInputStream);
        // SNAPPY_RAW is not supported as an input stream type, so just check that it doesn't throw an exception

        compressorInputStream = factory.createCompressorInputStream(" unknown", input);
        assertNull(compressorInputStream);

        // Test with null input stream
        input = null;
        compressorInputStream = factory.createCompressorInputStream(CompressorStreamFactory.GZIP, input);
        assertNull(compressorInputStream);

        // Test with invalid compressor name
        compressorInputStream = factory.createCompressorInputStream("invalid", input);
        assertNull(compressorInputStream);

        // Test with compressor name that is not a String
        Object name = 123;
        compressorInputStream = factory.createCompressorInputStream(name, input);
        assertNull(compressorInputStream);

        // Test with null output stream
        OutputStream output = null;
        CompressorOutputStream compressorOutputStream = null;

        compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, output);
        assertNull(compressorOutputStream);

        compressorOutputStream = factory.createCompressorOutputStream("GZIP", null);
        assertNull(compressorOutputStream);

        compressorOutputStream = factory.createCompressorOutputStream("unknown", output);
        assertNull(compressorOutputStream);

        compressorOutputStream = factory.createCompressorOutputStream((Object) 123, output);
        assertNull(compressorOutputStream);

        // Test with compressor name that is not a String
        compressorOutputStream = factory.createCompressorOutputStream((String) 123, output);
        assertNull(compressorOutputStream);

        // Test creating a compressor output stream with a known compressor name
        output = new ByteArrayOutputStream();
        compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, output);
        assertNotNull(compressorOutputStream);

        // Test that compressor stream factory can be used with a custom, non-default decompressConcatenated value
        CompressorStreamFactory factoryWithCustomDecompressConcatenated = new CompressorStreamFactory(true);
        compressorInputStream = factoryWithCustomDecompressConcatenated.createCompressorInputStream(CompressorStreamFactory.BZIP2, input);
        compressorOutputStream = factoryWithCustomDecompressConcatenated.createCompressorOutputStream(CompressorStreamFactory.BZIP2, output);
        byte[] testData = "Test data".getBytes();
        compressorInputStream.write(testData);
        compressorInputStream.close();
        assertEquals(testData.length, compressorOutputStream.size());
        assertEquals(testData, IOUtils.toByteArray(compressorOutputStream));
    }

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorOutputStream(String, OutputStream)}.
     * @throws IOException
     */
    @org.junit.Test
    public void testCreateCompressorOutputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        // Test with valid compressor names
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = null;

        compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, output);
        assertNotNull(compressorOutputStream);
        compressorOutputStream.finish();
        assertEquals(0, compressorOutputStream.size());

        compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.BZIP2, output);
        assertNotNull(compressorOutputStream);
        compressorOutputStream.finish();
        assertEquals(0, compressorOutputStream.size());

        compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.Z, output);
        assertNotNull(compressorOutputStream);
        compressorOutputStream.finish();
        assertEquals(0, compressorOutputStream.size());

        compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.DEFLATE, output);
        assertNotNull(compressorOutputStream);
        compressorOutputStream.finish();
        assertEquals(0, compressorOutputStream.size());

        compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.XZ, output);
        assertNotNull(compressorOutputStream);
        compressorOutputStream.finish();
        assertEquals(0, compressorOutputStream.size());

        compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.PACK200, output);
        assertNotNull(compressorOutputStream);
        compressorOutputStream.finish();
        assertEquals(0, compressorOutputStream.size());

        compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.SNAPPY_RAW, output);
        assertNotNull(compressorOutputStream);
        compressorOutputStream.finish();
        assertEquals(0, compressorOutputStream.size());

        compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.SNAPPY_FRAMED, output);
        assertNotNull(compressorOutputStream);
        compressorOutputStream.finish();
        assertEquals(0, compressorOutputStream.size());

        // Test with null output stream
        output = null;
        compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, output);
        assertNull(compressorOutputStream);

        // Test with invalid compressor name
        compressorOutputStream = factory.createCompressorOutputStream("invalid", output);
        assertNull(compressorOutputStream);

        // Test with compressor name that is not a String
        Object name = 123;
        compressorOutputStream = factory.createCompressorOutputStream(name, output);
        assertNull(compressorOutputStream);

        // Test with compressor name that is not supported as an output stream type
        compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.LZMA, output);
        assertNull(compressorOutputStream);

        // Test with compressor name that is not a supported compressor type
        compressorOutputStream = factory.createCompressorOutputStream("unknown", output);
        assertNull(compressorOutputStream);

        // Test with compressor name that is not a String
        compressorOutputStream = factory.createCompressorOutputStream((String) 123, output);
        assertNull(compressorOutputStream);

        // Test creating a compressor output stream with a custom, non-default decompressConcatenated value
        CompressorStreamFactory factoryWithCustomDecompressConcatenated = new CompressorStreamFactory(true);
        compressorOutputStream = factoryWithCustomDecompressConcatenated.createCompressorOutputStream(CompressorStreamFactory.BZIP2, output);
        byte[] testData = "Test data".getBytes();
        compressorOutputStream.write(testData);
        compressorOutputStream.close();
        assertEquals(testData.length, compressorOutputStream.size());
        assertEquals(testData, IOUtils.toByteArray(compressorOutputStream));

        // Test writing data to compressor output stream
        compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, output);
        compressorOutputStream.write(testData);
        compressorOutputStream.finish();
        assertEquals(testData.length, compressorOutputStream.size());
        assertEquals(testData, IOUtils.toByteArray(compressorOutputStream));

        // Test writing large data to compressor output stream
        byte[] largeTestData = new byte[1024 * 1024];
        compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, output);
        compressorOutputStream.write(largeTestData);
        compressorOutputStream.finish();
        assertEquals(largeTestData.length, compressorOutputStream.size());
    }

    /**
     * Test method for {@link CompressorStreamFactory#getDecompressConcatenated()}.
     */
    @org.junit.Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertFalse(factory.getDecompressConcatenated());

        CompressorStreamFactory factoryWithCustomDecompressConcatenated = new CompressorStreamFactory(true);
        assertTrue(factoryWithCustomDecompressConcatenated.getDecompressConcatenated());
    }
}