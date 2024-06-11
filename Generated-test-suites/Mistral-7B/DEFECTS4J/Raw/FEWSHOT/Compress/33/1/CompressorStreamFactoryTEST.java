package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.BaseTest;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

public class CompressorStreamFactoryTest extends BaseTest {

    @Test
    public void testCreateCompressorInputStreamWithSignature() throws IOException {
        InputStream testInput = new ByteArrayInputStream(BZIP2_SIGNATURE);

        CompressorInputStream compressedStream = new CompressorStreamFactory().createCompressorInputStream(
                CompressorStreamFactory.BZIP2, testInput);

        assertNotNull(compressedStream);
        assertSame(BZip2CompressorInputStream.class, compressedStream.getClass());
    }

    @Test
    public void testCreateCompressorInputStreamWithName() throws IOException {
        InputStream testInput = new ByteArrayInputStream(BZIP2_SIGNATURE);

        CompressorInputStream compressedStream = new CompressorStreamFactory().createCompressorInputStream(
                CompressorStreamFactory.BZIP2, testInput);

        InputStream input = new ByteArrayInputStream(DECOMPRESSED_DATA);
        CompressorInputStream compressedStreamFromName = new CompressorStreamFactory().createCompressorInputStream(
                CompressorStreamFactory.BZIP2, input);

        assertSame(compressedStream, compressedStreamFromName);
    }

    @Test
    public void testCreateCompressorOutputStreamWithName() throws IOException {
        OutputStream testOutput = new ByteArrayOutputStream();

        CompressorOutputStream compressedStream = new CompressorStreamFactory().createCompressorOutputStream(
                CompressorStreamFactory.BZIP2, testOutput);

        InputStream input = new ByteArrayInputStream(ORIGINAL_DATA);
        OutputStream output = new ByteArrayOutputStream();

        IOUtils.copy(input, compressedStream);
        compressedStream.finish();
        compressedStream.close();

        InputStream compressedInput = new CompressorStreamFactory().createCompressorInputStream(
                CompressorStreamFactory.BZip2, output.toByteArray());

        ByteArrayOutputStream resultOutput = new ByteArrayOutputStream();
        IOUtils.copy(compressedInput, resultOutput);
        assertArrayEquals(ORIGINAL_DATA, resultOutput.toByteArray());

        compressedStream = new CompressorStreamFactory().createCompressorOutputStream(
                CompressorStreamFactory.BZIP2, testOutput);
        input = new ByteArrayInputStream(ORIGINAL_DATA);

        IOUtils.copy(input, compressedStream);
        compressedStream.finish();
        compressedStream.close();

        InputStream compressedInputFromName = new CompressorStreamFactory().createCompressorInputStream(
                CompressorStreamFactory.BZIP2, testOutput.toByteArray());

        assertSame(compressedInput, compressedInputFromName);
    }

    @Test
    public void testCreateCompressorOutputStreamWithSignature() throws IOException {
        OutputStream testOutput = new ByteArrayOutputStream();

        CompressorOutputStream compressedStream = new CompressorStreamFactory().createCompressorOutputStream(
                CompressorStreamFactory.BZIP2, testOutput);

        InputStream testInput = new ByteArrayInputStream(BZIP2_SIGNATURE);
        CompressorOutputStream compressedStreamFromSignature = new CompressorStreamFactory().createCompressorOutputStream(
                CompressorStreamFactory.BZIP2, testInput);

        assertSame(compressedStream, compressedStreamFromSignature);
    }

    @Test
    public void testCreateCompressorInputStreamWithInvalidSignature() throws IOException {
        InputStream testInput = new ByteArrayInputStream(INVALID_SIGNATURE);

        try {
            new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.BZIP2, testInput);
            fail("Should throw an exception.");
        } catch (CompressorException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorOutputStreamWithInvalidName() throws IOException {
        try {
            new CompressorStreamFactory().createCompressorOutputStream(null, new ByteArrayOutputStream());
            fail("Should throw an exception.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetDecompressConcatenated() {
        boolean result = new CompressorStreamFactory().getDecompressConcatenated();
        assertFalse(result);

        new CompressorStreamFactory(true);
        result = new CompressorStreamFactory().getDecompressConcatenated();
        assertTrue(result);
    }

    private static final byte[] BZIP2_SIGNATURE = { (byte) 0x42, (byte) 0x5A, (byte) 0x68, (byte) 0x64,
            (byte) 0x67, (byte) 0x65, (byte) 0x21, (byte) 0x1B, (byte) 0x0B, (byte) 0x08, (byte) 0x05,
            (byte) 0x06, (byte) 0x01, (byte) 0x01, (byte) 0x00, (byte) 0x1C, (byte) 0x00 };

    private static final byte[] ORIGINAL_DATA = { 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!',
            ' ', 'T', 'h', 'i', 's', ' ', 'i', 's', ' ', 'a', 'G', 'e', 'e', 'k', '!' };

    private static final byte[] DECOMPRESSED_DATA = { 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd',
            '!', ' ', 'T', 'h', 'i', 's', ' ', 'i', 's', ' ', 'a', 'G', 'e', 'e', 'k', '!' };

    private static final byte[] INVALID_SIGNATURE = { (byte) 0x42, (byte) 0x5A, (byte) 0x68, (byte) 0x64,
            (byte) 0x67, (byte) 0x65, (byte) 0x20, (byte) 0x1B, (byte) 0x0B, (byte) 0x08, (byte) 0x05,
            (byte) 0x06, (byte) 0x01, (byte) 0x01, (byte) 0x00, (byte) 0x1C, (byte) 0x01 };
}