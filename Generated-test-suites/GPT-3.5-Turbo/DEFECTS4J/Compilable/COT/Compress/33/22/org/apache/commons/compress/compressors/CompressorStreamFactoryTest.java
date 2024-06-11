package org.apache.commons.compress.compressors;

import org.apache.commons.compress.compressors.*;
import org.apache.commons.compress.compressors.bzip2.*;
import org.apache.commons.compress.compressors.deflate.*;
import org.apache.commons.compress.compressors.gzip.*;
import org.apache.commons.compress.compressors.lzma.*;
import org.apache.commons.compress.compressors.pack200.*;
import org.apache.commons.compress.compressors.snappy.*;
import org.apache.commons.compress.compressors.xz.*;
import org.apache.commons.compress.compressors.z.*;
import org.junit.Assert;
import org.junit.Test;
import java.io.*;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream() throws CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        byte[] data = "Test data".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);

        // Test createCompressorInputStream with autodetection    
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(inputStream);
        Assert.assertNotNull(compressorInputStream);
        Assert.assertEquals(GzipCompressorInputStream.class, compressorInputStream.getClass());

        // Test createCompressorInputStream with known compressor name
        compressorInputStream = factory.createCompressorInputStream(CompressorStreamFactory.BZIP2, inputStream);
        Assert.assertNotNull(compressorInputStream);
        Assert.assertEquals(BZip2CompressorInputStream.class, compressorInputStream.getClass());
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStreamUnknown() throws CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        byte[] data = "Test data".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);

        // Test createCompressorInputStream with unknown compressor name
        factory.createCompressorInputStream("unknown", inputStream);
    }

    @Test
    public void testCreateCompressorOutputStream() throws CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        byte[] data = "Test data".getBytes();
        OutputStream outputStream = new ByteArrayOutputStream();

        // Test createCompressorOutputStream with known compressor name
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, outputStream);
        Assert.assertNotNull(compressorOutputStream);
        Assert.assertEquals(GzipCompressorOutputStream.class, compressorOutputStream.getClass());
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStreamUnknown() throws CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        byte[] data = "Test data".getBytes();
        OutputStream outputStream = new ByteArrayOutputStream();

        // Test createCompressorOutputStream with unknown compressor name
        factory.createCompressorOutputStream("unknown", outputStream);
    }
}
