package org.apache.commons.compress.compressors;

import static org.apache.commons.compress.compressors.CompressorStreamFactory.BZIP2;
import static org.apache.commons.compress.compressors.CompressorStreamFactory.DEFLATE;
import static org.apache.commons.compress.compressors.CompressorStreamFactory.GZIP;
import static org.apache.commons.compress.compressors.CompressorStreamFactory.PACK200;
import static org.apache.commons.compress.compressors.CompressorStreamFactory.XZ;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

/**
 * Test class for CompressorStreamFactory
 */
public class CompressorStreamFactoryTest {

    // Test factory initialization

    @org.junit.Test
    public void testFactoryInitialization() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertNull(factory.getDecompressConcatenated());

        factory = new CompressorStreamFactory(true);
        assertTrue(factory.getDecompressConcatenated());

        factory = new CompressorStreamFactory(false);
        assertFalse(factory.getDecompressConcatenated());
    }

    // Test createCompressorInputStream()

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStreamNullInputStream() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.createCompressorInputStream(null);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStreamNoMarkSupported() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.createCompressorInputStream(inputStream);
        inputStream.mark(0); // Should throw an exception
        factory.createCompressorInputStream(inputStream);
    }

    @org.junit.Test
    public void testCreateGzipCompressorInputStream() {
        String name = GZIP;
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'G', 'z', '\r', '\n', 'I', 'D', 'P'});
        CompressorInputStream compressorInputStream = null;
        try {
            compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(name, inputStream);
            assertTrue(compressorInputStream instanceof GzipCompressorInputStream);
            compressorInputStream.close();
        } catch (CompressorException e) {
            fail("Unexpected CompressorException: " + e.getMessage());
        }
    }

    @org.junit.Test
    public void testCreateBzip2CompressorInputStream() {
        String name = BZIP2;
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x42, 0x5A, 0x68, 0x64, 0x6F, 0x67, 0x75, 0x50, 0x42});
        CompressorInputStream compressorInputStream = null;
        try {
            compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(name, inputStream);
            assertTrue(compressorInputStream instanceof BZip2CompressorInputStream);
            compressorInputStream.close();
        } catch (CompressorException e) {
            fail("Unexpected CompressorException: " + e.getMessage());
        }
    }

    @org.junit.Test
    public void testCreateXzCompressorInputStream() {
        String name = XZ;
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0xFD, 0x37, 0x7A, 0x58, 0x5A, 0x42, 0x60, 0x82});
        CompressorInputStream compressorInputStream = null;
        try {
            compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(name, inputStream);
            assertTrue(compressorInputStream instanceof XZCompressorInputStream);
            compressorInputStream.close();
        } catch (CompressorException | IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @org.junit.Test(expected = CompressorException.class)
    public void testCreateUnknownCompressorInputStream() {
        String name = "Unknown";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        new CompressorStreamFactory().createCompressorInputStream(name, inputStream);
    }

    // Test createCompressorOutputStream()

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorOutputStreamNullOutputStream() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.createCompressorOutputStream(DEFLATE, null);
    }

    @org.junit.Test
    public void testCreateDeflateCompressorOutputStream() {
        String name = DEFLATE;
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = null;
        try {
            compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(name, outputStream);
            assertTrue(compressorOutputStream instanceof DeflateCompressorOutputStream);
            compressorOutputStream.finish();
            compressorOutputStream.close();
            assertEquals(0, outputStream.size());
        } catch (CompressorException e) {
            fail("Unexpected CompressorException: " + e.getMessage());
        }
    }

    @org.junit.Test(expected = CompressorException.class)
    public void testCreateUnknownCompressorOutputStream() {
        String name = "Unknown";
        OutputStream outputStream = new ByteArrayOutputStream();
        new CompressorStreamFactory().createCompressorOutputStream(name, outputStream);
    }
}