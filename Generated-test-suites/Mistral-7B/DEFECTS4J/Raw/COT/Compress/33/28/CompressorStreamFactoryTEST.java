package org.apache.commons.compress.compressors;

import static org.apache.commons.compress.compressors.CompressorStreamFactory.BZIP2;
import static org.apache.commons.compress.compressors.CompressorStreamFactory.DEFLATE;
import static org.apache.commons.compress.compressors.CompressorStreamFactory.GZIP;
import static org.apache.commons.compress.compressors.CompressorStreamFactory.PACK200;
import static org.apache.commons.compress.compressors.CompressorStreamFactory.XZ;
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
import org.apache.commons.compress.compressors.Pack200CompressorInputStream;
import org.apache.commons.compress.compressors.Pack200CompressorOutputStream;
import org.apache.commons.compress.compressors.XZCompressorInputStream;
import org.apache.commons.compress.compressors.XZCompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorFactory;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.utils.IOUtils;

/**
 * CompressorStreamFactoryTest class
 */
public class CompressorStreamFactoryTest {

    @org.junit.Test
    public void testCreateCompressorStreamFactory() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertNotNull(factory);
    }

    @org.junit.Test
    public void testCreateCompressorInputStreamFromInputStream() throws IOException {
        String name = GZIP;
        InputStream input = new ByteArrayInputStream(new byte[0]);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(name, input);
        assertNotNull(compressorInputStream);
        compressorInputStream.close();
    }

    @org.junit.Test
    public void testCreateCompressorInputStreamFromNameAndInputStream() throws IOException {
        String name = GZIP;
        InputStream input = new ByteArrayInputStream(new byte[0]);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(name, input);
        assertNotNull(compressorInputStream);
        compressorInputStream.close();
    }

    @org.junit.Test
    public void testCreateCompressorInputStreamUnsupportedAlgorithm() {
        String name = "unsupportedAlgorithm";
        InputStream input = new ByteArrayInputStream(new byte[0]);
        assertThrows(CompressorException.class, () -> new CompressorStreamFactory().createCompressorInputStream(name, input));
    }

    @org.junit.Test
    public void testCreateCompressorOutputStreamFromNameAndOutputStream() throws IOException {
        String name = XZ;
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(name, output);
        assertNotNull(compressorOutputStream);
        compressorOutputStream.finish();
        compressorOutputStream.close();
    }

    @org.junit.Test
    public void testCreateCompressorOutputStreamUnsupportedAlgorithm() {
        String name = "unsupportedAlgorithm";
        OutputStream output = new ByteArrayOutputStream();
        assertThrows(CompressorException.class, () -> new CompressorStreamFactory().createCompressorOutputStream(name, output));
    }

    @org.junit.Test
    public void testSetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        boolean decompressConcatenated = factory.getDecompressConcatenated();
        assertFalse(decompressConcatenated);

        factory = new CompressorStreamFactory(true);
        decompressConcatenated = factory.getDecompressConcatenated();
        assertTrue(decompressConcatenated);
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testSetDecompressConcatenatedWithSetConstructor() {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        factory.setDecompressConcatenated(false);
    }

    @org.junit.Test
    public void testCreateCompressorInputStreamWithDecompressConcatenated() throws IOException {
        String name = BZIP2;
        InputStream input = new ByteArrayInputStream(new byte[0]);
        boolean decompressConcatenated = true;
        CompressorInputStream compressorInputStream = new CompressorStreamFactory(decompressConcatenated).createCompressorInputStream(name, input);
        compressorInputStream.close();
    }
}