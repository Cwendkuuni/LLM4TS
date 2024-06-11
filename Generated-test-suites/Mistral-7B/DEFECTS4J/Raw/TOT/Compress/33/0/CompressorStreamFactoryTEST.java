package org.apache.commons.compress.compressors.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;

import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.XZCompressorInputStream;

/**
 * Test for {@link CompressorStreamFactory} class
 */
public class CompressorStreamFactoryTest {

    /**
     * Test creating compressor input streams from various compressors.
     * @throws IOException
     * @throws CompressorException
     */
    @org.junit.Test
    public void testCreateCompressorInputStream() throws IOException, CompressorException {
        // Test creating compressor input streams from known compressor names
        InputStream input = createTestInputStream();
        CompressorInputStream gzipInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.GZIP, input);
        assertTrue(gzipInputStream instanceof GzipCompressorInputStream);

        CompressorInputStream bzip2InputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.BZIP2, input);
        assertTrue(bzip2InputStream instanceof BZip2CompressorInputStream);

        CompressorInputStream deflateInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.DEFLATE, input);
        assertTrue(deflateInputStream instanceof DeflateCompressorInputStream);

        CompressorInputStream xzInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.XZ, input);
        assertTrue(xzInputStream instanceof XZCompressorInputStream);

        CompressorInputStream unknownInputStream = new CompressorStreamFactory()
                .createCompressorInputStream("unknown", input);
        assertTrue(unknownInputStream instanceof CompressorInputStream);
        // Test creating compressor input streams from unknown compressor names throws exception
        try {
            new CompressorStreamFactory().createCompressorInputStream("unknown", input);
            fail();
        } catch (CompressorException e) {
            // expected
        }
    }

    /**
     * Test creating compressor output streams from various compressors.
     * @throws IOException
     * @throws CompressorException
     */
    @org.junit.Test
    public void testCreateCompressorOutputStream() throws IOException, CompressorException {
        // Test creating compressor output streams for known compressors
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream gzipOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.GZIP, output);

        byte[] data = "Test data for compression".getBytes();
        gzipOutputStream.write(data);
        gzipOutputStream.finish();

        InputStream compressedInputStream = new ByteArrayInputStream(output.toByteArray());
        CompressorInputStream gzipDecompressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.GZIP, compressedInputStream);
        assertEquals(data.length, gzipDecompressorInputStream.available());

        // Test creating compressor output streams for other known compressors
        output = new ByteArrayOutputStream();
        CompressorOutputStream bzip2OutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.BZIP2, output);
        bzip2OutputStream.write("Test data for compression".getBytes());
        bzip2OutputStream.finish();

        InputStream compressedBzip2InputStream = new ByteArrayInputStream(output.toByteArray());
        CompressorInputStream bzip2DecompressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.BZIP2, compressedBzip2InputStream);
        assertEquals(13, bzip2DecompressorInputStream.read());

        output = new ByteArrayOutputStream();
        CompressorOutputStream xzOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.XZ, output);
        xzOutputStream.write(new byte[] { 78, 0, 1, 0, 0, 13, 80, 0, 0, 0, 26, 2, 0, 0, 0, 0, 0, 1 });
        xzOutputStream.write(new byte[] { 1, 2, 3 });
        xzOutputStream.finish();

        InputStream compressedXzInputStream = new ByteArrayInputStream(output.toByteArray());
        CompressorInputStream xzDecompressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.XZ, compressedXzInputStream);
        assertEquals(3, xzDecompressorInputStream.read());
        assertEquals(3, xzDecompressorInputStream.read());
        assertEquals(3, xzDecompressorInputStream.read());

        // Test creating compressor output stream for unknown compressor throws exception
        try {
            new CompressorStreamFactory().createCompressorOutputStream("unknown", output);
            fail();
        } catch (CompressorException e) {
            // expected
        }
    }

    /**
     * Creates a test input stream for tests
     * @return
     * @throws IOException
     */
    private InputStream createTestInputStream() throws IOException {
        return new ByteArrayInputStream("Test data for compression".getBytes());
    }

}