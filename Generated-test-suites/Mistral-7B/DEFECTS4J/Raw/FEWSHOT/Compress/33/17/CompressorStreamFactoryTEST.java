package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorInputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStreams() {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        InputStream input = IOUtils.toInputStream("Test data for compression.");

        // Test GZIP CompressorInputStream
        CompressorInputStream gzipInputStream = factory.createCompressorInputStream("gzip", input);
        assertInstanceOf(GzipCompressorInputStream.class, gzipInputStream);
        gzipInputStream.close();

        // Test BZIP2 CompressorInputStream
        CompressorInputStream bzip2InputStream = factory.createCompressorInputStream("bzip2", input);
        assertInstanceOf(BZip2CompressorInputStream.class, bzip2InputStream);
        bzip2InputStream.close();

        // Test XZ CompressorInputStream
        CompressorInputStream xzInputStream = factory.createCompressorInputStream("xz", input);
        assertInstanceOf(XZCompressorInputStream.class, xzInputStream);
        xzInputStream.close();

        // Test Deflate CompressorInputStream
        CompressorInputStream deflateInputStream = factory.createCompressorInputStream("deflate", input);
        assertInstanceOf(DeflateCompressorInputStream.class, deflateInputStream);
        deflateInputStream.close();

        // Test invalid compressor name
        try {
            factory.createCompressorInputStream("invalid", input);
            assert false;
        } catch (CompressorException e) {
            // Expected
        }

        input.close();
    }

    @Test
    public void testCreateCompressorOutputStreams() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream output = IOUtils.toOutputStream("Test data for compression.");

        // Test GZIP CompressorOutputStream
        CompressorOutputStream gzipOutputStream = factory.createCompressorOutputStream("gzip", output);
        assertInstanceOf(GzipCompressorOutputStream.class, gzipOutputStream);
        gzipOutputStream.finish();
        gzipOutputStream.close();

        // Test BZIP2 CompressorOutputStream
        CompressorOutputStream bzip2OutputStream = factory.createCompressorOutputStream("bzip2", output);
        assertInstanceOf(BZip2CompressorOutputStream.class, bzip2OutputStream);
        bzip2OutputStream.finish();
        bzip2OutputStream.close();

        // Test XZ CompressorOutputStream
        CompressorOutputStream xzOutputStream = factory.createCompressorOutputStream("xz", output);
        assertInstanceOf(XZCompressorOutputStream.class, xzOutputStream);
        xzOutputStream.finish();
        xzOutputStream.close();

        // Test Deflate CompressorOutputStream
        CompressorOutputStream deflateOutputStream = factory.createCompressorOutputStream("deflate", output);
        assertInstanceOf(DeflateCompressorOutputStream.class, deflateOutputStream);
        deflateOutputStream.finish();
        deflateOutputStream.close();

        // Test invalid compressor name
        try {
            factory.createCompressorOutputStream("invalid", output);
            assert false;
        } catch (CompressorException e) {
            // Expected
        }

        output.close();
    }

    @Test
    public void testDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        boolean decompressConcatenated = factory.getDecompressConcatenated();
        assert true;
        assertNull(decompressConcatenated = factory.getDecompressConcatenated());
    }
}