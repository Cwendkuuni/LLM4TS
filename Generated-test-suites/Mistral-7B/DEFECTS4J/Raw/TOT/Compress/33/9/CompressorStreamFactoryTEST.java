package org.apache.commons.compress.compressors.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorFactory;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.NoSuchAlgorithmException;
import org.apache.commons.compress.compressors.UnsupportedFormatException;

/**
 * Test cases for {@link CompressorStreamFactory}.
 */
public class CompressorStreamFactoryTest {

    /**
     * Test creating a compressor input stream from a known signature.
     *
     * @throws Exception on failure
     */
    @org.junit.Test
    public void testCreateCompressorInputStreamFromSignature() throws Exception {
        final byte[] gzipSignature = GzipCompressorInputStream.SIGNATURE;
        final byte[] bzip2Signature = BZip2CompressorInputStream.SIGNATURE;

        final InputStream testIS = new ByteArrayInputStream(gzipSignature);
        final CompressorInputStream gzipIS = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.GZIP, testIS);
        assertNotNull(gzipIS);
        assertTrue(gzipIS instanceof GzipCompressorInputStream);

        final InputStream bzip2IS = new ByteArrayInputStream(bzip2Signature);
        final CompressorInputStream bzip2ISInstance = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.BZIP2, bzip2IS);
        assertNotNull(bzip2ISInstance);
        assertTrue(bzip2ISInstance instanceof BZip2CompressorInputStream);
    }

    /**
     * Test creating an unsupported compressor input stream from a known signature.
     *
     * @throws Exception on failure
     */
    @org.junit.Test
    public void testCreateCompressorInputStreamFromUnknownSignature() throws Exception {
        final byte[] snappyRawSignature = SnappyCompressorInputStream.SIGNATURE;

        final InputStream snappyInput = new ByteArrayInputStream(snappyRawSignature);
        final CompressorInputStream snappyInputInstance = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.SNAPPY_RAW, snappyInput);

        assertNull(snappyInputInstance);
    }

    /**
     * Test creating a compressor input stream from a compressor name and an input stream.
     *
     * @throws Exception on failure
     */
    @org.junit.Test
    public void testCreateCompressorInputStreamWithNameAndInputStream() throws Exception {
        final InputStream testIS = new ByteArrayInputStream(GzipCompressorInputStream.SIGNATURE);
        final CompressorInputStream gzipIS = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.GZIP, testIS);
        assertNotNull(gzipIS);
        assertTrue(gzipIS instanceof GzipCompressorInputStream);
    }

    /**
     * Test creating a compressor output stream from a compressor name and an output stream.
     *
     * @throws Exception on failure
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamWithNameAndOutputStream() throws Exception {
        final OutputStream testOS = new ByteArrayOutputStream();
        final CompressorOutputStream gzipOS = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.GZIP, testOS);
        assertNotNull(gzipOS);
        assertTrue(gzipOS instanceof GzipCompressorOutputStream);
    }

    /**
     * Test creating an unsupported compressor output stream from a compressor name and an output stream.
     *
     * @throws Exception on failure
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamWithUnsupportedNameAndOutputStream() throws Exception {
        final OutputStream testOS = new ByteArrayOutputStream();
        final CompressorOutputStream snappyOS = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.SNAPPY_FRAMED, testOS);
        assertNull(snappyOS);
    }

    /**
     * Test creating a compressor input stream with an invalid compressor name.
     *
     * @throws Exception on failure
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStreamWithNullCompressorName() throws Exception {
        new CompressorStreamFactory().createCompressorInputStream(null, new ByteArrayInputStream(0));
    }

    /**
     * Test creating a compressor input stream with a null input stream.
     *
     * @throws Exception on failure
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStreamWithNullInputStream() throws Exception {
        new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.GZIP, null);
    }

    /**
     * Test creating a compressor input stream with an invalid compressor name.
     *
     * @throws Exception on failure
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStreamWithInvalidCompressorName() throws Exception {
        new CompressorStreamFactory().createCompressorInputStream("INVALID", new ByteArrayInputStream(0));
    }

    /**
     * Test creating a compressor output stream with a null compressor name.
     *
     * @throws Exception on failure
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorOutputStreamWithNullCompressorNameString() throws Exception {
        new CompressorStreamFactory().createCompressorOutputStream(null, new ByteArrayOutputStream());
    }

    /**
     * Test creating a compressor output stream with a null output stream.
     *
     * @throws Exception on failure
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorOutputStreamWithNullOutputStream() throws Exception {
        new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.GZIP, null);
    }

    /**
     * Test creating a compressor input stream using known compressor name.
     *
     * @throws Exception on failure
     */
    @org.junit.Test
    public void testCreateCompressorInputStreamWithBzip2() throws Exception {
        final InputStream testIS = new ByteArrayInputStream(BZip2CompressorInputStream.SIGNATURE);
        final CompressorInputStream bzip2IS = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.BZIP2, testIS);
        assertNotNull(bzip2IS);
        assertTrue(bzip2IS instanceof BZip2CompressorInputStream);
    }

    /**
     * Test creating a compressor input stream using an unsupported compressor name.
     *
     * @throws Exception on failure
     */
    @org.junit.Test
    public void testCreateCompressorInputStreamWithUnknownCompressor() throws Exception {
        final InputStream testIS = new ByteArrayInputStream(GzipCompressorInputStream.SIGNATURE);
        final CompressorInputStream unknownCompressorIS = new CompressorStreamFactory()
                .createCompressorInputStream("UNKNOWN", testIS);
        assertNull(unknownCompressorIS);
    }

    /**
     * Test creating a compressor output stream using known compressor name.
     *
     * @throws Exception on failure
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamWithGzip() throws Exception {
        final OutputStream testOS = new ByteArrayOutputStream();
        final CompressorOutputStream gzipOS = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.GZIP, testOS);
        assertNotNull(gzipOS);
        assertTrue(gzipOS instanceof GzipCompressorOutputStream);
    }

    /**
     * Test creating a compressor output stream using an unsupported compressor name.
     *
     * @throws Exception on failure
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamWithUnsupportedCompressor() throws Exception {
        final OutputStream testOS = new ByteArrayOutputStream();
        final CompressorOutputStream snappyOS = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.SNAPPY_FRAMED, testOS);
        assertNull(snappyOS);
    }

    /**
     * Test creating a compressor input stream from a compressor name and an input stream and
     * then compressing data to it and back.
     *
     * @throws Exception on failure
     */
    @org.junit.Test
    public void testCreateCompressorInputStreamAndOutputStream() throws Exception {
        final OutputStream out = new ByteArrayOutputStream();

        final String compressorName = "BZIP2";
        final CompressorOutputStream compressorOS = new CompressorStreamFactory()
                .createCompressorOutputStream(compressorName, out);
        assertNotNull(compressorOS);
        assertTrue(compressorOS instanceof BZip2CompressorOutputStream);

        final InputStream in = new ByteArrayInputStream(new byte[10]);
        final CompressorInputStream compressorIS = new CompressorStreamFactory()
                .createCompressorInputStream(compressorName, in);
        assertNotNull(compressorIS);
        assertTrue(compressorIS instanceof BZip2CompressorInputStream);

        compressorOS.write(new byte[10]);
        compressorOS.close();

        byte[] expected = new byte[12]; // including signature and data
        IOUtils.readFully(compressorIS, expected, 0, expected.length);
        IOUtils.compare(in, new ByteArrayInputStream(expected), false);
        compressorIS.close();
    }

    /**
     * Test creating a compressor input stream from a compressor name and an input stream without
     * signature check.
     *
     * @throws Exception on failure
     */
    @org.junit.Test
    public void testCreateCompressorInputStreamWithoutSignatureCheck() throws Exception {
        final OutputStream out = new ByteArrayOutputStream();

        final String compressorName = "GZIP";
        final CompressorOutputStream compressorOS = new CompressorStreamFactory()
                .createCompressorOutputStream(compressorName, out);
        assertNotNull(compressorOS);
        assertTrue(compressorOS instanceof GzipCompressorOutputStream);

        final InputStream in = new ByteArrayInputStream(new byte[10]);
        final CompressorInputStream compressorIS = new CompressorStreamFactory()
                .createCompressorInputStream(compressorName, in, false);
        assertNotNull(compressorIS);
        assertTrue(compressorIS instanceof GzipCompressorInputStream);

        compressorOS.write(new byte[10]);
        compressorOS.close();

        byte[] expected = new byte[10]; // only data
        IOUtils.readFully(compressorIS, expected, 0, expected.length);
        IOUtils.compare(in, new ByteArrayInputStream(expected), false);
        compressorIS.close();
    }

    /**
     * Test creating a compressor input stream from a compressor name and an invalid input stream.
     *
     * @throws Exception on failure
     */
    @org.junit.Test(expected = IOException.class)
    public void testCreateCompressorInputStreamWithInvalidInputStream() throws Exception {
        final String compressorName = "GZIP";
        final InputStream invalidIS = new ByteArrayInputStream(new byte[]{-1});
        new CompressorStreamFactory()
                .createCompressorInputStream(compressorName, invalidIS);
    }

    /**
     * Test creating a compressor input stream from an unsupported compressor name and an input stream.
     *
     * @throws Exception on failure
     */
    @org.junit.Test(expected = UnsupportedFormatException.class)
    public void testCreateCompressorInputStreamWithUnsupportedCompressorAndInputStream() throws Exception {
        final String compressorName = "UNKNOWN";
        final InputStream testIS = new ByteArrayInputStream(GzipCompressorInputStream.SIGNATURE);
        new CompressorStreamFactory()
                .createCompressorInputStream(compressorName, testIS);
    }

    /**
     * Test creating a compressor output stream from an unsupported compressor name and an output stream.
     *
     * @throws Exception on failure
     */
    @org.junit.Test(expected = NoSuchAlgorithmException.class)
    public void testCreateCompressorOutputStreamWithUnsupportedCompressorAndOutputStream() throws Exception {
        final String compressorName = "UNKNOWN";
        final OutputStream testOS = new ByteArrayOutputStream();
        new CompressorStreamFactory()
                .createCompressorOutputStream(compressorName, testOS);
    }

    /**
     * Test creating a compressor input stream from a compressor name and a null input stream.
     *
     * @throws Exception on failure
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStreamWithNullInputStreamAndName() throws Exception {
        new CompressorStreamFactory().createCompressorInputStream("GZIP", null);
    }

    /**
     * Test creating a compressor input stream from a compressor name and a null input stream.
     *
     * @throws Exception on failure
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStreamWithNullNameAndInputStream() throws Exception {
        new CompressorStreamFactory().createCompressorInputStream(null, new ByteArrayInputStream(0));
    }

    /**
     * Test creating a compressor input stream from a compressor name and a null output stream.
     *
     * @throws Exception on failure
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorOutputStreamWithNullNameAndOutputStream() throws Exception {
        new CompressorStreamFactory().createCompressorOutputStream(null, new ByteArrayOutputStream());
    }

    /**
     * Test creating a compressor input stream from a compressor name and a null output stream.
     *
     * @throws Exception on failure
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorOutputStreamWithNullOutputStreamAndName() throws Exception {
        new CompressorStreamFactory().createCompressorOutputStream("GZIP", null);
    }
}