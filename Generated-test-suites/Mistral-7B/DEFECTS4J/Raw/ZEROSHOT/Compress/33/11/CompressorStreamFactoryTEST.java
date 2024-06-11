package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.snappy.SnappyCompressorInputStream;
import org.apache.commons.compress.compressors.z.ZCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

public class CompressorStreamFactoryTest {

    @org.junit.Test
    public void testBZip2Input() throws IOException {
        final byte[] testData = "The quick brown fox jumps over the lazy dog".getBytes();
        final InputStream testInputStream = new ByteArrayInputStream(testData);
        final String name = CompressorStreamFactory.BZIP2;
        CompressorInputStream in = null;

        try {
            in = new CompressorStreamFactory().createCompressorInputStream(name, testInputStream);
            byte[] readData = new byte[testData.length];
            IOUtils.readFully(in, readData);
            assertArrayEquals(testData, readData);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(testInputStream);
        }
    }

    @org.junit.Test
    public void testBZip2Output() throws IOException {
        final String name = CompressorStreamFactory.BZIP2;
        final byte[] testData = "The quick brown fox jumps over the lazy dog".getBytes();
        final InputStream inputStream = new ByteArrayInputStream(testData);
        final OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream out = null;

        try {
            out = new CompressorStreamFactory().createCompressorOutputStream(name, outputStream);
            IOUtils.copy(inputStream, out);
            out.finish();
            out.close();
            byte[] readData = outputStream.toByteArray();
            CompressorInputStream compressedInputStream = new CompressorStreamFactory()
                    .createCompressorInputStream(name, new ByteArrayInputStream(readData));
            byte[] outData = new byte[testData.length];
            IOUtils.readFully(compressedInputStream, outData);
            assertEquals(testData.length, outData.length);
            assertArrayEquals(testData, outData);
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }

    @org.junit.Test
    public void testDeflateInput() throws IOException {
        final byte[] testData = "The quick brown fox jumps over the lazy dog".getBytes();
        final InputStream testInputStream = new ByteArrayInputStream(testData);
        final String name = CompressorStreamFactory.DEFLATE;
        CompressorInputStream in = null;

        try {
            in = new CompressorStreamFactory().createCompressorInputStream(name, testInputStream);
            byte[] readData = new byte[testData.length];
            IOUtils.readFully(in, readData);
            assertArrayEquals(testData, readData);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(testInputStream);
        }
    }

    @org.junit.Test
    public void testDeflateOutput() throws IOException {
        final String name = CompressorStreamFactory.DEFLATE;
        final byte[] testData = "The quick brown fox jumps over the lazy dog".getBytes();
        final InputStream inputStream = new ByteArrayInputStream(testData);
        final OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream out = null;

        try {
            out = new CompressorStreamFactory().createCompressorOutputStream(name, outputStream);
            IOUtils.copy(inputStream, out);
            out.finish();
            out.close();
            byte[] readData = outputStream.toByteArray();
            CompressorInputStream compressedInputStream = new CompressorStreamFactory()
                    .createCompressorInputStream(name, new ByteArrayInputStream(readData));
            byte[] outData = new byte[testData.length];
            IOUtils.readFully(compressedInputStream, outData);
            assertEquals(testData.length, outData.length);
            assertArrayEquals(testData, outData);
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }

    @org.junit.Test
    public void testGzipInput() throws IOException {
        final byte[] testData = "The quick brown fox jumps over the lazy dog".getBytes();
        final InputStream testInputStream = new ByteArrayInputStream(testData);
        final String name = CompressorStreamFactory.GZIP;
        CompressorInputStream in = null;

        try {
            in = new CompressorStreamFactory().createCompressorInputStream(name, testInputStream);
            byte[] readData = new byte[testData.length];
            IOUtils.readFully(in, readData);
            assertArrayEquals(testData, readData);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(testInputStream);
        }
    }

    @org.junit.Test(expected = CompressorException.class)
    public void testInvalidCompressorInput() throws IOException {
        final InputStream testInputStream = new ByteArrayInputStream("The quick brown fox jumps over the lazy dog".getBytes());
        final String name = "INVALID";
        new CompressorStreamFactory().createCompressorInputStream(name, testInputStream);
    }

    @org.junit.Test(expected = CompressorException.class)
    public void testInvalidCompressorOutput() throws IOException {
        final String name = "INVALID";
        final OutputStream outputStream = new ByteArrayOutputStream();
        new CompressorStreamFactory().createCompressorOutputStream(name, outputStream);
    }

    @org.junit.Test
    public void testSnappyInput() throws IOException {
        final byte[] testData = "The quick brown fox jumps over the lazy dog".getBytes();
        final InputStream testInputStream = new ByteArrayInputStream(testData);
        final String name = CompressorStreamFactory.SNAPPY_RAW;
        CompressorInputStream in = null;

        try {
            in = new CompressorStreamFactory().createCompressorInputStream(name, testInputStream);
            byte[] readData = new byte[testData.length];
            IOUtils.readFully(in, readData);
            assertArrayEquals(testData, readData);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(testInputStream);
        }
    }

    @org.junit.Test
    public void testSnappyFramedInput() throws IOException {
        final byte[] testData = "The quick brown fox jumps over the lazy dog".getBytes();
        final InputStream testInputStream = new ByteArrayInputStream(testData);
        final String name = CompressorStreamFactory.SNAPPY_FRAMED;
        CompressorInputStream in = null;

        try {
            in = new CompressorStreamFactory().createCompressorInputStream(name, testInputStream);
            byte[] readData = new byte[testData.length];
            IOUtils.readFully(in, readData);
            assertArrayEquals(testData, readData);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(testInputStream);
        }
    }

    @org.junit.Test
    public void testZInput() throws IOException {
        final byte[] testData = "The quick brown fox jumps over the lazy dog".getBytes();
        final InputStream testInputStream = new ByteArrayInputStream(testData);
        final String name = CompressorStreamFactory.Z;
        CompressorInputStream in = null;

        try {
            in = new CompressorStreamFactory().createCompressorInputStream(name, testInputStream);
            byte[] readData = new byte[testData.length];
            IOUtils.readFully(in, readData);
            assertArrayEquals(testData, readData);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(testInputStream);
        }
    }

    @org.junit.Test
    public void testCreateCompressorInputStreamWithName() throws IOException {
        final byte[] testData = "The quick brown fox jumps over the lazy dog".getBytes();
        final InputStream testInputStream = new ByteArrayInputStream(testData);

        CompressorInputStream in = null;

        try {
            in = new CompressorStreamFactory()
                    .createCompressorInputStream("BZIP2", testInputStream);
            byte[] readData = new byte[testData.length];
            IOUtils.readFully(in, readData);
            assertArrayEquals(testData, readData);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(testInputStream);
        }
    }

    @org.junit.Test
    public void testCreateCompressorInputStreamWithNameAndNull() {
        assertNull(new CompressorStreamFactory()
                .createCompressorInputStream("BZIP2", null));
    }

    @org.junit.Test
    public void testCreateCompressorInputStreamWithNameAndEmptyStream() throws IOException {
        final InputStream testInputStream = new ByteArrayInputStream(new byte[0]);

        CompressorInputStream in = null;

        try {
            in = new CompressorStreamFactory()
                    .createCompressorInputStream("BZIP2", testInputStream);
            byte[] readData = new byte[0];
            IOUtils.readFully(in, readData);
            assertArrayEquals(new byte[0], readData);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(testInputStream);
        }
    }

    @org.junit.Test
    public void testCreateCompressorOutputStreamWithName() throws IOException {
        final byte[] testData = "The quick brown fox jumps over the lazy dog".getBytes();
        final OutputStream outputStream = new ByteArrayOutputStream();

        CompressorOutputStream out = null;

        try {
            out = new CompressorStreamFactory()
                    .createCompressorOutputStream("BZIP2", outputStream);
            IOUtils.write(testData, out);
            out.finish();
            byte[] readData = outputStream.toByteArray();
            CompressorInputStream compressedInputStream = new CompressorStreamFactory()
                    .createCompressorInputStream("BZIP2", new ByteArrayInputStream(readData));
            byte[] outData = new byte[testData.length];
            IOUtils.readFully(compressedInputStream, outData);
            assertEquals(testData.length, outData.length);
            assertArrayEquals(testData, outData);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

    @org.junit.Test
    public void testCreateCompressorOutputStreamWithNameAndNull() {
        assertNull(new CompressorStreamFactory()
                .createCompressorOutputStream("BZIP2", null));
    }

    @org.junit.Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertFalse(factory.getDecompressConcatenated());

        CompressorStreamFactory factory2 = new CompressorStreamFactory(true);
        assertTrue(factory2.getDecompressConcatenated());
    }
}