package org.apache.commons.compress.compressors;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.CompressorFactory;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;

public class CompressorStreamFactoryTest {

    @org.junit.Test
    public void testCreateCompressorInputStreamBZIP2() throws IOException {
        String testData = "This is a test string.";
        InputStream input = new ByteArrayInputStream(testData.getBytes());
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                                                          .createCompressorInputStream(CompressorStreamFactory.BZIP2, input);
        byte[] output = new byte[testData.length()];
        int length = IOUtils.readFully(compressorInputStream, output);
        compressorInputStream.close();
        assertEquals(testData, new String(output, 0, length));
    }

    @org.junit.Test
    public void testCreateCompressorInputStreamBZIP2_WrongName() throws IOException {
        InputStream input = new ByteArrayInputStream("Test data.".getBytes());
        assertThrows(CompressorException.class, () -> {
            new CompressorStreamFactory().createCompressorInputStream("WRONG_NAME", input);
        });
    }

    @org.junit.Test
    public void testCreateCompressorOutputStreamBZIP2() throws IOException {
        String testData = "This is a test string.";
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                                                         .createCompressorOutputStream(CompressorStreamFactory.BZIP2, output);
        compressorOutputStream.write(testData.getBytes());
        compressorOutputStream.finish();
        compressorOutputStream.close();
        byte[] compressedData = output.toByteArray();
        InputStream input = new ByteArrayInputStream(compressedData);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                                                          .createCompressorInputStream(CompressorStreamFactory.BZIP2, input);
        byte[] resultData = new byte[testData.length()];
        int length = IOUtils.readFully(compressorInputStream, resultData);
        compressorInputStream.close();
        assertEquals(testData, new String(resultData, 0, length));
    }

    // Add similar test cases for other compression methods (GZIP, XZ, LZMA, PACK200, SNAPPY_RAW, SNAPPY_FRAMED, DEFLATE)

    @org.junit.Test
    public void testCreateCompressorInputStreamUnknownFormat() throws IOException {
        InputStream input = new ByteArrayInputStream("Test data.".getBytes());
        assertThrows(CompressorException.class, () -> {
            new CompressorStreamFactory().createCompressorInputStream("UNKNOWN_FORMAT", input);
        });
    }

    @org.junit.Test
    public void testCreateCompressorOutputStreamUnknownFormat() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        assertThrows(CompressorException.class, () -> {
            new CompressorStreamFactory().createCompressorOutputStream("UNKNOWN_FORMAT", output);
        });
    }

    @org.junit.Test
    public void testCreateCompressorInputStreamNullInputStream() {
        assertThrows(IllegalArgumentException.class, () -> {
            new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.BZIP2, null);
        });
    }

    @org.junit.Test
    public void testCreateCompressorOutputStreamNullOutputStream() {
        assertThrows(IllegalArgumentException.class, () -> {
            new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.BZIP2, null);
        });
    }

    @org.junit.Test
    public void testDecompressConcatenated() throws IOException {
        String testData = "Test data 1\nTest data 2";
        String concatenatedData = testData + testData;
        InputStream input = new ByteArrayInputStream(concatenatedData.getBytes());
        CompressorInputStream compressorInputStream = new CompressorStreamFactory(true)
                                                          .createCompressorInputStream(CompressorStreamFactory.GZIP, input);
        byte[] output = new byte[testData.length() * 2];
        int length = IOUtils.readFully(compressorInputStream, output);
        compressorInputStream.close();
        assertEquals(testData, new String(output, 0, length));
    }
}