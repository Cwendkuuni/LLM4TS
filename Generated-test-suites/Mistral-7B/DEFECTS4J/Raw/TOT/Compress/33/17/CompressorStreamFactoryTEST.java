package org.apache.commons.compress.compressors.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.NoSuchAlgorithmException;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Test for {@link CompressorStreamFactory}.
 */
public class CompressorStreamFactoryTest {

    /**
     * Test createCompressorInputStream with valid inputs.
     */
    @org.junit.Test
    public void testCreateCompressorInputStreamValid() throws IOException {
        // Create compressor stream factory
        CompressorStreamFactory factory = new CompressorStreamFactory();

        // Create Gzip compressor input stream
        final InputStream gzipInput = new ByteArrayInputStream(
                new byte[] { (byte) 0x1f, (byte) 0x8b, (byte) 0x08, 0, 0, 0, 0, 0, // GZip signature
                1, 0, // Compressed length
                0, 0, 0, 0, // Modification time
                (byte) 0x08, (byte) 0x00, // Compressed headers length
                0, 0, 0, 0, // Uncompressed length
                0, 0, 0, 0, // Check sum
                0, 0, 0, 0, // Operating system
                0, 0, 0, 0, // File type
                0, 0, 0, 0, // File name length
                0, 'G', 'P', 'G', 'Z', // File name
                0, 0, 0, 0, // Compressed comment length
                0, 0, 0, 0 // Compressed comment
                });
        CompressorInputStream gzipStream = factory.createCompressorInputStream(GZipCompressorInputStream.class, gzipInput);
        assertNotNull(gzipStream);
        assertEquals(GzipCompressorInputStream.class, gzipStream.getClass());

        // Create BZip2 compressor input stream
        final InputStream bzip2Input = new ByteArrayInputStream(
                new byte[] { (byte) 0x42, (byte) 0x5A, (byte) 0x68, 0, 0, 0, 0, 0, // BZip2 signature
                0, 0, 0, 0, 0, 0, 0, 0, // Compressed data length
                0, 0, 0, 0, 0, 0, 0, 0, // Uncompressed data length
                0, 0, 0, 0, 0, 0, 0, 0, // Checksum
                0, 0, 0, 0, 0, 0, 0, 0, // Magic number
                0, 0, 0, 0, 0, 0, 0, 0, // Data offset
                0, 0, 0, 0, 0, 0, 0, 0 // Data length
                });
        CompressorInputStream bzip2Stream = factory.createCompressorInputStream(BZip2CompressorInputStream.class, bzip2Input);
        assertNotNull(bzip2Stream);
        assertEquals(BZip2CompressorInputStream.class, bzip2Stream.getClass());

        // Create XZ compressor input stream
        final InputStream xzInput = new ByteArrayInputStream(
                new byte[] { 0x78, 0x4C, 0x6A, 0x58, 0x5A, 0x42, 0x60, 0x82, // XZ signature
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // Compressed data length
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // Uncompressed data length
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // Checksum
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // Data offset
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // Data length
                0, 0 // End of central directory record size
                });
        CompressorInputStream xzStream = factory.createCompressorInputStream(XZCompressorInputStream.class, xzInput);
        assertNotNull(xzStream);
        assertEquals(XZCompressorInputStream.class, xzStream.getClass());

        // Create Deflate compressor input stream
        final InputStream deflateInput = new ByteArrayInputStream(
                new byte[] { (byte) 0x78, (byte) 0x5E, (byte) 0x43, 0, 0, 0, 0, 0, // Deflate signature
                0, 0, 0, 0, // Compressed data length
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // Uncompressed data length
                0, 0, 0, 0, // Checksum
                0, 0, 0, 0 // Offset
                });
        CompressorInputStream deflateStream = factory.createCompressorInputStream(DeflateCompressorInputStream.class, deflateInput);
        assertNotNull(deflateStream);
        assertEquals(DeflateCompressorInputStream.class, deflateStream.getClass());

        // Create compressor output streams and test writing to them with various compressor names
        final OutputStream out = new ByteArrayOutputStream();
        CompressorOutputStream gzipStreamOut = factory.createCompressorOutputStream(GZipCompressorOutputStream.class, out);
        gzipStreamOut.write("test".getBytes());
        gzipStreamOut.finish();
        assertEquals(10, out.size());
        gzipStreamOut.close();

        CompressorOutputStream bzip2StreamOut = factory.createCompressorOutputStream(BZip2CompressorOutputStream.class, out);
        IOUtils.write("test".getBytes(), bzip2StreamOut);
        bzip2StreamOut.finish();
        bzip2StreamOut.close();
        assertEquals(14, out.size());

        CompressorOutputStream xzStreamOut = factory.createCompressorOutputStream(XZCompressorOutputStream.class, out);
        IOUtils.write("test".getBytes(), xzStreamOut);
        xzStreamOut.finish();
        xzStreamOut.close();
        assertEquals(16, out.size());

        CompressorOutputStream deflateStreamOut = factory.createCompressorOutputStream(DeflateCompressorOutputStream.class, out);
        IOUtils.write("test".getBytes(), deflateStreamOut);
        deflateStreamOut.finish();
        deflateStreamOut.close();
        assertEquals(5, out.size());
    }

    /**
     * Test createCompressorInputStream with invalid inputs.
     */
    @org.junit.Test(expected = CompressorException.class)
    public void testCreateCompressorInputStreamInvalid() throws IOException, NoSuchAlgorithmException {
        // Create compressor stream factory
        CompressorStreamFactory factory = new CompressorStreamFactory();

        // Test create compressor input stream with null name
        factory.createCompressorInputStream(null, new ByteArrayInputStream(new byte[0]));

        // Test create compressor input stream with null input stream
        factory.createCompressorInputStream(GZipCompressorInputStream.class, null);

        // Test create compressor input stream with invalid compressor name
        factory.createCompressorInputStream("invalid", new ByteArrayInputStream(new byte[0]));

        // Test create compressor input stream with unknown compressor name
        factory.createCompressorInputStream("unknown", new ByteArrayInputStream(new byte[0]));
    }

    /**
     * Test createCompressorOutputStream with valid inputs.
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamValid() throws IOException {
        // Create compressor stream factory
        CompressorStreamFactory factory = new CompressorStreamFactory();

        // Create Gzip compressor output stream
        final OutputStream gzipOutput = new ByteArrayOutputStream();
        CompressorOutputStream gzipStreamOut = factory.createCompressorOutputStream(GZipCompressorOutputStream.class, gzipOutput);
        gzipStreamOut.write("test".getBytes());
        gzipStreamOut.finish();
        assertEquals(10, gzipOutput.size());
        assertNull(gzipStreamOut.getNextHeader());
        gzipStreamOut.close();

        // Create BZip2 compressor output stream
        final OutputStream bzip2Output = new ByteArrayOutputStream();
        CompressorOutputStream bzip2StreamOut = factory.createCompressorOutputStream(BZip2CompressorOutputStream.class, bzip2Output);
        bzip2StreamOut.write("test".getBytes());
        bzip2StreamOut.finish();
        assertEquals(14, bzip2Output.size());
        assertNull(bzip2StreamOut.getNextHeader());
        bzip2StreamOut.close();

        // Create XZ compressor output stream
        final OutputStream xzOutput = new ByteArrayOutputStream();
        CompressorOutputStream xzStreamOut = factory.createCompressorOutputStream(XZCompressorOutputStream.class, xzOutput);
        xzStreamOut.write("test".getBytes());
        xzStreamOut.finish();
        assertEquals(16, xzOutput.size());
        assertNull(xzStreamOut.getNextHeader());
        xzStreamOut.close();

        // Create Deflate compressor output stream
        final OutputStream deflateOutput = new ByteArrayOutputStream();
        CompressorOutputStream deflateStreamOut = factory.createCompressorOutputStream(DeflateCompressorOutputStream.class, deflateOutput);
        deflateStreamOut.write("test".getBytes());
        deflateStreamOut.finish();
        assertEquals(5, deflateOutput.size());
        assertNull(deflateStreamOut.getNextHeader());
        deflateStreamOut.close();
    }

    /**
     * Test createCompressorOutputStream with invalid inputs.
     */
    @org.junit.Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStreamInvalid() throws IOException, NoSuchAlgorithmException {
        // Create compressor stream factory
        CompressorStreamFactory factory = new CompressorStreamFactory();

        // Test create compressor output stream with null name
        factory.createCompressorOutputStream(null, new ByteArrayOutputStream());

        // Test create compressor output stream with null output stream
        factory.createCompressorOutputStream(GZipCompressorOutputStream.class, null);

        // Test create compressor output stream with invalid compressor name
        factory.createCompressorOutputStream("invalid", new ByteArrayOutputStream());

        // Test create compressor output stream with unknown compressor name
        factory.createCompressorOutputStream("unknown", new ByteArrayOutputStream());
    }
}