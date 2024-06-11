package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.NoSuchAlgorithmException;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

/**
 * CompressorStreamFactoryTest
 */
public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream() throws IOException, NoSuchAlgorithmException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        // Test with valid compressor names
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream("GZIP", inputStream);
        assertNotNull(compressorInputStream);
        compressorInputStream.close();

        compressorInputStream = factory.createCompressorInputStream("BZIP2", inputStream);
        assertNotNull(compressorInputStream);
        compressorInputStream.close();

        compressorInputStream = factory.createCompressorInputStream("DEFLATE", inputStream);
        assertNotNull(compressorInputStream);
        compressorInputStream.close();

        // Test with invalid compressor names
        compressorInputStream = factory.createCompressorInputStream("INVALID", inputStream);
        assertThrows(CompressorException.class, () -> compressorInputStream.close());

        // Test with null input stream
        assertThrows(IllegalArgumentException.class, () -> factory.createCompressorInputStream("BZIP2", null));
    }

    @Test
    public void testCreateCompressorInputStreamWithCustomName() throws IOException, NoSuchAlgorithmException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream("XZ", inputStream);
        assertNotNull(compressorInputStream);
        compressorInputStream.close();

        factory = new CompressorStreamFactory("XZ");
        compressorInputStream = factory.createCompressorInputStream("XZ", inputStream);
        assertNotNull(compressorInputStream);
        compressorInputStream.close();

        assertThrows(CompressorException.class, () -> factory.createCompressorInputStream("INVALID", inputStream));

        // Test with null input stream
        assertThrows(IllegalArgumentException.class, () -> factory.createCompressorInputStream("XZ", null));
    }

    @Test
    public void testCreateCompressorOutputStream() throws IOException, NoSuchAlgorithmException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("GZIP", outputStream);
        assertNotNull(compressorOutputStream);
        compressorOutputStream.finish();
        compressorOutputStream.close();

        byte[] expectedOutput = IOUtils.toByteArray(new GzipCompressorInputStream(
                new ByteArrayInputStream(new byte[0])));
        byte[] actualOutput = ((ByteArrayOutputStream) outputStream).toByteArray();
        assertArrayEquals(expectedOutput, actualOutput);

        compressorOutputStream = factory.createCompressorOutputStream("BZIP2", outputStream);
        assertNotNull(compressorOutputStream);
        compressorOutputStream.finish();
        compressorOutputStream.close();

        expectedOutput = IOUtils.toByteArray(new BZip2CompressorInputStream(
                new ByteArrayInputStream(new byte[0])));
        actualOutput = ((ByteArrayOutputStream) outputStream).toByteArray();
        assertArrayEquals(expectedOutput, actualOutput);

        compressorOutputStream = factory.createCompressorOutputStream("DEFLATE", outputStream);
        assertNotNull(compressorOutputStream);
        compressorOutputStream.finish();
        compressorOutputStream.close();

        expectedOutput = IOUtils.toByteArray(new DeflateCompressorOutputStream(
                new ByteArrayOutputStream(1024)));
        actualOutput = ((ByteArrayOutputStream) outputStream).toByteArray();
        assertArrayEquals(expectedOutput, actualOutput);

        // Test with invalid compressor names
        assertThrows(CompressorException.class, () -> factory.createCompressorOutputStream("INVALID", outputStream));

        // Test with null output stream
        assertThrows(IllegalArgumentException.class, () -> factory.createCompressorOutputStream("BZIP2", null));
    }

    @Test
    public void testCreateCompressorOutputStreamWithCustomName() throws IOException, NoSuchAlgorithmException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("XZ", outputStream);
        assertNotNull(compressorOutputStream);
        compressorOutputStream.finish();
        compressorOutputStream.close();

        byte[] expectedOutput = IOUtils.toByteArray(new XZCompressorOutputStream(
                new ByteArrayOutputStream(1024)));
        byte[] actualOutput = ((ByteArrayOutputStream) outputStream).toByteArray();
        assertArrayEquals(expectedOutput, actualOutput);

        factory = new CompressorStreamFactory("XZ");
        compressorOutputStream = factory.createCompressorOutputStream("XZ", outputStream);
        assertNotNull(compressorOutputStream);
        compressorOutputStream.finish();
        compressorOutputStream.close();

        expectedOutput = IOUtils.toByteArray(new XZCompressorOutputStream(
                new ByteArrayOutputStream(1024)));
        actualOutput = ((ByteArrayOutputStream) outputStream).toByteArray();
        assertArrayEquals(expectedOutput, actualOutput);

        assertThrows(CompressorException.class, () -> factory.createCompressorOutputStream("INVALID", outputStream));

        // Test with null output stream
        assertThrows(IllegalArgumentException.class, () -> factory.createCompressorOutputStream("XZ", null));
    }

    @Test
    public void testSetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertFalse(factory.getDecompressConcatenated());

        factory = new CompressorStreamFactory(true);
        assertTrue(factory.getDecompressConcatenated());

        factory = new CompressorStreamFactory();
        assertFalse(factory.getDecompressConcatenated());

        try {
            factory.setDecompressConcatenated(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            factory.setDecompressConcatenated(3);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}