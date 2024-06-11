package org.apache.commons.compress.compressors.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Test class for {@link CompressorStreamFactory}
 */
public class CompressorStreamFactoryTest {

    private static final String TEST_DATA = "This is a test.";

    /**
     * Test createCompressorInputStream(String, InputStream)
     */
    @org.junit.Test
    public void testCreateCompressorInputStreamStringInputStream() {
        final InputStream input = new ByteArrayInputStream(TEST_DATA.getBytes());

        final String name = "bzip2";
        final CompressorInputStream expected = new BZip2CompressorInputStream(input, false);

        final CompressorStreamFactory factory = new CompressorStreamFactory();
        final CompressorInputStream actual = factory.createCompressorInputStream(name, input);
        assertSame(expected, actual);
    }

    /**
     * Test createCompressorInputStream(String, InputStream) with null name
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStreamNullName() {
        final InputStream input = new ByteArrayInputStream(TEST_DATA.getBytes());

        final CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.createCompressorInputStream(null, input);
    }

    /**
     * Test createCompressorInputStream(String, InputStream) with null input
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStreamNullInputStream() {
        final String name = "bzip2";

        final CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.createCompressorInputStream(name, null);
    }

    /**
     * Test createCompressorInputStream(InputStream)
     */
    @org.junit.Test
    public void testCreateCompressorInputStreamInputStream() {
        final InputStream input = new ByteArrayInputStream(TEST_DATA.getBytes());

        final CompressorInputStream expected = new BZip2CompressorInputStream(input, false);

        final CompressorStreamFactory factory = new CompressorStreamFactory();
        final CompressorInputStream actual = factory.createCompressorInputStream(input);
        assertSame(expected, actual);
    }

    /**
     * Test createCompressorInputStream(InputStream) with null input
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStreamNullInputStream2() {
        final CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.createCompressorInputStream(null);
    }

    /**
     * Test createCompressorOutputStream(String, OutputStream)
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamStringOutputStream() {
        final OutputStream output = new ByteArrayOutputStream();

        final String name = "bzip2";
        final CompressorOutputStream expected = new BZip2CompressorOutputStream(output);

        final CompressorStreamFactory factory = new CompressorStreamFactory();
        final CompressorOutputStream actual = factory.createCompressorOutputStream(name, output);
        assertSame(expected, actual);
    }

    /**
     * Test createCompressorOutputStream(String, OutputStream) with null name
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorOutputStreamNullName() {
        final OutputStream output = new ByteArrayOutputStream();

        final CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.createCompressorOutputStream(null, output);
    }

    /**
     * Test createCompressorOutputStream(String, OutputStream) with null output
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorOutputStreamNullOutputStream() {
        final String name = "bzip2";

        final CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.createCompressorOutputStream(name, null);
    }

    /**
     * Test createCompressorOutputStream(String, OutputStream) with wrong name
     */
    @org.junit.Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStreamWrongName() {
        final OutputStream output = new ByteArrayOutputStream();

        final String name = "unknown";

        final CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.createCompressorOutputStream(name, output);
    }

    /**
     * Test createCompressorOutputStream(OutputStream)
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamOutputStream() {
        final OutputStream output = new ByteArrayOutputStream();

        final CompressorOutputStream expected = new BZip2CompressorOutputStream(output);

        final CompressorStreamFactory factory = new CompressorStreamFactory();
        final CompressorOutputStream actual = factory.createCompressorOutputStream(output);
        assertSame(expected, actual);
    }

    /**
     * Test createCompressorOutputStream(OutputStream) with null output
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorOutputStreamNullOutputStream2() {
        final CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.createCompressorOutputStream(null);
    }

    /**
     * Test createCompressorOutputStream(InputStream, OutputStream)
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamInputStreamOutputStream() throws IOException {
        final InputStream input = new ByteArrayInputStream(TEST_DATA.getBytes());
        final OutputStream output = new ByteArrayOutputStream();

        final String name = "bzip2";

        final CompressorOutputStream expected = new BZip2CompressorOutputStream(output);
        IOUtils.copy(input, expected);
        final byte[] expectedBytes = output.toByteArray();

        final CompressorStreamFactory factory = new CompressorStreamFactory();
        final CompressorOutputStream actual = factory.createCompressorOutputStream(name, output);
        IOUtils.copy(input, actual);
        final byte[] actualBytes = output.toByteArray();

        assertArrayEquals(expectedBytes, actualBytes);
    }

    /**
     * Test createCompressorOutputStream(InputStream, OutputStream) with null input
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorOutputStreamNullInputStream() {
        final OutputStream output = new ByteArrayOutputStream();
        final CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.createCompressorOutputStream(null, output);
    }

    /**
     * Test createCompressorOutputStream(InputStream, OutputStream) with null output
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorOutputStreamNullOutputStream() {
        final InputStream input = new ByteArrayInputStream(TEST_DATA.getBytes());
        final CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.createCompressorOutputStream(input, null);
    }

    /**
     * Test createCompressorOutputStream(InputStream, OutputStream) with different names
     */
    @org.junit.Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStreamDifferentNames() throws IOException {
        final InputStream input = new ByteArrayInputStream(TEST_DATA.getBytes());
        final OutputStream output = new ByteArrayOutputStream();

        final CompressorOutputStream expectedBz2 = new BZip2CompressorOutputStream(output);
        IOUtils.copy(input, expectedBz2);
        final byte[] expectedBz2Bytes = output.toByteArray();

        final CompressorOutputStream expectedGzip = new GzipCompressorOutputStream(output);
        IOUtils.copy(input, expectedGzip);
        final byte[] expectedGzipBytes = output.toByteArray();

        final String nameBz2 = "bzip2";
        final String nameGzip = "gzip";

        final CompressorStreamFactory factory = new CompressorStreamFactory();
        final CompressorOutputStream actual = factory.createCompressorOutputStream(nameBz2, output);
        IOUtils.copy(input, actual);
        final byte[] actualBytesBz2 = output.toByteArray();

        final CompressorOutputStream actualGzip = factory.createCompressorOutputStream(nameGzip, output);
        IOUtils.copy(input, actualGzip);
        final byte[] actualBytesGzip = output.toByteArray();

        assertArrayEquals(expectedBz2Bytes, actualBytesBz2);
        assertArrayEquals(expectedGzipBytes, actualBytesGzip);
        assertNotEquals(actualBytesBz2, actualBytesGzip);
    }

    /**
     * Test createCompressorOutputStream(InputStream, OutputStream) with decompressConcatenated = true
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamInputStreamOutputStreamDecompressConcatenated()
            throws IOException {
        final InputStream input = new ByteArrayInputStream(TEST_DATA.getBytes());
        final OutputStream output = new ByteArrayOutputStream();

        final String name = "bzip2";
        final boolean decompressConcatenated = true;

        final CompressorOutputStream expected = new BZip2CompressorOutputStream(output, decompressConcatenated);

        final CompressorStreamFactory factory = new CompressorStreamFactory(decompressConcatenated);
        final CompressorOutputStream actual = factory.createCompressorOutputStream(name, output);
        assertSame(expected, actual);
    }

    /**
     * Test setDecompressConcatenated()
     */
    @org.junit.Test
    public void testSetDecompressConcatenated() {
        final CompressorStreamFactory factory = new CompressorStreamFactory();
        assertFalse(factory.getDecompressConcatenated());

        factory.setDecompressConcatenated(true);
        assertTrue(factory.getDecompressConcatenated());
    }

    /**
     * Test setDecompressConcatenated() with already set value
     */
    @org.junit.Test
    public void testSetDecompressConcatenatedAlreadySet() {
        final CompressorStreamFactory factory = new CompressorStreamFactory(true);
        assertSame(factory.getDecompressConcatenated(), factory.getDecompressConcatenated());
    }

    /**
     * Test setDecompressConcatenated() with null factory
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSetDecompressConcatenatedNull() {
        setDecompressConcatenated(null, true);
    }

    /**
     * Test setDecompressConcatenated()
     */
    private static void setDecompressConcatenated(CompressorStreamFactory factory, boolean value) {
        factory.setDecompressConcatenated(value);
    }
}