package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.compressors.CompressorStreamFactory.CompressorType;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;

public class CompressorStreamFactoryTest {

    @org.junit.Test
    public void testCreateCompressorInputStream_BZIP2() throws IOException {
        InputStream input = new ByteArrayInputStream(BZip2CompressorInputStream.SIGNATURE);
        CompressorInputStream compressorInput = CompressorStreamFactory.createCompressorInputStream(CompressorStreamFactory.BZIP2, input);
        assertNotNull(compressorInput);
        assertEquals(BZip2CompressorInputStream.class, compressorInput.getClass());
        compressorInput.close();
    }

    @org.junit.Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_UnknownSignature() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[12]);
        CompressorStreamFactory.createCompressorInputStream("unknown", input);
    }

    @org.junit.Test
    public void testCreateCompressorInputStream_GZIP() throws IOException {
        InputStream input = new GzipCompressorInputStream(new ByteArrayInputStream(GzipCompressorInputStream.SIGNATURE));
        CompressorInputStream compressorInput = CompressorStreamFactory.createCompressorInputStream(CompressorStreamFactory.GZIP, input);
        assertNotNull(compressorInput);
        assertEquals(GzipCompressorInputStream.class, compressorInput.getClass());
        compressorInput.close();
        input.close();
    }

    @org.junit.Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_InvalidGzipSignature() throws IOException {
        InputStream input = new ByteArrayInputStream("Invalid GZIP signature".getBytes());
        CompressorStreamFactory.createCompressorInputStream(CompressorStreamFactory.GZIP, input);
    }

    @org.junit.Test
    public void testCreateCompressorInputStream_None() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[12]);
        CompressorInputStream compressorInput = CompressorStreamFactory.createCompressorInputStream("none", input);
        assertNull(compressorInput);
    }

    @org.junit.Test
    public void testCreateCompressorOutputStream_BZIP2() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutput = CompressorStreamFactory.createCompressorOutputStream(CompressorStreamFactory.BZIP2, output);
        assertNotNull(compressorOutput);
        assertEquals(BZip2CompressorOutputStream.class, compressorOutput.getClass());
        compressorOutput.finish();
        compressorOutput.close();
        assertNull(compressorOutput.getError());
        assertEquals(BZip2CompressorOutputStream.SIGNATURE, output.toByteArray());
    }

    @org.junit.Test
    public void testCreateCompressorOutputStream_GZIP() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutput = CompressorStreamFactory.createCompressorOutputStream(CompressorStreamFactory.GZIP, output);
        assertNotNull(compressorOutput);
        assertEquals(GzipCompressorOutputStream.class, compressorOutput.getClass());
        compressorOutput.finish();
        compressorOutput.close();
        assertNull(compressorOutput.getError());
        assertEquals(GzipCompressorOutputStream.SIGNATURE, output.toByteArray());
    }

    @org.junit.Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStream_UnknownCompressor() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        CompressorStreamFactory.createCompressorOutputStream("unknown", output);
    }

    @org.junit.Test
    public void testCreateCompressorOutputStream_None() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutput = CompressorStreamFactory.createCompressorOutputStream("none", output);
        assertNull(compressorOutput);
    }

    @org.junit.Test
    public void testCreateCompressorOutputStream_Deflate() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutput = CompressorStreamFactory.createCompressorOutputStream(CompressorStreamFactory.DEFLATE, output);
        assertNotNull(compressorOutput);
        assertEquals(DeflateCompressorOutputStream.class, compressorOutput.getClass());
        compressorOutput.finish();
        compressorOutput.close();
        assertNull(compressorOutput.getError());
        assertEquals(DeflateCompressorOutputStream.SIGNATURE, output.toByteArray());
    }

    @org.junit.Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertFalse(factory.getDecompressConcatenated());
        CompressorStreamFactory factory2 = new CompressorStreamFactory(true);
        assertTrue(factory2.getDecompressConcatenated());
    }
}