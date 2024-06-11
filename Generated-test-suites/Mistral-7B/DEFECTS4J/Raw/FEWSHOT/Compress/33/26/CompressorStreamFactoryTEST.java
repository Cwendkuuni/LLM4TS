package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.XZCompressorInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorFactory;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        // Test BZIP2
        byte[] bzip2Signature = new byte[]{0x42, 0x5a, 0x68, 0x64, 0x67, 0x65, 0x20, 0x42, 0x5a, 0x68, 0x64, 0x67, 0x65, 0x20, 0x2b, 0x50, 0x42};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bzip2Signature);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream("BZIP2", inputStream);
        assertInstanceOf(BZip2CompressorInputStream.class, compressorInputStream);

        // Test GZIP
        byte[] gzipSignature = new byte[]{0x1f, 0x8b, 0x08, 0x00, 0x47, 0x49, 0x46, 0x38, 0x37, 0x61};
        ByteArrayInputStream gzipInputStream = new ByteArrayInputStream(gzipSignature);
        CompressorInputStream compressorInputStreamGzip = factory.createCompressorInputStream("GZIP", gzipInputStream);
        assertInstanceOf(GzipCompressorInputStream.class, compressorInputStreamGzip);

        // Test XZ
        byte[] xzSignature = new byte[]{0xfd, 0x37, 0x7a, 0x58, 0x5a, 0x42, 0x60, 0x82, 0x0b, 0x01, 0x0b, 0x05};
        ByteArrayInputStream xzInputStream = new ByteArrayInputStream(xzSignature);
        CompressorInputStream compressorInputStreamXz = factory.createCompressorInputStream("XZ", xzInputStream);
        assertInstanceOf(XZCompressorInputStream.class, compressorInputStreamXz);

        // Test Unknown compressor
        ByteArrayInputStream unknownInputStream = new ByteArrayInputStream(new byte[]{0x00, 0x01, 0x02});
        try {
            factory.createCompressorInputStream("Unknown", unknownInputStream);
            assert false;
        } catch (CompressorException e) {
            // Expected
        }

        // Test null compressor name
        try {
            factory.createCompressorInputStream(null, unknownInputStream);
            assert false;
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test null input stream
        try {
            factory.createCompressorInputStream("BZIP2", null);
            assert false;
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorOutputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        // Test BZIP2
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("BZIP2", outputStream);
        assertInstanceOf(BZip2CompressorOutputStream.class, compressorOutputStream);

        // Test GZIP
        ByteArrayOutputStream gzipOutputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStreamGzip = factory.createCompressorOutputStream("GZIP", gzipOutputStream);
        assertInstanceOf(GzipCompressorOutputStream.class, compressorOutputStreamGzip);

        // Test XZ
        ByteArrayOutputStream xzOutputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStreamXz = factory.createCompressorOutputStream("XZ", xzOutputStream);
        assertInstanceOf(XZCompressorOutputStream.class, compressorOutputStreamXz);

        // Test Unknown compressor
        ByteArrayOutputStream unknownOutputStream = new ByteArrayOutputStream();
        try {
            factory.createCompressorOutputStream("Unknown", unknownOutputStream);
            assert false;
        } catch (CompressorException e) {
            // Expected
        }

        // Test null compressor name
        try {
            factory.createCompressorOutputStream(null, unknownOutputStream);
            assert false;
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test null output stream
        try {
            factory.createCompressorOutputStream("BZIP2", null);
            assert false;
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        assertTrue(factory.getDecompressConcatenated());
    }
}