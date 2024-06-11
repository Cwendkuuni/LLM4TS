package org.apache.commons.compress.compressors.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.SnappyCompressorInputStream;
import org.apache.commons.compress.compressors.XZCompressorInputStream;
import org.apache.commons.compress.compressors.XZCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream() throws IOException {
        // Test creating a compressor input stream from a known input stream
        InputStream input = new ByteArrayInputStream("Test data".getBytes());
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.DEFLATE, input);
        byte[] expected = "Test data".getBytes();
        byte[] actual = new byte[expected.length];
        IOUtils.readFully(compressorInputStream, actual);
        assertArrayEquals(expected, actual);
        compressorInputStream.close();

        // Test creating a compressor input stream from a known name and input stream
        InputStream input2 = new ByteArrayInputStream("Test data".getBytes());
        CompressorInputStream compressorInputStream2 = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.GZIP, input2);
        byte[] expected2 = "Test data".getBytes();
        byte[] actual2 = new byte[expected2.length];
        IOUtils.readFully(compressorInputStream2, actual2);
        assertArrayEquals(expected2, actual2);
        compressorInputStream2.close();

        // Test creating a compressor input stream with an unknown name
        InputStream input3 = new ByteArrayInputStream("Test data".getBytes());
        assertThrows(CompressorException.class, () -> {
            new CompressorStreamFactory().createCompressorInputStream("unknown", input3);
        });

        // Test creating a compressor input stream with a null input stream
        assertThrows(IllegalArgumentException.class, () -> {
            new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.GZIP, null);
        });

        // Test creating a compressor input stream with a null name
        assertThrows(IllegalArgumentException.class, () -> {
            new CompressorStreamFactory().createCompressorInputStream(null, new ByteArrayInputStream("Test data".getBytes()));
        });

        // Test creating a compressor input stream with a non-marked input stream
        InputStream input4 = new ByteArrayInputStream("Test data".getBytes());
        assertThrows(IllegalArgumentException.class, () -> {
            new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.BZIP2, input4);
        });

        // Test creating a compressor input stream with a BZip2 input stream
        InputStream bzip2Input = new ByteArrayInputStream(BZip2CompressorInputStream.createInput("Test data".getBytes()));
        CompressorInputStream compressorInputStreamBzip2 = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.BZIP2, bzip2Input);
        byte[] expectedBzip2 = "Test data".getBytes();
        byte[] actualBzip2 = new byte[expectedBzip2.length];
        IOUtils.readFully(compressorInputStreamBzip2, actualBzip2);
        assertArrayEquals(expectedBzip2, actualBzip2);
        compressorInputStreamBzip2.close();

        // Test creating a compressor input stream with a XZ input stream
        InputStream xzInput = new ByteArrayInputStream(XZCompressorInputStream.createInput("Test data".getBytes()));
        CompressorInputStream compressorInputStreamXz = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.XZ, xzInput);
        byte[] expectedXz = "Test data".getBytes();
        byte[] actualXz = new byte[expectedXz.length];
        IOUtils.readFully(compressorInputStreamXz, actualXz);
        assertArrayEquals(expectedXz, actualXz);
        compressorInputStreamXz.close();

        // Test creating a compressor input stream with a Deflate input stream
        InputStream deflateInput = new ByteArrayInputStream(DeflateCompressorInputStream.createInput("Test data".getBytes()));
        CompressorInputStream compressorInputStreamDeflate = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.DEFLATE, deflateInput);
        byte[] expectedDeflate = "Test data".getBytes();
        byte[] actualDeflate = new byte[expectedDeflate.length];
        IOUtils.readFully(compressorInputStreamDeflate, actualDeflate);
        assertArrayEquals(expectedDeflate, actualDeflate);
        compressorInputStreamDeflate.close();

        // Test creating a compressor input stream with a Snappy input stream
        InputStream snappyInput = new ByteArrayInputStream(SnappyCompressorInputStream.createInput("Test data".getBytes()));
        CompressorInputStream compressorInputStreamSnappy = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.SNAPPY_RAW, snappyInput);
        byte[] expectedSnappy = "Test data".getBytes();
        byte[] actualSnappy = new byte[expectedSnappy.length];
        IOUtils.readFully(compressorInputStreamSnappy, actualSnappy);
        assertArrayEquals(expectedSnappy, actualSnappy);
        compressorInputStreamSnappy.close();

    }

    @Test
    public void testCreateCompressorOutputStream() throws IOException {
        // Test creating a compressor output stream from a known output stream and name
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.GZIP, output);
        String testData = "Test data";
        compressorOutputStream.write(testData.getBytes());
        compressorOutputStream.finish();
        byte[] expected = testData.getBytes("UTF-8");
        byte[] actual = ((ByteArrayOutputStream) output).toByteArray();
        assertArrayEquals(expected, actual);
        compressorOutputStream.close();

        // Test creating a compressor output stream from a known name and output stream
        OutputStream output2 = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream2 = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.BZIP2, output2);
        String testData2 = "Test data";
        compressorOutputStream2.write(testData2.getBytes());
        compressorOutputStream2.finish();
        byte[] expected2 = testData2.getBytes("UTF-8");
        byte[] actual2 = ((ByteArrayOutputStream) output2).toByteArray();
        assertArrayEquals(expected2, actual2);
        compressorOutputStream2.close();

        // Test creating a compressor output stream with an unknown name
        OutputStream output3 = new ByteArrayOutputStream();
        assertThrows(CompressorException.class, () -> {
            new CompressorStreamFactory().createCompressorOutputStream("unknown", output3);
        });

        // Test creating a compressor output stream with a null output stream
        assertThrows(IllegalArgumentException.class, () -> {
            new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.GZIP, null);
        });

        // Test creating a compressor output stream with a null name
        assertThrows(IllegalArgumentException.class, () -> {
            new CompressorStreamFactory().createCompressorOutputStream(null, new ByteArrayOutputStream());
        });

        // Test creating a compressor output stream with a BZip2 output stream
        OutputStream bzip2Output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStreamBzip2 = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.BZIP2, bzip2Output);
        String testDataBzip2 = "Test data";
        compressorOutputStreamBzip2.write(testDataBzip2.getBytes());
        compressorOutputStreamBzip2.finish();
        byte[] expectedBzip2 = testDataBzip2.getBytes("UTF-8");
        byte[] actualBzip2 = ((ByteArrayOutputStream) bzip2Output).toByteArray();
        assertArrayEquals(expectedBzip2, actualBzip2);
        compressorOutputStreamBzip2.close();

        // Test creating a compressor output stream with a XZ output stream
        OutputStream xzOutput = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStreamXz = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.XZ, xzOutput);
        String testDataXz = "Test data";
        compressorOutputStreamXz.write(testDataXz.getBytes());
        compressorOutputStreamXz.finish();
        byte[] expectedXz = testDataXz.getBytes("UTF-8");
        byte[] actualXz = ((ByteArrayOutputStream) xzOutput).toByteArray();
        assertArrayEquals(expectedXz, actualXz);
        compressorOutputStreamXz.close();

        // Test creating a compressor output stream with a Deflate output stream
        OutputStream deflateOutput = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStreamDeflate = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.DEFLATE, deflateOutput);
        String testDataDeflate = "Test data";
        compressorOutputStreamDeflate.write(testDataDeflate.getBytes());
        compressorOutputStreamDeflate.finish();
        byte[] expectedDeflate = testDataDeflate.getBytes("UTF-8");
        byte[] actualDeflate = ((ByteArrayOutputStream) deflateOutput).toByteArray();
        assertArrayEquals(expectedDeflate, actualDeflate);
        compressorOutputStreamDeflate.close();

        // Test creating a compressor output stream with a Snappy output stream
        OutputStream snappyOutput = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStreamSnappy = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.SNAPPY_RAW, snappyOutput);
        String testDataSnappy = "Test data";
        compressorOutputStreamSnappy.write(testDataSnappy.getBytes());
        compressorOutputStreamSnappy.finish();
        byte[] expectedSnappy = testDataSnappy.getBytes("UTF-8");
        byte[] actualSnappy = ((ByteArrayOutputStream) snappyOutput).toByteArray();
        assertArrayEquals(expectedSnappy, actualSnappy);
        compressorOutputStreamSnappy.close();

    }

    @Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertFalse(factory.getDecompressConcatenated());

        CompressorStreamFactory factory2 = new CompressorStreamFactory(true);
        assertTrue(factory2.getDecompressConcatenated());
    }
}