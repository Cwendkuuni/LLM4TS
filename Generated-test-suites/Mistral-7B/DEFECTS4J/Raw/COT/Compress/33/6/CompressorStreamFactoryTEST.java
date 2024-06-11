package org.apache.commons.compress.compressors; // replace with your test package if needed

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.XZCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

public class CompressorStreamFactoryTest {

    

    @org.junit.Test
    public void testCreateCompressorInputStream_GZIP() {
        InputStream input = new ByteArrayInputStream(GzipCompressorInputStream.GZIP_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.GZIP, input);
        assertTrue(compressorInputStream instanceof GzipCompressorInputStream);
    }

    @org.junit.Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_InvalidSignature_GZIP() {
        InputStream input = new ByteArrayInputStream("invalid_signature".getBytes());
        new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.GZIP, input);
    }

    @org.junit.Test
    public void testCreateCompressorInputStream_BZIP2() {
        InputStream input = new ByteArrayInputStream(BZip2CompressorInputStream.BZIP2_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.BZIP2, input);
        assertTrue(compressorInputStream instanceof BZip2CompressorInputStream);
    }

    @org.junit.Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_InvalidSignature_BZIP2() {
        InputStream input = new ByteArrayInputStream("invalid_signature".getBytes());
        new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.BZIP2, input);
    }

    @org.junit.Test
    public void testCreateCompressorInputStream_XZ() {
        InputStream input = new ByteArrayInputStream(XZCompressorInputStream.XZ_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.XZ, input);
        assertTrue(compressorInputStream instanceof XZCompressorInputStream);
    }

    @org.junit.Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_InvalidSignature_XZ() {
        InputStream input = new ByteArrayInputStream("invalid_signature".getBytes());
        new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.XZ, input);
    }

    @org.junit.Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_UnknownAlgorithm() {
        InputStream input = new ByteArrayInputStream(GzipCompressorInputStream.GZIP_SIGNATURE);
        new CompressorStreamFactory().createCompressorInputStream("unknown_algorithm", input);
    }

    @org.junit.Test
    public void testCreateCompressorOutputStream_GZIP() {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.GZIP, output);
        assertTrue(compressorOutputStream instanceof GzipCompressorOutputStream);
    }

    @org.junit.Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStream_InvalidName() {
        OutputStream output = new ByteArrayOutputStream();
        new CompressorStreamFactory().createCompressorOutputStream("invalid_name", output);
    }

    @org.junit.Test
    public void testCreateCompressorOutputStream_BZIP2() {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.BZIP2, output);
        assertTrue(compressorOutputStream instanceof BZip2CompressorOutputStream);
    }

    @org.junit.Test
    public void testCreateCompressorOutputStream_XZ() {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.XZ, output);
        assertTrue(compressorOutputStream instanceof XZCompressorOutputStream);
    }

    @org.junit.Test
    public void testCreateCompressorOutputStream_DEFLATE() {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.DEFLATE, output);
        assertTrue(compressorOutputStream instanceof DeflateCompressorOutputStream);
    }

    @org.junit.Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertFalse(factory.getDecompressConcatenated());

        CompressorStreamFactory factoryConcatenated = new CompressorStreamFactory(true);
        assertTrue(factoryConcatenated.getDecompressConcatenated());
    }

    @org.junit.Test
    public void testCreateCompressorInputStream_SameSignature_GZIP() throws IOException {
        byte[] gzipSignature = GzipCompressorInputStream.GZIP_SIGNATURE;
        InputStream input = new ByteArrayInputStream(gzipSignature);
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.GZIP, output);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.GZIP, input);

        IOUtils.copy(compressorInputStream, compressorOutputStream);
        compressorInputStream.close();
        compressorOutputStream.close();

        byte[] outputBytes = ((ByteArrayOutputStream) output).toByteArray();
        assertArrayEquals(gzipSignature, outputBytes);
    }

    @org.junit.Test
    public void testCreateCompressorInputStream_SameSignature_BZIP2() throws IOException {
        byte[] bzip2Signature = BZip2CompressorInputStream.BZIP2_SIGNATURE;
        InputStream input = new ByteArrayInputStream(bzip2Signature);
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.BZIP2, output);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.BZIP2, input);

        IOUtils.copy(compressorInputStream, compressorOutputStream);
        compressorInputStream.close();
        compressorOutputStream.close();

        byte[] outputBytes = ((ByteArrayOutputStream) output).toByteArray();
        assertArrayEquals(bzip2Signature, outputBytes);
    }

    @org.junit.Test
    public void testCreateCompressorInputStream_SameSignature_XZ() throws IOException {
        byte[] xzSignature = XZCompressorInputStream.XZ_SIGNATURE;
        InputStream input = new ByteArrayInputStream(xzSignature);
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.XZ, output);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.XZ, input);

        IOUtils.copy(compressorInputStream, compressorOutputStream);
        compressorInputStream.close();
        compressorOutputStream.close();

        byte[] outputBytes = ((ByteArrayOutputStream) output).toByteArray();
        assertArrayEquals(xzSignature, outputBytes);
    }

    
}