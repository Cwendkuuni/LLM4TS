package org.apache.commons.compress.compressors; // replace with the actual package name if needed

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for CompressorStreamFactory.
 */
public class CompressorStreamFactoryTest {

    private static final String GZIP_SIGNATURE = "GZ".getBytes();
    private static final String BZIP2_SIGNATURE = new byte[]{(byte) 0x42, (byte) 0x5A, (byte) 0x68, (byte) 0x64,
            (byte) 0x67, (byte) 0x75, (byte) 0x73, (byte) 0x68, (byte) 0x6F, (byte) 0x77, (byte) 0x73,
            (byte) 0x21, (byte) 0x1B};
    private static final String XZ_SIGNATURE = new byte[]{(byte) 0xFD, (byte) 0x37, (byte) 0x7A, (byte) 0x58,
            (byte) 0x53, (byte) 0x42, (byte) 0x61, (byte) 0x76, (byte) 0x65, (byte) 0x52, (byte) 0x65,
            (byte) 0x63, (byte) 0x65, (byte) 0x74, (byte) 0x21, (byte) 0x1B};
    private static final byte[] DEFLATE_SIGNATURE = new byte[0];

    @Test
    public void testCreateCompressorInputStreamWithValidSignature() throws IOException, CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        // Test Gzip input stream
        InputStream gzipInput = new ByteArrayInputStream(GZIP_SIGNATURE);
        CompressorInputStream gzipInputStream = factory.createCompressorInputStream("gzip", gzipInput);
        assertNotNull(gzipInputStream);

        // Test BZip2 input stream
        InputStream bzip2Input = new ByteArrayInputStream(BZIP2_SIGNATURE);
        CompressorInputStream bzip2InputStream = factory.createCompressorInputStream("bzip2", bzip2Input);
        assertNotNull(bzip2InputStream);

        // Test XZ input stream
        InputStream xzInput = new ByteArrayInputStream(XZ_SIGNATURE);
        CompressorInputStream xzInputStream = factory.createCompressorInputStream("xz", xzInput);
        assertNotNull(xzInputStream);

        // Test Deflate input stream (signature not checked as it's autodetected)
        InputStream deflateInput = new ByteArrayInputStream(DEFLATE_SIGNATURE);
        CompressorInputStream deflateInputStream = factory.createCompressorInputStream("deflate", deflateInput);
        assertNotNull(deflateInputStream);
    }

    @Test
    public void testCreateCompressorInputStreamWithInvalidSignature() throws IOException, CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        // Test unknown signature
        InputStream unknownInput = new ByteArrayInputStream("unknown".getBytes());
        assertThrows(CompressorException.class, () -> factory.createCompressorInputStream("unknown", unknownInput));

        // Test Gzip signature with BZip2 expected
        InputStream gzipWithBzip2SignatureInput = new ByteArrayInputStream(BZIP2_SIGNATURE);
        assertThrows(CompressorException.class, () -> factory.createCompressorInputStream("gzip", gzipWithBzip2SignatureInput));

        // Test BZip2 signature with Gzip expected
        InputStream bzip2WithGzipSignatureInput = new ByteArrayInputStream(GZIP_SIGNATURE);
        assertThrows(CompressorException.class, () -> factory.createCompressorInputStream("bzip2", bzip2WithGzipSignatureInput));
    }

    @Test
    public void testCreateCompressorInputStreamWithNullInputStream() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertThrows(IllegalArgumentException.class, () -> factory.createCompressorInputStream("gzip", null));
    }

    @Test
    public void testCreateCompressorOutputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        // Test Gzip output stream
        OutputStream gzipOutput = new ByteArrayOutputStream();
        CompressorOutputStream gzipOutputStream = factory.createCompressorOutputStream("gzip", gzipOutput);
        assertNotNull(gzipOutputStream);

        // Test BZip2 output stream
        OutputStream bzip2Output = new ByteArrayOutputStream();
        CompressorOutputStream bzip2OutputStream = factory.createCompressorOutputStream("bzip2", bzip2Output);
        assertNotNull(bzip2OutputStream);

        // Test XZ output stream
        OutputStream xzOutput = new ByteArrayOutputStream();
        CompressorOutputStream xzOutputStream = factory.createCompressorOutputStream("xz", xzOutput);
        assertNotNull(xzOutputStream);

        // Test Deflate output stream
        OutputStream deflateOutput = new ByteArrayOutputStream();
        CompressorOutputStream deflateOutputStream = factory.createCompressorOutputStream("deflate", deflateOutput);
        assertNotNull(deflateOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStreamWithNullOutputStream() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertThrows(IllegalArgumentException.class, () -> factory.createCompressorOutputStream("gzip", null));
    }
}