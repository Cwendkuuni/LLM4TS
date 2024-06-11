package org.apache.commons.compress.compressors;

import static org.junit.Assert.*;

import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.CompressorStreamFactory.CompressorType;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.test.utils.TestUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test class for CompressorStreamFactory.
 */
public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_Autodetect() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        InputStream testInputStream = new ByteArrayInputStream(TestUtils.GZIP_SIGNATURE);

        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(testInputStream);
        assertEquals(CompressorType.GZIP, compressorInputStream.getType());

        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStream_ByName() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        InputStream testInputStream = new ByteArrayInputStream(TestUtils.GZIP_SIGNATURE);

        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(
                CompressorStreamFactory.CompressorType.GZIP, testInputStream);
        assertEquals(CompressorType.GZIP, compressorInputStream.getType());

        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStream_InvalidType() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        InputStream testInputStream = new ByteArrayInputStream(TestUtils.GZIP_SIGNATURE);

        try {
            CompressorInputStream compressorInputStream = factory.createCompressorInputStream("invalid", testInputStream);
            fail("Expected CompressorException");
        } catch (CompressorException e) {
            assertEquals("Compressor: invalid not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateCompressorInputStream_InvalidSignature() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        InputStream testInputStream = new ByteArrayInputStream(TestUtils.INVALID_SIGNATURE);

        try {
            CompressorInputStream compressorInputStream = factory.createCompressorInputStream(testInputStream);
            fail("Expected CompressorException");
        } catch (CompressorException e) {
            assertTrue(e.getMessage().startsWith("Failed to detect Compressor from InputStream."));
        }
    }

    @Test
    public void testCreateCompressorOutputStream_Autodetect() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        OutputStream testOutputStream = new ByteArrayOutputStream();

        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(testOutputStream);
        assertEquals(CompressorType.GZIP, compressorOutputStream.getType());

        compressorOutputStream.finish();
        compressorOutputStream.close();

        InputStream testInputStream = new ByteArrayInputStream(TestUtils.GZIP_SIGNATURE);

        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(testInputStream);
        assertEquals(CompressorType.GZIP, compressorInputStream.getType());

        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStream_ByName() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        OutputStream testOutputStream = new ByteArrayOutputStream();

        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(
                CompressorStreamFactory.CompressorType.BZIP2, testOutputStream);
        assertEquals(CompressorType.BZIP2, compressorOutputStream.getType());

        compressorOutputStream.finish();
        compressorOutputStream.close();

        InputStream testInputStream = new ByteArrayInputStream(TestUtils.BZIP2_SIGNATURE);

        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(testInputStream);
        assertEquals(CompressorType.BZIP2, compressorInputStream.getType());

        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStream_InvalidType() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        OutputStream testOutputStream = new ByteArrayOutputStream();

        try {
            CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("invalid", testOutputStream);
            fail("Expected CompressorException");
        } catch (CompressorException e) {
            assertEquals("Compressor: invalid not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateGzipCompressorInputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        InputStream testInputStream = new ByteArrayInputStream(TestUtils.GZIP_SIGNATURE);

        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(testInputStream);
        assertEquals(CompressorType.GZIP, compressorInputStream.getType());

        compressorInputStream.close();
    }

    @Test
    public void testCreateBzip2CompressorInputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        InputStream testInputStream = new ByteArrayInputStream(TestUtils.BZIP2_SIGNATURE);

        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(testInputStream);
        assertEquals(CompressorType.BZIP2, compressorInputStream.getType());

        compressorInputStream.close();
    }

    @Test
    public void testCreateXZCompressorInputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        InputStream testInputStream = new ByteArrayInputStream(TestUtils.XZ_SIGNATURE);

        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(testInputStream);
        assertEquals(CompressorType.XZ, compressorInputStream.getType());

        compressorInputStream.close();
    }

    @Test
    public void testCreateGzipCompressorOutputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        OutputStream testOutputStream = new ByteArrayOutputStream();

        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(testOutputStream);
        assertEquals(CompressorType.GZIP, compressorOutputStream.getType());

        compressorOutputStream.finish();
        compressorOutputStream.close();

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(TestUtils.GZIP_SIGNATURE);

        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(testInputStream);
        assertEquals(CompressorType.GZIP, compressorInputStream.getType());

        compressorInputStream.close();

        ByteArrayInputStream decompressedInputStream = new ByteArrayInputStream(TestUtils.DECOMPRESSED_DATA);
        OutputStream outputStream = new ByteArrayOutputStream();

        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, outputStream);
        compressorOutputStream.write(TestUtils.DECOMPRESSED_DATA);
        compressorOutputStream.finish();
        compressorOutputStream.close();

        byte[] expectedData = TestUtils.GZIP_SIGNATURE;

        byte[] actualData = TestUtils.getBytesFromInputStream(testInputStream);

        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testCreateBzip2CompressorOutputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        OutputStream testOutputStream = new ByteArrayOutputStream();

        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(testOutputStream);
        assertEquals(CompressorType.BZIP2, compressorOutputStream.getType());

        compressorOutputStream.finish();
        compressorOutputStream.close();

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(TestUtils.BZIP2_SIGNATURE);

        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(testInputStream);
        assertEquals(CompressorType.BZIP2, compressorInputStream.getType());

        compressorInputStream.close();

        ByteArrayInputStream decompressedInputStream = new ByteArrayInputStream(TestUtils.DECOMPRESSED_DATA);
        OutputStream outputStream = new ByteArrayOutputStream();

        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.BZIP2, outputStream);
        compressorOutputStream.write(TestUtils.DECOMPRESSED_DATA);
        compressorOutputStream.finish();
        compressorOutputStream.close();

        byte[] expectedData = TestUtils.BZIP2_SIGNATURE;

        byte[] actualData = TestUtils.getBytesFromInputStream(testInputStream);

        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testCreateXZCompressorOutputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        OutputStream testOutputStream = new ByteArrayOutputStream();

        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(testOutputStream);
        assertEquals(CompressorType.XZ, compressorOutputStream.getType());

        compressorOutputStream.finish();
        compressorOutputStream.close();

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(TestUtils.XZ_SIGNATURE);

        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(testInputStream);
        assertEquals(CompressorType.XZ, compressorInputStream.getType());

        compressorInputStream.close();

        ByteArrayInputStream decompressedInputStream = new ByteArrayInputStream(TestUtils.DECOMPRESSED_DATA);
        OutputStream outputStream = new ByteArrayOutputStream();

        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.XZ, outputStream);
        compressorOutputStream.write(TestUtils.DECOMPRESSED_DATA);
        compressorOutputStream.finish();
        compressorOutputStream.close();

        byte[] expectedData = TestUtils.XZ_SIGNATURE;

        byte[] actualData = TestUtils.getBytesFromInputStream(testInputStream);

        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testCreateCompressorOutputStream_DecompressConcatenated() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);

        OutputStream testOutputStream = new ByteArrayOutputStream();

        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, testOutputStream);
        compressorOutputStream.write(TestUtils.GZIP_SIGNATURE);
        compressorOutputStream.write(TestUtils.GZIP_SIGNATURE);
        compressorOutputStream.finish();
        compressorOutputStream.close();

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(TestUtils.GZIP_SIGNATURE_WITH_TRAILER);

        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(testInputStream);
        assertEquals(CompressorType.GZIP, compressorInputStream.getType());

        byte[] actualData = TestUtils.getBytesFromInputStream(compressorInputStream);
        byte[] expectedData = TestUtils.DECOMPRESSED_DATA;

        assertArrayEquals(expectedData, actualData);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStream_DecompressConcatenated_Invalid() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);

        OutputStream testOutputStream = new ByteArrayOutputStream();

        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.XZ, testOutputStream);
        compressorOutputStream.write(TestUtils.XZ_SIGNATURE);
        compressorOutputStream.write(TestUtils.XZ_SIGNATURE);
        compressorOutputStream.finish();
        compressorOutputStream.close();

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(TestUtils.XZ_SIGNATURE_WITH_TRAILER);

        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(testInputStream);
        compressorInputStream.close();

        fail("Expected CompressorException");
    }

    private static byte[] getBytesFromInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[inputStream.available()];
        int bytesRead = inputStream.read(buffer);
        byte[] result = new byte[bytesRead];

        System.arraycopy(buffer, 0, result, 0, bytesRead);

        return result;
    }
}